package services;

import models.Doctor;
import models.Staff;
import utils.DatabaseManager;
import utils.ValidationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * StaffService class for managing staff operations
 */
public class StaffService {
    private static final String STAFF_FILE = "staff.dat";
    private static final String DOCTORS_FILE = "doctors.dat";
    private DatabaseManager dbManager;
    private List<Staff> staffMembers;
    private List<Doctor> doctors;
    
    public StaffService() {
        this.dbManager = DatabaseManager.getInstance();
        this.staffMembers = loadStaff();
        this.doctors = loadDoctors();
    }
    
    /**
     * Load staff from file
     */
    private List<Staff> loadStaff() {
        return dbManager.loadData(STAFF_FILE);
    }
    
    /**
     * Load doctors from file
     */
    private List<Doctor> loadDoctors() {
        return dbManager.loadData(DOCTORS_FILE);
    }
    
    /**
     * Save staff to file
     */
    private boolean saveStaff() {
        return dbManager.saveData(staffMembers, STAFF_FILE);
    }
    
    /**
     * Save doctors to file
     */
    private boolean saveDoctors() {
        return dbManager.saveData(doctors, DOCTORS_FILE);
    }
    
    /**
     * Add a new staff member
     */
    public boolean addStaff(Staff staff) {
        if (staff == null) {
            System.out.println("Staff cannot be null.");
            return false;
        }
        
        // Validate staff data
        if (!validateStaff(staff)) {
            return false;
        }
        
        // Check if staff ID already exists
        if (findStaffById(staff.getStaffId()) != null) {
            System.out.println("Staff with ID " + staff.getStaffId() + " already exists.");
            return false;
        }
        
        // Add staff to list
        staffMembers.add(staff);
        
        // Save to file
        if (saveStaff()) {
            System.out.println("Staff added successfully: " + staff.getFullName());
            return true;
        } else {
            // Remove from list if save failed
            staffMembers.remove(staff);
            System.out.println("Failed to save staff data.");
            return false;
        }
    }
    
    /**
     * Add a new doctor
     */
    public boolean addDoctor(Doctor doctor) {
        if (doctor == null) {
            System.out.println("Doctor cannot be null.");
            return false;
        }
        
        // Validate doctor data
        if (!validateDoctor(doctor)) {
            return false;
        }
        
        // Check if doctor ID already exists
        if (findDoctorById(doctor.getDoctorId()) != null) {
            System.out.println("Doctor with ID " + doctor.getDoctorId() + " already exists.");
            return false;
        }
        
        // Add doctor to list
        doctors.add(doctor);
        
        // Save to file
        if (saveDoctors()) {
            System.out.println("Doctor added successfully: " + doctor.getFullName());
            return true;
        } else {
            // Remove from list if save failed
            doctors.remove(doctor);
            System.out.println("Failed to save doctor data.");
            return false;
        }
    }
    
    /**
     * Update an existing staff member
     */
    public boolean updateStaff(Staff updatedStaff) {
        if (updatedStaff == null) {
            System.out.println("Staff cannot be null.");
            return false;
        }
        
        // Validate staff data
        if (!validateStaff(updatedStaff)) {
            return false;
        }
        
        // Find existing staff
        Staff existingStaff = findStaffById(updatedStaff.getStaffId());
        if (existingStaff == null) {
            System.out.println("Staff with ID " + updatedStaff.getStaffId() + " not found.");
            return false;
        }
        
        // Update staff data
        int index = staffMembers.indexOf(existingStaff);
        staffMembers.set(index, updatedStaff);
        
        // Save to file
        if (saveStaff()) {
            System.out.println("Staff updated successfully: " + updatedStaff.getFullName());
            return true;
        } else {
            // Revert changes if save failed
            staffMembers.set(index, existingStaff);
            System.out.println("Failed to save staff data.");
            return false;
        }
    }
    
