package services;

import models.MedicalSupply;
import utils.DatabaseManager;
import utils.ValidationUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * InventoryService class for managing medical supply inventory
 */
public class InventoryService {
    private static final String SUPPLIES_FILE = "medical_supplies.dat";
    private DatabaseManager dbManager;
    private List<MedicalSupply> supplies;
    
    public InventoryService() {
        this.dbManager = DatabaseManager.getInstance();
        this.supplies = loadSupplies();
    }
    
    /**
     * Load supplies from file
     */
    private List<MedicalSupply> loadSupplies() {
        return dbManager.loadData(SUPPLIES_FILE);
    }
    
    /**
     * Save supplies to file
     */
    private boolean saveSupplies() {
        return dbManager.saveData(supplies, SUPPLIES_FILE);
    }
    
    /**
     * Add a new medical supply
     */
    public boolean addSupply(MedicalSupply supply) {
        if (supply == null) {
            System.out.println("Medical supply cannot be null.");
            return false;
        }
        
        // Validate supply data
        if (!validateSupply(supply)) {
            return false;
        }
        
        // Check if supply ID already exists
        if (findSupplyById(supply.getSupplyId()) != null) {
            System.out.println("Supply with ID " + supply.getSupplyId() + " already exists.");
            return false;
        }
        
        // Update status based on stock and expiry
        supply.updateStatus();
        
        // Add supply to list
        supplies.add(supply);
        
        // Save to file
        if (saveSupplies()) {
            System.out.println("Medical supply added successfully: " + supply.getName());
            return true;
        } else {
            // Remove from list if save failed
            supplies.remove(supply);
            System.out.println("Failed to save supply data.");
            return false;
        }
    }
    
    /**
     * Update an existing medical supply
     */
    public boolean updateSupply(MedicalSupply updatedSupply) {
        if (updatedSupply == null) {
            System.out.println("Medical supply cannot be null.");
            return false;
        }
        
        // Validate supply data
        if (!validateSupply(updatedSupply)) {
            return false;
        }
        
        // Find existing supply
        MedicalSupply existingSupply = findSupplyById(updatedSupply.getSupplyId());
        if (existingSupply == null) {
            System.out.println("Supply with ID " + updatedSupply.getSupplyId() + " not found.");
            return false;
        }
        
        // Update status based on stock and expiry
        updatedSupply.updateStatus();
        
        // Update supply data
        int index = supplies.indexOf(existingSupply);
        supplies.set(index, updatedSupply);
        
        // Save to file
        if (saveSupplies()) {
            System.out.println("Medical supply updated successfully: " + updatedSupply.getName());
            return true;
        } else {
            // Revert changes if save failed
            supplies.set(index, existingSupply);
            System.out.println("Failed to save supply data.");
            return false;
        }
    }
    
    /**
     * Add stock to a supply
     */
    public boolean addStock(String supplyId, int quantity) {
        MedicalSupply supply = findSupplyById(supplyId);
        if (supply == null) {
            System.out.println("Supply with ID " + supplyId + " not found.");
            return false;
        }
        
        if (!ValidationUtils.isPositiveInteger(quantity)) {
            System.out.println("Quantity must be positive.");
            return false;
        }
        
        supply.addStock(quantity);
        
        if (saveSupplies()) {
            System.out.println("Stock added successfully. New stock: " + supply.getCurrentStock());
            return true;
        } else {
            System.out.println("Failed to save supply data.");
            return false;
        }
    }
    
    /**
     * Remove stock from a supply
     */
    public boolean removeStock(String supplyId, int quantity) {
        MedicalSupply supply = findSupplyById(supplyId);
        if (supply == null) {
            System.out.println("Supply with ID " + supplyId + " not found.");
            return false;
        }
        
        if (!ValidationUtils.isPositiveInteger(quantity)) {
            System.out.println("Quantity must be positive.");
            return false;
        }
        
        if (!supply.removeStock(quantity)) {
            System.out.println("Insufficient stock. Available: " + supply.getCurrentStock());
            return false;
        }
        
        if (saveSupplies()) {
            System.out.println("Stock removed successfully. Remaining stock: " + supply.getCurrentStock());
            return true;
        } else {
            System.out.println("Failed to save supply data.");
            return false;
        }
    }
    
