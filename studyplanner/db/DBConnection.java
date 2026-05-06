package com.studyplanner.db;

import java.sql.*;

/**
 * DATABASE CONNECTION - SINGLETON PATTERN
 */
public class DBConnection {

    private static volatile DBConnection instance = null;

    private final Connection connection;
    private final SQLException connectionError;

    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DEFAULT_DB_URL ="jdbc:mysql://localhost:3306/study_planner?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String DEFAULT_DB_USER = "root";
    private static final String DEFAULT_DB_PASSWORD = "Gavril777*";

    private DBConnection() throws SQLException {
        try {
            Class.forName(DB_DRIVER);
            System.out.println("✓ MySQL JDBC Driver loaded successfully");

            String dbUrl = getenvOrDefault("STUDYPLANNER_DB_URL", DEFAULT_DB_URL);
            String dbUser = getenvOrDefault("STUDYPLANNER_DB_USER", DEFAULT_DB_USER);
            String dbPassword = getenvOrDefault("STUDYPLANNER_DB_PASSWORD", DEFAULT_DB_PASSWORD);

            this.connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            this.connectionError = null;

            System.out.println("✓ Database connected successfully!");
        } catch (ClassNotFoundException e) {
            throw new SQLException("JDBC Driver not found. Add mysql-connector-j JAR to classpath.", e);
        } catch (SQLException e) {
            throw new SQLException("Connection failed: " + e.getMessage(), e);
        }
    }

    private DBConnection(SQLException connectionError) {
        this.connection = null;
        this.connectionError = connectionError;
    }

    public static DBConnection getInstance() {
        if (instance == null) {
            synchronized (DBConnection.class) {
                if (instance == null) {
                    try {
                        instance = new DBConnection();
                    } catch (SQLException e) {
                        System.err.println("Failed to create database connection: " + e.getMessage());
                        instance = new DBConnection(e);
                    }
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        return this.connection;
    }

    public ResultSet executeQuery(String query) throws SQLException {
        ensureConnected();
        try {
            Statement statement = this.connection.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException e) {
            System.err.println("❌ Query execution error: " + e.getMessage());
            throw e;
        }
    }

    public int executeUpdate(String query) throws SQLException {
        ensureConnected();
        try {
            Statement statement = this.connection.createStatement();
            return statement.executeUpdate(query);
        } catch (SQLException e) {
            System.err.println("❌ Update execution error: " + e.getMessage());
            throw e;
        }
    }

    public PreparedStatement prepareStatement(String query) throws SQLException {
        ensureConnected();
        return this.connection.prepareStatement(query);
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("✓ Database connection closed");
            } catch (SQLException e) {
                System.err.println("❌ Error closing connection: " + e.getMessage());
            }
        }
    }

    public void clearAllData() throws SQLException {
        ensureConnected();
        try {
            // Disable foreign key checks temporarily
            executeUpdate("SET FOREIGN_KEY_CHECKS=0");
            
            // Delete all data from tables
            executeUpdate("DELETE FROM tasks");
            executeUpdate("DELETE FROM sessions");
            executeUpdate("DELETE FROM subjects");
            
            // Re-enable foreign key checks
            executeUpdate("SET FOREIGN_KEY_CHECKS=1");
            
            System.out.println("✓ All database data cleared successfully!");
        } catch (SQLException e) {
            System.err.println("❌ Error clearing database: " + e.getMessage());
            throw e;
        }
    }

    private void ensureConnected() throws SQLException {
        if (this.connection != null) {
            return;
        }
        if (this.connectionError != null) {
            throw new SQLException("Database not connected: " + this.connectionError.getMessage(), this.connectionError);
        }
        throw new SQLException("Database not connected.");
    }

    private static String getenvOrDefault(String key, String defaultValue) {
        try {
            String value = System.getenv(key);
            if (value == null || value.isBlank()) {
                return defaultValue;
            }
            return value;
        } catch (Exception ignored) {
            return defaultValue;
        }
    }
}

