package com.dao;

import com.model.Question;
import com.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionDAO {

    public boolean addQuestion(Question question) {
        String sql = "INSERT INTO questions (exam_id, content, question_type, option_a, option_b, option_c, option_d, correct_answer, marks) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, question.getExamId());
            pstmt.setString(2, question.getContent());
            pstmt.setString(3, question.getQuestionType());
            pstmt.setString(4, question.getOptionA());
            pstmt.setString(5, question.getOptionB());
            pstmt.setString(6, question.getOptionC());
            pstmt.setString(7, question.getOptionD());
            pstmt.setString(8, question.getCorrectAnswer());
            pstmt.setInt(9, question.getMarks());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Question> getQuestionsByExamId(int examId) {
        List<Question> questions = new ArrayList<>();
        String sql = "SELECT * FROM questions WHERE exam_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, examId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Question q = new Question();
                    q.setQuestionId(rs.getInt("question_id"));
                    q.setExamId(rs.getInt("exam_id"));
                    q.setContent(rs.getString("content"));
                    q.setQuestionType(rs.getString("question_type"));
                    q.setOptionA(rs.getString("option_a"));
                    q.setOptionB(rs.getString("option_b"));
                    q.setOptionC(rs.getString("option_c"));
                    q.setOptionD(rs.getString("option_d"));
                    q.setCorrectAnswer(rs.getString("correct_answer"));
                    q.setMarks(rs.getInt("marks"));
                    questions.add(q);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }
}
