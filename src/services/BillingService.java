package services;

import models.Bill;
import models.Patient;
import models.Appointment;
import utils.DatabaseManager;
import utils.ValidationUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * BillingService class for managing billing and invoicing operations
 */
public class BillingService {
    private static final String BILLS_FILE = "bills.dat";
    private DatabaseManager dbManager;
    private List<Bill> bills;
    private PatientService patientService;
    private AppointmentService appointmentService;
    
    public BillingService(PatientService patientService, AppointmentService appointmentService) {
        this.dbManager = DatabaseManager.getInstance();
        this.bills = loadBills();
        this.patientService = patientService;
        this.appointmentService = appointmentService;
    }
    
    /**
     * Load bills from file
     */
    private List<Bill> loadBills() {
        return dbManager.loadData(BILLS_FILE);
    }
    
    /**
     * Save bills to file
     */
    private boolean saveBills() {
        return dbManager.saveData(bills, BILLS_FILE);
    }
    
    /**
     * Create a new bill
     */
    public boolean createBill(Bill bill) {
        if (bill == null) {
            System.out.println("Bill cannot be null.");
            return false;
        }
        
        // Validate bill data
        if (!validateBill(bill)) {
            return false;
        }
        
        // Check if bill ID already exists
        if (findBillById(bill.getBillId()) != null) {
            System.out.println("Bill with ID " + bill.getBillId() + " already exists.");
            return false;
        }
        
        // Calculate totals
        bill.calculateTotals();
        
        // Add bill to list
        bills.add(bill);
        
        // Save to file
        if (saveBills()) {
            System.out.println("Bill created successfully: " + bill.getBillId());
            return true;
        } else {
            // Remove from list if save failed
            bills.remove(bill);
            System.out.println("Failed to save bill data.");
            return false;
        }
    }
    
    /**
     * Create bill from appointment
     */
    public boolean createBillFromAppointment(String appointmentId) {
        Appointment appointment = appointmentService.findAppointmentById(appointmentId);
        if (appointment == null) {
            System.out.println("Appointment with ID " + appointmentId + " not found.");
            return false;
        }
        
        // Check if bill already exists for this appointment
        Bill existingBill = findBillByAppointmentId(appointmentId);
        if (existingBill != null) {
            System.out.println("Bill already exists for appointment: " + appointmentId);
            return false;
        }
        
        // Create new bill
        Bill bill = new Bill(generateBillId(), appointment.getPatientId(), appointmentId);
        
        // Add consultation fee
        if (appointment.getConsultationFee() > 0) {
            bill.addItem("Consultation Fee", 1, appointment.getConsultationFee());
        }
        
        return createBill(bill);
    }
    
    /**
     * Update an existing bill
     */
    public boolean updateBill(Bill updatedBill) {
        if (updatedBill == null) {
            System.out.println("Bill cannot be null.");
            return false;
        }
        
        // Validate bill data
        if (!validateBill(updatedBill)) {
            return false;
        }
        
        // Find existing bill
        Bill existingBill = findBillById(updatedBill.getBillId());
        if (existingBill == null) {
            System.out.println("Bill with ID " + updatedBill.getBillId() + " not found.");
            return false;
        }
        
        // Calculate totals
        updatedBill.calculateTotals();
        
        // Update bill data
        int index = bills.indexOf(existingBill);
        bills.set(index, updatedBill);
        
        // Save to file
        if (saveBills()) {
            System.out.println("Bill updated successfully: " + updatedBill.getBillId());
            return true;
        } else {
            // Revert changes if save failed
            bills.set(index, existingBill);
            System.out.println("Failed to save bill data.");
            return false;
        }
    }
    
    /**
     * Process payment for a bill
     */
    public boolean processPayment(String billId, double paymentAmount, String paymentMethod) {
        Bill bill = findBillById(billId);
        if (bill == null) {
            System.out.println("Bill with ID " + billId + " not found.");
            return false;
        }
        
        if (!ValidationUtils.isPositiveNumber(paymentAmount)) {
            System.out.println("Payment amount must be positive.");
            return false;
        }
        
        if (paymentAmount > bill.getBalanceAmount()) {
            System.out.println("Payment amount cannot exceed balance amount.");
            return false;
        }
        
        // Update payment information
        bill.setPaidAmount(bill.getPaidAmount() + paymentAmount);
        if (ValidationUtils.isNotEmpty(paymentMethod)) {
            bill.setPaymentMethod(paymentMethod);
        }
        
        // Save changes
        if (saveBills()) {
            System.out.println("Payment processed successfully. Amount: $" + paymentAmount);
            if (bill.isPaid()) {
                System.out.println("Bill fully paid.");
            } else {
                System.out.println("Remaining balance: $" + bill.getBalanceAmount());
            }
            return true;
        } else {
            System.out.println("Failed to save payment data.");
            return false;
        }
    }
    
