package services;

import models.HealthRecord;
import models.Patient;
import utils.DatabaseManager;
import utils.ValidationUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * EHRService class for managing Electronic Health Records
 */
public class EHRService {
    private static final String HEALTH_RECORDS_FILE = "health_records.dat";
    private DatabaseManager dbManager;
    private List<HealthRecord> healthRecords;
    private PatientService patientService;
    private StaffService staffService;
    
    public EHRService(PatientService patientService, StaffService staffService) {
        this.dbManager = DatabaseManager.getInstance();
        this.healthRecords = loadHealthRecords();
        this.patientService = patientService;
        this.staffService = staffService;
    }
    
    /**
     * Load health records from file
     */
    private List<HealthRecord> loadHealthRecords() {
        return dbManager.loadData(HEALTH_RECORDS_FILE);
    }
    
    /**
     * Save health records to file
     */
    private boolean saveHealthRecords() {
        return dbManager.saveData(healthRecords, HEALTH_RECORDS_FILE);
    }
    
    /**
     * Add a new health record
     */
    public boolean addHealthRecord(HealthRecord record) {
        if (record == null) {
            System.out.println("Health record cannot be null.");
            return false;
        }
        
        // Validate health record data
        if (!validateHealthRecord(record)) {
            return false;
        }
        
        // Check if record ID already exists
        if (findHealthRecordById(record.getRecordId()) != null) {
            System.out.println("Health record with ID " + record.getRecordId() + " already exists.");
            return false;
        }
        
        // Add record to list
        healthRecords.add(record);
        
        // Save to file
        if (saveHealthRecords()) {
            System.out.println("Health record added successfully: " + record.getRecordId());
            return true;
        } else {
            // Remove from list if save failed
            healthRecords.remove(record);
            System.out.println("Failed to save health record data.");
            return false;
        }
    }
    
    /**
     * Update an existing health record
     */
    public boolean updateHealthRecord(HealthRecord updatedRecord) {
        if (updatedRecord == null) {
            System.out.println("Health record cannot be null.");
            return false;
        }
        
        // Validate health record data
        if (!validateHealthRecord(updatedRecord)) {
            return false;
        }
        
        // Find existing record
        HealthRecord existingRecord = findHealthRecordById(updatedRecord.getRecordId());
        if (existingRecord == null) {
            System.out.println("Health record with ID " + updatedRecord.getRecordId() + " not found.");
            return false;
        }
        
        // Update record data
        int index = healthRecords.indexOf(existingRecord);
        healthRecords.set(index, updatedRecord);
        
        // Save to file
        if (saveHealthRecords()) {
            System.out.println("Health record updated successfully: " + updatedRecord.getRecordId());
            return true;
        } else {
            // Revert changes if save failed
            healthRecords.set(index, existingRecord);
            System.out.println("Failed to save health record data.");
            return false;
        }
    }
    
