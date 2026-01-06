package com.controller;

import com.dao.ExamDAO;
import com.dao.QuestionDAO;
import com.dao.StudentExamDAO;
import com.dao.UserDAO;
import com.model.Exam;
import com.model.Question;
import com.model.User;
import com.model.Answer;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import javax.servlet.ServletException;

public class ControllerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private UserDAO userDAO;
    private ExamDAO examDAO;
    private QuestionDAO questionDAO;
    private StudentExamDAO studentExamDAO;

    public ControllerServlet() {
        super();
        userDAO = new UserDAO();
        examDAO = new ExamDAO();
        questionDAO = new QuestionDAO();
        studentExamDAO = new StudentExamDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "home";
        }

        try {
            switch (action) {
                case "home":
                    showLogin(request, response);
                    break;
                case "login":
                    login(request, response);
                    break;
                case "logout":
                    logout(request, response);
                    break;
                case "dashboard":
                    showDashboard(request, response);
                    break;
                // Instructor Actions
                case "create_exam":
                    createExam(request, response);
                    break;
                case "view_exams":
                    listInstructorExams(request, response);
                    break;
                case "add_questions":
                    addQuestions(request, response);
                    break;
                case "publish_exam":
                    publishExam(request, response);
                    break;
                // Student Actions
                case "student_exam_list":
                    listStudentExams(request, response);
                    break;
                case "start_exam":
                    startExam(request, response);
                    break;
                case "submit_exam":
                    submitExam(request, response);
                    break;
                default:
                    showLogin(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred: " + e.getMessage());
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    private void showLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password"); // In real app, hash this
        
        User user = userDAO.authenticate(username, password);
        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            response.sendRedirect("controller?action=dashboard");
        } else {
            request.setAttribute("error", "Invalid credentials");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }

    private void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect("index.jsp");
    }

    private void showDashboard(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) { // Session check
            response.sendRedirect("index.jsp");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        String role = user.getRole();
        
        if ("ADMIN".equals(role)) {
            request.getRequestDispatcher("admin/dashboard.jsp").forward(request, response);
        } else if ("INSTRUCTOR".equals(role)) {
            request.getRequestDispatcher("instructor/dashboard.jsp").forward(request, response);
        } else if ("STUDENT".equals(role)) {
            request.getRequestDispatcher("student/dashboard.jsp").forward(request, response);
        }
    }

    // --- Instructor Methods ---
    private void createExam(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getMethod().equalsIgnoreCase("POST")) {
            User user = (User) request.getSession().getAttribute("user");
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String startTimeStr = request.getParameter("start_time"); // Format: yyyy-MM-dd'T'HH:mm
            String durationStr = request.getParameter("duration");

            // Basic parsing, simplified for brevity
            Timestamp startTime = Timestamp.valueOf(startTimeStr.replace("T", " ") + ":00");
            int duration = Integer.parseInt(durationStr);

            Exam exam = new Exam();
            exam.setTitle(title);
            exam.setDescription(description);
            exam.setStartTime(startTime);
            exam.setDurationMinutes(duration);
            exam.setInstructorId(user.getUserId());

            examDAO.createExam(exam);
            response.sendRedirect("controller?action=view_exams");
        } else {
            request.getRequestDispatcher("instructor/create_exam.jsp").forward(request, response);
        }
    }

    private void listInstructorExams(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        List<Exam> exams = examDAO.getExamsByInstructor(user.getUserId());
        request.setAttribute("exams", exams);
        request.getRequestDispatcher("instructor/exam_list.jsp").forward(request, response);
    }

    private void addQuestions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getMethod().equalsIgnoreCase("POST")) {
            int examId = Integer.parseInt(request.getParameter("exam_id"));
            String content = request.getParameter("content");
            String type = request.getParameter("type");
            String optA = request.getParameter("option_a");
            String optB = request.getParameter("option_b");
            String optC = request.getParameter("option_c");
            String optD = request.getParameter("option_d");
            String correct = request.getParameter("correct_answer");
            int marks = Integer.parseInt(request.getParameter("marks"));

            Question q = new Question();
            q.setExamId(examId);
            q.setContent(content);
            q.setQuestionType(type);
            q.setOptionA(optA);
            q.setOptionB(optB);
            q.setOptionC(optC);
            q.setOptionD(optD);
            q.setCorrectAnswer(correct);
            q.setMarks(marks);

            questionDAO.addQuestion(q);
            response.sendRedirect("controller?action=add_questions&exam_id=" + examId);
        } else {
            int examId = Integer.parseInt(request.getParameter("exam_id"));
            request.setAttribute("exam_id", examId);
            // Optionally fetch existing questions to show
            request.getRequestDispatcher("instructor/add_question.jsp").forward(request, response);
        }
    }
    
    private void publishExam(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int examId = Integer.parseInt(request.getParameter("exam_id"));
        examDAO.publishExam(examId);
        response.sendRedirect("controller?action=view_exams");
    }

    // --- Student Methods ---
    private void listStudentExams(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Exam> exams = examDAO.getAllPublishedExams();
        request.setAttribute("exams", exams);
        request.getRequestDispatcher("student/exam_list.jsp").forward(request, response);
    }

    private void startExam(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        int examId = Integer.parseInt(request.getParameter("exam_id"));

        if (studentExamDAO.hasTakenExam(user.getUserId(), examId)) {
            request.setAttribute("error", "You have already taken this exam.");
            listStudentExams(request, response);
            return;
        }

        int studentExamId = studentExamDAO.startExam(user.getUserId(), examId);
        List<Question> questions = questionDAO.getQuestionsByExamId(examId);
        
        // Randomize questions
        Collections.shuffle(questions);

        request.getSession().setAttribute("current_exam_questions", questions);
        request.getSession().setAttribute("current_student_exam_id", studentExamId);
        request.getSession().setAttribute("current_exam_id", examId);
        
        request.getRequestDispatcher("student/take_exam.jsp").forward(request, response);
    }

    private void submitExam(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int studentExamId = (Integer) request.getSession().getAttribute("current_student_exam_id");
        @SuppressWarnings("unchecked")
        List<Question> questions = (List<Question>) request.getSession().getAttribute("current_exam_questions");
        
        double totalScore = 0;

        for (Question q : questions) {
            String userAnswer = request.getParameter("q_" + q.getQuestionId());
            double points = 0;
            
            if (userAnswer != null) {
                // Auto-grading for MCQ and True/False
                if (!"SHORT_ANSWER".equals(q.getQuestionType())) {
                    if (userAnswer.equalsIgnoreCase(q.getCorrectAnswer())) {
                        points = q.getMarks();
                        totalScore += points;
                    }
                }
                
                // Save answer
                Answer ans = new Answer();
                ans.setStudentExamId(studentExamId);
                ans.setQuestionId(q.getQuestionId());
                ans.setGivenAnswer(userAnswer);
                ans.setPointsAwarded(points);
                studentExamDAO.saveAnswer(ans);
            }
        }

        studentExamDAO.submitExam(studentExamId, totalScore);
        
        // Cleanup session
        request.getSession().removeAttribute("current_exam_questions");
        request.getSession().removeAttribute("current_student_exam_id");
        request.getSession().removeAttribute("current_exam_id");

        response.sendRedirect("controller?action=dashboard");
    }
}
