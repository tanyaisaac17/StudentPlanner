package com.studyplanner.model;


@DatabaseEntity(
    tableName = "tasks",
    description = "To-Do task for study planner",
    auditable = true
)
public class Task extends Record {
    
    private String taskDescription;
    private boolean isCompleted;
    

    public Task() {
        super();
        this.taskDescription = "";
        this.isCompleted = false;
    }
    

    public Task(int id, String taskDescription) {
        super(id);
        this.taskDescription = taskDescription;
        this.isCompleted = false;
    }
    

    public Task(String taskDescription) {
        super();
        this.taskDescription = taskDescription;
        this.isCompleted = false;
    }
    
@Override
    public String getDisplayName() {
        return "Task: " + this.taskDescription + " [" + (isCompleted ? "DONE" : "PENDING") + "]";
    }
    
    @Override
    public String getRecordInfo() {
        return "Description: " + this.taskDescription + " | Completed: " + this.isCompleted;
    }
    
public String getTaskDescription() {
        return this.taskDescription;
    }
    
    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }
    
    public boolean isCompleted() {
        return this.isCompleted;
    }
    
    public void setCompleted(boolean completed) {
        isCompleted = completed;
        if (completed) {
            this.status = Status.COMPLETED;
        }
    }
    
    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", taskDescription='" + taskDescription + '\'' +
                ", isCompleted=" + isCompleted +
                ", status=" + status +
                '}';
    }
}
