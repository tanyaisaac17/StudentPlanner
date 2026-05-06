package com.studyplanner.service;

import com.studyplanner.db.DBConnection;
import com.studyplanner.model.Subject;
import com.studyplanner.model.StudySession;
import com.studyplanner.model.Task;
import com.studyplanner.model.User;
import com.studyplanner.util.PasswordHasher;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * SERVICE IMPLEMENTATION
 * 
 * KEY CONCEPTS DEMONSTRATED:
 * - Implements interface PlannerService
 * - ArrayList for storing data in memory
 * - HashMap for key-value storage
 * - Iterator for collection traversal
 * - Exception handling (try-catch-finally)
 * - Input validation
 * - Resource management
 * 
 * NOTE ON STRING vs StringBuilder vs StringBuffer:
 * 
 * STRING:
 * - Immutable (cannot be changed after creation)
 * - Each modification creates new String object
 * - Use when: String won't change, single concatenation
 * - Memory: Creates new object in memory for each concatenation
 * - Thread-safe: Yes (immutable)
 * 
 * STRINGBUILDER:
 * - Mutable (can be changed)
 * - More efficient for multiple concatenations in loop
 * - Use when: Building string with many concatenations (NOT thread-safe)
 * - Memory: Efficient, modifies same object
 * - Thread-safe: No
 * 
 * STRINGBUFFER:
 * - Mutable (can be changed)
 * - Thread-safe version of StringBuilder
 * - Synchronized methods (slower than StringBuilder)
 * - Use when: Multiple concatenations AND multithreading needed
 * - Memory: Similar to StringBuilder but with synchronization overhead
 * - Thread-safe: Yes
 * 
 * EXAMPLE in this class:
 * - String used for simple values
 * - StringBuilder used in buildQueryString() for building dynamic SQL queries
 */
public class PlannerServiceImpl implements PlannerService {
    
    // Instance of database connection (Singleton)
    private DBConnection dbConnection;
    
    // HashMap to cache subjects in memory for faster access
    // Key: Subject ID, Value: Subject object
    private HashMap<Integer, Subject> subjectCache;
    
    // ArrayList to store multiple subjects
    private ArrayList<Subject> subjectList;
    
    // Similarly for sessions and tasks
    private HashMap<Integer, StudySession> sessionCache;
    private ArrayList<StudySession> sessionList;
    
    private HashMap<Integer, Task> taskCache;
    private ArrayList<Task> taskList;
    
    /**
     * CONSTRUCTOR: Initialize data structures and get database connection
     * Initializes all ArrayList and HashMap collections
     */
    public PlannerServiceImpl() {
        this.dbConnection = DBConnection.getInstance();
        
        // Initialize collections
        this.subjectCache = new HashMap<>();
        this.subjectList = new ArrayList<>();
        
        this.sessionCache = new HashMap<>();
        this.sessionList = new ArrayList<>();
        
        this.taskCache = new HashMap<>();
        this.taskList = new ArrayList<>();

        ensureTaskCompletedColumn();
    }

    private void ensureTaskCompletedColumn() {
        try {
            if (dbConnection == null || dbConnection.getConnection() == null) {
                return;
            }

            var conn = dbConnection.getConnection();
            var meta = conn.getMetaData();
            try (var rs = meta.getColumns(conn.getCatalog(), null, "tasks", "completed")) {
                if (rs.next()) {
                    return; // column exists
                }
            }

            // Add missing column (keeps older DB setups working with task completion UI)
            try (var stmt = conn.createStatement()) {
                stmt.executeUpdate("ALTER TABLE tasks ADD COLUMN completed BOOLEAN NOT NULL DEFAULT FALSE");
            }
        } catch (Exception ignored) {
            // Schema check should not crash the app in demo setups
        }
    }
    
    // ============= SUBJECT OPERATIONS =============
    
