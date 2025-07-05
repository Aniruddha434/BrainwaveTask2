package utils;

import models.*;
import services.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

/**
 * SampleDataInitializer class for populating the system with sample data
 */
public class SampleDataInitializer {
    
    public static void initializeSampleData() {
        System.out.println("Initializing sample data...");
        
        // Initialize services
        PatientService patientService = new PatientService();
        StaffService staffService = new StaffService();
        AppointmentService appointmentService = new AppointmentService(patientService, staffService);
        EHRService ehrService = new EHRService(patientService, staffService);
        BillingService billingService = new BillingService(patientService, appointmentService);
        InventoryService inventoryService = new InventoryService();
        
        // Create sample patients
        createSamplePatients(patientService);
        
        // Create sample doctors
        createSampleDoctors(staffService);
        
        // Create sample staff
        createSampleStaff(staffService);
        
        // Create sample appointments
        createSampleAppointments(appointmentService);
        
        // Create sample health records
        createSampleHealthRecords(ehrService);
        
        // Create sample medical supplies
        createSampleMedicalSupplies(inventoryService);
        
        // Create sample bills
        createSampleBills(billingService);
        
        System.out.println("Sample data initialization completed!");
    }
    
    private static void createSamplePatients(PatientService patientService) {
        System.out.println("Creating sample patients...");
        
        // Patient 1
        Patient patient1 = new Patient("P0001", "John", "Doe", 
                                     LocalDate.of(1985, 5, 15), "Male", "555-0101");
        patient1.setEmail("john.doe@email.com");
        patient1.setAddress("123 Main St, City, State 12345");
        patient1.setBloodGroup("O+");
        patient1.setEmergencyContact("Jane Doe");
        patient1.setEmergencyPhone("555-0102");
        patientService.registerPatient(patient1);
        
        // Patient 2
        Patient patient2 = new Patient("P0002", "Sarah", "Johnson", 
                                     LocalDate.of(1990, 8, 22), "Female", "555-0201");
        patient2.setEmail("sarah.johnson@email.com");
        patient2.setAddress("456 Oak Ave, City, State 12345");
        patient2.setBloodGroup("A+");
        patient2.setEmergencyContact("Mike Johnson");
        patient2.setEmergencyPhone("555-0202");
        patientService.registerPatient(patient2);
        
        // Patient 3
        Patient patient3 = new Patient("P0003", "Michael", "Brown", 
                                     LocalDate.of(1978, 12, 3), "Male", "555-0301");
        patient3.setEmail("michael.brown@email.com");
        patient3.setAddress("789 Pine St, City, State 12345");
        patient3.setBloodGroup("B-");
        patient3.setEmergencyContact("Lisa Brown");
        patient3.setEmergencyPhone("555-0302");
        patientService.registerPatient(patient3);
        
        // Patient 4
        Patient patient4 = new Patient("P0004", "Emily", "Davis", 
                                     LocalDate.of(1995, 3, 18), "Female", "555-0401");
        patient4.setEmail("emily.davis@email.com");
        patient4.setAddress("321 Elm St, City, State 12345");
        patient4.setBloodGroup("AB+");
        patient4.setEmergencyContact("Robert Davis");
        patient4.setEmergencyPhone("555-0402");
        patientService.registerPatient(patient4);
        
        // Patient 5
        Patient patient5 = new Patient("P0005", "David", "Wilson", 
                                     LocalDate.of(1982, 7, 9), "Male", "555-0501");
        patient5.setEmail("david.wilson@email.com");
        patient5.setAddress("654 Maple Ave, City, State 12345");
        patient5.setBloodGroup("O-");
        patient5.setEmergencyContact("Mary Wilson");
        patient5.setEmergencyPhone("555-0502");
        patientService.registerPatient(patient5);
    }
    
