package com.controller;

import com.dao.ExamDAO;
import com.dao.QuestionDAO;
import com.dao.StudentExamDAO;
import com.dao.UserDAO;
import com.model.Exam;
import com.model.Question;
import com.model.StudentExam;
import com.model.User;
import com.model.Answer;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
                case "view_questions":
                    viewQuestions(request, response);
                    break;
                case "edit_question":
                    editQuestion(request, response);
                    break;
                case "update_question":
                    updateQuestion(request, response);
                    break;
                case "delete_question":
                    deleteQuestion(request, response);
                    break;
                case "view_submissions":
                    viewSubmissions(request, response);
                    break;
                case "grade_exam":
                    gradeExam(request, response);
                    break;
                case "update_score":
                    updateScore(request, response);
                    break;
                case "publish_results":
                    publishResults(request, response);
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
                case "view_results":
                    viewStudentResults(request, response);
                    break;
                // Admin Actions
                case "admin_users":
                    listUsers(request, response);
                    break;
                case "create_user":
                    createUser(request, response);
                    break;
                case "delete_user":
                    deleteUser(request, response);
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
    
    // Admin Permission Check Helper could be added, but for now we rely on role routing.

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

    private void viewQuestions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        int examId = Integer.parseInt(request.getParameter("exam_id"));
        
        Exam exam = examDAO.getExamById(examId);
        if (exam == null || exam.getInstructorId() != user.getUserId()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
            return;
        }

        List<Question> questions = questionDAO.getQuestionsByExamId(examId);
        request.setAttribute("exam", exam);
        request.setAttribute("questions", questions);
        request.getRequestDispatcher("instructor/view_questions.jsp").forward(request, response);
    }

    private void editQuestion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        int questionId = Integer.parseInt(request.getParameter("question_id"));
        
        Question question = questionDAO.getQuestionById(questionId);
        if (question == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Question not found");
            return;
        }

        Exam exam = examDAO.getExamById(question.getExamId());
        if (exam == null || exam.getInstructorId() != user.getUserId()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
            return;
        }

        request.setAttribute("question", question);
        request.getRequestDispatcher("instructor/edit_question.jsp").forward(request, response);
    }

    private void updateQuestion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        int questionId = Integer.parseInt(request.getParameter("question_id"));
        int examId = Integer.parseInt(request.getParameter("exam_id")); // Passed for security check & weak verify

        // Verify Ownership
        Exam exam = examDAO.getExamById(examId);
        if (exam == null || exam.getInstructorId() != user.getUserId()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
            return;
        }

        Question q = questionDAO.getQuestionById(questionId);
        if (q == null || q.getExamId() != examId) {
             response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Question/Exam mismatch");
             return;
        }

        q.setContent(request.getParameter("content"));
        q.setQuestionType(request.getParameter("type"));
        q.setOptionA(request.getParameter("option_a"));
        q.setOptionB(request.getParameter("option_b"));
        q.setOptionC(request.getParameter("option_c"));
        q.setOptionD(request.getParameter("option_d"));
        q.setCorrectAnswer(request.getParameter("correct_answer"));
        q.setMarks(Integer.parseInt(request.getParameter("marks")));

        questionDAO.updateQuestion(q);
        response.sendRedirect("controller?action=view_questions&exam_id=" + examId);
    }

    private void deleteQuestion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        int questionId = Integer.parseInt(request.getParameter("question_id"));
        int examId = Integer.parseInt(request.getParameter("exam_id"));

        Exam exam = examDAO.getExamById(examId);
        if (exam == null || exam.getInstructorId() != user.getUserId()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
            return;
        }
        
        // Ensure question belongs to this exam (extra safety)
        Question q = questionDAO.getQuestionById(questionId);
        if (q != null && q.getExamId() == examId) {
            questionDAO.deleteQuestion(questionId);
        }

        response.sendRedirect("controller?action=view_questions&exam_id=" + examId);
    }
    
    private void publishExam(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int examId = Integer.parseInt(request.getParameter("exam_id"));
        examDAO.publishExam(examId);
        response.sendRedirect("controller?action=view_exams");
    }

    private void publishResults(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int examId = Integer.parseInt(request.getParameter("exam_id"));
        examDAO.publishResults(examId);
        response.sendRedirect("controller?action=view_submissions&exam_id=" + examId);
    }

    private void viewSubmissions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        int examId = Integer.parseInt(request.getParameter("exam_id"));
        
        Exam exam = examDAO.getExamById(examId);
        if (exam == null || exam.getInstructorId() != user.getUserId()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
            return;
        }

        List<StudentExam> submissions = studentExamDAO.getSubmissionsByExamId(examId);
        
        // Map student IDs to User objects for display
        Map<Integer, User> studentMap = new HashMap<>();
        for (StudentExam se : submissions) {
            if (!studentMap.containsKey(se.getStudentId())) {
                studentMap.put(se.getStudentId(), userDAO.getUserById(se.getStudentId()));
            }
        }
        
        request.setAttribute("exam_id", examId);
        request.setAttribute("submissions", submissions);
        request.setAttribute("studentMap", studentMap);
        
        request.getRequestDispatcher("instructor/view_submissions.jsp").forward(request, response);
    }

    private void gradeExam(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int studentExamId = Integer.parseInt(request.getParameter("student_exam_id"));
        StudentExam studentExam = studentExamDAO.getStudentExam(studentExamId);
        List<Answer> answers = studentExamDAO.getAnswersByStudentExamId(studentExamId);
        List<Question> questions = questionDAO.getQuestionsByExamId(studentExam.getExamId());
        
        // Map Question ID to Question object
        Map<Integer, Question> questionMap = new HashMap<>();
        for (Question q : questions) {
            questionMap.put(q.getQuestionId(), q);
        }
        
        User student = userDAO.getUserById(studentExam.getStudentId());
        
        request.setAttribute("studentExam", studentExam);
        request.setAttribute("answers", answers);
        request.setAttribute("questionMap", questionMap);
        request.setAttribute("student", student);
        
        request.getRequestDispatcher("instructor/grade_exam.jsp").forward(request, response);
    }

    private void updateScore(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int studentExamId = Integer.parseInt(request.getParameter("student_exam_id"));
        
        Map<String, String[]> params = request.getParameterMap();
        for (String key : params.keySet()) {
            if (key.startsWith("score_")) {
                int answerId = Integer.parseInt(key.substring(6)); // score_123 -> 123
                double score = Double.parseDouble(params.get(key)[0]);
                studentExamDAO.updateAnswerScore(answerId, score);
            }
        }
        
        studentExamDAO.updateTotalScore(studentExamId);
        
        StudentExam se = studentExamDAO.getStudentExam(studentExamId);
        response.sendRedirect("controller?action=view_submissions&exam_id=" + se.getExamId());
    }

    // --- Student Methods ---
    private void listStudentExams(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        List<Exam> exams = examDAO.getAllPublishedExams(); // Already filters by is_published=TRUE
        java.util.Set<Integer> takenExamIds = studentExamDAO.getTakenExamIds(user.getUserId());
        
        request.setAttribute("exams", exams);
        request.setAttribute("takenExamIds", takenExamIds);
        request.getRequestDispatcher("student/exam_list.jsp").forward(request, response);
    }

    private void startExam(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        int examId = Integer.parseInt(request.getParameter("exam_id"));

        Exam exam = examDAO.getExamById(examId);
        
        // Start Date Enforcement
        if (exam.getStartTime().after(new Timestamp(System.currentTimeMillis()))) {
             request.setAttribute("error", "This exam has not started yet. Start time: " + exam.getStartTime());
             listStudentExams(request, response);
             return;
        }

        if (studentExamDAO.hasTakenExam(user.getUserId(), examId)) {
            // Anti-Retake Policy Enforcement
            request.setAttribute("error", "<strong>Policy Violation:</strong> You have already taken this exam. Retakes are not permitted.");
            listStudentExams(request, response);
            return;
        }

        int studentExamId = studentExamDAO.startExam(user.getUserId(), examId);
        
        // Randomization Logic
        List<Question> allQuestions = questionDAO.getQuestionsByExamId(examId);
        Collections.shuffle(allQuestions);
        
        // Fix: Use all questions as there is no total_questions column
        List<Question> selectedQuestions = allQuestions; 
        List<Integer> selectedQuestionIds = new java.util.ArrayList<>();
        for(Question q : selectedQuestions) {
            selectedQuestionIds.add(q.getQuestionId());
        }
        
        // Persist Selection
        studentExamDAO.saveExamQuestions(studentExamId, selectedQuestionIds);

        // Calculate Exam End Time for Timer
        StudentExam se = studentExamDAO.getStudentExam(studentExamId);
        long endTimeMillis = se.getStartTime().getTime() + (exam.getDurationMinutes() * 60 * 1000);
        
        request.getSession().setAttribute("current_exam_questions", selectedQuestions);
        request.getSession().setAttribute("current_student_exam_id", studentExamId);
        request.getSession().setAttribute("current_exam_id", examId);
        request.setAttribute("exam_end_time", endTimeMillis);
        
        request.getRequestDispatcher("student/take_exam.jsp").forward(request, response);
    }
    
    private void viewStudentResults(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        List<StudentExam> results = studentExamDAO.getResultsByStudentId(user.getUserId());
        

        
        request.setAttribute("results", results);
        request.getRequestDispatcher("student/results.jsp").forward(request, response);
    }

    private void submitExam(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int studentExamId = (Integer) request.getSession().getAttribute("current_student_exam_id");
        @SuppressWarnings("unchecked")
        List<Question> questions = (List<Question>) request.getSession().getAttribute("current_exam_questions");
        
        double totalScore = 0;
        
        // Time Validation
        StudentExam se = studentExamDAO.getStudentExam(studentExamId);
        Exam exam = examDAO.getExamById(se.getExamId());
        long allowedEndTime = se.getStartTime().getTime() + (exam.getDurationMinutes() * 60 * 1000) + (2 * 60 * 1000); // 2 min buffer
        
        if (System.currentTimeMillis() > allowedEndTime) {
             // STRICT FIX: Mark as SUBMITTED but don't grade (or grade 0). 
             // Requirement: "Reject submission if current time > exam end time". 
             // We'll mark it submitted with 0 score for now or just ignore answers.
             // Actually, to respect "Reject submission", we can show error page, 
             // BUT we must update status to SUBMITTED to close the loop so they can't try again.
             studentExamDAO.submitExam(studentExamId, 0.0); // Close the exam
             response.sendError(HttpServletResponse.SC_FORBIDDEN, "Submission Rejected: Time Limit Exceeded");
             return;
        }

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

    // --- Admin Methods ---
    private void listUsers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<User> users = userDAO.getAllUsers();
        request.setAttribute("users", users);
        request.getRequestDispatcher("admin/dashboard.jsp").forward(request, response);
    }

    private void createUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getMethod().equalsIgnoreCase("POST")) {
            User u = new User();
            u.setUsername(request.getParameter("username"));
            u.setPassword(request.getParameter("password")); // Plain text for simplicity per constraints
            u.setFullName(request.getParameter("full_name"));
            u.setRole(request.getParameter("role"));
            
            if (userDAO.isUsernameTaken(u.getUsername())) {
                request.setAttribute("error", "Username taken");
                request.getRequestDispatcher("admin/create_user.jsp").forward(request, response);
                return;
            }
            
            userDAO.register(u);
            response.sendRedirect("controller?action=admin_users");
        } else {
            request.getRequestDispatcher("admin/create_user.jsp").forward(request, response);
        }
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter("user_id"));
        userDAO.deleteUser(userId);
        response.sendRedirect("controller?action=admin_users");
    }
}
