package main;

import models.*;
import services.*;
import utils.DateUtils;
import utils.ValidationUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

/**
 * Main Hospital Management System Application
 */
public class HospitalManagementSystem {
    private Scanner scanner;
    private PatientService patientService;
    private StaffService staffService;
    private AppointmentService appointmentService;
    private EHRService ehrService;
    private BillingService billingService;
    private InventoryService inventoryService;
    
    public HospitalManagementSystem() {
        this.scanner = new Scanner(System.in);
        initializeServices();
    }
    
    private void initializeServices() {
        System.out.println("Initializing Hospital Management System...");
        
        // Initialize services in dependency order
        this.patientService = new PatientService();
        this.staffService = new StaffService();
        this.appointmentService = new AppointmentService(patientService, staffService);
        this.ehrService = new EHRService(patientService, staffService);
        this.billingService = new BillingService(patientService, appointmentService);
        this.inventoryService = new InventoryService();
        
        System.out.println("System initialized successfully!");
    }
    
    public void run() {
        System.out.println("\n=== Welcome to Hospital Management System ===");
        
        while (true) {
            displayMainMenu();
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    handlePatientManagement();
                    break;
                case 2:
                    handleAppointmentManagement();
                    break;
                case 3:
                    handleHealthRecords();
                    break;
                case 4:
                    handleBillingAndInvoicing();
                    break;
                case 5:
                    handleInventoryManagement();
                    break;
                case 6:
                    handleStaffManagement();
                    break;
                case 7:
                    displaySystemStatistics();
                    break;
                case 8:
                    displaySystemAlerts();
                    break;
                case 0:
                    System.out.println("Thank you for using Hospital Management System!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private void displayMainMenu() {
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("1. Patient Management");
        System.out.println("2. Appointment Management");
        System.out.println("3. Electronic Health Records");
        System.out.println("4. Billing and Invoicing");
        System.out.println("5. Inventory Management");
        System.out.println("6. Staff Management");
        System.out.println("7. System Statistics");
        System.out.println("8. System Alerts");
        System.out.println("0. Exit");
        System.out.println("==================");
    }
    
    private void handlePatientManagement() {
        while (true) {
            System.out.println("\n=== PATIENT MANAGEMENT ===");
            System.out.println("1. Register New Patient");
            System.out.println("2. Search Patient");
            System.out.println("3. Update Patient");
            System.out.println("4. View All Patients");
            System.out.println("5. Patient Statistics");
            System.out.println("0. Back to Main Menu");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    registerNewPatient();
                    break;
                case 2:
                    searchPatient();
                    break;
                case 3:
                    updatePatient();
                    break;
                case 4:
                    viewAllPatients();
                    break;
                case 5:
                    patientService.printPatientStatistics();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private void registerNewPatient() {
        System.out.println("\n=== REGISTER NEW PATIENT ===");
        
        String patientId = patientService.generatePatientId();
        System.out.println("Generated Patient ID: " + patientId);
        
        String firstName = getStringInput("First Name: ");
        String lastName = getStringInput("Last Name: ");
        LocalDate birthDate = getDateInput("Date of Birth (YYYY-MM-DD): ");
        String gender = getStringInput("Gender (Male/Female/Other): ");
        String phone = getStringInput("Phone Number: ");
        String email = getStringInput("Email (optional): ");
        String address = getStringInput("Address (optional): ");
        String emergencyContact = getStringInput("Emergency Contact Name (optional): ");
        String emergencyPhone = getStringInput("Emergency Contact Phone (optional): ");
        String bloodGroup = getStringInput("Blood Group (optional, e.g., A+, B-, O+): ");
        
        Patient patient = new Patient(patientId, firstName, lastName, birthDate, gender, phone);
        if (ValidationUtils.isNotEmpty(email)) patient.setEmail(email);
        if (ValidationUtils.isNotEmpty(address)) patient.setAddress(address);
        if (ValidationUtils.isNotEmpty(emergencyContact)) patient.setEmergencyContact(emergencyContact);
        if (ValidationUtils.isNotEmpty(emergencyPhone)) patient.setEmergencyPhone(emergencyPhone);
        if (ValidationUtils.isNotEmpty(bloodGroup)) patient.setBloodGroup(bloodGroup);
        
        if (patientService.registerPatient(patient)) {
            System.out.println("Patient registered successfully!");
        }
    }
    
    private void searchPatient() {
        System.out.println("\n=== SEARCH PATIENT ===");
        System.out.println("1. Search by ID");
        System.out.println("2. Search by Name");
        System.out.println("3. Search by Phone");
        
        int choice = getIntInput("Enter search type: ");
        
        switch (choice) {
            case 1:
                String id = getStringInput("Enter Patient ID: ");
                Patient patient = patientService.findPatientById(id);
                if (patient != null) {
                    displayPatientInfo(patient);
                } else {
                    System.out.println("Patient not found.");
                }
                break;
            case 2:
                String name = getStringInput("Enter Patient Name: ");
                List<Patient> patientsByName = patientService.searchPatientsByName(name);
                displayPatientList(patientsByName);
                break;
            case 3:
                String phone = getStringInput("Enter Phone Number: ");
                List<Patient> patientsByPhone = patientService.searchPatientsByPhone(phone);
                displayPatientList(patientsByPhone);
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }
    
    private void handleAppointmentManagement() {
        while (true) {
            System.out.println("\n=== APPOINTMENT MANAGEMENT ===");
            System.out.println("1. Schedule New Appointment");
            System.out.println("2. View Appointments");
            System.out.println("3. Update Appointment");
            System.out.println("4. Cancel Appointment");
            System.out.println("5. Complete Appointment");
            System.out.println("6. Today's Appointments");
            System.out.println("7. Appointment Statistics");
            System.out.println("0. Back to Main Menu");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    scheduleNewAppointment();
                    break;
                case 2:
                    viewAppointments();
                    break;
                case 3:
                    updateAppointment();
                    break;
                case 4:
                    cancelAppointment();
                    break;
                case 5:
                    completeAppointment();
                    break;
                case 6:
                    viewTodaysAppointments();
                    break;
                case 7:
                    appointmentService.printAppointmentStatistics();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private void scheduleNewAppointment() {
        System.out.println("\n=== SCHEDULE NEW APPOINTMENT ===");
        
        String appointmentId = appointmentService.generateAppointmentId();
        System.out.println("Generated Appointment ID: " + appointmentId);
        
        String patientId = getStringInput("Patient ID: ");
        String doctorId = getStringInput("Doctor ID: ");
        LocalDateTime appointmentDateTime = getDateTimeInput("Appointment Date and Time (YYYY-MM-DD HH:MM): ");
        String reason = getStringInput("Reason for Visit: ");
        
        Appointment appointment = new Appointment(appointmentId, patientId, doctorId, appointmentDateTime, reason);
        
        if (appointmentService.scheduleAppointment(appointment)) {
            System.out.println("Appointment scheduled successfully!");
        }
    }
    
    private void displaySystemStatistics() {
        System.out.println("\n=== SYSTEM STATISTICS ===");
        patientService.printPatientStatistics();
        appointmentService.printAppointmentStatistics();
        ehrService.printHealthRecordStatistics();
        billingService.printBillingStatistics();
        inventoryService.printInventoryStatistics();
    }
    
    private void displaySystemAlerts() {
        System.out.println("\n=== SYSTEM ALERTS ===");
        
        // Inventory alerts
        List<String> inventoryAlerts = inventoryService.generateSupplyAlerts();
        if (!inventoryAlerts.isEmpty()) {
            System.out.println("\nInventory Alerts:");
            inventoryAlerts.forEach(alert -> System.out.println("⚠ " + alert));
        }
        
        // Overdue bills
        List<Bill> overdueBills = billingService.getOverdueBills();
        if (!overdueBills.isEmpty()) {
            System.out.println("\nOverdue Bills:");
            overdueBills.forEach(bill -> 
                System.out.println("⚠ Bill " + bill.getBillId() + " - Patient: " + 
                                 bill.getPatientId() + " - Amount: $" + bill.getBalanceAmount()));
        }
        
        // Follow-up appointments needed
        List<HealthRecord> followUpRecords = ehrService.getRecordsWithFollowUp();
        if (!followUpRecords.isEmpty()) {
            System.out.println("\nFollow-up Required:");
            followUpRecords.forEach(record -> 
                System.out.println("⚠ Patient " + record.getPatientId() + 
                                 " - Next visit: " + DateUtils.formatDateForDisplay(record.getNextVisitDate().toLocalDate())));
        }
        
        if (inventoryAlerts.isEmpty() && overdueBills.isEmpty() && followUpRecords.isEmpty()) {
            System.out.println("No alerts at this time.");
        }
    }
    
    // Utility methods for input handling
    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
    
    private double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
    
    private LocalDate getDateInput(String prompt) {
        while (true) {
            String input = getStringInput(prompt);
            LocalDate date = DateUtils.parseDate(input);
            if (date != null) {
                return date;
            }
            System.out.println("Please enter date in YYYY-MM-DD format.");
        }
    }
    
    private LocalDateTime getDateTimeInput(String prompt) {
        while (true) {
            String input = getStringInput(prompt);
            LocalDateTime dateTime = DateUtils.parseDateTime(input);
            if (dateTime != null) {
                return dateTime;
            }
            System.out.println("Please enter date and time in YYYY-MM-DD HH:MM format.");
        }
    }
    
    private void displayPatientInfo(Patient patient) {
        System.out.println("\n=== PATIENT INFORMATION ===");
        System.out.println("ID: " + patient.getPatientId());
        System.out.println("Name: " + patient.getFullName());
        System.out.println("Age: " + patient.getAge());
        System.out.println("Gender: " + patient.getGender());
        System.out.println("Phone: " + patient.getPhoneNumber());
        System.out.println("Email: " + (patient.getEmail() != null ? patient.getEmail() : "Not provided"));
        System.out.println("Blood Group: " + (patient.getBloodGroup() != null ? patient.getBloodGroup() : "Not provided"));
        System.out.println("Registration Date: " + DateUtils.formatDateForDisplay(patient.getRegistrationDate()));
        System.out.println("Status: " + (patient.isActive() ? "Active" : "Inactive"));
        System.out.println("===========================");
    }
    
    private void displayPatientList(List<Patient> patients) {
        if (patients.isEmpty()) {
            System.out.println("No patients found.");
            return;
        }
        
        System.out.println("\n=== PATIENT LIST ===");
        for (Patient patient : patients) {
            System.out.println(patient.toString());
        }
        System.out.println("====================");
    }
    
    // Placeholder methods for other functionalities
    private void updatePatient() {
        System.out.println("Update Patient functionality - To be implemented");
    }
    
    private void viewAllPatients() {
        List<Patient> allPatients = patientService.getAllActivePatients();
        displayPatientList(allPatients);
    }
    
    private void viewAppointments() {
        System.out.println("View Appointments functionality - To be implemented");
    }
    
    private void updateAppointment() {
        System.out.println("Update Appointment functionality - To be implemented");
    }
    
    private void cancelAppointment() {
        String appointmentId = getStringInput("Enter Appointment ID to cancel: ");
        appointmentService.cancelAppointment(appointmentId);
    }
    
    private void completeAppointment() {
        String appointmentId = getStringInput("Enter Appointment ID to complete: ");
        String notes = getStringInput("Enter completion notes (optional): ");
        appointmentService.completeAppointment(appointmentId, notes);
    }
    
    private void viewTodaysAppointments() {
        List<Appointment> todaysAppointments = appointmentService.getTodaysAppointments();
        System.out.println("\n=== TODAY'S APPOINTMENTS ===");
        if (todaysAppointments.isEmpty()) {
            System.out.println("No appointments scheduled for today.");
        } else {
            todaysAppointments.forEach(System.out::println);
        }
        System.out.println("============================");
    }
    
    private void handleHealthRecords() {
        System.out.println("Health Records functionality - To be implemented");
    }
    
    private void handleBillingAndInvoicing() {
        System.out.println("Billing and Invoicing functionality - To be implemented");
    }
    
    private void handleInventoryManagement() {
        System.out.println("Inventory Management functionality - To be implemented");
    }
    
    private void handleStaffManagement() {
        System.out.println("Staff Management functionality - To be implemented");
    }
    
    public static void main(String[] args) {
        HospitalManagementSystem hms = new HospitalManagementSystem();
        hms.run();
    }
}
