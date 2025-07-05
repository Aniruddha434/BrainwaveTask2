# Hospital Management System

A comprehensive Java-based Hospital Management System designed to manage various aspects of a hospital or healthcare facility.

## Features

### 1. Patient Registration

- Register new patients with complete demographic information
- Search and update existing patient records
- Input validation for patient data

### 2. Appointment Scheduling

- Schedule appointments between patients and doctors
- View upcoming appointments
- Manage appointment conflicts and rescheduling

### 3. Electronic Health Records (EHR)

- Maintain comprehensive patient medical history
- Record diagnoses, treatments, and prescriptions
- Track patient visits and medical procedures

### 4. Billing and Invoicing

- Generate bills for medical services and treatments
- Track payment status and outstanding amounts
- Print invoices and receipts

### 5. Inventory Management

- Manage medical supplies and equipment
- Track stock levels and expiry dates
- Generate alerts for low stock items

### 6. Staff Management

- Manage hospital staff (doctors, nurses, administrators)
- Track staff schedules and specializations
- Monitor staff performance and assignments

## Project Structure

```
src/
├── main/
│   └── HospitalManagementSystem.java
├── models/
│   ├── Patient.java
│   ├── Doctor.java
│   ├── Appointment.java
│   ├── HealthRecord.java
│   ├── Bill.java
│   ├── MedicalSupply.java
│   └── Staff.java
├── services/
│   ├── PatientService.java
│   ├── AppointmentService.java
│   ├── EHRService.java
│   ├── BillingService.java
│   ├── InventoryService.java
│   └── StaffService.java
├── utils/
│   ├── DatabaseManager.java
│   ├── DateUtils.java
│   └── ValidationUtils.java
└── data/
    └── (data storage files)
```

## How to Run

1. Compile all Java files:

   ```bash
   javac -d bin src/**/*.java
   ```

2. Run the main application:
   ```bash
   java -cp bin main.HospitalManagementSystem
   ```

## System Requirements

- Java 8 or higher
- Minimum 512MB RAM
- 100MB disk space for data storage

## Usage

The system provides a menu-driven interface where users can:

1. Manage patients (register, search, update)
2. Schedule and manage appointments
3. Maintain electronic health records
4. Handle billing and invoicing
5. Manage medical inventory
6. Administer staff information

## Data Storage

The system uses file-based serialization for data persistence. All data is stored in the `data/` directory as serialized objects.

## Testing Results

The Hospital Management System has been successfully tested with the following results:

### ✅ Compilation

- All Java files compile without errors
- No dependency issues
- Clean compilation with javac

### ✅ Sample Data Initialization

- Successfully creates 5 sample patients
- Successfully creates 4 sample doctors
- Successfully creates 3 sample staff members
- Successfully creates 3 sample appointments
- Successfully creates 2 sample health records
- Successfully creates 3 sample medical supplies
- Successfully creates 2 sample bills with payment processing

### ✅ Core Functionality Testing

- **Patient Management**: Registration, search by ID/name/phone, view all patients ✅
- **System Statistics**: Displays comprehensive statistics for all modules ✅
- **System Alerts**: Shows inventory alerts (low stock items) ✅
- **Data Persistence**: All data is properly saved to and loaded from files ✅
- **Menu Navigation**: All menu systems work correctly ✅
- **Input Validation**: Proper validation and error handling ✅

### ✅ Data Storage

- Creates `data/` directory automatically
- Stores data in serialized format (.dat files)
- Maintains data integrity across sessions

## Quick Start Guide

1. **Compile the application:**

   ```bash
   # Windows
   compile_and_run.bat

   # Linux/Mac
   chmod +x compile_and_run.sh
   ./compile_and_run.sh
   ```

2. **Initialize sample data (recommended for first run):**

   - When prompted, choose 'y' to initialize sample data
   - This creates realistic test data to explore the system

3. **Explore the features:**
   - Start with "System Statistics" (option 7) to see overview
   - Check "System Alerts" (option 8) to see low stock alerts
   - Try "Patient Management" (option 1) to search and view patients

## Sample Data Overview

The system comes with pre-configured sample data:

### Patients (5)

- John Doe (P0001) - Male, 40 years, O+ blood type
- Sarah Johnson (P0002) - Female, 35 years, A+ blood type
- Michael Brown (P0003) - Male, 47 years, B- blood type
- Emily Davis (P0004) - Female, 30 years, AB+ blood type
- David Wilson (P0005) - Male, 43 years, O- blood type

### Doctors (4)

- Dr. James Smith (D0001) - Cardiologist, $200 consultation fee
- Dr. Lisa Anderson (D0002) - Pediatrician, $150 consultation fee
- Dr. Robert Johnson (D0003) - Orthopedic Surgeon, $250 consultation fee
- Dr. Maria Garcia (D0004) - General Practitioner, $120 consultation fee

### Medical Supplies (3)

- Paracetamol 500mg (MS0001) - 500 units in stock
- Surgical Gloves (MS0002) - 1000 pairs in stock
- Insulin Syringes (MS0003) - 25 units (LOW STOCK ALERT)

## Architecture Highlights

### Design Patterns Used

- **Service Layer Pattern**: Separates business logic from presentation
- **Data Access Object (DAO) Pattern**: DatabaseManager handles all file operations
- **Model-View-Controller (MVC)**: Clear separation of concerns
- **Singleton Pattern**: DatabaseManager uses singleton for consistency

### Key Features Implemented

- **Comprehensive Data Models**: Rich domain models with validation
- **File-based Persistence**: Reliable data storage using Java serialization
- **Input Validation**: Extensive validation using ValidationUtils
- **Date/Time Handling**: Robust date operations with DateUtils
- **Alert System**: Proactive monitoring for low stock, overdue bills, etc.
- **Statistics Dashboard**: Real-time system statistics
- **Modular Architecture**: Easy to extend and maintain

## Future Enhancements

- Database integration (MySQL/PostgreSQL)
- Web-based interface using Spring Boot
- Report generation (PDF/Excel)
- Backup and restore functionality
- Multi-user authentication and authorization
- Email notifications for alerts
- Appointment reminders
- Insurance claim processing
- Laboratory test integration
- Prescription management system
