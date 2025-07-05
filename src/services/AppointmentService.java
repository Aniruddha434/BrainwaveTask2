package services;

import models.Appointment;
import models.Doctor;
import models.Patient;
import utils.DatabaseManager;
import utils.DateUtils;
import utils.ValidationUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * AppointmentService class for managing appointment operations
 */
public class AppointmentService {
    private static final String APPOINTMENTS_FILE = "appointments.dat";
    private DatabaseManager dbManager;
    private List<Appointment> appointments;
    private PatientService patientService;
    private StaffService staffService;
    
    public AppointmentService(PatientService patientService, StaffService staffService) {
        this.dbManager = DatabaseManager.getInstance();
        this.appointments = loadAppointments();
        this.patientService = patientService;
        this.staffService = staffService;
    }
    
    /**
     * Load appointments from file
     */
    private List<Appointment> loadAppointments() {
        return dbManager.loadData(APPOINTMENTS_FILE);
    }
    
    /**
     * Save appointments to file
     */
    private boolean saveAppointments() {
        return dbManager.saveData(appointments, APPOINTMENTS_FILE);
    }
    
    /**
     * Schedule a new appointment
     */
    public boolean scheduleAppointment(Appointment appointment) {
        if (appointment == null) {
            System.out.println("Appointment cannot be null.");
            return false;
        }
        
        // Validate appointment data
        if (!validateAppointment(appointment)) {
            return false;
        }
        
        // Check if appointment ID already exists
        if (findAppointmentById(appointment.getAppointmentId()) != null) {
            System.out.println("Appointment with ID " + appointment.getAppointmentId() + " already exists.");
            return false;
        }
        
        // Check for conflicts
        if (hasConflict(appointment)) {
            System.out.println("Appointment conflicts with existing appointment.");
            return false;
        }
        
        // Add appointment to list
        appointments.add(appointment);
        
        // Save to file
        if (saveAppointments()) {
            System.out.println("Appointment scheduled successfully: " + appointment.getAppointmentId());
            return true;
        } else {
            // Remove from list if save failed
            appointments.remove(appointment);
            System.out.println("Failed to save appointment data.");
            return false;
        }
    }
    
    /**
     * Update an existing appointment
     */
    public boolean updateAppointment(Appointment updatedAppointment) {
        if (updatedAppointment == null) {
            System.out.println("Appointment cannot be null.");
            return false;
        }
        
        // Find existing appointment
        Appointment existingAppointment = findAppointmentById(updatedAppointment.getAppointmentId());
        if (existingAppointment == null) {
            System.out.println("Appointment with ID " + updatedAppointment.getAppointmentId() + " not found.");
            return false;
        }
        
        // Validate updated appointment data
        if (!validateAppointment(updatedAppointment)) {
            return false;
        }
        
        // Check for conflicts (excluding current appointment)
        if (hasConflictExcluding(updatedAppointment, existingAppointment.getAppointmentId())) {
            System.out.println("Updated appointment conflicts with existing appointment.");
            return false;
        }
        
        // Update appointment data
        int index = appointments.indexOf(existingAppointment);
        appointments.set(index, updatedAppointment);
        
        // Save to file
        if (saveAppointments()) {
            System.out.println("Appointment updated successfully: " + updatedAppointment.getAppointmentId());
            return true;
        } else {
            // Revert changes if save failed
            appointments.set(index, existingAppointment);
            System.out.println("Failed to save appointment data.");
            return false;
        }
    }
    
    /**
     * Cancel an appointment
     */
    public boolean cancelAppointment(String appointmentId) {
        Appointment appointment = findAppointmentById(appointmentId);
        if (appointment == null) {
            System.out.println("Appointment with ID " + appointmentId + " not found.");
            return false;
        }
        
        if (!appointment.canBeCancelled()) {
            System.out.println("Appointment cannot be cancelled in its current status: " + appointment.getStatus());
            return false;
        }
        
        appointment.setStatus(Appointment.AppointmentStatus.CANCELLED);
        if (saveAppointments()) {
            System.out.println("Appointment cancelled: " + appointmentId);
            return true;
        } else {
            System.out.println("Failed to save appointment data.");
            return false;
        }
    }
    
    /**
     * Complete an appointment
     */
    public boolean completeAppointment(String appointmentId, String notes) {
        Appointment appointment = findAppointmentById(appointmentId);
        if (appointment == null) {
            System.out.println("Appointment with ID " + appointmentId + " not found.");
            return false;
        }
        
        appointment.setStatus(Appointment.AppointmentStatus.COMPLETED);
        if (ValidationUtils.isNotEmpty(notes)) {
            appointment.setNotes(notes);
        }
        
        if (saveAppointments()) {
            System.out.println("Appointment completed: " + appointmentId);
            return true;
        } else {
            System.out.println("Failed to save appointment data.");
            return false;
        }
    }
    
