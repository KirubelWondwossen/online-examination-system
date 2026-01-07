package com.model;

import java.sql.Timestamp;

public class StudentExam {
    private int studentExamId;
    private int studentId;
    private int examId;
    private Timestamp startTime;
    private Timestamp endTime;
    private double totalScore;
    private String status; // IN_PROGRESS, SUBMITTED, GRADED
    
    // Transient fields for View
    private String examTitle;
    private boolean resultPublished;

    public StudentExam() {}

    // Getters and Setters
    public int getStudentExamId() { return studentExamId; }
    public void setStudentExamId(int studentExamId) { this.studentExamId = studentExamId; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public int getExamId() { return examId; }
    public void setExamId(int examId) { this.examId = examId; }

    public Timestamp getStartTime() { return startTime; }
    public void setStartTime(Timestamp startTime) { this.startTime = startTime; }

    public Timestamp getEndTime() { return endTime; }
    public void setEndTime(Timestamp endTime) { this.endTime = endTime; }

    public double getTotalScore() { return totalScore; }
    public void setTotalScore(double totalScore) { this.totalScore = totalScore; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getExamTitle() { return examTitle; }
    public void setExamTitle(String examTitle) { this.examTitle = examTitle; }

    public boolean isResultPublished() { return resultPublished; }
    public void setResultPublished(boolean resultPublished) { this.resultPublished = resultPublished; }
}