    @Override
    public boolean addSubject(Subject subject) throws Exception {
        // INPUT VALIDATION
        if (subject == null || !validateSubjectName(subject.getName())) {
            System.err.println("❌ Invalid subject name!");
            return false;
        }
        
        try {
            /**
             * PARAMETERIZED QUERY: Prevents SQL Injection
             * ? placeholders are filled with actual values safely
             */
            String query = "INSERT INTO subjects (name) VALUES (?)";
            PreparedStatement pstmt = dbConnection.prepareStatement(query);
            pstmt.setString(1, subject.getName());
            
            int result = pstmt.executeUpdate();
            
            if (result > 0) {
                System.out.println("✓ Subject added successfully!");
                
                // Add to in-memory cache
                subjectList.add(subject);
                if (subject.getId() > 0) {
                    subjectCache.put(subject.getId(), subject);
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("❌ Database error adding subject: " + e.getMessage());
            throw new Exception("Failed to add subject", e);
        }
        return false;
    }
    
    @Override
    public List<Subject> getAllSubjects() throws Exception {
        /**
         * STRINGBUILDER EXAMPLE: Building dynamic query efficiently
         * More efficient than String concatenation in loops
         */
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT id, name FROM subjects");
        
        try {
            ResultSet rs = dbConnection.executeQuery(queryBuilder.toString());
            
            // Clear previous data
            subjectList.clear();
            subjectCache.clear();
            
            /**
             * WHILE LOOP: Process ResultSet rows
             * Similar to array iteration but for database result
             */
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                
                Subject subject = new Subject(id, name, 0);
                subjectList.add(subject);
                subjectCache.put(id, subject);
            }
            
            rs.close();
            return subjectList;
            
        } catch (SQLException e) {
            System.err.println("❌ Database error fetching subjects: " + e.getMessage());
            throw new Exception("Failed to fetch subjects", e);
        }
    }
    
    @Override
    public Subject getSubjectById(int id) throws Exception {
        // Check cache first (optimization)
        if (subjectCache.containsKey(id)) {
            return subjectCache.get(id);
        }
        
        try {
            String query = "SELECT id, name FROM subjects WHERE id = ?";
            PreparedStatement pstmt = dbConnection.prepareStatement(query);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Subject subject = new Subject(rs.getInt("id"), rs.getString("name"), 0);
                subjectCache.put(id, subject);
                return subject;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching subject: " + e.getMessage());
            throw new Exception("Failed to fetch subject", e);
        }
        return null;
    }
    
    @Override
    public boolean deleteSubject(int id) throws Exception {
        try {
            String query = "DELETE FROM subjects WHERE id = ?";
            PreparedStatement pstmt = dbConnection.prepareStatement(query);
            pstmt.setInt(1, id);
            
            int result = pstmt.executeUpdate();
            
            if (result > 0) {
                // Remove from cache
                subjectCache.remove(id);
                
                // Remove from list using ITERATOR
                /**
                 * ITERATOR PATTERN: Safe way to remove elements while iterating
                 * Using iterator instead of for loop prevents ConcurrentModificationException
                 */
                Iterator<Subject> iterator = subjectList.iterator();
                while (iterator.hasNext()) {
                    Subject subject = iterator.next();
                    if (subject.getId() == id) {
                        iterator.remove(); // Safe removal during iteration
                        break;
                    }
                }
                System.out.println("✓ Subject deleted successfully!");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error deleting subject: " + e.getMessage());
            throw new Exception("Failed to delete subject", e);
        }
        return false;
    }
    
    // ============= STUDY SESSION OPERATIONS =============
    
    @Override
    public boolean addStudySession(StudySession session) throws Exception {
        if (session == null || !validateStudyHours(session.getHours())) {
            System.err.println("❌ Invalid study session data!");
            return false;
        }
        
        try {
            String query = "INSERT INTO sessions (subject, hours) VALUES (?, ?)";
            PreparedStatement pstmt = dbConnection.prepareStatement(query);
            pstmt.setString(1, session.getSubject());
            pstmt.setInt(2, session.getHours());
            
            int result = pstmt.executeUpdate();
            
            if (result > 0) {
                System.out.println("✓ Study session added successfully!");
                sessionList.add(session);
                if (session.getId() > 0) {
                    sessionCache.put(session.getId(), session);
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error adding session: " + e.getMessage());
            throw new Exception("Failed to add session", e);
        }
        return false;
    }
    
    @Override
    public List<StudySession> getAllSessions() throws Exception {
        try {
            String query = "SELECT id, subject, hours FROM sessions";
            ResultSet rs = dbConnection.executeQuery(query);
            
            sessionList.clear();
            sessionCache.clear();
            
            while (rs.next()) {
                int id = rs.getInt("id");
                String subject = rs.getString("subject");
                int hours = rs.getInt("hours");
                
                StudySession session = new StudySession(id, subject, hours);
                sessionList.add(session);
                sessionCache.put(id, session);
            }
            
            rs.close();
            return sessionList;
            
        } catch (SQLException e) {
            System.err.println("❌ Error fetching sessions: " + e.getMessage());
            throw new Exception("Failed to fetch sessions", e);
        }
    }
    
    @Override
    public boolean deleteSession(int id) throws Exception {
        try {
            String query = "DELETE FROM sessions WHERE id = ?";
            PreparedStatement pstmt = dbConnection.prepareStatement(query);
            pstmt.setInt(1, id);
            
            int result = pstmt.executeUpdate();
            
            if (result > 0) {
                sessionCache.remove(id);
                
                // Using ITERATOR to safely remove from list
                Iterator<StudySession> iterator = sessionList.iterator();
                while (iterator.hasNext()) {
                    if (iterator.next().getId() == id) {
                        iterator.remove();
                        break;
                    }
                }
                System.out.println("✓ Session deleted successfully!");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error deleting session: " + e.getMessage());
            throw new Exception("Failed to delete session", e);
        }
        return false;
    }
    
    // ============= TASK OPERATIONS =============
    
    @Override
    public boolean addTask(Task task) throws Exception {
        if (task == null || !validateTaskDescription(task.getTaskDescription())) {
            System.err.println("❌ Invalid task data!");
            return false;
        }
        
        try {
            String query = "INSERT INTO tasks (task) VALUES (?)";
            PreparedStatement pstmt = dbConnection.prepareStatement(query);
            pstmt.setString(1, task.getTaskDescription());
            
            int result = pstmt.executeUpdate();
            
            if (result > 0) {
                System.out.println("✓ Task added successfully!");
                taskList.add(task);
                if (task.getId() > 0) {
                    taskCache.put(task.getId(), task);
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error adding task: " + e.getMessage());
            throw new Exception("Failed to add task", e);
        }
        return false;
    }
    
    @Override
    public List<Task> getAllTasks() throws Exception {
        try {
            String query = "SELECT id, task, completed FROM tasks";
            ResultSet rs = dbConnection.executeQuery(query);
            
            taskList.clear();
            taskCache.clear();
            
            while (rs.next()) {
                int id = rs.getInt("id");
                String taskDesc = rs.getString("task");
                boolean completed = rs.getBoolean("completed");
                
                Task task = new Task(id, taskDesc);
                task.setCompleted(completed);
                taskList.add(task);
                taskCache.put(id, task);
            }
            
            rs.close();
            return taskList;
            
        } catch (SQLException e) {
            System.err.println("❌ Error fetching tasks: " + e.getMessage());
            throw new Exception("Failed to fetch tasks", e);
        }
    }
    
    @Override
    public boolean deleteTask(int id) throws Exception {
        try {
            String query = "DELETE FROM tasks WHERE id = ?";
            PreparedStatement pstmt = dbConnection.prepareStatement(query);
            pstmt.setInt(1, id);
            
            int result = pstmt.executeUpdate();
            
            if (result > 0) {
                taskCache.remove(id);
                
                Iterator<Task> iterator = taskList.iterator();
                while (iterator.hasNext()) {
                    if (iterator.next().getId() == id) {
                        iterator.remove();
                        break;
                    }
                }
                System.out.println("✓ Task deleted successfully!");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error deleting task: " + e.getMessage());
            throw new Exception("Failed to delete task", e);
        }
        return false;
    }
    
    @Override
    public Task getTaskById(int id) throws Exception {
        try {
            // Check cache first
            if (taskCache.containsKey(id)) {
                return taskCache.get(id);
            }
            
            String query = "SELECT id, task, completed FROM tasks WHERE id = ?";
            PreparedStatement pstmt = dbConnection.prepareStatement(query);
            pstmt.setInt(1, id);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String taskDesc = rs.getString("task");
                boolean completed = rs.getBoolean("completed");
                
                Task task = new Task(id, taskDesc);
                task.setCompleted(completed);
                taskCache.put(id, task);
                return task;
            }
            
            rs.close();
            return null;
            
        } catch (SQLException e) {
            System.err.println("❌ Error fetching task: " + e.getMessage());
            throw new Exception("Failed to fetch task", e);
        }
    }
    
    @Override
    public boolean updateTask(Task task) throws Exception {
        try {
            String query = "UPDATE tasks SET task = ?, completed = ? WHERE id = ?";
            PreparedStatement pstmt = dbConnection.prepareStatement(query);
            pstmt.setString(1, task.getTaskDescription());
            pstmt.setBoolean(2, task.isCompleted());
            pstmt.setInt(3, task.getId());
            
            int result = pstmt.executeUpdate();
            
            if (result > 0) {
                taskCache.put(task.getId(), task);
                System.out.println("✓ Task updated successfully!");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error updating task: " + e.getMessage());
            throw new Exception("Failed to update task", e);
        }
        return false;
    }
    
    // ============= VALIDATION METHODS =============
    
    @Override
    public boolean validateSubjectName(String name) {
        return name != null && !name.trim().isEmpty() && name.length() <= 50;
    }
    
    @Override
    public boolean validateStudyHours(int hours) {
        return hours > 0 && hours <= 24;
    }
    
    @Override
    public boolean validateTaskDescription(String description) {
        return description != null && !description.trim().isEmpty() && description.length() <= 100;
    }

    // ============= USER / AUTH OPERATIONS =============

    @Override
    public User registerUser(String username, String password) throws Exception {
        String name = username == null ? "" : username.trim();
        if (name.length() < 3 || name.length() > 30) {
            throw new Exception("Username must be 3-30 characters");
        }
        if (password == null || password.length() < 4) {
            throw new Exception("Password must be at least 4 characters");
        }

        String salt = PasswordHasher.newSalt();
        String hash = PasswordHasher.hash(password, salt);

        try {
            String query = "INSERT INTO users (username, password_hash, salt) VALUES (?, ?, ?)";
            PreparedStatement pstmt = dbConnection.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setString(2, hash);
            pstmt.setString(3, salt);

            int rows;
            try {
                rows = pstmt.executeUpdate();
            } catch (SQLException e) {
                // MySQL duplicate key
                if (e.getErrorCode() == 1062) {
                    throw new Exception("Username '" + name + "' is already taken");
                }
                throw e;
            }

            if (rows <= 0) {
                throw new Exception("Failed to create account");
            }

            int newId = -1;
            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys != null && keys.next()) {
                    newId = keys.getInt(1);
                }
            } catch (SQLException ignored) {
                // Some drivers require Statement.RETURN_GENERATED_KEYS — fall back to lookup below
            }
            if (newId <= 0) {
                String lookup = "SELECT id FROM users WHERE username = ?";
                PreparedStatement p2 = dbConnection.prepareStatement(lookup);
                p2.setString(1, name);
                try (ResultSet rs = p2.executeQuery()) {
                    if (rs.next()) newId = rs.getInt("id");
                }
            }

            System.out.println("✓ User registered: " + name);
            return new User(newId, name);
        } catch (SQLException e) {
            System.err.println("❌ Database error registering user: " + e.getMessage());
            throw new Exception("Failed to register user: " + e.getMessage(), e);
        }
    }

    @Override
    public User authenticateUser(String username, String password) throws Exception {
        if (username == null || password == null) return null;
        String name = username.trim();
        if (name.isEmpty() || password.isEmpty()) return null;

        try {
            String query = "SELECT id, username, password_hash, salt FROM users WHERE username = ?";
            PreparedStatement pstmt = dbConnection.prepareStatement(query);
            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next()) return null;
                String storedHash = rs.getString("password_hash");
                String salt = rs.getString("salt");
                if (PasswordHasher.matches(password, salt, storedHash)) {
                    return new User(rs.getInt("id"), rs.getString("username"));
                }
                return null;
            }
        } catch (SQLException e) {
            System.err.println("❌ Database error authenticating user: " + e.getMessage());
            throw new Exception("Authentication failed: " + e.getMessage(), e);
        }
    }
}
