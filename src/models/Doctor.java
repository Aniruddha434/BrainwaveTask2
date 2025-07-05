package models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Doctor model class representing a hospital doctor
 */
public class Doctor implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String doctorId;
    private String firstName;
    private String lastName;
    private String specialization;
    private String qualification;
    private String phoneNumber;
    private String email;
    private String department;
    private double consultationFee;
    private LocalTime startTime;
    private LocalTime endTime;
    private List<String> workingDays;
    private int experienceYears;
    private String licenseNumber;
    private boolean isAvailable;
    private LocalDate joinDate;
    
    // Constructors
    public Doctor() {
        this.workingDays = new ArrayList<>();
        this.isAvailable = true;
        this.joinDate = LocalDate.now();
    }
    
    public Doctor(String doctorId, String firstName, String lastName, 
                  String specialization, String phoneNumber) {
        this();
        this.doctorId = doctorId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialization = specialization;
        this.phoneNumber = phoneNumber;
    }
    
    // Getters and Setters
    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    
    public String getQualification() { return qualification; }
    public void setQualification(String qualification) { this.qualification = qualification; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public double getConsultationFee() { return consultationFee; }
    public void setConsultationFee(double consultationFee) { this.consultationFee = consultationFee; }
    
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    
    public List<String> getWorkingDays() { return workingDays; }
    public void setWorkingDays(List<String> workingDays) { this.workingDays = workingDays; }
    public void addWorkingDay(String day) { this.workingDays.add(day); }
    
    public int getExperienceYears() { return experienceYears; }
    public void setExperienceYears(int experienceYears) { this.experienceYears = experienceYears; }
    
    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }
    
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
    
    public LocalDate getJoinDate() { return joinDate; }
    public void setJoinDate(LocalDate joinDate) { this.joinDate = joinDate; }
    
    // Utility methods
    public String getFullName() {
        return "Dr. " + firstName + " " + lastName;
    }
    
    public boolean isWorkingDay(String day) {
        return workingDays.contains(day.toUpperCase());
    }
    
    public boolean isWorkingTime(LocalTime time) {
        return time.isAfter(startTime) && time.isBefore(endTime);
    }
    
    @Override
    public String toString() {
        return String.format("Doctor{ID='%s', Name='%s', Specialization='%s', Fee=%.2f}", 
                           doctorId, getFullName(), specialization, consultationFee);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Doctor doctor = (Doctor) obj;
        return doctorId != null ? doctorId.equals(doctor.doctorId) : doctor.doctorId == null;
    }
    
    @Override
    public int hashCode() {
        return doctorId != null ? doctorId.hashCode() : 0;
    }
}
