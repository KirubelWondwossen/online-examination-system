<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib uri="http://java.sun.com/jsp/jstl/core"
prefix="c" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>View Questions - Exam #${exam.examId}</title>
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
      .action-link {
        margin-right: 10px;
        text-decoration: none;
        color: #007bff;
      }
      .action-link.delete {
        color: #dc3545;
      }
      .btn {
        padding: 0.5rem 1rem;
        background-color: #28a745;
        color: white;
        text-decoration: none;
        border-radius: 4px;
        display: inline-block;
        margin-bottom: 1rem;
      }
    </style>
  </head>
  <body>
    <jsp:include page="../common/header.jsp" />
    <div style="display: flex; flex: 1">
      <jsp:include page="../common/sidebar.jsp" />
      <main>
        <h2>Questions for Exam: ${exam.title}</h2>
        <a
          href="controller?action=add_questions&exam_id=${exam.examId}"
          class="btn"
          >+ Add New Question</a
        >
        <a
          href="controller?action=view_exams"
          class="btn"
          style="background-color: #6c757d; margin-left: 10px"
          >Back to Exams</a
        >

        <table>
          <thead>
            <tr>
              <th style="width: 5%">ID</th>
              <th style="width: 50%">Content</th>
              <th style="width: 15%">Type</th>
              <th style="width: 10%">Marks</th>
              <th style="width: 20%">Actions</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach var="q" items="${questions}">
              <tr>
                <td>${q.questionId}</td>
                <td>
                  <c:choose>
                    <c:when test="${q.content.length() > 60}">
                      ${q.content.substring(0, 60)}...
                    </c:when>
                    <c:otherwise> ${q.content} </c:otherwise>
                  </c:choose>
                </td>
                <td>${q.questionType}</td>
                <td>${q.marks}</td>
                <td>
                  <a
                    href="controller?action=edit_question&question_id=${q.questionId}"
                    class="action-link"
                    >Edit</a
                  >
                  <a
                    href="controller?action=delete_question&question_id=${q.questionId}&exam_id=${exam.examId}"
                    class="action-link delete"
                    onclick="return confirm('Are you sure you want to delete this question? This cannot be undone.')"
                    >Delete</a
                  >
                </td>
              </tr>
            </c:forEach>
            <c:if test="${empty questions}">
              <tr>
                <td colspan="5">No questions found for this exam.</td>
              </tr>
            </c:if>
          </tbody>
        </table>
      </main>
    </div>
    <jsp:include page="../common/footer.jsp" />
  </body>
</html>
