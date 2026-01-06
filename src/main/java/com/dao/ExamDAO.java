package com.dao;

import com.model.Exam;
import com.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExamDAO {

    public boolean createExam(Exam exam) {
        String sql = "INSERT INTO exams (title, description, start_time, duration_minutes, instructor_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, exam.getTitle());
            pstmt.setString(2, exam.getDescription());
            pstmt.setTimestamp(3, exam.getStartTime());
            pstmt.setInt(4, exam.getDurationMinutes());
            pstmt.setInt(5, exam.getInstructorId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Exam> getExamsByInstructor(int instructorId) {
        List<Exam> exams = new ArrayList<>();
        String sql = "SELECT * FROM exams WHERE instructor_id = ? ORDER BY created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, instructorId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    exams.add(mapResultSetToExam(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exams;
    }

    public List<Exam> getAllPublishedExams() {
        List<Exam> exams = new ArrayList<>();
        String sql = "SELECT * FROM exams WHERE is_published = TRUE ORDER BY start_time ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    exams.add(mapResultSetToExam(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exams;
    }

    public Exam getExamById(int examId) {
        String sql = "SELECT * FROM exams WHERE exam_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, examId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToExam(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean publishExam(int examId) {
        String sql = "UPDATE exams SET is_published = TRUE WHERE exam_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, examId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Exam mapResultSetToExam(ResultSet rs) throws SQLException {
        Exam exam = new Exam();
        exam.setExamId(rs.getInt("exam_id"));
        exam.setTitle(rs.getString("title"));
        exam.setDescription(rs.getString("description"));
        exam.setStartTime(rs.getTimestamp("start_time"));
        exam.setDurationMinutes(rs.getInt("duration_minutes"));
        exam.setInstructorId(rs.getInt("instructor_id"));
        exam.setPublished(rs.getBoolean("is_published"));
        exam.setCreatedAt(rs.getTimestamp("created_at"));
        return exam;
    }
}
