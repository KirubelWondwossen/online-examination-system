<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib uri="http://java.sun.com/jsp/jstl/core"
prefix="c" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>Available Exams</title>
    <style>
      body {
        font-family: sans-serif;
        margin: 0;
        padding: 0;
        display: flex;
        flex-direction: column;
        min-height: 100vh;
      }
      main {
        padding: 2rem;
        flex: 1;
      }
      table {
        width: 100%;
        border-collapse: collapse;
        margin-top: 1rem;
      }
      th,
      td {
        padding: 0.75rem;
        text-align: left;
        border-bottom: 1px solid #ddd;
      }
      th {
        background-color: #f8f9fa;
      }
      .btn {
        display: inline-block;
        padding: 5px 10px;
        background-color: #28a745;
        color: white;
        text-decoration: none;
        border-radius: 4px;
      }
      .error {
        color: red;
        margin-bottom: 1rem;
      }
    </style>
  </head>
  <body>
    <jsp:include page="../common/header.jsp" />
    <div style="display: flex; flex: 1">
      <jsp:include page="../common/sidebar.jsp" />
      <main>
        <h2>Available Exams</h2>
        <c:if test="${not empty error}">
          <div class="error">${error}</div>
        </c:if>
        <table>
          <thead>
            <tr>
              <th>Assigned ID</th>
              <th>Title</th>
              <th>Duration</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach var="exam" items="${exams}">
              <tr>
                <td>${exam.examId}</td>
                <td>${exam.title}</td>
                <td>${exam.durationMinutes} mins</td>
                <td>
                  <a
                    href="controller?action=start_exam&exam_id=${exam.examId}"
                    class="btn"
                    >Take Exam</a
                  >
                </td>
              </tr>
            </c:forEach>
            <c:if test="${empty exams}">
              <tr>
                <td colspan="4">No published exams available.</td>
              </tr>
            </c:if>
          </tbody>
        </table>
      </main>
    </div>
    <jsp:include page="../common/footer.jsp" />
  </body>
</html>
