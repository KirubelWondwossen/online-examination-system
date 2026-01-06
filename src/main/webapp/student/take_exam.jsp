<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Take Exam</title>
<style>
    body { font-family: sans-serif; margin: 0; padding: 0; display: flex; flex-direction: column; min-height: 100vh; }
    main { padding: 2rem; flex: 1; max-width: 800px; margin: 0 auto; }
    .question-block { background: #fff; border: 1px solid #ddd; padding: 1.5rem; margin-bottom: 1.5rem; border-radius: 8px; }
    .question-title { font-weight: bold; margin-bottom: 1rem; }
    .options label { display: block; margin-bottom: 0.5rem; cursor: pointer; }
    button { padding: 1rem 2rem; background-color: #28a745; color: white; border: none; font-size: 1.1rem; cursor: pointer; border-radius: 4px; }
    .timer-box { background: #fff3cd; padding: 1rem; margin-bottom: 1rem; border: 1px solid #ffeeba; position: sticky; top: 0; z-index: 100; font-size: 1.2rem; font-weight: bold; text-align: center; }
</style>
<script>
    // Server-side End Time passed as milliseconds
    var endTime = Number("${not empty exam_end_time ? exam_end_time : 0}"); 

    function updateTimer() {
        var now = new Date().getTime();
        var distance = endTime - now;

        if (distance < 0) {
            document.getElementById("timer").innerHTML = "EXPIRED";
            document.getElementById("examForm").submit();
            return;
        }

        var hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
        var minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
        var seconds = Math.floor((distance % (1000 * 60)) / 1000);

        document.getElementById("timer").innerHTML = hours + "h " + minutes + "m " + seconds + "s ";
    }

    setInterval(updateTimer, 1000);
</script>
</head>
<body onload="updateTimer()">
    <jsp:include page="../common/header.jsp" />
    <main>
        <h2>Exam in Progress</h2>
        <div class="timer-box">
            Time Remaining: <span id="timer">Loading...</span>
        </div>

        <form id="examForm" action="controller" method="post">
            <input type="hidden" name="action" value="submit_exam">
            
            <c:forEach var="q" items="${sessionScope.current_exam_questions}" varStatus="loop">
                <div class="question-block">
                    <div class="question-title">Q${loop.index + 1}. ${q.content} <span style="font-weight:normal; font-size:0.9em; color:#666;">(${q.marks} marks)</span></div>
                    
                    <c:choose>
                        <c:when test="${q.questionType == 'MCQ'}">
                            <div class="options">
                                <label><input type="radio" name="q_${q.questionId}" value="A" required> A. ${q.optionA}</label>
                                <label><input type="radio" name="q_${q.questionId}" value="B"> B. ${q.optionB}</label>
                                <label><input type="radio" name="q_${q.questionId}" value="C"> C. ${q.optionC}</label>
                                <label><input type="radio" name="q_${q.questionId}" value="D"> D. ${q.optionD}</label>
                            </div>
                        </c:when>
                        <c:when test="${q.questionType == 'TRUE_FALSE'}">
                            <div class="options">
                                <label><input type="radio" name="q_${q.questionId}" value="TRUE" required> True</label>
                                <label><input type="radio" name="q_${q.questionId}" value="FALSE"> False</label>
                            </div>
                        </c:when>
                        <c:when test="${q.questionType == 'SHORT_ANSWER'}">
                            <textarea name="q_${q.questionId}" rows="3" style="width:100%; padding:0.5rem;" placeholder="Your answer..." required></textarea>
                        </c:when>
                    </c:choose>
                </div>
            </c:forEach>
            
            <button type="submit" onclick="return confirm('Are you sure you want to submit?')">Submit Exam</button>
        </form>
    </main>
    <jsp:include page="../common/footer.jsp" />
</body>
</html>
