package com.model;

public class Answer {
    private int answerId;
    private int studentExamId;
    private int questionId;
    private String givenAnswer;
    private double pointsAwarded;

    public Answer() {}

    // Getters and Setters
    public int getAnswerId() { return answerId; }
    public void setAnswerId(int answerId) { this.answerId = answerId; }

    public int getStudentExamId() { return studentExamId; }
    public void setStudentExamId(int studentExamId) { this.studentExamId = studentExamId; }

    public int getQuestionId() { return questionId; }
    public void setQuestionId(int questionId) { this.questionId = questionId; }

    public String getGivenAnswer() { return givenAnswer; }
    public void setGivenAnswer(String givenAnswer) { this.givenAnswer = givenAnswer; }

    public double getPointsAwarded() { return pointsAwarded; }
    public void setPointsAwarded(double pointsAwarded) { this.pointsAwarded = pointsAwarded; }
}