    /**
     * Update an existing doctor
     */
    public boolean updateDoctor(Doctor updatedDoctor) {
        if (updatedDoctor == null) {
            System.out.println("Doctor cannot be null.");
            return false;
        }
        
        // Validate doctor data
        if (!validateDoctor(updatedDoctor)) {
            return false;
        }
        
        // Find existing doctor
        Doctor existingDoctor = findDoctorById(updatedDoctor.getDoctorId());
        if (existingDoctor == null) {
            System.out.println("Doctor with ID " + updatedDoctor.getDoctorId() + " not found.");
            return false;
        }
        
        // Update doctor data
        int index = doctors.indexOf(existingDoctor);
        doctors.set(index, updatedDoctor);
        
        // Save to file
        if (saveDoctors()) {
            System.out.println("Doctor updated successfully: " + updatedDoctor.getFullName());
            return true;
        } else {
            // Revert changes if save failed
            doctors.set(index, existingDoctor);
            System.out.println("Failed to save doctor data.");
            return false;
        }
    }
    
    /**
     * Find staff by ID
     */
    public Staff findStaffById(String staffId) {
        if (!ValidationUtils.isNotEmpty(staffId)) {
            return null;
        }
        
        return staffMembers.stream()
                .filter(staff -> staff.getStaffId().equalsIgnoreCase(staffId.trim()))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Find doctor by ID
     */
    public Doctor findDoctorById(String doctorId) {
        if (!ValidationUtils.isNotEmpty(doctorId)) {
            return null;
        }
        
        return doctors.stream()
                .filter(doctor -> doctor.getDoctorId().equalsIgnoreCase(doctorId.trim()))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Search staff by name
     */
    public List<Staff> searchStaffByName(String name) {
        if (!ValidationUtils.isNotEmpty(name)) {
            return new ArrayList<>();
        }
        
        String searchName = name.trim().toLowerCase();
        return staffMembers.stream()
                .filter(staff -> 
                    staff.getFirstName().toLowerCase().contains(searchName) ||
                    staff.getLastName().toLowerCase().contains(searchName) ||
                    staff.getFullName().toLowerCase().contains(searchName))
                .collect(Collectors.toList());
    }
    
    /**
     * Search doctors by name
     */
    public List<Doctor> searchDoctorsByName(String name) {
        if (!ValidationUtils.isNotEmpty(name)) {
            return new ArrayList<>();
        }
        
        String searchName = name.trim().toLowerCase();
        return doctors.stream()
                .filter(doctor -> 
                    doctor.getFirstName().toLowerCase().contains(searchName) ||
                    doctor.getLastName().toLowerCase().contains(searchName) ||
                    doctor.getFullName().toLowerCase().contains(searchName))
                .collect(Collectors.toList());
    }
    
    /**
     * Get doctors by specialization
     */
    public List<Doctor> getDoctorsBySpecialization(String specialization) {
        if (!ValidationUtils.isNotEmpty(specialization)) {
            return new ArrayList<>();
        }
        
        return doctors.stream()
                .filter(doctor -> doctor.getSpecialization().equalsIgnoreCase(specialization.trim()))
                .collect(Collectors.toList());
    }
    
    /**
     * Get staff by role
     */
    public List<Staff> getStaffByRole(Staff.StaffRole role) {
        if (role == null) {
            return new ArrayList<>();
        }
        
        return staffMembers.stream()
                .filter(staff -> staff.getRole() == role)
                .collect(Collectors.toList());
    }
    
    /**
     * Get staff by department
     */
    public List<Staff> getStaffByDepartment(String department) {
        if (!ValidationUtils.isNotEmpty(department)) {
            return new ArrayList<>();
        }
        
        return staffMembers.stream()
                .filter(staff -> staff.getDepartment().equalsIgnoreCase(department.trim()))
                .collect(Collectors.toList());
    }
    
    /**
     * Get all active staff
     */
    public List<Staff> getAllActiveStaff() {
        return staffMembers.stream()
                .filter(Staff::isActive)
                .collect(Collectors.toList());
    }
    
    /**
     * Get all available doctors
     */
    public List<Doctor> getAllAvailableDoctors() {
        return doctors.stream()
                .filter(Doctor::isAvailable)
                .collect(Collectors.toList());
    }
    
    /**
     * Get all staff
     */
    public List<Staff> getAllStaff() {
        return new ArrayList<>(staffMembers);
    }
    
    /**
     * Get all doctors
     */
    public List<Doctor> getAllDoctors() {
        return new ArrayList<>(doctors);
    }
    
    /**
     * Generate next staff ID
     */
    public String generateStaffId() {
        int maxId = 0;
        for (Staff staff : staffMembers) {
            String id = staff.getStaffId();
            if (id.startsWith("S") && id.length() > 1) {
                try {
                    int numId = Integer.parseInt(id.substring(1));
                    maxId = Math.max(maxId, numId);
                } catch (NumberFormatException e) {
                    // Ignore invalid IDs
                }
            }
        }
        return "S" + String.format("%04d", maxId + 1);
    }
    
    /**
     * Generate next doctor ID
     */
    public String generateDoctorId() {
        int maxId = 0;
        for (Doctor doctor : doctors) {
            String id = doctor.getDoctorId();
            if (id.startsWith("D") && id.length() > 1) {
                try {
                    int numId = Integer.parseInt(id.substring(1));
                    maxId = Math.max(maxId, numId);
                } catch (NumberFormatException e) {
                    // Ignore invalid IDs
                }
            }
        }
        return "D" + String.format("%04d", maxId + 1);
    }
    
    /**
     * Validate staff data
     */
    private boolean validateStaff(Staff staff) {
        // Validate required fields
        if (!ValidationUtils.isValidId(staff.getStaffId())) {
            System.out.println("Invalid staff ID.");
            return false;
        }
        
        if (!ValidationUtils.isValidName(staff.getFirstName())) {
            System.out.println("Invalid first name.");
            return false;
        }
        
        if (!ValidationUtils.isValidName(staff.getLastName())) {
            System.out.println("Invalid last name.");
            return false;
        }
        
        if (staff.getRole() == null) {
            System.out.println("Staff role is required.");
            return false;
        }
        
        if (!ValidationUtils.isNotEmpty(staff.getDepartment())) {
            System.out.println("Department is required.");
            return false;
        }
        
        // Validate optional fields if provided
        if (ValidationUtils.isNotEmpty(staff.getPhoneNumber()) && 
            !ValidationUtils.isValidPhoneNumber(staff.getPhoneNumber())) {
            System.out.println("Invalid phone number.");
            return false;
        }
        
        if (ValidationUtils.isNotEmpty(staff.getEmail()) && 
            !ValidationUtils.isValidEmail(staff.getEmail())) {
            System.out.println("Invalid email address.");
            return false;
        }
        
        if (staff.getSalary() > 0 && !ValidationUtils.isValidSalary(staff.getSalary())) {
            System.out.println("Invalid salary amount.");
            return false;
        }
        
        return true;
    }
    
    /**
     * Validate doctor data
     */
    private boolean validateDoctor(Doctor doctor) {
        // Validate required fields
        if (!ValidationUtils.isValidId(doctor.getDoctorId())) {
            System.out.println("Invalid doctor ID.");
            return false;
        }
        
        if (!ValidationUtils.isValidName(doctor.getFirstName())) {
            System.out.println("Invalid first name.");
            return false;
        }
        
        if (!ValidationUtils.isValidName(doctor.getLastName())) {
            System.out.println("Invalid last name.");
            return false;
        }
        
        if (!ValidationUtils.isNotEmpty(doctor.getSpecialization())) {
            System.out.println("Specialization is required.");
            return false;
        }
        
        // Validate optional fields if provided
        if (ValidationUtils.isNotEmpty(doctor.getPhoneNumber()) && 
            !ValidationUtils.isValidPhoneNumber(doctor.getPhoneNumber())) {
            System.out.println("Invalid phone number.");
            return false;
        }
        
        if (ValidationUtils.isNotEmpty(doctor.getEmail()) && 
            !ValidationUtils.isValidEmail(doctor.getEmail())) {
            System.out.println("Invalid email address.");
            return false;
        }
        
        if (doctor.getConsultationFee() > 0 && 
            !ValidationUtils.isValidConsultationFee(doctor.getConsultationFee())) {
            System.out.println("Invalid consultation fee.");
            return false;
        }
        
        return true;
    }
}
