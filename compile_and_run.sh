#!/bin/bash

echo "Hospital Management System - Compile and Run Script"
echo "==================================================="

# Create bin directory if it doesn't exist
mkdir -p bin

# Compile all Java files
echo "Compiling Java files..."
javac -d bin -cp src src/models/*.java src/utils/*.java src/services/*.java src/main/*.java

if [ $? -ne 0 ]; then
    echo "Compilation failed!"
    exit 1
fi

echo "Compilation successful!"
echo

# Ask user if they want to initialize sample data
read -p "Do you want to initialize sample data? (y/n): " INIT_DATA
if [[ "$INIT_DATA" == "y" || "$INIT_DATA" == "Y" ]]; then
    echo "Initializing sample data..."
    java -cp bin utils.SampleDataInitializer
    echo
fi

# Run the main application
echo "Starting Hospital Management System..."
echo
java -cp bin main.HospitalManagementSystem