    /**
     * Find health record by ID
     */
    public HealthRecord findHealthRecordById(String recordId) {
        if (!ValidationUtils.isNotEmpty(recordId)) {
            return null;
        }
        
        return healthRecords.stream()
                .filter(record -> record.getRecordId().equalsIgnoreCase(recordId.trim()))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Get health records by patient ID
     */
    public List<HealthRecord> getHealthRecordsByPatient(String patientId) {
        if (!ValidationUtils.isNotEmpty(patientId)) {
            return new ArrayList<>();
        }
        
        return healthRecords.stream()
                .filter(record -> record.getPatientId().equalsIgnoreCase(patientId.trim()))
                .filter(HealthRecord::isActive)
                .sorted((r1, r2) -> r2.getVisitDate().compareTo(r1.getVisitDate())) // Most recent first
                .collect(Collectors.toList());
    }
    
    /**
     * Get health records by doctor ID
     */
    public List<HealthRecord> getHealthRecordsByDoctor(String doctorId) {
        if (!ValidationUtils.isNotEmpty(doctorId)) {
            return new ArrayList<>();
        }
        
        return healthRecords.stream()
                .filter(record -> record.getDoctorId().equalsIgnoreCase(doctorId.trim()))
                .filter(HealthRecord::isActive)
                .sorted((r1, r2) -> r2.getVisitDate().compareTo(r1.getVisitDate()))
                .collect(Collectors.toList());
    }
    
    /**
     * Search health records by diagnosis
     */
    public List<HealthRecord> searchByDiagnosis(String diagnosis) {
        if (!ValidationUtils.isNotEmpty(diagnosis)) {
            return new ArrayList<>();
        }
        
        String searchDiagnosis = diagnosis.trim().toLowerCase();
        return healthRecords.stream()
                .filter(record -> record.getDiagnosis() != null && 
                                record.getDiagnosis().toLowerCase().contains(searchDiagnosis))
                .filter(HealthRecord::isActive)
                .collect(Collectors.toList());
    }
    
    /**
     * Get patient's medical history summary
     */
    public String getPatientMedicalHistorySummary(String patientId) {
        List<HealthRecord> records = getHealthRecordsByPatient(patientId);
        if (records.isEmpty()) {
            return "No medical history found for patient: " + patientId;
        }
        
        Patient patient = patientService.findPatientById(patientId);
        StringBuilder summary = new StringBuilder();
        
        summary.append("=== Medical History Summary ===\n");
        if (patient != null) {
            summary.append("Patient: ").append(patient.getFullName()).append("\n");
            summary.append("Patient ID: ").append(patientId).append("\n");
            summary.append("Age: ").append(patient.getAge()).append("\n");
            summary.append("Blood Group: ").append(patient.getBloodGroup() != null ? patient.getBloodGroup() : "Not specified").append("\n");
        }
        summary.append("Total Records: ").append(records.size()).append("\n\n");
        
        summary.append("Recent Visits:\n");
        for (int i = 0; i < Math.min(5, records.size()); i++) {
            HealthRecord record = records.get(i);
            summary.append("- ").append(record.getVisitDate().toLocalDate())
                   .append(": ").append(record.getDiagnosis() != null ? record.getDiagnosis() : "No diagnosis")
                   .append("\n");
        }
        
        // Get unique diagnoses
        List<String> uniqueDiagnoses = records.stream()
                .map(HealthRecord::getDiagnosis)
                .filter(d -> d != null && !d.trim().isEmpty())
                .distinct()
                .collect(Collectors.toList());
        
        if (!uniqueDiagnoses.isEmpty()) {
            summary.append("\nPrevious Diagnoses:\n");
            uniqueDiagnoses.forEach(diagnosis -> summary.append("- ").append(diagnosis).append("\n"));
        }
        
        return summary.toString();
    }
    
    /**
     * Get records with follow-up required
     */
    public List<HealthRecord> getRecordsWithFollowUp() {
        return healthRecords.stream()
                .filter(HealthRecord::hasFollowUp)
                .filter(HealthRecord::isActive)
                .sorted((r1, r2) -> r1.getNextVisitDate().compareTo(r2.getNextVisitDate()))
                .collect(Collectors.toList());
    }
    
    /**
     * Generate next record ID
     */
    public String generateRecordId() {
        int maxId = 0;
        for (HealthRecord record : healthRecords) {
            String id = record.getRecordId();
            if (id.startsWith("HR") && id.length() > 2) {
                try {
                    int numId = Integer.parseInt(id.substring(2));
                    maxId = Math.max(maxId, numId);
                } catch (NumberFormatException e) {
                    // Ignore invalid IDs
                }
            }
        }
        return "HR" + String.format("%04d", maxId + 1);
    }
    
    /**
     * Deactivate a health record
     */
    public boolean deactivateHealthRecord(String recordId) {
        HealthRecord record = findHealthRecordById(recordId);
        if (record == null) {
            System.out.println("Health record with ID " + recordId + " not found.");
            return false;
        }
        
        record.setActive(false);
        if (saveHealthRecords()) {
            System.out.println("Health record deactivated: " + recordId);
            return true;
        } else {
            record.setActive(true); // Revert change
            System.out.println("Failed to save health record data.");
            return false;
        }
    }
    
    /**
     * Get all active health records
     */
    public List<HealthRecord> getAllActiveHealthRecords() {
        return healthRecords.stream()
                .filter(HealthRecord::isActive)
                .collect(Collectors.toList());
    }
    
    /**
     * Validate health record data
     */
    private boolean validateHealthRecord(HealthRecord record) {
        // Validate required fields
        if (!ValidationUtils.isValidId(record.getRecordId())) {
            System.out.println("Invalid record ID.");
            return false;
        }
        
        if (!ValidationUtils.isValidId(record.getPatientId())) {
            System.out.println("Invalid patient ID.");
            return false;
        }
        
        if (!ValidationUtils.isValidId(record.getDoctorId())) {
            System.out.println("Invalid doctor ID.");
            return false;
        }
        
        if (!ValidationUtils.isNotEmpty(record.getChiefComplaint())) {
            System.out.println("Chief complaint is required.");
            return false;
        }
        
        // Validate that patient exists
        Patient patient = patientService.findPatientById(record.getPatientId());
        if (patient == null) {
            System.out.println("Patient with ID " + record.getPatientId() + " not found.");
            return false;
        }
        
        // Validate that doctor exists
        if (staffService.findDoctorById(record.getDoctorId()) == null) {
            System.out.println("Doctor with ID " + record.getDoctorId() + " not found.");
            return false;
        }
        
        // Validate vital signs if provided
        if (record.getHeight() > 0 && (record.getHeight() < 30 || record.getHeight() > 300)) {
            System.out.println("Invalid height. Must be between 30-300 cm.");
            return false;
        }
        
        if (record.getWeight() > 0 && (record.getWeight() < 1 || record.getWeight() > 500)) {
            System.out.println("Invalid weight. Must be between 1-500 kg.");
            return false;
        }
        
        if (record.getTemperature() > 0 && (record.getTemperature() < 30 || record.getTemperature() > 45)) {
            System.out.println("Invalid temperature. Must be between 30-45°C.");
            return false;
        }
        
        if (record.getHeartRate() > 0 && (record.getHeartRate() < 30 || record.getHeartRate() > 200)) {
            System.out.println("Invalid heart rate. Must be between 30-200 bpm.");
            return false;
        }
        
        return true;
    }
    
    /**
     * Get health record statistics
     */
    public void printHealthRecordStatistics() {
        int totalRecords = healthRecords.size();
        int activeRecords = (int) healthRecords.stream().filter(HealthRecord::isActive).count();
        int recordsWithFollowUp = (int) healthRecords.stream().filter(HealthRecord::hasFollowUp).count();
        
        System.out.println("\n=== Health Record Statistics ===");
        System.out.println("Total Records: " + totalRecords);
        System.out.println("Active Records: " + activeRecords);
        System.out.println("Records with Follow-up: " + recordsWithFollowUp);
        System.out.println("================================");
    }
}
