package com.dao;

import com.model.StudentExam;
import com.model.Answer;
import com.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public java.util.Set<Integer> getTakenExamIds(int studentId) {
        java.util.Set<Integer> taken = new java.util.HashSet<>();
        String sql = "SELECT exam_id FROM student_exams WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    taken.add(rs.getInt("exam_id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return taken;
    }

    public void saveAnswer(Answer answer) {
        // STRICT FIX: UPDATE existing placeholder from startExam, do not INSERT duplicates
        String sql = "UPDATE student_answers SET given_answer = ?, points_awarded = ? WHERE student_exam_id = ? AND question_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, answer.getGivenAnswer());
            pstmt.setDouble(2, answer.getPointsAwarded());
            pstmt.setInt(3, answer.getStudentExamId());
            pstmt.setInt(4, answer.getQuestionId());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<StudentExam> getSubmissionsByExamId(int examId) {
        List<StudentExam> submissions = new ArrayList<>();
        // STRICT FIX: Filter ONLY 'SUBMITTED' or 'GRADED'. Exclude IN_PROGRESS.
        String sql = "SELECT * FROM student_exams WHERE exam_id = ? AND status IN ('SUBMITTED', 'GRADED') ORDER BY end_time DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, examId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    StudentExam se = new StudentExam();
                    se.setStudentExamId(rs.getInt("student_exam_id"));
                    se.setStudentId(rs.getInt("student_id"));
                    se.setExamId(rs.getInt("exam_id"));
                    se.setStartTime(rs.getTimestamp("start_time"));
                    se.setEndTime(rs.getTimestamp("end_time"));
                    se.setTotalScore(rs.getDouble("total_score"));
                    se.setStatus(rs.getString("status"));
                    submissions.add(se);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return submissions;
    }

    public List<Answer> getAnswersByStudentExamId(int studentExamId) {
        List<Answer> answers = new ArrayList<>();
        String sql = "SELECT * FROM student_answers WHERE student_exam_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, studentExamId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Answer ans = new Answer();
                    ans.setAnswerId(rs.getInt("answer_id"));
                    ans.setStudentExamId(rs.getInt("student_exam_id"));
                    ans.setQuestionId(rs.getInt("question_id"));
                    ans.setGivenAnswer(rs.getString("given_answer"));
                    ans.setPointsAwarded(rs.getDouble("points_awarded"));
                    answers.add(ans);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return answers;
    }

    public void updateAnswerScore(int answerId, double points) {
        String sql = "UPDATE student_answers SET points_awarded = ? WHERE answer_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDouble(1, points);
            pstmt.setInt(2, answerId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTotalScore(int studentExamId) {
        String sql = "UPDATE student_exams SET total_score = (SELECT SUM(points_awarded) FROM student_answers WHERE student_exam_id = ?), status = 'GRADED' WHERE student_exam_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, studentExamId);
            pstmt.setInt(2, studentExamId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<StudentExam> getStudentHistory(int studentId) {
        List<StudentExam> history = new ArrayList<>();
        String sql = "SELECT * FROM student_exams WHERE student_id = ? ORDER BY start_time DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, studentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    StudentExam se = new StudentExam();
                    se.setStudentExamId(rs.getInt("student_exam_id"));
                    se.setStudentId(rs.getInt("student_id"));
                    se.setExamId(rs.getInt("exam_id"));
                    se.setStartTime(rs.getTimestamp("start_time"));
                    se.setEndTime(rs.getTimestamp("end_time"));
                    se.setTotalScore(rs.getDouble("total_score"));
                    se.setStatus(rs.getString("status"));
                    history.add(se);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return history;
    }
    public void saveExamQuestions(int studentExamId, List<Integer> questionIds) {
        String sql = "INSERT INTO student_answers (student_exam_id, question_id, given_answer, points_awarded) VALUES (?, ?, NULL, 0)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            for (int qId : questionIds) {
                pstmt.setInt(1, studentExamId);
                pstmt.setInt(2, qId);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Integer> getQuestionsForStudentExam(int studentExamId) {
        List<Integer> questionIds = new ArrayList<>();
        String sql = "SELECT question_id FROM student_answers WHERE student_exam_id = ? ORDER BY answer_id ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, studentExamId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    questionIds.add(rs.getInt("question_id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questionIds;
    }
}
