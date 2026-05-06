package com.studyplanner.model;

/**
 * INHERITANCE: Subject extends (inherits from) Record abstract class
 * Inherits all properties and methods from Record
 * 
 * KEY CONCEPTS DEMONSTRATED:
 * - extends keyword for inheritance
 * - super() to call parent constructor
 * - @Override annotation to indicate method overriding
 * - Constructors (default and parameterized)
 */
@DatabaseEntity(
    tableName = "subjects",
    description = "Subject being studied",
    auditable = true
)
public class Subject extends Record {
    
    private String name;
    
    // WRAPPER CLASS EXAMPLE: Integer is wrapper class for primitive int
    // Wrapper classes provide utility methods and can be used in collections
    private Integer credits; // Wrapper class instead of primitive int
    
    /**
     * DEFAULT CONSTRUCTOR
     * Calls parent class (Record) default constructor via super()
     * super() must be first statement in constructor
     */
    public Subject() {
        super(); // Calls Record() constructor
        this.name = "";
        this.credits = 0; // AUTOBOXING: int 0 automatically converted to Integer
    }
    
    /**
     * PARAMETERIZED CONSTRUCTOR
     * Calls parent constructor with id parameter
     */
    public Subject(int id, String name, Integer credits) {
        super(id); // Calls Record(int id) constructor
        this.name = name;
        this.credits = credits;
    }
    
    /**
     * Constructor with only name
     * Demonstrates multiple constructors (constructor overloading)
     */
    public Subject(String name) {
        super(); // Calls default Record constructor
        this.name = name;
        this.credits = 0;
    }
    
/**
     * METHOD OVERRIDE: Implements abstract method from Record
     * @Override annotation helps detect errors during compilation
     * Returns display name of the subject
     */
    @Override
    public String getDisplayName() {
        return "Subject: " + this.name;
    }
    
    /**
     * METHOD OVERRIDE: Implements abstract method from Record
     * Provides subject-specific information
     */
    @Override
    public String getRecordInfo() {
        // AUTOBOXING/UNBOXING: Integer credits automatically converted to String
        return "Name: " + this.name + " | Credits: " + this.credits;
    }
    
public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * UNBOXING: Integer wrapper class automatically converted to primitive int
     * Wrapper classes allow null values and provide utility methods
     */
    public Integer getCredits() {
        return this.credits;
    }
    
    public void setCredits(Integer credits) {
        this.credits = credits;
    }
    
    /**
     * STATIC FINAL EXAMPLE: Constant value used for validation
     * static: belongs to class, not instance (shared across all objects)
     * final: cannot be modified once assigned
     */
    public static final int MAX_CREDITS = 6;
    
    /**
     * Validates if credits are within allowed range
     * Uses static final constant defined above
     */
    public boolean isValidCredits() {
        return this.credits > 0 && this.credits <= MAX_CREDITS;
    }
    
    @Override
    public String toString() {
        return "Subject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", credits=" + credits +
                ", status=" + status +
                '}';
    }
}