    /**
     * Find bill by ID
     */
    public Bill findBillById(String billId) {
        if (!ValidationUtils.isNotEmpty(billId)) {
            return null;
        }
        
        return bills.stream()
                .filter(bill -> bill.getBillId().equalsIgnoreCase(billId.trim()))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Find bill by appointment ID
     */
    public Bill findBillByAppointmentId(String appointmentId) {
        if (!ValidationUtils.isNotEmpty(appointmentId)) {
            return null;
        }
        
        return bills.stream()
                .filter(bill -> appointmentId.equalsIgnoreCase(bill.getAppointmentId()))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Get bills by patient ID
     */
    public List<Bill> getBillsByPatient(String patientId) {
        if (!ValidationUtils.isNotEmpty(patientId)) {
            return new ArrayList<>();
        }
        
        return bills.stream()
                .filter(bill -> bill.getPatientId().equalsIgnoreCase(patientId.trim()))
                .filter(Bill::isActive)
                .sorted((b1, b2) -> b2.getBillDate().compareTo(b1.getBillDate())) // Most recent first
                .collect(Collectors.toList());
    }
    
    /**
     * Get unpaid bills
     */
    public List<Bill> getUnpaidBills() {
        return bills.stream()
                .filter(bill -> bill.getPaymentStatus() == Bill.PaymentStatus.PENDING ||
                              bill.getPaymentStatus() == Bill.PaymentStatus.PARTIAL)
                .filter(Bill::isActive)
                .sorted((b1, b2) -> b1.getDueDate().compareTo(b2.getDueDate()))
                .collect(Collectors.toList());
    }
    
    /**
     * Get overdue bills
     */
    public List<Bill> getOverdueBills() {
        return bills.stream()
                .filter(Bill::isOverdue)
                .filter(Bill::isActive)
                .sorted((b1, b2) -> b1.getDueDate().compareTo(b2.getDueDate()))
                .collect(Collectors.toList());
    }
    
    /**
     * Get paid bills
     */
    public List<Bill> getPaidBills() {
        return bills.stream()
                .filter(Bill::isPaid)
                .filter(Bill::isActive)
                .sorted((b1, b2) -> b2.getBillDate().compareTo(b1.getBillDate()))
                .collect(Collectors.toList());
    }
    
    /**
     * Generate bill invoice
     */
    public String generateInvoice(String billId) {
        Bill bill = findBillById(billId);
        if (bill == null) {
            return "Bill not found: " + billId;
        }
        
        Patient patient = patientService.findPatientById(bill.getPatientId());
        StringBuilder invoice = new StringBuilder();
        
        invoice.append("=== HOSPITAL INVOICE ===\n");
        invoice.append("Invoice ID: ").append(bill.getBillId()).append("\n");
        invoice.append("Date: ").append(bill.getBillDate().toLocalDate()).append("\n");
        invoice.append("Due Date: ").append(bill.getDueDate().toLocalDate()).append("\n\n");
        
        if (patient != null) {
            invoice.append("Patient Information:\n");
            invoice.append("Name: ").append(patient.getFullName()).append("\n");
            invoice.append("ID: ").append(patient.getPatientId()).append("\n");
            invoice.append("Phone: ").append(patient.getPhoneNumber()).append("\n\n");
        }
        
        invoice.append("Services:\n");
        invoice.append("----------------------------------------\n");
        for (Bill.BillItem item : bill.getItems()) {
            invoice.append(item.toString()).append("\n");
        }
        invoice.append("----------------------------------------\n");
        
        invoice.append(String.format("Subtotal: $%.2f\n", bill.getSubtotal()));
        if (bill.getTaxAmount() > 0) {
            invoice.append(String.format("Tax: $%.2f\n", bill.getTaxAmount()));
        }
        if (bill.getDiscountAmount() > 0) {
            invoice.append(String.format("Discount: -$%.2f\n", bill.getDiscountAmount()));
        }
        invoice.append(String.format("Total Amount: $%.2f\n", bill.getTotalAmount()));
        invoice.append(String.format("Paid Amount: $%.2f\n", bill.getPaidAmount()));
        invoice.append(String.format("Balance: $%.2f\n", bill.getBalanceAmount()));
        invoice.append("Status: ").append(bill.getPaymentStatus()).append("\n");
        
        if (ValidationUtils.isNotEmpty(bill.getNotes())) {
            invoice.append("\nNotes: ").append(bill.getNotes()).append("\n");
        }
        
        invoice.append("\nThank you for choosing our hospital!\n");
        invoice.append("========================\n");
        
        return invoice.toString();
    }
    
    /**
     * Generate next bill ID
     */
    public String generateBillId() {
        int maxId = 0;
        for (Bill bill : bills) {
            String id = bill.getBillId();
            if (id.startsWith("B") && id.length() > 1) {
                try {
                    int numId = Integer.parseInt(id.substring(1));
                    maxId = Math.max(maxId, numId);
                } catch (NumberFormatException e) {
                    // Ignore invalid IDs
                }
            }
        }
        return "B" + String.format("%04d", maxId + 1);
    }
    
    /**
     * Get all active bills
     */
    public List<Bill> getAllActiveBills() {
        return bills.stream()
                .filter(Bill::isActive)
                .collect(Collectors.toList());
    }
    
    /**
     * Validate bill data
     */
    private boolean validateBill(Bill bill) {
        // Validate required fields
        if (!ValidationUtils.isValidId(bill.getBillId())) {
            System.out.println("Invalid bill ID.");
            return false;
        }
        
        if (!ValidationUtils.isValidId(bill.getPatientId())) {
            System.out.println("Invalid patient ID.");
            return false;
        }
        
        // Validate that patient exists
        Patient patient = patientService.findPatientById(bill.getPatientId());
        if (patient == null) {
            System.out.println("Patient with ID " + bill.getPatientId() + " not found.");
            return false;
        }
        
        // Validate appointment if provided
        if (ValidationUtils.isNotEmpty(bill.getAppointmentId())) {
            Appointment appointment = appointmentService.findAppointmentById(bill.getAppointmentId());
            if (appointment == null) {
                System.out.println("Appointment with ID " + bill.getAppointmentId() + " not found.");
                return false;
            }
        }
        
        // Validate amounts
        if (!ValidationUtils.isNonNegativeNumber(bill.getTaxAmount())) {
            System.out.println("Tax amount cannot be negative.");
            return false;
        }
        
        if (!ValidationUtils.isNonNegativeNumber(bill.getDiscountAmount())) {
            System.out.println("Discount amount cannot be negative.");
            return false;
        }
        
        if (!ValidationUtils.isNonNegativeNumber(bill.getPaidAmount())) {
            System.out.println("Paid amount cannot be negative.");
            return false;
        }
        
        return true;
    }
    
    /**
     * Get billing statistics
     */
    public void printBillingStatistics() {
        int totalBills = bills.size();
        int activeBills = (int) bills.stream().filter(Bill::isActive).count();
        int paidBills = (int) bills.stream().filter(Bill::isPaid).count();
        int overdueBills = (int) bills.stream().filter(Bill::isOverdue).count();
        
        double totalRevenue = bills.stream()
                .filter(Bill::isActive)
                .mapToDouble(Bill::getPaidAmount)
                .sum();
        
        double outstandingAmount = bills.stream()
                .filter(Bill::isActive)
                .mapToDouble(Bill::getBalanceAmount)
                .sum();
        
        System.out.println("\n=== Billing Statistics ===");
        System.out.println("Total Bills: " + totalBills);
        System.out.println("Active Bills: " + activeBills);
        System.out.println("Paid Bills: " + paidBills);
        System.out.println("Overdue Bills: " + overdueBills);
        System.out.printf("Total Revenue: $%.2f\n", totalRevenue);
        System.out.printf("Outstanding Amount: $%.2f\n", outstandingAmount);
        System.out.println("==========================");
    }
}
