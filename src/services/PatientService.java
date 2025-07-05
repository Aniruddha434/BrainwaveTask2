package services;

import models.Patient;
import utils.DatabaseManager;
import utils.ValidationUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * PatientService class for managing patient operations
 */
public class PatientService {
    private static final String PATIENTS_FILE = "patients.dat";
    private DatabaseManager dbManager;
    private List<Patient> patients;
    
    public PatientService() {
        this.dbManager = DatabaseManager.getInstance();
        this.patients = loadPatients();
    }
    
    /**
     * Load patients from file
     */
    private List<Patient> loadPatients() {
        return dbManager.loadData(PATIENTS_FILE);
    }
    
    /**
     * Save patients to file
     */
    private boolean savePatients() {
        return dbManager.saveData(patients, PATIENTS_FILE);
    }
    
    /**
     * Register a new patient
     */
    public boolean registerPatient(Patient patient) {
        if (patient == null) {
            System.out.println("Patient cannot be null.");
            return false;
        }
        
        // Validate patient data
        if (!validatePatient(patient)) {
            return false;
        }
        
        // Check if patient ID already exists
        if (findPatientById(patient.getPatientId()) != null) {
            System.out.println("Patient with ID " + patient.getPatientId() + " already exists.");
            return false;
        }
        
        // Add patient to list
        patients.add(patient);
        
        // Save to file
        if (savePatients()) {
            System.out.println("Patient registered successfully: " + patient.getFullName());
            return true;
        } else {
            // Remove from list if save failed
            patients.remove(patient);
            System.out.println("Failed to save patient data.");
            return false;
        }
    }
    
    /**
     * Update an existing patient
     */
    public boolean updatePatient(Patient updatedPatient) {
        if (updatedPatient == null) {
            System.out.println("Patient cannot be null.");
            return false;
        }
        
        // Validate patient data
        if (!validatePatient(updatedPatient)) {
            return false;
        }
        
        // Find existing patient
        Patient existingPatient = findPatientById(updatedPatient.getPatientId());
        if (existingPatient == null) {
            System.out.println("Patient with ID " + updatedPatient.getPatientId() + " not found.");
            return false;
        }
        
        // Update patient data
        int index = patients.indexOf(existingPatient);
        patients.set(index, updatedPatient);
        
        // Save to file
        if (savePatients()) {
            System.out.println("Patient updated successfully: " + updatedPatient.getFullName());
            return true;
        } else {
            // Revert changes if save failed
            patients.set(index, existingPatient);
            System.out.println("Failed to save patient data.");
            return false;
        }
    }
    
