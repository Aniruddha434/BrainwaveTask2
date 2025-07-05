package models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * HealthRecord model class representing a patient's electronic health record
 */
public class HealthRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String recordId;
    private String patientId;
    private String doctorId;
    private LocalDateTime visitDate;
    private String chiefComplaint;
    private String symptoms;
    private String diagnosis;
    private String treatment;
    private List<String> prescriptions;
    private String labResults;
    private String notes;
    private double height; // in cm
    private double weight; // in kg
    private String bloodPressure;
    private double temperature; // in Celsius
    private int heartRate;
    private String followUpInstructions;
    private LocalDateTime nextVisitDate;
    private boolean isActive;
    
    // Constructors
    public HealthRecord() {
        this.prescriptions = new ArrayList<>();
        this.visitDate = LocalDateTime.now();
        this.isActive = true;
    }
    
    public HealthRecord(String recordId, String patientId, String doctorId, String chiefComplaint) {
        this();
        this.recordId = recordId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.chiefComplaint = chiefComplaint;
    }
    
    // Getters and Setters
    public String getRecordId() { return recordId; }
    public void setRecordId(String recordId) { this.recordId = recordId; }
    
    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    
    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }
    
    public LocalDateTime getVisitDate() { return visitDate; }
    public void setVisitDate(LocalDateTime visitDate) { this.visitDate = visitDate; }
    
    public String getChiefComplaint() { return chiefComplaint; }
    public void setChiefComplaint(String chiefComplaint) { this.chiefComplaint = chiefComplaint; }
    
    public String getSymptoms() { return symptoms; }
    public void setSymptoms(String symptoms) { this.symptoms = symptoms; }
    
    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    
    public String getTreatment() { return treatment; }
    public void setTreatment(String treatment) { this.treatment = treatment; }
    
    public List<String> getPrescriptions() { return prescriptions; }
    public void setPrescriptions(List<String> prescriptions) { this.prescriptions = prescriptions; }
    public void addPrescription(String prescription) { this.prescriptions.add(prescription); }
    
    public String getLabResults() { return labResults; }
    public void setLabResults(String labResults) { this.labResults = labResults; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }
    
    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }
    
    public String getBloodPressure() { return bloodPressure; }
    public void setBloodPressure(String bloodPressure) { this.bloodPressure = bloodPressure; }
    
    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }
    
    public int getHeartRate() { return heartRate; }
    public void setHeartRate(int heartRate) { this.heartRate = heartRate; }
    
    public String getFollowUpInstructions() { return followUpInstructions; }
    public void setFollowUpInstructions(String followUpInstructions) { this.followUpInstructions = followUpInstructions; }
    
    public LocalDateTime getNextVisitDate() { return nextVisitDate; }
    public void setNextVisitDate(LocalDateTime nextVisitDate) { this.nextVisitDate = nextVisitDate; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    // Utility methods
    public double getBMI() {
        if (height > 0 && weight > 0) {
            double heightInMeters = height / 100.0;
            return weight / (heightInMeters * heightInMeters);
        }
        return 0.0;
    }
    
    public String getBMICategory() {
        double bmi = getBMI();
        if (bmi < 18.5) return "Underweight";
        else if (bmi < 25) return "Normal weight";
        else if (bmi < 30) return "Overweight";
        else return "Obese";
    }
    
    public boolean hasFollowUp() {
        return nextVisitDate != null && nextVisitDate.isAfter(LocalDateTime.now());
    }
    
    @Override
    public String toString() {
        return String.format("HealthRecord{ID='%s', Patient='%s', Doctor='%s', Date='%s', Diagnosis='%s'}", 
                           recordId, patientId, doctorId, visitDate, diagnosis);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        HealthRecord record = (HealthRecord) obj;
        return recordId != null ? recordId.equals(record.recordId) : record.recordId == null;
    }
    
    @Override
    public int hashCode() {
        return recordId != null ? recordId.hashCode() : 0;
    }
}
