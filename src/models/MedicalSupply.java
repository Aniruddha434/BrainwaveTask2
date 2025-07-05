package models;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * MedicalSupply model class representing medical supplies and equipment inventory
 */
public class MedicalSupply implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public enum SupplyCategory {
        MEDICATION, SURGICAL_INSTRUMENTS, DIAGNOSTIC_EQUIPMENT, 
        CONSUMABLES, PROTECTIVE_EQUIPMENT, EMERGENCY_SUPPLIES
    }
    
    public enum SupplyStatus {
        AVAILABLE, LOW_STOCK, OUT_OF_STOCK, EXPIRED, DAMAGED, RECALLED
    }
    
    private String supplyId;
    private String name;
    private String description;
    private SupplyCategory category;
    private String manufacturer;
    private String batchNumber;
    private int currentStock;
    private int minimumStock;
    private int maximumStock;
    private double unitPrice;
    private String unit; // e.g., "pieces", "boxes", "bottles"
    private LocalDate expiryDate;
    private LocalDate lastRestocked;
    private String supplier;
    private String storageLocation;
    private SupplyStatus status;
    private boolean requiresPrescription;
    private String notes;
    private boolean isActive;
    
    // Constructors
    public MedicalSupply() {
        this.status = SupplyStatus.AVAILABLE;
        this.isActive = true;
        this.lastRestocked = LocalDate.now();
    }
    
    public MedicalSupply(String supplyId, String name, SupplyCategory category, 
                        int currentStock, int minimumStock) {
        this();
        this.supplyId = supplyId;
        this.name = name;
        this.category = category;
        this.currentStock = currentStock;
        this.minimumStock = minimumStock;
        updateStatus();
    }
    
    // Getters and Setters
    public String getSupplyId() { return supplyId; }
    public void setSupplyId(String supplyId) { this.supplyId = supplyId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public SupplyCategory getCategory() { return category; }
    public void setCategory(SupplyCategory category) { this.category = category; }
    
    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
    
    public String getBatchNumber() { return batchNumber; }
    public void setBatchNumber(String batchNumber) { this.batchNumber = batchNumber; }
    
    public int getCurrentStock() { return currentStock; }
    public void setCurrentStock(int currentStock) { 
        this.currentStock = currentStock; 
        updateStatus();
    }
    
    public int getMinimumStock() { return minimumStock; }
    public void setMinimumStock(int minimumStock) { 
        this.minimumStock = minimumStock; 
        updateStatus();
    }
    
    public int getMaximumStock() { return maximumStock; }
    public void setMaximumStock(int maximumStock) { this.maximumStock = maximumStock; }
    
    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }
    
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    
    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { 
        this.expiryDate = expiryDate; 
        updateStatus();
    }
    
    public LocalDate getLastRestocked() { return lastRestocked; }
    public void setLastRestocked(LocalDate lastRestocked) { this.lastRestocked = lastRestocked; }
    
    public String getSupplier() { return supplier; }
    public void setSupplier(String supplier) { this.supplier = supplier; }
    
    public String getStorageLocation() { return storageLocation; }
    public void setStorageLocation(String storageLocation) { this.storageLocation = storageLocation; }
    
    public SupplyStatus getStatus() { return status; }
    public void setStatus(SupplyStatus status) { this.status = status; }
    
    public boolean isRequiresPrescription() { return requiresPrescription; }
    public void setRequiresPrescription(boolean requiresPrescription) { this.requiresPrescription = requiresPrescription; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    // Utility methods
    public void addStock(int quantity) {
        this.currentStock += quantity;
        this.lastRestocked = LocalDate.now();
        updateStatus();
    }
    
    public boolean removeStock(int quantity) {
        if (currentStock >= quantity) {
            this.currentStock -= quantity;
            updateStatus();
            return true;
        }
        return false;
    }
    
    public void updateStatus() {
        if (expiryDate != null && expiryDate.isBefore(LocalDate.now())) {
            status = SupplyStatus.EXPIRED;
        } else if (currentStock <= 0) {
            status = SupplyStatus.OUT_OF_STOCK;
        } else if (currentStock <= minimumStock) {
            status = SupplyStatus.LOW_STOCK;
        } else {
            status = SupplyStatus.AVAILABLE;
        }
    }
    
    public boolean isExpired() {
        return expiryDate != null && expiryDate.isBefore(LocalDate.now());
    }
    
    public boolean isLowStock() {
        return currentStock <= minimumStock;
    }
    
    public boolean isOutOfStock() {
        return currentStock <= 0;
    }
    
    public long getDaysUntilExpiry() {
        if (expiryDate != null) {
            return LocalDate.now().until(expiryDate).getDays();
        }
        return Long.MAX_VALUE;
    }
    
    public double getTotalValue() {
        return currentStock * unitPrice;
    }
    
    @Override
    public String toString() {
        return String.format("MedicalSupply{ID='%s', Name='%s', Stock=%d, Status='%s'}", 
                           supplyId, name, currentStock, status);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        MedicalSupply supply = (MedicalSupply) obj;
        return supplyId != null ? supplyId.equals(supply.supplyId) : supply.supplyId == null;
    }
    
    @Override
    public int hashCode() {
        return supplyId != null ? supplyId.hashCode() : 0;
    }
}
