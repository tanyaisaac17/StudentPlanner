# ⚡ Quick Start Guide - Student Study Planner

## 🚀 Get Running in 5 Minutes

### Prerequisites Check
```bash
java -version  # Should show Java 17 or 21
mysql --version  # Should show MySQL installed
```

### 1. Start MySQL (if not already running)

**Windows**:
```bash
# MySQL should start automatically as service
# Or manually start: Start Menu → Services → MySQL
```

**macOS**:
```bash
brew services start mysql
# or
mysql.server start
```

**Linux**:
```bash
sudo service mysql start
```

### 2. Create Database (Copy & Paste)

Open MySQL terminal and run:
```bash
mysql -u root
```

Then paste this:
```sql
CREATE DATABASE IF NOT EXISTS study_planner;
USE study_planner;

CREATE TABLE subjects (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE sessions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    subject VARCHAR(50) NOT NULL,
    hours INT NOT NULL CHECK (hours > 0 AND hours <= 24),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tasks (
    id INT AUTO_INCREMENT PRIMARY KEY,
    task VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

SHOW TABLES;
```

### 3. Download MySQL JDBC Driver

- Go to: https://dev.mysql.com/downloads/connector/j/
- Download `mysql-connector-java-8.0.33.jar`
- Save to: `C:\Users\DELL\Downloads\`

### 4. Update DBConnection.java (if needed)

Edit: `src/com/studyplanner/db/DBConnection.java`

Line 34-36:
```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/study_planner";
private static final String DB_USER = "root";
private static final String DB_PASSWORD = ""; // Add your MySQL password here if any
```

### 5. Compile & Run

**Open Command Prompt/PowerShell in project folder**:

```bash
cd C:\Users\DELL\OneDrive\Desktop\StudentPlanner
```

**Set classpath and compile**:
```batch
set CLASSPATH=C:\Users\DELL\Downloads\mysql-connector-java-8.0.33.jar;.

javac -d . src/com/studyplanner/model/*.java
javac -d . src/com/studyplanner/db/*.java
javac -d . src/com/studyplanner/service/*.java
javac -d . src/com/studyplanner/gui/*.java
javac -d . src/com/studyplanner/main/*.java
```

**Run application**:
```batch
java -cp . com.studyplanner.main.StudyPlannerApp
```

✅ **Application should launch!**

---

## 📝 Using the Application

### Adding a Subject
1. Click **"➕ Add Subject"**
2. Enter: "Mathematics"
3. Select Credits: 3
4. Click **"✓ Add Subject"**

### Adding a Study Session
1. Click **"⏱️ Add Study Session"**
2. Select Subject: "Mathematics"
3. Enter Hours: 2
4. Click **"✓ Add Session"**

### Adding a Task
1. Click **"✓ Add Task"**
2. Enter: "Solve chapter 5 exercises"
3. Click **"✓ Add Task"**

### Viewing Data
1. Click **"📊 View Data"**
2. Data loads in background (demonstrates multithreading)
3. Click **"🔄 Refresh Data"** to get latest

---

## 🔧 Troubleshooting

### "Cannot find symbol: class DBConnection"
**Fix**: Compile model first:
```batch
javac -d . src/com/studyplanner/model/*.java
javac -d . src/com/studyplanner/db/*.java
```

### "JDBC Driver not found"
**Fix**: Check CLASSPATH:
```batch
set CLASSPATH=C:\Users\DELL\Downloads\mysql-connector-java-8.0.33.jar;.
java -cp . com.studyplanner.main.StudyPlannerApp
```

### "Cannot connect to database"
**Fix**:
1. Check MySQL is running: `mysql -u root` (should connect)
2. Check database exists: `SHOW DATABASES;`
3. Check credentials in DBConnection.java

### "Table doesn't exist"
**Fix**: Re-run the SQL CREATE TABLE commands from Step 2

---

## 📦 File Structure

```
StudentPlanner/
├── src/com/studyplanner/
│   ├── main/StudyPlannerApp.java ← Start here
│   ├── gui/DashboardFrame.java ← Main window
│   ├── service/PlannerServiceImpl.java ← Business logic
│   ├── db/DBConnection.java ← Database connection
│   └── model/ ← Data classes
├── SETUP_GUIDE.md ← Detailed setup
├── JAVA_CONCEPTS_GUIDE.md ← Learn Java concepts
└── QUICK_START.md ← This file
```

---

## 🎓 Key Java Concepts to Understand

1. **Inheritance**: How Subject/Task extend Record
2. **Singleton**: Single DBConnection instance
3. **Interface**: PlannerService contract
4. **Collections**: ArrayList, HashMap, Iterator
5. **Exception Handling**: Try-catch for database errors
6. **Multithreading**: Background data loading
7. **JDBC**: Database queries
8. **Swing**: GUI components and event handling

👉 See `JAVA_CONCEPTS_GUIDE.md` for detailed explanations

---

## ✨ Pro Tips

1. **Run with One Command** - Create `run.bat` (see SETUP_GUIDE.md)
2. **Debug Mode** - Add `System.out.println()` to trace execution
3. **Check Database** - Verify data in MySQL:
   ```sql
   SELECT * FROM subjects;
   SELECT * FROM sessions;
   SELECT * FROM tasks;
   ```
4. **Clear Database** - Delete all data:
   ```sql
   DELETE FROM subjects;
   DELETE FROM sessions;
   DELETE FROM tasks;
   ```

---

## 📖 Study Tips for Viva

Before your viva exam:

1. Read through each Java file
2. Understand each method and why it exists
3. Review JAVA_CONCEPTS_GUIDE.md
4. Prepare answers for:
   - What is inheritance?
   - Why is Record abstract?
   - How does Singleton pattern work?
   - What is multithreading?
   - Why use PreparedStatement?
   - What are Collections?

---

**Happy Learning! 🎓**

If stuck, check:
1. SETUP_GUIDE.md - Detailed setup instructions
2. JAVA_CONCEPTS_GUIDE.md - Concept explanations
3. Code comments - Inline explanations in source files
4. Console output - Check error messages