    /**
     * Find appointment by ID
     */
    public Appointment findAppointmentById(String appointmentId) {
        if (!ValidationUtils.isNotEmpty(appointmentId)) {
            return null;
        }
        
        return appointments.stream()
                .filter(appointment -> appointment.getAppointmentId().equalsIgnoreCase(appointmentId.trim()))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Get appointments by patient ID
     */
    public List<Appointment> getAppointmentsByPatient(String patientId) {
        if (!ValidationUtils.isNotEmpty(patientId)) {
            return new ArrayList<>();
        }
        
        return appointments.stream()
                .filter(appointment -> appointment.getPatientId().equalsIgnoreCase(patientId.trim()))
                .collect(Collectors.toList());
    }
    
    /**
     * Get appointments by doctor ID
     */
    public List<Appointment> getAppointmentsByDoctor(String doctorId) {
        if (!ValidationUtils.isNotEmpty(doctorId)) {
            return new ArrayList<>();
        }
        
        return appointments.stream()
                .filter(appointment -> appointment.getDoctorId().equalsIgnoreCase(doctorId.trim()))
                .collect(Collectors.toList());
    }
    
    /**
     * Get appointments by date
     */
    public List<Appointment> getAppointmentsByDate(LocalDate date) {
        if (date == null) {
            return new ArrayList<>();
        }
        
        return appointments.stream()
                .filter(appointment -> appointment.getAppointmentDateTime().toLocalDate().equals(date))
                .collect(Collectors.toList());
    }
    
    /**
     * Get upcoming appointments
     */
    public List<Appointment> getUpcomingAppointments() {
        return appointments.stream()
                .filter(Appointment::isUpcoming)
                .sorted((a1, a2) -> a1.getAppointmentDateTime().compareTo(a2.getAppointmentDateTime()))
                .collect(Collectors.toList());
    }
    
    /**
     * Get today's appointments
     */
    public List<Appointment> getTodaysAppointments() {
        return getAppointmentsByDate(LocalDate.now());
    }
    
    /**
     * Generate next appointment ID
     */
    public String generateAppointmentId() {
        int maxId = 0;
        for (Appointment appointment : appointments) {
            String id = appointment.getAppointmentId();
            if (id.startsWith("A") && id.length() > 1) {
                try {
                    int numId = Integer.parseInt(id.substring(1));
                    maxId = Math.max(maxId, numId);
                } catch (NumberFormatException e) {
                    // Ignore invalid IDs
                }
            }
        }
        return "A" + String.format("%04d", maxId + 1);
    }
    
    /**
     * Check if appointment has conflicts
     */
    private boolean hasConflict(Appointment newAppointment) {
        return hasConflictExcluding(newAppointment, null);
    }
    
    /**
     * Check if appointment has conflicts excluding a specific appointment
     */
    private boolean hasConflictExcluding(Appointment newAppointment, String excludeAppointmentId) {
        LocalDateTime newStart = newAppointment.getAppointmentDateTime();
        LocalDateTime newEnd = newStart.plusMinutes(newAppointment.getDurationInMinutes());
        
        return appointments.stream()
                .filter(existing -> excludeAppointmentId == null || 
                                  !existing.getAppointmentId().equals(excludeAppointmentId))
                .filter(existing -> existing.getDoctorId().equals(newAppointment.getDoctorId()))
                .filter(existing -> existing.getStatus() == Appointment.AppointmentStatus.SCHEDULED ||
                                  existing.getStatus() == Appointment.AppointmentStatus.CONFIRMED)
                .anyMatch(existing -> {
                    LocalDateTime existingStart = existing.getAppointmentDateTime();
                    LocalDateTime existingEnd = existingStart.plusMinutes(existing.getDurationInMinutes());
                    return DateUtils.timePeriodsOverlap(newStart, newEnd, existingStart, existingEnd);
                });
    }
    
    /**
     * Validate appointment data
     */
    private boolean validateAppointment(Appointment appointment) {
        // Validate required fields
        if (!ValidationUtils.isValidId(appointment.getAppointmentId())) {
            System.out.println("Invalid appointment ID.");
            return false;
        }
        
        if (!ValidationUtils.isValidId(appointment.getPatientId())) {
            System.out.println("Invalid patient ID.");
            return false;
        }
        
        if (!ValidationUtils.isValidId(appointment.getDoctorId())) {
            System.out.println("Invalid doctor ID.");
            return false;
        }
        
        if (!ValidationUtils.isValidAppointmentTime(appointment.getAppointmentDateTime())) {
            System.out.println("Invalid appointment time. Must be in the future and during business hours.");
            return false;
        }
        
        // Validate that patient exists
        Patient patient = patientService.findPatientById(appointment.getPatientId());
        if (patient == null) {
            System.out.println("Patient with ID " + appointment.getPatientId() + " not found.");
            return false;
        }
        
        // Validate that doctor exists
        Doctor doctor = staffService.findDoctorById(appointment.getDoctorId());
        if (doctor == null) {
            System.out.println("Doctor with ID " + appointment.getDoctorId() + " not found.");
            return false;
        }
        
        // Set consultation fee from doctor
        appointment.setConsultationFee(doctor.getConsultationFee());
        
        return true;
    }
    
    /**
     * Get appointment statistics
     */
    public void printAppointmentStatistics() {
        int totalAppointments = appointments.size();
        long scheduledCount = appointments.stream().filter(a -> a.getStatus() == Appointment.AppointmentStatus.SCHEDULED).count();
        long completedCount = appointments.stream().filter(a -> a.getStatus() == Appointment.AppointmentStatus.COMPLETED).count();
        long cancelledCount = appointments.stream().filter(a -> a.getStatus() == Appointment.AppointmentStatus.CANCELLED).count();
        
        System.out.println("\n=== Appointment Statistics ===");
        System.out.println("Total Appointments: " + totalAppointments);
        System.out.println("Scheduled: " + scheduledCount);
        System.out.println("Completed: " + completedCount);
        System.out.println("Cancelled: " + cancelledCount);
        System.out.println("==============================");
    }
}