    /**
     * Find patient by ID
     */
    public Patient findPatientById(String patientId) {
        if (!ValidationUtils.isNotEmpty(patientId)) {
            return null;
        }
        
        return patients.stream()
                .filter(patient -> patient.getPatientId().equalsIgnoreCase(patientId.trim()))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Search patients by name
     */
    public List<Patient> searchPatientsByName(String name) {
        if (!ValidationUtils.isNotEmpty(name)) {
            return new ArrayList<>();
        }
        
        String searchName = name.trim().toLowerCase();
        return patients.stream()
                .filter(patient -> 
                    patient.getFirstName().toLowerCase().contains(searchName) ||
                    patient.getLastName().toLowerCase().contains(searchName) ||
                    patient.getFullName().toLowerCase().contains(searchName))
                .collect(Collectors.toList());
    }
    
    /**
     * Search patients by phone number
     */
    public List<Patient> searchPatientsByPhone(String phone) {
        if (!ValidationUtils.isNotEmpty(phone)) {
            return new ArrayList<>();
        }
        
        String searchPhone = ValidationUtils.formatPhoneNumber(phone);
        return patients.stream()
                .filter(patient -> 
                    ValidationUtils.formatPhoneNumber(patient.getPhoneNumber()).contains(searchPhone))
                .collect(Collectors.toList());
    }
    
    /**
     * Get all active patients
     */
    public List<Patient> getAllActivePatients() {
        return patients.stream()
                .filter(Patient::isActive)
                .collect(Collectors.toList());
    }
    
    /**
     * Get all patients
     */
    public List<Patient> getAllPatients() {
        return new ArrayList<>(patients);
    }
    
    /**
     * Deactivate a patient
     */
    public boolean deactivatePatient(String patientId) {
        Patient patient = findPatientById(patientId);
        if (patient == null) {
            System.out.println("Patient with ID " + patientId + " not found.");
            return false;
        }
        
        patient.setActive(false);
        if (savePatients()) {
            System.out.println("Patient deactivated: " + patient.getFullName());
            return true;
        } else {
            patient.setActive(true); // Revert change
            System.out.println("Failed to save patient data.");
            return false;
        }
    }
    
    /**
     * Activate a patient
     */
    public boolean activatePatient(String patientId) {
        Patient patient = findPatientById(patientId);
        if (patient == null) {
            System.out.println("Patient with ID " + patientId + " not found.");
            return false;
        }
        
        patient.setActive(true);
        if (savePatients()) {
            System.out.println("Patient activated: " + patient.getFullName());
            return true;
        } else {
            patient.setActive(false); // Revert change
            System.out.println("Failed to save patient data.");
            return false;
        }
    }
    
    /**
     * Generate next patient ID
     */
    public String generatePatientId() {
        int maxId = 0;
        for (Patient patient : patients) {
            String id = patient.getPatientId();
            if (id.startsWith("P") && id.length() > 1) {
                try {
                    int numId = Integer.parseInt(id.substring(1));
                    maxId = Math.max(maxId, numId);
                } catch (NumberFormatException e) {
                    // Ignore invalid IDs
                }
            }
        }
        return "P" + String.format("%04d", maxId + 1);
    }
    
    /**
     * Validate patient data
     */
    private boolean validatePatient(Patient patient) {
        // Validate required fields
        if (!ValidationUtils.isValidId(patient.getPatientId())) {
            System.out.println("Invalid patient ID.");
            return false;
        }
        
        if (!ValidationUtils.isValidName(patient.getFirstName())) {
            System.out.println("Invalid first name.");
            return false;
        }
        
        if (!ValidationUtils.isValidName(patient.getLastName())) {
            System.out.println("Invalid last name.");
            return false;
        }
        
        if (!ValidationUtils.isValidBirthDate(patient.getDateOfBirth())) {
            System.out.println("Invalid birth date.");
            return false;
        }
        
        if (!ValidationUtils.isValidGender(patient.getGender())) {
            System.out.println("Invalid gender. Must be Male, Female, or Other.");
            return false;
        }
        
        if (!ValidationUtils.isValidPhoneNumber(patient.getPhoneNumber())) {
            System.out.println("Invalid phone number.");
            return false;
        }
        
        // Validate optional fields if provided
        if (ValidationUtils.isNotEmpty(patient.getEmail()) && 
            !ValidationUtils.isValidEmail(patient.getEmail())) {
            System.out.println("Invalid email address.");
            return false;
        }
        
        if (ValidationUtils.isNotEmpty(patient.getBloodGroup()) && 
            !ValidationUtils.isValidBloodGroup(patient.getBloodGroup())) {
            System.out.println("Invalid blood group.");
            return false;
        }
        
        return true;
    }
    
    /**
     * Get patient statistics
     */
    public void printPatientStatistics() {
        int totalPatients = patients.size();
        int activePatients = (int) patients.stream().filter(Patient::isActive).count();
        int inactivePatients = totalPatients - activePatients;
        
        System.out.println("\n=== Patient Statistics ===");
        System.out.println("Total Patients: " + totalPatients);
        System.out.println("Active Patients: " + activePatients);
        System.out.println("Inactive Patients: " + inactivePatients);
        System.out.println("==========================");
    }
}
