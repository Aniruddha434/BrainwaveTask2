@echo off
echo Hospital Management System - Compile and Run Script
echo ===================================================

REM Create bin directory if it doesn't exist
if not exist "bin" mkdir bin

REM Compile all Java files
echo Compiling Java files...
javac -d bin -cp src src\models\*.java src\utils\*.java src\services\*.java src\main\*.java

if %ERRORLEVEL% NEQ 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo Compilation successful!
echo.

REM Ask user if they want to initialize sample data
set /p INIT_DATA="Do you want to initialize sample data? (y/n): "
if /i "%INIT_DATA%"=="y" (
    echo Initializing sample data...
    java -cp bin utils.SampleDataInitializer
    echo.
)

REM Run the main application
echo Starting Hospital Management System...
echo.
java -cp bin main.HospitalManagementSystem

pause