    private static void createSampleDoctors(StaffService staffService) {
        System.out.println("Creating sample doctors...");
        
        // Doctor 1 - Cardiologist
        Doctor doctor1 = new Doctor("D0001", "James", "Smith", "Cardiology", "555-1001");
        doctor1.setQualification("MD, FACC");
        doctor1.setEmail("dr.smith@hospital.com");
        doctor1.setDepartment("Cardiology");
        doctor1.setConsultationFee(200.00);
        doctor1.setExperienceYears(15);
        doctor1.setStartTime(LocalTime.of(9, 0));
        doctor1.setEndTime(LocalTime.of(17, 0));
        doctor1.setWorkingDays(Arrays.asList("MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"));
        staffService.addDoctor(doctor1);
        
        // Doctor 2 - Pediatrician
        Doctor doctor2 = new Doctor("D0002", "Lisa", "Anderson", "Pediatrics", "555-1002");
        doctor2.setQualification("MD, FAAP");
        doctor2.setEmail("dr.anderson@hospital.com");
        doctor2.setDepartment("Pediatrics");
        doctor2.setConsultationFee(150.00);
        doctor2.setExperienceYears(12);
        doctor2.setStartTime(LocalTime.of(8, 0));
        doctor2.setEndTime(LocalTime.of(16, 0));
        doctor2.setWorkingDays(Arrays.asList("MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"));
        staffService.addDoctor(doctor2);
        
        // Doctor 3 - Orthopedic Surgeon
        Doctor doctor3 = new Doctor("D0003", "Robert", "Johnson", "Orthopedics", "555-1003");
        doctor3.setQualification("MD, FAAOS");
        doctor3.setEmail("dr.johnson@hospital.com");
        doctor3.setDepartment("Orthopedics");
        doctor3.setConsultationFee(250.00);
        doctor3.setExperienceYears(18);
        doctor3.setStartTime(LocalTime.of(10, 0));
        doctor3.setEndTime(LocalTime.of(18, 0));
        doctor3.setWorkingDays(Arrays.asList("MONDAY", "WEDNESDAY", "FRIDAY"));
        staffService.addDoctor(doctor3);
        
        // Doctor 4 - General Practitioner
        Doctor doctor4 = new Doctor("D0004", "Maria", "Garcia", "General Medicine", "555-1004");
        doctor4.setQualification("MD");
        doctor4.setEmail("dr.garcia@hospital.com");
        doctor4.setDepartment("General Medicine");
        doctor4.setConsultationFee(120.00);
        doctor4.setExperienceYears(8);
        doctor4.setStartTime(LocalTime.of(9, 0));
        doctor4.setEndTime(LocalTime.of(17, 0));
        doctor4.setWorkingDays(Arrays.asList("MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"));
        staffService.addDoctor(doctor4);
    }
    
    private static void createSampleStaff(StaffService staffService) {
        System.out.println("Creating sample staff...");
        
        // Nurse 1
        Staff nurse1 = new Staff("S0001", "Jennifer", "White", Staff.StaffRole.NURSE, "General Ward");
        nurse1.setPhoneNumber("555-2001");
        nurse1.setEmail("jennifer.white@hospital.com");
        nurse1.setQualification("RN, BSN");
        nurse1.setSalary(65000);
        nurse1.setShiftStartTime(LocalTime.of(7, 0));
        nurse1.setShiftEndTime(LocalTime.of(19, 0));
        staffService.addStaff(nurse1);
        
        // Administrator
        Staff admin1 = new Staff("S0002", "Thomas", "Miller", Staff.StaffRole.ADMINISTRATOR, "Administration");
        admin1.setPhoneNumber("555-2002");
        admin1.setEmail("thomas.miller@hospital.com");
        admin1.setQualification("MBA, Healthcare Administration");
        admin1.setSalary(85000);
        admin1.setShiftStartTime(LocalTime.of(8, 0));
        admin1.setShiftEndTime(LocalTime.of(17, 0));
        staffService.addStaff(admin1);
        
        // Pharmacist
        Staff pharmacist1 = new Staff("S0003", "Susan", "Taylor", Staff.StaffRole.PHARMACIST, "Pharmacy");
        pharmacist1.setPhoneNumber("555-2003");
        pharmacist1.setEmail("susan.taylor@hospital.com");
        pharmacist1.setQualification("PharmD");
        pharmacist1.setSalary(95000);
        pharmacist1.setShiftStartTime(LocalTime.of(9, 0));
        pharmacist1.setShiftEndTime(LocalTime.of(18, 0));
        staffService.addStaff(pharmacist1);
    }
    
    private static void createSampleAppointments(AppointmentService appointmentService) {
        System.out.println("Creating sample appointments...");
        
        // Appointment 1
        Appointment appointment1 = new Appointment("A0001", "P0001", "D0001", 
                                                 LocalDateTime.now().plusDays(1).withHour(10).withMinute(0), 
                                                 "Chest pain and shortness of breath");
        appointmentService.scheduleAppointment(appointment1);
        
        // Appointment 2
        Appointment appointment2 = new Appointment("A0002", "P0002", "D0002", 
                                                 LocalDateTime.now().plusDays(2).withHour(14).withMinute(30), 
                                                 "Child vaccination");
        appointmentService.scheduleAppointment(appointment2);
        
        // Appointment 3
        Appointment appointment3 = new Appointment("A0003", "P0003", "D0004", 
                                                 LocalDateTime.now().plusDays(3).withHour(11).withMinute(0), 
                                                 "Annual checkup");
        appointmentService.scheduleAppointment(appointment3);
    }
    
