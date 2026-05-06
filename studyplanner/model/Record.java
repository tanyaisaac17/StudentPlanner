package com.studyplanner.model;


public abstract class Record {
    
    // Protected: Can be accessed by subclasses (Subject, Task, StudySession)
    protected int id;
    protected long createdAt;
    protected Status status;
    
    public Record() {
        this(0);
    }
    
    public Record(int id) {
        this.id = id;
        this.createdAt = System.currentTimeMillis();
        this.status = Status.PENDING;
    }
    
    public abstract String getDisplayName();
    
    public abstract String getRecordInfo();
    
    public void displayRecord() {
        System.out.println("========================================");
        System.out.println("Record ID: " + this.id);
        System.out.println("Created At: " + this.createdAt);
        System.out.println("Status: " + this.status.getDisplayName());
        System.out.println("Details: " + this.getRecordInfo());
        System.out.println("========================================");
    }
    
    /**
     * RECURSION EXAMPLE: Simple recursive function to demonstrate the concept
     * Calculates factorial of a number
     * Base case: when n <= 1, return 1
     * Recursive case: n * factorial(n-1)
     */
    public static int calculateFactorial(int n) {
        // Base case: terminating condition for recursion
        if (n <= 1) {
            return 1;
        }
        // Recursive case: function calls itself with smaller parameter
        return n * calculateFactorial(n - 1);
    }
    
    // ============= GETTER AND SETTER METHODS =============
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public long getCreatedAt() {
        return this.createdAt;
    }
    
    public Status getStatus() {
        return this.status;
    }
    
    public void setStatus(Status status) {
        this.status = status;
    }
}
