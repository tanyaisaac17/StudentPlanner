package com.studyplanner.service;

import com.studyplanner.model.Subject;
import com.studyplanner.model.StudySession;
import com.studyplanner.model.Task;
import com.studyplanner.model.User;
import java.util.ArrayList;
import java.util.List;


public interface PlannerService {

    // ============= USER / AUTH OPERATIONS =============

    /**
     * Registers a new user with a hashed password.
     * @return the persisted User on success
     * @throws Exception if validation fails or username is already taken
     */
    User registerUser(String username, String password) throws Exception;

    /**
     * Verifies credentials.
     * @return the matching User on success, null if credentials are invalid
     * @throws Exception only on database/system errors
     */
    User authenticateUser(String username, String password) throws Exception;


    
    // Subject operations
    boolean addSubject(Subject subject) throws Exception;
    
    /**
     * Retrieves all subjects from database
     * @return List of Subject objects
     * @throws Exception if database operation fails
     */
    List<Subject> getAllSubjects() throws Exception;
    
    /**
     * Retrieves a subject by ID
     * @param id Subject ID
     * @return Subject object if found, null otherwise
     * @throws Exception if database operation fails
     */
    Subject getSubjectById(int id) throws Exception;
    
    /**
     * Deletes a subject by ID
     * @param id Subject ID
     * @return true if successful, false otherwise
     * @throws Exception if database operation fails
     */
    boolean deleteSubject(int id) throws Exception;
    
    // Study session operations
    boolean addStudySession(StudySession session) throws Exception;
    
    /**
     * Retrieves all study sessions from database
     * @return List of StudySession objects
     * @throws Exception if database operation fails
     */
    List<StudySession> getAllSessions() throws Exception;
    
    /**
     * Deletes a study session by ID
     * @param id Session ID
     * @return true if successful, false otherwise
     * @throws Exception if database operation fails
     */
    boolean deleteSession(int id) throws Exception;
    
    // ============= TASK OPERATIONS =============
    
    /**
     * Adds a new task to database
     * @param task Task object to add
     * @return true if successful, false otherwise
     * @throws Exception if database operation fails
     */
    boolean addTask(Task task) throws Exception;
    
    /**
     * Retrieves all tasks from database
     * @return List of Task objects
     * @throws Exception if database operation fails
     */
    List<Task> getAllTasks() throws Exception;
    
    /**
     * Deletes a task by ID
     * @param id Task ID
     * @return true if successful, false otherwise
     * @throws Exception if database operation fails
     */
    boolean deleteTask(int id) throws Exception;
    
    /**
     * Retrieves a task by ID
     * @param id Task ID
     * @return Task object if found, null otherwise
     * @throws Exception if database operation fails
     */
    Task getTaskById(int id) throws Exception;
    
    /**
     * Updates an existing task in database
     * @param task Task object with updated values
     * @return true if successful, false otherwise
     * @throws Exception if database operation fails
     */
    boolean updateTask(Task task) throws Exception;
    
    // ============= UTILITY METHODS =============
    
    /**
     * Validates if a subject name is not empty
     * @param name Subject name
     * @return true if valid, false otherwise
     */
    boolean validateSubjectName(String name);
    
    /**
     * Validates if study hours are positive
     * @param hours Study hours
     * @return true if valid, false otherwise
     */
    boolean validateStudyHours(int hours);
    
    /**
     * Validates if task description is not empty
     * @param description Task description
     * @return true if valid, false otherwise
     */
    boolean validateTaskDescription(String description);
}