    /**
     * Find supply by ID
     */
    public MedicalSupply findSupplyById(String supplyId) {
        if (!ValidationUtils.isNotEmpty(supplyId)) {
            return null;
        }
        
        return supplies.stream()
                .filter(supply -> supply.getSupplyId().equalsIgnoreCase(supplyId.trim()))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Search supplies by name
     */
    public List<MedicalSupply> searchSuppliesByName(String name) {
        if (!ValidationUtils.isNotEmpty(name)) {
            return new ArrayList<>();
        }
        
        String searchName = name.trim().toLowerCase();
        return supplies.stream()
                .filter(supply -> supply.getName().toLowerCase().contains(searchName))
                .filter(MedicalSupply::isActive)
                .collect(Collectors.toList());
    }
    
    /**
     * Get supplies by category
     */
    public List<MedicalSupply> getSuppliesByCategory(MedicalSupply.SupplyCategory category) {
        if (category == null) {
            return new ArrayList<>();
        }
        
        return supplies.stream()
                .filter(supply -> supply.getCategory() == category)
                .filter(MedicalSupply::isActive)
                .collect(Collectors.toList());
    }
    
    /**
     * Get low stock supplies
     */
    public List<MedicalSupply> getLowStockSupplies() {
        return supplies.stream()
                .filter(MedicalSupply::isLowStock)
                .filter(MedicalSupply::isActive)
                .sorted((s1, s2) -> Integer.compare(s1.getCurrentStock(), s2.getCurrentStock()))
                .collect(Collectors.toList());
    }
    
    /**
     * Get out of stock supplies
     */
    public List<MedicalSupply> getOutOfStockSupplies() {
        return supplies.stream()
                .filter(MedicalSupply::isOutOfStock)
                .filter(MedicalSupply::isActive)
                .collect(Collectors.toList());
    }
    
    /**
     * Get expired supplies
     */
    public List<MedicalSupply> getExpiredSupplies() {
        return supplies.stream()
                .filter(MedicalSupply::isExpired)
                .filter(MedicalSupply::isActive)
                .sorted((s1, s2) -> s1.getExpiryDate().compareTo(s2.getExpiryDate()))
                .collect(Collectors.toList());
    }
    
    /**
     * Get supplies expiring soon (within 30 days)
     */
    public List<MedicalSupply> getSuppliesExpiringSoon() {
        LocalDate thirtyDaysFromNow = LocalDate.now().plusDays(30);
        
        return supplies.stream()
                .filter(supply -> supply.getExpiryDate() != null)
                .filter(supply -> supply.getExpiryDate().isAfter(LocalDate.now()) && 
                                supply.getExpiryDate().isBefore(thirtyDaysFromNow))
                .filter(MedicalSupply::isActive)
                .sorted((s1, s2) -> s1.getExpiryDate().compareTo(s2.getExpiryDate()))
                .collect(Collectors.toList());
    }
    
    /**
     * Get supplies by supplier
     */
    public List<MedicalSupply> getSuppliesBySupplier(String supplier) {
        if (!ValidationUtils.isNotEmpty(supplier)) {
            return new ArrayList<>();
        }
        
        return supplies.stream()
                .filter(supply -> supplier.equalsIgnoreCase(supply.getSupplier()))
                .filter(MedicalSupply::isActive)
                .collect(Collectors.toList());
    }
    
    /**
     * Generate supply alerts
     */
    public List<String> generateSupplyAlerts() {
        List<String> alerts = new ArrayList<>();
        
        // Low stock alerts
        List<MedicalSupply> lowStock = getLowStockSupplies();
        for (MedicalSupply supply : lowStock) {
            alerts.add("LOW STOCK: " + supply.getName() + " - Current: " + 
                      supply.getCurrentStock() + ", Minimum: " + supply.getMinimumStock());
        }
        
        // Out of stock alerts
        List<MedicalSupply> outOfStock = getOutOfStockSupplies();
        for (MedicalSupply supply : outOfStock) {
            alerts.add("OUT OF STOCK: " + supply.getName());
        }
        
        // Expiry alerts
        List<MedicalSupply> expiringSoon = getSuppliesExpiringSoon();
        for (MedicalSupply supply : expiringSoon) {
            long daysUntilExpiry = supply.getDaysUntilExpiry();
            alerts.add("EXPIRING SOON: " + supply.getName() + " - Expires in " + 
                      daysUntilExpiry + " days (" + supply.getExpiryDate() + ")");
        }
        
        // Expired alerts
        List<MedicalSupply> expired = getExpiredSupplies();
        for (MedicalSupply supply : expired) {
            alerts.add("EXPIRED: " + supply.getName() + " - Expired on " + supply.getExpiryDate());
        }
        
        return alerts;
    }
    
    /**
     * Generate next supply ID
     */
    public String generateSupplyId() {
        int maxId = 0;
        for (MedicalSupply supply : supplies) {
            String id = supply.getSupplyId();
            if (id.startsWith("MS") && id.length() > 2) {
                try {
                    int numId = Integer.parseInt(id.substring(2));
                    maxId = Math.max(maxId, numId);
                } catch (NumberFormatException e) {
                    // Ignore invalid IDs
                }
            }
        }
        return "MS" + String.format("%04d", maxId + 1);
    }
    
    /**
     * Get all active supplies
     */
    public List<MedicalSupply> getAllActiveSupplies() {
        return supplies.stream()
                .filter(MedicalSupply::isActive)
                .collect(Collectors.toList());
    }
    
    /**
     * Deactivate a supply
     */
    public boolean deactivateSupply(String supplyId) {
        MedicalSupply supply = findSupplyById(supplyId);
        if (supply == null) {
            System.out.println("Supply with ID " + supplyId + " not found.");
            return false;
        }
        
        supply.setActive(false);
        if (saveSupplies()) {
            System.out.println("Supply deactivated: " + supply.getName());
            return true;
        } else {
            supply.setActive(true); // Revert change
            System.out.println("Failed to save supply data.");
            return false;
        }
    }
    
    /**
     * Validate supply data
     */
    private boolean validateSupply(MedicalSupply supply) {
        // Validate required fields
        if (!ValidationUtils.isValidId(supply.getSupplyId())) {
            System.out.println("Invalid supply ID.");
            return false;
        }
        
        if (!ValidationUtils.isNotEmpty(supply.getName())) {
            System.out.println("Supply name is required.");
            return false;
        }
        
        if (supply.getCategory() == null) {
            System.out.println("Supply category is required.");
            return false;
        }
        
        if (!ValidationUtils.isNonNegativeInteger(supply.getCurrentStock())) {
            System.out.println("Current stock cannot be negative.");
            return false;
        }
        
        if (!ValidationUtils.isNonNegativeInteger(supply.getMinimumStock())) {
            System.out.println("Minimum stock cannot be negative.");
            return false;
        }
        
        if (supply.getMaximumStock() > 0 && supply.getMaximumStock() < supply.getMinimumStock()) {
            System.out.println("Maximum stock cannot be less than minimum stock.");
            return false;
        }
        
        if (!ValidationUtils.isNonNegativeNumber(supply.getUnitPrice())) {
            System.out.println("Unit price cannot be negative.");
            return false;
        }
        
        // Validate expiry date if provided
        if (supply.getExpiryDate() != null && supply.getExpiryDate().isBefore(LocalDate.now())) {
            System.out.println("Warning: Supply is already expired.");
        }
        
        return true;
    }
    
    /**
     * Get inventory statistics
     */
    public void printInventoryStatistics() {
        int totalSupplies = supplies.size();
        int activeSupplies = (int) supplies.stream().filter(MedicalSupply::isActive).count();
        int lowStockCount = getLowStockSupplies().size();
        int outOfStockCount = getOutOfStockSupplies().size();
        int expiredCount = getExpiredSupplies().size();
        int expiringSoonCount = getSuppliesExpiringSoon().size();
        
        double totalValue = supplies.stream()
                .filter(MedicalSupply::isActive)
                .mapToDouble(MedicalSupply::getTotalValue)
                .sum();
        
        System.out.println("\n=== Inventory Statistics ===");
        System.out.println("Total Supplies: " + totalSupplies);
        System.out.println("Active Supplies: " + activeSupplies);
        System.out.println("Low Stock Items: " + lowStockCount);
        System.out.println("Out of Stock Items: " + outOfStockCount);
        System.out.println("Expired Items: " + expiredCount);
        System.out.println("Expiring Soon (30 days): " + expiringSoonCount);
        System.out.printf("Total Inventory Value: $%.2f\n", totalValue);
        System.out.println("============================");
    }
}
