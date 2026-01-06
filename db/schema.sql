-- Database Schema for Academic Online Examination System
-- Database: online_exam_system

DROP DATABASE IF EXISTS online_exam_system;
CREATE DATABASE online_exam_system;
USE online_exam_system;

-- 1. Users Table
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL, -- Plain text for now as per constraints, ideally hashed
    full_name VARCHAR(100) NOT NULL,
    role ENUM('ADMIN', 'INSTRUCTOR', 'STUDENT') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Exams Table
CREATE TABLE exams (
    exam_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    start_time DATETIME NOT NULL,
    duration_minutes INT NOT NULL,
    instructor_id INT NOT NULL,
    is_published BOOLEAN DEFAULT FALSE,
    is_result_published BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (instructor_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 3. Questions Table
CREATE TABLE questions (
    question_id INT AUTO_INCREMENT PRIMARY KEY,
    exam_id INT NOT NULL,
    content TEXT NOT NULL,
    question_type ENUM('MCQ', 'TRUE_FALSE', 'SHORT_ANSWER') NOT NULL,
    option_a VARCHAR(255),
    option_b VARCHAR(255),
    option_c VARCHAR(255),
    option_d VARCHAR(255),
    correct_answer TEXT NOT NULL, -- For MCQ: 'A', 'B', etc. For T/F: 'TRUE'. For Short: Keyword/Text
    marks INT NOT NULL DEFAULT 1,
    FOREIGN KEY (exam_id) REFERENCES exams(exam_id) ON DELETE CASCADE
);

-- 4. Student Exams (Attempts)
CREATE TABLE student_exams (
    student_exam_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    exam_id INT NOT NULL,
    start_time DATETIME,
    end_time DATETIME,
    total_score DECIMAL(5, 2) DEFAULT 0.0,
    status ENUM('IN_PROGRESS', 'SUBMITTED', 'GRADED') DEFAULT 'IN_PROGRESS',
    FOREIGN KEY (student_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (exam_id) REFERENCES exams(exam_id) ON DELETE CASCADE
);

-- 5. Student Answers
CREATE TABLE student_answers (
    answer_id INT AUTO_INCREMENT PRIMARY KEY,
    student_exam_id INT NOT NULL,
    question_id INT NOT NULL,
    given_answer TEXT,
    points_awarded DECIMAL(5, 2) DEFAULT 0.0,
    FOREIGN KEY (student_exam_id) REFERENCES student_exams(student_exam_id) ON DELETE CASCADE,
    FOREIGN KEY (question_id) REFERENCES questions(question_id) ON DELETE CASCADE
);

-- Initial Data Seeding
INSERT INTO users (username, password, full_name, role) VALUES 
('admin', 'admin123', 'System Admin', 'ADMIN'),
('inst1', 'inst123', 'John Doe', 'INSTRUCTOR'),
('stud1', 'stud123', 'Jane Smith', 'STUDENT');
