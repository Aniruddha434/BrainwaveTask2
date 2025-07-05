package utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

/**
 * ValidationUtils class for input validation
 */
public class ValidationUtils {
    
    // Regular expression patterns
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );
    
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^[+]?[1-9]?[0-9]{7,15}$"
    );
    
    private static final Pattern NAME_PATTERN = Pattern.compile(
        "^[a-zA-Z\\s'-]{2,50}$"
    );
    
    private static final Pattern ID_PATTERN = Pattern.compile(
        "^[A-Z0-9]{3,20}$"
    );
    
    /**
     * Validate if string is not null and not empty
     */
    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }
    
    /**
     * Validate email format
     */
    public static boolean isValidEmail(String email) {
        if (!isNotEmpty(email)) return false;
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }
    
    /**
     * Validate phone number format
     */
    public static boolean isValidPhoneNumber(String phone) {
        if (!isNotEmpty(phone)) return false;
        // Remove spaces, dashes, and parentheses
        String cleanPhone = phone.replaceAll("[\\s\\-\\(\\)]", "");
        return PHONE_PATTERN.matcher(cleanPhone).matches();
    }
    
    /**
     * Validate name format
     */
    public static boolean isValidName(String name) {
        if (!isNotEmpty(name)) return false;
        return NAME_PATTERN.matcher(name.trim()).matches();
    }
    
    /**
     * Validate ID format
     */
    public static boolean isValidId(String id) {
        if (!isNotEmpty(id)) return false;
        return ID_PATTERN.matcher(id.trim().toUpperCase()).matches();
    }
    
    /**
     * Validate age (must be between 0 and 150)
     */
    public static boolean isValidAge(int age) {
        return age >= 0 && age <= 150;
    }
    
    /**
     * Validate birth date (must be in the past and not more than 150 years ago)
     */
    public static boolean isValidBirthDate(LocalDate birthDate) {
        if (birthDate == null) return false;
        LocalDate now = LocalDate.now();
        LocalDate minDate = now.minusYears(150);
        return birthDate.isAfter(minDate) && birthDate.isBefore(now);
    }
    
    /**
     * Validate future date
     */
    public static boolean isValidFutureDate(LocalDate date) {
        if (date == null) return false;
        return date.isAfter(LocalDate.now());
    }
    
    /**
     * Validate future datetime
     */
    public static boolean isValidFutureDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return false;
        return dateTime.isAfter(LocalDateTime.now());
    }
    
    /**
     * Validate positive number
     */
    public static boolean isPositiveNumber(double number) {
        return number > 0;
    }
    
    /**
     * Validate non-negative number
     */
    public static boolean isNonNegativeNumber(double number) {
        return number >= 0;
    }
    
    /**
     * Validate positive integer
     */
    public static boolean isPositiveInteger(int number) {
        return number > 0;
    }
    
    /**
     * Validate non-negative integer
     */
    public static boolean isNonNegativeInteger(int number) {
        return number >= 0;
    }
    
    /**
     * Validate gender
     */
    public static boolean isValidGender(String gender) {
        if (!isNotEmpty(gender)) return false;
        String upperGender = gender.trim().toUpperCase();
        return upperGender.equals("MALE") || upperGender.equals("FEMALE") || upperGender.equals("OTHER");
    }
    
    /**
     * Validate blood group
     */
    public static boolean isValidBloodGroup(String bloodGroup) {
        if (!isNotEmpty(bloodGroup)) return false;
        String upperBloodGroup = bloodGroup.trim().toUpperCase();
        return upperBloodGroup.matches("^(A|B|AB|O)[+-]$");
    }
    
    /**
     * Validate string length
     */
    public static boolean isValidLength(String str, int minLength, int maxLength) {
        if (str == null) return false;
        int length = str.trim().length();
        return length >= minLength && length <= maxLength;
    }
    
    /**
     * Validate numeric string
     */
    public static boolean isNumeric(String str) {
        if (!isNotEmpty(str)) return false;
        try {
            Double.parseDouble(str.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Validate integer string
     */
    public static boolean isInteger(String str) {
        if (!isNotEmpty(str)) return false;
        try {
            Integer.parseInt(str.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Validate appointment time (must be during business hours)
     */
    public static boolean isValidAppointmentTime(LocalDateTime appointmentTime) {
        if (appointmentTime == null) return false;
        
        // Check if it's in the future
        if (!appointmentTime.isAfter(LocalDateTime.now())) {
            return false;
        }
        
        // Check if it's during business hours (8 AM to 6 PM)
        int hour = appointmentTime.getHour();
        return hour >= 8 && hour < 18;
    }
    
    /**
     * Validate consultation fee
     */
    public static boolean isValidConsultationFee(double fee) {
        return fee >= 0 && fee <= 10000; // Reasonable range for consultation fee
    }
    
    /**
     * Validate stock quantity
     */
    public static boolean isValidStockQuantity(int quantity) {
        return quantity >= 0 && quantity <= 100000; // Reasonable range for stock
    }
    
    /**
     * Validate salary
     */
    public static boolean isValidSalary(double salary) {
        return salary >= 0 && salary <= 1000000; // Reasonable range for salary
    }
    
    /**
     * Clean and format phone number
     */
    public static String formatPhoneNumber(String phone) {
        if (!isNotEmpty(phone)) return "";
        return phone.replaceAll("[\\s\\-\\(\\)]", "");
    }
    
    /**
     * Clean and format name
     */
    public static String formatName(String name) {
        if (!isNotEmpty(name)) return "";
        return name.trim().replaceAll("\\s+", " ");
    }
    
    /**
     * Clean and format ID
     */
    public static String formatId(String id) {
        if (!isNotEmpty(id)) return "";
        return id.trim().toUpperCase();
    }
    
    /**
     * Clean and format email
     */
    public static String formatEmail(String email) {
        if (!isNotEmpty(email)) return "";
        return email.trim().toLowerCase();
    }
    
    /**
     * Get validation error message for common validations
     */
    public static String getValidationError(String fieldName, String value, String validationType) {
        switch (validationType.toLowerCase()) {
            case "required":
                return fieldName + " is required.";
            case "email":
                return "Please enter a valid email address.";
            case "phone":
                return "Please enter a valid phone number.";
            case "name":
                return "Please enter a valid name (2-50 characters, letters only).";
            case "id":
                return "Please enter a valid ID (3-20 characters, alphanumeric).";
            case "positive":
                return fieldName + " must be a positive number.";
            case "future_date":
                return fieldName + " must be a future date.";
            case "birth_date":
                return "Please enter a valid birth date.";
            default:
                return fieldName + " is invalid.";
        }
    }
}
