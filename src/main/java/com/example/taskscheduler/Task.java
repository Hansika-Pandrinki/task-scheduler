package com.example.taskscheduler;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int priority;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    private int retryCount;
    private LocalDateTime createdAt;

    public Task() {}

    public Task(String name, int priority) {
        this.name = name;
        this.priority = priority;
        this.status = TaskStatus.PENDING;
        this.retryCount = 0;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public int getPriority() { return priority; }
    public TaskStatus getStatus() { return status; }
    public int getRetryCount() { return retryCount; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setStatus(TaskStatus status) { this.status = status; }
    public void setRetryCount(int retryCount) { this.retryCount = retryCount; }
}