package com.studyplanner.model;

@DatabaseEntity(
    tableName = "sessions",
    description = "Study session record",
    auditable = true
)
public class StudySession extends Record {
    
    private String subject;
    private int hours;
    private long startTime;
    
    public StudySession() {
        super();
        this.subject = "";
        this.hours = 0;
        this.startTime = 0;
    }
    
    public StudySession(int id, String subject, int hours) {
        super(id);
        this.subject = subject;
        this.hours = hours;
        this.startTime = System.currentTimeMillis();
    }
    
    public StudySession(String subject, int hours) {
        super();
        this.subject = subject;
        this.hours = hours;
        this.startTime = System.currentTimeMillis();
    }
    
    // ============= METHOD OVERRIDING =============
    @Override
    public String getDisplayName() {
        return "Study Session: " + this.subject + " (" + this.hours + " hours)";
    }
    
    @Override
    public String getRecordInfo() {
        return "Subject: " + this.subject + " | Hours: " + this.hours + " | Started: " + this.startTime;
    }
    
    // ============= GETTER AND SETTER METHODS =============
    public String getSubject() {
        return this.subject;
    }
    
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public int getHours() {
        return this.hours;
    }
    
    public void setHours(int hours) {
        this.hours = hours;
    }
    
    public long getStartTime() {
        return this.startTime;
    }
    
    @Override
    public String toString() {
        return "StudySession{" +
                "id=" + id +
                ", subject='" + subject + '\'' +
                ", hours=" + hours +
                ", status=" + status +
                '}';
    }
}
