package utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

/**
 * DateUtils class for date and time utility functions
 */
public class DateUtils {
    
    // Date formatters
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static final DateTimeFormatter DISPLAY_DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy");
    public static final DateTimeFormatter DISPLAY_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
    
    /**
     * Parse date string to LocalDate
     */
    public static LocalDate parseDate(String dateString) {
        try {
            return LocalDate.parse(dateString, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            System.err.println("Error parsing date: " + dateString);
            return null;
        }
    }
    
    /**
     * Parse time string to LocalTime
     */
    public static LocalTime parseTime(String timeString) {
        try {
            return LocalTime.parse(timeString, TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            System.err.println("Error parsing time: " + timeString);
            return null;
        }
    }
    
    /**
     * Parse datetime string to LocalDateTime
     */
    public static LocalDateTime parseDateTime(String dateTimeString) {
        try {
            return LocalDateTime.parse(dateTimeString, DATETIME_FORMATTER);
        } catch (DateTimeParseException e) {
            System.err.println("Error parsing datetime: " + dateTimeString);
            return null;
        }
    }
    
    /**
     * Format LocalDate to string
     */
    public static String formatDate(LocalDate date) {
        if (date == null) return "";
        return date.format(DATE_FORMATTER);
    }
    
    /**
     * Format LocalTime to string
     */
    public static String formatTime(LocalTime time) {
        if (time == null) return "";
        return time.format(TIME_FORMATTER);
    }
    
    /**
     * Format LocalDateTime to string
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        return dateTime.format(DATETIME_FORMATTER);
    }
    
    /**
     * Format LocalDate for display
     */
    public static String formatDateForDisplay(LocalDate date) {
        if (date == null) return "";
        return date.format(DISPLAY_DATE_FORMATTER);
    }
    
    /**
     * Format LocalDateTime for display
     */
    public static String formatDateTimeForDisplay(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        return dateTime.format(DISPLAY_DATETIME_FORMATTER);
    }
    
    /**
     * Get current date
     */
    public static LocalDate getCurrentDate() {
        return LocalDate.now();
    }
    
    /**
     * Get current time
     */
    public static LocalTime getCurrentTime() {
        return LocalTime.now();
    }
    
    /**
     * Get current datetime
     */
    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }
    
    /**
     * Calculate age from birth date
     */
    public static int calculateAge(LocalDate birthDate) {
        if (birthDate == null) return 0;
        return (int) ChronoUnit.YEARS.between(birthDate, LocalDate.now());
    }
    
    /**
     * Calculate days between two dates
     */
    public static long daysBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) return 0;
        return ChronoUnit.DAYS.between(startDate, endDate);
    }
    
    /**
     * Calculate hours between two datetimes
     */
    public static long hoursBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime == null || endDateTime == null) return 0;
        return ChronoUnit.HOURS.between(startDateTime, endDateTime);
    }
    
    /**
     * Calculate minutes between two datetimes
     */
    public static long minutesBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime == null || endDateTime == null) return 0;
        return ChronoUnit.MINUTES.between(startDateTime, endDateTime);
    }
    
    /**
     * Check if a date is in the past
     */
    public static boolean isPastDate(LocalDate date) {
        if (date == null) return false;
        return date.isBefore(LocalDate.now());
    }
    
    /**
     * Check if a datetime is in the past
     */
    public static boolean isPastDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return false;
        return dateTime.isBefore(LocalDateTime.now());
    }
    
    /**
     * Check if a date is in the future
     */
    public static boolean isFutureDate(LocalDate date) {
        if (date == null) return false;
        return date.isAfter(LocalDate.now());
    }
    
    /**
     * Check if a datetime is in the future
     */
    public static boolean isFutureDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return false;
        return dateTime.isAfter(LocalDateTime.now());
    }
    
    /**
     * Check if a date is today
     */
    public static boolean isToday(LocalDate date) {
        if (date == null) return false;
        return date.equals(LocalDate.now());
    }
    
    /**
     * Add days to a date
     */
    public static LocalDate addDays(LocalDate date, long days) {
        if (date == null) return null;
        return date.plusDays(days);
    }
    
    /**
     * Add hours to a datetime
     */
    public static LocalDateTime addHours(LocalDateTime dateTime, long hours) {
        if (dateTime == null) return null;
        return dateTime.plusHours(hours);
    }
    
    /**
     * Add minutes to a datetime
     */
    public static LocalDateTime addMinutes(LocalDateTime dateTime, long minutes) {
        if (dateTime == null) return null;
        return dateTime.plusMinutes(minutes);
    }
    
    /**
     * Get the start of day for a date
     */
    public static LocalDateTime getStartOfDay(LocalDate date) {
        if (date == null) return null;
        return date.atStartOfDay();
    }
    
    /**
     * Get the end of day for a date
     */
    public static LocalDateTime getEndOfDay(LocalDate date) {
        if (date == null) return null;
        return date.atTime(23, 59, 59);
    }
    
    /**
     * Check if two time periods overlap
     */
    public static boolean timePeriodsOverlap(LocalDateTime start1, LocalDateTime end1, 
                                           LocalDateTime start2, LocalDateTime end2) {
        if (start1 == null || end1 == null || start2 == null || end2 == null) {
            return false;
        }
        return start1.isBefore(end2) && start2.isBefore(end1);
    }
    
    /**
     * Get day of week as string
     */
    public static String getDayOfWeek(LocalDate date) {
        if (date == null) return "";
        return date.getDayOfWeek().toString();
    }
}
