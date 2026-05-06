package com.studyplanner.model;

/**
 * ENUM: A special data type that allows a variable to be a set of predefined constants
 * Enums are useful for status/state management, menu options, etc.
 * Benefits: Type-safe, prevents invalid values, improves code readability
 */
public enum Status {
    ACTIVE("Active"),
    PENDING("Pending"),
    COMPLETED("Completed"),
    FAILED("Failed");
    
    // Instance variable (each enum constant can have associated data)
    private final String displayName;
    
    // Constructor: Called when enum constant is created
    Status(String displayName) {
        this.displayName = displayName;
    }
    
    // Getter for displayName
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Method to get Status from string value
     * Example: Status.fromString("Active") returns Status.ACTIVE
     */
    public static Status fromString(String value) {
        for (Status status : Status.values()) {
            if (status.displayName.equalsIgnoreCase(value)) {
                return status;
            }
        }
        return Status.PENDING; // Default status
    }
}
