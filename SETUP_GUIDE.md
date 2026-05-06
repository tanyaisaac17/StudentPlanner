# 📚 Student Study Planner - Setup & Compilation Guide

## ✅ Prerequisites

Before running this application, ensure you have:

1. **Java JDK 17 or 21** installed
   - Download from: https://www.oracle.com/java/technologies/downloads/
   - Verify: `java -version` in terminal

2. **MySQL Server** installed and running
   - Download from: https://dev.mysql.com/downloads/mysql/
   - Default port: 3306
   - Default user: root

3. **MySQL JDBC Driver** (mysql-connector-java JAR)
   - Download from: https://dev.mysql.com/downloads/connector/j/
   - Or use version 8.0.33 or later

---

## 🗄️ Step 1: Create MySQL Database

Open MySQL terminal and execute:

```sql
-- Create database
CREATE DATABASE IF NOT EXISTS study_planner;
USE study_planner;

-- Create subjects table
CREATE TABLE subjects (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create sessions table
CREATE TABLE sessions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    subject VARCHAR(50) NOT NULL,
    hours INT NOT NULL CHECK (hours > 0 AND hours <= 24),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create tasks table
CREATE TABLE tasks (
    id INT AUTO_INCREMENT PRIMARY KEY,
    task VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Verify tables created
SHOW TABLES;
DESC subjects;
DESC sessions;
DESC tasks;
```

---

## 📥 Step 2: Download MySQL JDBC Driver

