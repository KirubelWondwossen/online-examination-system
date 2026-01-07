package com.model;

import java.sql.Timestamp;

public class Exam {
    private int examId;
    private String title;
    private String description;
    private Timestamp startTime;
    private int durationMinutes;
    private int instructorId;
    private boolean isPublished;
    private boolean isResultPublished;
    private int totalQuestions;
    private Timestamp createdAt;

    public Exam() {}

    public Exam(int examId, String title, String description, Timestamp startTime, int durationMinutes, int instructorId, boolean isPublished) {
        this.examId = examId;
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.durationMinutes = durationMinutes;
        this.instructorId = instructorId;
        this.isPublished = isPublished;
    }

    // Getters and Setters
    public int getExamId() { return examId; }
    public void setExamId(int examId) { this.examId = examId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Timestamp getStartTime() { return startTime; }
    public void setStartTime(Timestamp startTime) { this.startTime = startTime; }

    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }

    public int getInstructorId() { return instructorId; }
    public void setInstructorId(int instructorId) { this.instructorId = instructorId; }

    public boolean isPublished() { return isPublished; }
    public void setPublished(boolean published) { isPublished = published; }

    public boolean isResultPublished() { return isResultPublished; }
    public void setResultPublished(boolean resultPublished) { isResultPublished = resultPublished; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public int getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }
}
