package models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Staff model class representing hospital staff members
 */
public class Staff implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public enum StaffRole {
        DOCTOR, NURSE, ADMINISTRATOR, TECHNICIAN, PHARMACIST, 
        RECEPTIONIST, SECURITY, MAINTENANCE, MANAGER
    }
    
    public enum EmploymentStatus {
        ACTIVE, INACTIVE, ON_LEAVE, TERMINATED, RETIRED
    }
    
    private String staffId;
    private String firstName;
    private String lastName;
    private StaffRole role;
    private String department;
    private String phoneNumber;
    private String email;
    private String address;
    private LocalDate dateOfBirth;
    private String gender;
    private LocalDate hireDate;
    private double salary;
    private String qualification;
    private String licenseNumber;
    private List<String> specializations;
    private LocalTime shiftStartTime;
    private LocalTime shiftEndTime;
    private List<String> workingDays;
    private EmploymentStatus employmentStatus;
    private String emergencyContact;
    private String emergencyPhone;
    private String notes;
    private boolean isActive;
    
    // Constructors
    public Staff() {
        this.specializations = new ArrayList<>();
        this.workingDays = new ArrayList<>();
        this.employmentStatus = EmploymentStatus.ACTIVE;
        this.hireDate = LocalDate.now();
        this.isActive = true;
    }
    
    public Staff(String staffId, String firstName, String lastName, 
                 StaffRole role, String department) {
        this();
        this.staffId = staffId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.department = department;
    }
    
    // Getters and Setters
    public String getStaffId() { return staffId; }
    public void setStaffId(String staffId) { this.staffId = staffId; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public StaffRole getRole() { return role; }
    public void setRole(StaffRole role) { this.role = role; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    public LocalDate getHireDate() { return hireDate; }
    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }
    
    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }
    
    public String getQualification() { return qualification; }
    public void setQualification(String qualification) { this.qualification = qualification; }
    
    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }
    
    public List<String> getSpecializations() { return specializations; }
    public void setSpecializations(List<String> specializations) { this.specializations = specializations; }
    public void addSpecialization(String specialization) { this.specializations.add(specialization); }
    
    public LocalTime getShiftStartTime() { return shiftStartTime; }
    public void setShiftStartTime(LocalTime shiftStartTime) { this.shiftStartTime = shiftStartTime; }
    
    public LocalTime getShiftEndTime() { return shiftEndTime; }
    public void setShiftEndTime(LocalTime shiftEndTime) { this.shiftEndTime = shiftEndTime; }
    
    public List<String> getWorkingDays() { return workingDays; }
    public void setWorkingDays(List<String> workingDays) { this.workingDays = workingDays; }
    public void addWorkingDay(String day) { this.workingDays.add(day); }
    
    public EmploymentStatus getEmploymentStatus() { return employmentStatus; }
    public void setEmploymentStatus(EmploymentStatus employmentStatus) { this.employmentStatus = employmentStatus; }
    
    public String getEmergencyContact() { return emergencyContact; }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }
    
    public String getEmergencyPhone() { return emergencyPhone; }
    public void setEmergencyPhone(String emergencyPhone) { this.emergencyPhone = emergencyPhone; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    // Utility methods
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    public int getAge() {
        if (dateOfBirth != null) {
            return LocalDate.now().getYear() - dateOfBirth.getYear();
        }
        return 0;
    }
    
    public int getYearsOfService() {
        return LocalDate.now().getYear() - hireDate.getYear();
    }
    
    public boolean isWorkingDay(String day) {
        return workingDays.contains(day.toUpperCase());
    }
    
    public boolean isWorkingTime(LocalTime time) {
        if (shiftStartTime != null && shiftEndTime != null) {
            return time.isAfter(shiftStartTime) && time.isBefore(shiftEndTime);
        }
        return false;
    }
    
    public boolean isDoctor() {
        return role == StaffRole.DOCTOR;
    }
    
    public boolean isNurse() {
        return role == StaffRole.NURSE;
    }
    
    public boolean isAdministrator() {
        return role == StaffRole.ADMINISTRATOR;
    }
    
    @Override
    public String toString() {
        return String.format("Staff{ID='%s', Name='%s', Role='%s', Department='%s'}", 
                           staffId, getFullName(), role, department);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Staff staff = (Staff) obj;
        return staffId != null ? staffId.equals(staff.staffId) : staff.staffId == null;
    }
    
    @Override
    public int hashCode() {
        return staffId != null ? staffId.hashCode() : 0;
    }
}