1. Download `mysql-connector-java-8.0.33.jar` (or latest)
2. Save it to a known location (e.g., `C:\Users\DELL\Downloads\`)

---

## 🔧 Step 3: Update Database Connection (If Needed)

Edit `src/com/studyplanner/db/DBConnection.java`:

```java
// Line 31-34: Update these if your database credentials are different
private static final String DB_URL = "jdbc:mysql://localhost:3306/study_planner";
private static final String DB_USER = "root";
private static final String DB_PASSWORD = ""; // Add password if needed
```

---

## 💻 Step 4: Compilation Commands

Navigate to the project root directory and compile all Java files:

### Windows (Using Command Prompt/PowerShell):

```batch
cd C:\Users\DELL\OneDrive\Desktop\StudentPlanner

:: Set CLASSPATH to include MySQL JDBC JAR
set CLASSPATH=C:\Users\DELL\Downloads\mysql-connector-java-8.0.33.jar;.

:: Compile all Java source files
javac -d . -cp . src/com/studyplanner/model/*.java
javac -d . -cp . src/com/studyplanner/db/*.java
javac -d . -cp . src/com/studyplanner/service/*.java
javac -d . -cp . src/com/studyplanner/gui/*.java
javac -d . -cp . src/com/studyplanner/main/*.java

:: Or compile all at once:
javac -d . -cp .; src/com/studyplanner/**/*.java
```

### macOS/Linux:

```bash
cd ~/Desktop/StudentPlanner

# Set CLASSPATH
export CLASSPATH=/path/to/mysql-connector-java-8.0.33.jar:.

# Compile
javac -d . -cp . src/com/studyplanner/model/*.java
javac -d . -cp . src/com/studyplanner/db/*.java
javac -d . -cp . src/com/studyplanner/service/*.java
javac -d . -cp . src/com/studyplanner/gui/*.java
javac -d . -cp . src/com/studyplanner/main/*.java
```

---

## ▶️ Step 5: Run the Application

### Windows:

```batch
:: Set CLASSPATH
set CLASSPATH=C:\Users\DELL\Downloads\mysql-connector-java-8.0.33.jar;.

:: Run application
java -cp . com.studyplanner.main.StudyPlannerApp
```

### macOS/Linux:

```bash
export CLASSPATH=/path/to/mysql-connector-java-8.0.33.jar:.
java -cp . com.studyplanner.main.StudyPlannerApp
```

---

## 🎯 Alternative: One-Command Compilation & Execution

### Windows (create `run.bat`):

```batch
@echo off
setlocal enabledelayedexpansion

set JDBC_JAR=C:\Users\DELL\Downloads\mysql-connector-java-8.0.33.jar
set CLASSPATH=%JDBC_JAR%;.

echo ========================================
echo Compiling Student Study Planner...
echo ========================================

javac -d . -cp . src/com/studyplanner/model/*.java
javac -d . -cp . src/com/studyplanner/db/*.java
javac -d . -cp . src/com/studyplanner/service/*.java
javac -d . -cp . src/com/studyplanner/gui/*.java
javac -d . -cp . src/com/studyplanner/main/*.java

if errorlevel 1 (
    echo ❌ Compilation failed!
    pause
    exit /b 1
)

echo.
echo ========================================
echo Starting Student Study Planner...
echo ========================================
echo.

java -cp . com.studyplanner.main.StudyPlannerApp

pause
```

Then run: `run.bat`

---

## 📋 File Structure

```
StudyPlanner/
├── src/
│   └── com/studyplanner/
│       ├── main/
│       │   └── StudyPlannerApp.java      (Entry point)
│       ├── db/
│       │   └── DBConnection.java         (Database connection - Singleton)
│       ├── model/
│       │   ├── Record.java               (Abstract base class)
│       │   ├── Subject.java              (Extends Record)
│       │   ├── StudySession.java         (Extends Record)
│       │   ├── Task.java                 (Extends Record)
│       │   ├── Status.java               (Enum)
│       │   └── DatabaseEntity.java       (Custom Annotation)
│       ├── service/
│       │   ├── PlannerService.java       (Interface)
│       │   └── PlannerServiceImpl.java    (Implementation)
│       └── gui/
│           ├── DashboardFrame.java       (Main window)
│           ├── AddSubjectForm.java       (Add subject dialog)
│           ├── AddSessionForm.java       (Add session dialog)
│           ├── AddTaskForm.java          (Add task dialog)
│           └── ViewDataFrame.java        (View all data - with multithreading)
├── com/ (compiled .class files after compilation)
└── SETUP_GUIDE.md (this file)
```

---

## ❌ Troubleshooting

### Error: "Cannot find symbol: class DBConnection"
- **Cause**: Compilation order issue
- **Solution**: Compile in this order: model → db → service → gui → main

### Error: "JDBC Driver not found"
- **Cause**: MySQL connector JAR not in classpath
- **Solution**: Ensure `CLASSPATH` includes path to `mysql-connector-java-X.X.X.jar`

### Error: "Cannot connect to database"
- **Cause**: MySQL not running or wrong credentials
- **Solution**: 
  - Check MySQL is running: `mysql -u root`
  - Verify database exists: `SHOW DATABASES;`
  - Update credentials in DBConnection.java

### Error: "Table doesn't exist"
- **Cause**: Database tables not created
- **Solution**: Run the SQL commands from Step 1

### GUI not appearing or responding
- **Cause**: Application running but GUI not shown
- **Solution**: Check console for error messages

---

## 🧪 Testing the Application

1. **Launch the application** using Step 5 commands
2. **Add a Subject**: Click "Add Subject" → Enter name and credits → Click "Add Subject"
3. **Add a Study Session**: Click "Add Study Session" → Select subject → Enter hours → Click "Add Session"
4. **Add a Task**: Click "Add Task" → Enter description → Click "Add Task"
5. **View Data**: Click "View Data" → Data will load in background thread (demonstrates multithreading)
6. **Refresh**: Click "Refresh Data" to fetch latest data from database

---

## 📚 Java Concepts Demonstrated

This project demonstrates:

- ✅ **OOP**: Classes, Inheritance, Encapsulation, Polymorphism
- ✅ **Abstract Classes**: Record base class
- ✅ **Interfaces**: PlannerService interface
- ✅ **Enums**: Status enum
- ✅ **Custom Annotations**: @DatabaseEntity
- ✅ **Collections**: ArrayList, HashMap, Iterator
- ✅ **Recursion**: Factorial calculation
- ✅ **Multithreading**: Background data loading
- ✅ **Swing GUI**: JFrame, JDialog, JPanel, Layouts
- ✅ **Exception Handling**: Try-catch-finally blocks
- ✅ **JDBC**: Database connectivity and queries
- ✅ **Singleton Pattern**: DBConnection
- ✅ **Design Patterns**: Service layer, MVC-like architecture
- ✅ **Wrapper Classes**: Integer, boxing/unboxing
- ✅ **String Handling**: String, StringBuilder, StringBuffer explained

---

## 🎓 For Viva Preparation

Key topics to review:

1. **Inheritance**: How Subject/Task/StudySession extend Record
2. **Abstract Classes**: Why Record is abstract, how subclasses override methods
3. **Interfaces**: How PlannerServiceImpl implements PlannerService
4. **Singleton Pattern**: How DBConnection ensures only one instance
5. **JDBC**: How PreparedStatement prevents SQL injection
6. **Collections**: When to use ArrayList vs HashMap vs Iterator
7. **Multithreading**: Why background threads are needed in GUI apps
8. **Swing**: How layouts organize GUI components

---

## ✨ Additional Notes

- All code includes comments explaining Java concepts
- See PlannerServiceImpl.java for String vs StringBuilder vs StringBuffer explanation
- See Record.java for Recursion example
- See ViewDataFrame.java for Multithreading example
- See DBConnection.java for Singleton pattern
- See DashboardFrame.java for GUI event handling

---

**Happy Coding! 🚀**

