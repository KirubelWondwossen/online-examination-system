package com.dao;

import com.model.StudentExam;
import com.model.Answer;
import com.util.DBConnection;
import java.sql.*;

public class StudentExamDAO {

    public int startExam(int studentId, int examId) {
        String sql = "INSERT INTO student_exams (student_id, exam_id, start_time, status) VALUES (?, ?, NOW(), 'IN_PROGRESS')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, examId);
            
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean submitExam(int studentExamId, double totalScore) {
        String sql = "UPDATE student_exams SET end_time = NOW(), total_score = ?, status = 'SUBMITTED' WHERE student_exam_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDouble(1, totalScore);
            pstmt.setInt(2, studentExamId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public StudentExam getStudentExam(int studentExamId) {
        String sql = "SELECT * FROM student_exams WHERE student_exam_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, studentExamId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    StudentExam se = new StudentExam();
                    se.setStudentExamId(rs.getInt("student_exam_id"));
                    se.setStudentId(rs.getInt("student_id"));
                    se.setExamId(rs.getInt("exam_id"));
                    se.setStartTime(rs.getTimestamp("start_time"));
                    se.setEndTime(rs.getTimestamp("end_time"));
                    se.setTotalScore(rs.getDouble("total_score"));
                    se.setStatus(rs.getString("status"));
                    return se;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Check if student has already taken the exam
    public boolean hasTakenExam(int studentId, int examId) {
        String sql = "SELECT 1 FROM student_exams WHERE student_id = ? AND exam_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, examId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void saveAnswer(Answer answer) {
        String sql = "INSERT INTO student_answers (student_exam_id, question_id, given_answer, points_awarded) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, answer.getStudentExamId());
            pstmt.setInt(2, answer.getQuestionId());
            pstmt.setString(3, answer.getGivenAnswer());
            pstmt.setDouble(4, answer.getPointsAwarded());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
