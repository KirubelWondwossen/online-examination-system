<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Grade Exam</title>
<style>
    body { font-family: sans-serif; display: flex; flex-direction: column; min-height: 100vh; margin: 0; }
    main { flex: 1; padding: 2rem; }
    .question-block { border: 1px solid #ddd; padding: 1rem; margin-bottom: 1rem; border-radius: 5px; }
    .answer-block { background-color: #f9f9f9; padding: 0.5rem; margin-top: 0.5rem; border-left: 3px solid #007bff; }
    .score-input { width: 60px; padding: 5px; margin-left: 10px; }
    .btn { padding: 0.8rem 1.5rem; background-color: #28a745; color: white; border: none; font-size: 1rem; cursor: pointer; border-radius: 4px; }
    .auto-graded-badge { background: #e2e3e5; color: #383d41; padding: 2px 6px; font-size: 0.8em; border-radius: 4px; }
</style>
</head>
<body>
    <jsp:include page="../common/header.jsp" />
    <div style="display: flex; flex: 1;">
        <jsp:include page="../common/sidebar.jsp" />
        <main>
            <h2>Grading: ${student.fullName}</h2>
            <p><strong>Status:</strong> ${studentExam.status} | <strong>Current Score:</strong> ${studentExam.totalScore}</p>
            
            <form action="controller" method="post">
                <input type="hidden" name="action" value="update_score">
                <input type="hidden" name="student_exam_id" value="${studentExam.studentExamId}">
                
                <input type="hidden" name="student_exam_id" value="${studentExam.studentExamId}">


                <c:forEach var="ans" items="${answers}">
                    <c:set var="q" value="${questionMap[ans.questionId]}" />
                    <div class="question-block">
                        <div style="margin-bottom: 0.5rem;">
                            <strong>Q: ${q.content}</strong> 
                            <span style="float: right; color: #666;">Max Marks: ${q.marks}</span>
                        </div>
                        
                        <div class="answer-block">
                            <strong>Student Answer:</strong><br/>
                            <pre style="white-space: pre-wrap;">${ans.givenAnswer}</pre>
                        </div>
                        
                        <div style="margin-top: 1rem; display: flex; align-items: center;">
                            <label><strong>Score:</strong></label>
                            <input type="number" step="0.5" min="0" max="${q.marks}" 
                                   name="score_${ans.answerId}" 
                                   value="${ans.pointsAwarded}" 
                                   class="score-input">
                            
                            <c:if test="${q.questionType != 'SHORT_ANSWER'}">
                                <span class="auto-graded-badge" style="margin-left: 10px;">Auto-Graded (${q.questionType})</span>
                                <span style="margin-left: 10px; font-size: 0.9em;">Correct Answer: ${q.correctAnswer}</span>
                            </c:if>
                        </div>
                    </div>
                </c:forEach>
                
                <div style="margin-top: 2rem;">
                    <button type="submit" class="btn">Save Grades & Finalize</button>
                    <a href="controller?action=view_submissions&exam_id=${studentExam.examId}" style="margin-left: 1rem;">Cancel</a>
                </div>
            </form>
        </main>
    </div>
    <jsp:include page="../common/footer.jsp" />
</body>
</html>
