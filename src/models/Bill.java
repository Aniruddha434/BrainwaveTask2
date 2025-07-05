package models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Bill model class representing a patient's medical bill
 */
public class Bill implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public enum PaymentStatus {
        PENDING, PARTIAL, PAID, OVERDUE, CANCELLED
    }
    
    public static class BillItem implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private String description;
        private int quantity;
        private double unitPrice;
        private double totalPrice;
        
        public BillItem(String description, int quantity, double unitPrice) {
            this.description = description;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
            this.totalPrice = quantity * unitPrice;
        }
        
        // Getters and Setters
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { 
            this.quantity = quantity; 
            this.totalPrice = quantity * unitPrice;
        }
        
        public double getUnitPrice() { return unitPrice; }
        public void setUnitPrice(double unitPrice) { 
            this.unitPrice = unitPrice; 
            this.totalPrice = quantity * unitPrice;
        }
        
        public double getTotalPrice() { return totalPrice; }
        
        @Override
        public String toString() {
            return String.format("%s - Qty: %d, Unit: $%.2f, Total: $%.2f", 
                               description, quantity, unitPrice, totalPrice);
        }
    }
    
    private String billId;
    private String patientId;
    private String appointmentId;
    private LocalDateTime billDate;
    private List<BillItem> items;
    private double subtotal;
    private double taxAmount;
    private double discountAmount;
    private double totalAmount;
    private double paidAmount;
    private double balanceAmount;
    private PaymentStatus paymentStatus;
    private LocalDateTime dueDate;
    private String paymentMethod;
    private String notes;
    private boolean isActive;
    
    // Constructors
    public Bill() {
        this.items = new ArrayList<>();
        this.billDate = LocalDateTime.now();
        this.dueDate = LocalDateTime.now().plusDays(30); // 30 days payment term
        this.paymentStatus = PaymentStatus.PENDING;
        this.isActive = true;
    }
    
    public Bill(String billId, String patientId, String appointmentId) {
        this();
        this.billId = billId;
        this.patientId = patientId;
        this.appointmentId = appointmentId;
    }
    
    // Getters and Setters
    public String getBillId() { return billId; }
    public void setBillId(String billId) { this.billId = billId; }
    
    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    
    public String getAppointmentId() { return appointmentId; }
    public void setAppointmentId(String appointmentId) { this.appointmentId = appointmentId; }
    
    public LocalDateTime getBillDate() { return billDate; }
    public void setBillDate(LocalDateTime billDate) { this.billDate = billDate; }
    
    public List<BillItem> getItems() { return items; }
    public void setItems(List<BillItem> items) { 
        this.items = items; 
        calculateTotals();
    }
    
    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
    
    public double getTaxAmount() { return taxAmount; }
    public void setTaxAmount(double taxAmount) { this.taxAmount = taxAmount; }
    
    public double getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(double discountAmount) { this.discountAmount = discountAmount; }
    
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    
    public double getPaidAmount() { return paidAmount; }
    public void setPaidAmount(double paidAmount) { 
        this.paidAmount = paidAmount; 
        this.balanceAmount = totalAmount - paidAmount;
        updatePaymentStatus();
    }
    
    public double getBalanceAmount() { return balanceAmount; }
    public void setBalanceAmount(double balanceAmount) { this.balanceAmount = balanceAmount; }
    
    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }
    
    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }
    
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    // Utility methods
    public void addItem(String description, int quantity, double unitPrice) {
        items.add(new BillItem(description, quantity, unitPrice));
        calculateTotals();
    }
    
    public void removeItem(int index) {
        if (index >= 0 && index < items.size()) {
            items.remove(index);
            calculateTotals();
        }
    }
    
    public void calculateTotals() {
        subtotal = items.stream().mapToDouble(BillItem::getTotalPrice).sum();
        totalAmount = subtotal + taxAmount - discountAmount;
        balanceAmount = totalAmount - paidAmount;
        updatePaymentStatus();
    }
    
    private void updatePaymentStatus() {
        if (paidAmount >= totalAmount) {
            paymentStatus = PaymentStatus.PAID;
        } else if (paidAmount > 0) {
            paymentStatus = PaymentStatus.PARTIAL;
        } else if (LocalDateTime.now().isAfter(dueDate)) {
            paymentStatus = PaymentStatus.OVERDUE;
        } else {
            paymentStatus = PaymentStatus.PENDING;
        }
    }
    
    public boolean isOverdue() {
        return LocalDateTime.now().isAfter(dueDate) && balanceAmount > 0;
    }
    
    public boolean isPaid() {
        return paymentStatus == PaymentStatus.PAID;
    }
    
    @Override
    public String toString() {
        return String.format("Bill{ID='%s', Patient='%s', Total=$%.2f, Status='%s'}", 
                           billId, patientId, totalAmount, paymentStatus);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Bill bill = (Bill) obj;
        return billId != null ? billId.equals(bill.billId) : bill.billId == null;
    }
    
    @Override
    public int hashCode() {
        return billId != null ? billId.hashCode() : 0;
    }
}