    private static void createSampleHealthRecords(EHRService ehrService) {
        System.out.println("Creating sample health records...");
        
        // Health Record 1
        HealthRecord record1 = new HealthRecord("HR0001", "P0001", "D0001", "Chest pain");
        record1.setSymptoms("Chest pain, shortness of breath, fatigue");
        record1.setDiagnosis("Hypertension");
        record1.setTreatment("Prescribed ACE inhibitors, lifestyle changes");
        record1.addPrescription("Lisinopril 10mg once daily");
        record1.setHeight(175);
        record1.setWeight(80);
        record1.setBloodPressure("140/90");
        record1.setHeartRate(85);
        record1.setTemperature(36.8);
        ehrService.addHealthRecord(record1);
        
        // Health Record 2
        HealthRecord record2 = new HealthRecord("HR0002", "P0002", "D0002", "Routine vaccination");
        record2.setSymptoms("None");
        record2.setDiagnosis("Healthy child - routine vaccination");
        record2.setTreatment("Administered MMR vaccine");
        record2.setHeight(120);
        record2.setWeight(25);
        record2.setTemperature(36.5);
        record2.setHeartRate(95);
        ehrService.addHealthRecord(record2);
    }
    
    private static void createSampleMedicalSupplies(InventoryService inventoryService) {
        System.out.println("Creating sample medical supplies...");
        
        // Medical Supply 1
        MedicalSupply supply1 = new MedicalSupply("MS0001", "Paracetamol 500mg", 
                                                MedicalSupply.SupplyCategory.MEDICATION, 500, 50);
        supply1.setDescription("Pain reliever and fever reducer");
        supply1.setManufacturer("PharmaCorp");
        supply1.setUnitPrice(0.25);
        supply1.setUnit("tablets");
        supply1.setExpiryDate(LocalDate.now().plusYears(2));
        supply1.setSupplier("MedSupply Inc.");
        supply1.setStorageLocation("Pharmacy - Shelf A1");
        inventoryService.addSupply(supply1);
        
        // Medical Supply 2
        MedicalSupply supply2 = new MedicalSupply("MS0002", "Surgical Gloves", 
                                                MedicalSupply.SupplyCategory.PROTECTIVE_EQUIPMENT, 1000, 100);
        supply2.setDescription("Latex-free surgical gloves");
        supply2.setManufacturer("SafeHands Ltd.");
        supply2.setUnitPrice(0.15);
        supply2.setUnit("pairs");
        supply2.setSupplier("Medical Equipment Co.");
        supply2.setStorageLocation("Supply Room - Cabinet B2");
        inventoryService.addSupply(supply2);
        
        // Medical Supply 3 - Low stock item
        MedicalSupply supply3 = new MedicalSupply("MS0003", "Insulin Syringes", 
                                                MedicalSupply.SupplyCategory.CONSUMABLES, 25, 100);
        supply3.setDescription("1ml insulin syringes with fine needle");
        supply3.setManufacturer("DiabCare");
        supply3.setUnitPrice(0.50);
        supply3.setUnit("pieces");
        supply3.setExpiryDate(LocalDate.now().plusYears(3));
        supply3.setSupplier("Diabetes Supply Co.");
        supply3.setStorageLocation("Pharmacy - Refrigerated Section");
        inventoryService.addSupply(supply3);
    }
    
    private static void createSampleBills(BillingService billingService) {
        System.out.println("Creating sample bills...");
        
        // Bill 1
        Bill bill1 = new Bill("B0001", "P0001", "A0001");
        bill1.addItem("Consultation Fee - Cardiology", 1, 200.00);
        bill1.addItem("ECG Test", 1, 75.00);
        bill1.addItem("Blood Pressure Monitoring", 1, 25.00);
        bill1.setTaxAmount(30.00);
        billingService.createBill(bill1);
        
        // Bill 2
        Bill bill2 = new Bill("B0002", "P0002", "A0002");
        bill2.addItem("Consultation Fee - Pediatrics", 1, 150.00);
        bill2.addItem("MMR Vaccine", 1, 45.00);
        bill2.setTaxAmount(19.50);
        billingService.createBill(bill2);
        
        // Process partial payment for Bill 1
        billingService.processPayment("B0001", 200.00, "Credit Card");
    }
    
    public static void main(String[] args) {
        initializeSampleData();
    }
}
