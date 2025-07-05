package models;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Appointment model class representing a patient-doctor appointment
 */
public class Appointment implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public enum AppointmentStatus {
        SCHEDULED, CONFIRMED, IN_PROGRESS, COMPLETED, CANCELLED, NO_SHOW
    }
    
    private String appointmentId;
    private String patientId;
    private String doctorId;
    private LocalDateTime appointmentDateTime;
    private String reason;
    private String notes;
    private AppointmentStatus status;
    private double consultationFee;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    
    // Constructors
    public Appointment() {
        this.status = AppointmentStatus.SCHEDULED;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public Appointment(String appointmentId, String patientId, String doctorId, 
                      LocalDateTime appointmentDateTime, String reason) {
        this();
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDateTime = appointmentDateTime;
        this.reason = reason;
    }
    
    // Getters and Setters
    public String getAppointmentId() { return appointmentId; }
    public void setAppointmentId(String appointmentId) { this.appointmentId = appointmentId; }
    
    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    
    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }
    
    public LocalDateTime getAppointmentDateTime() { return appointmentDateTime; }
    public void setAppointmentDateTime(LocalDateTime appointmentDateTime) { 
        this.appointmentDateTime = appointmentDateTime; 
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getReason() { return reason; }
    public void setReason(String reason) { 
        this.reason = reason; 
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { 
        this.notes = notes; 
        this.updatedAt = LocalDateTime.now();
    }
    
    public AppointmentStatus getStatus() { return status; }
    public void setStatus(AppointmentStatus status) { 
        this.status = status; 
        this.updatedAt = LocalDateTime.now();
    }
    
    public double getConsultationFee() { return consultationFee; }
    public void setConsultationFee(double consultationFee) { this.consultationFee = consultationFee; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    
    // Utility methods
    public boolean isUpcoming() {
        return appointmentDateTime.isAfter(LocalDateTime.now()) && 
               (status == AppointmentStatus.SCHEDULED || status == AppointmentStatus.CONFIRMED);
    }
    
    public boolean isPast() {
        return appointmentDateTime.isBefore(LocalDateTime.now());
    }
    
    public boolean canBeCancelled() {
        return status == AppointmentStatus.SCHEDULED || status == AppointmentStatus.CONFIRMED;
    }
    
    public boolean canBeRescheduled() {
        return status == AppointmentStatus.SCHEDULED || status == AppointmentStatus.CONFIRMED;
    }
    
    public long getDurationInMinutes() {
        // Default appointment duration is 30 minutes
        return 30;
    }
    
    @Override
    public String toString() {
        return String.format("Appointment{ID='%s', Patient='%s', Doctor='%s', DateTime='%s', Status='%s'}", 
                           appointmentId, patientId, doctorId, appointmentDateTime, status);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Appointment appointment = (Appointment) obj;
        return appointmentId != null ? appointmentId.equals(appointment.appointmentId) : appointment.appointmentId == null;
    }
    
    @Override
    public int hashCode() {
        return appointmentId != null ? appointmentId.hashCode() : 0;
    }
}
