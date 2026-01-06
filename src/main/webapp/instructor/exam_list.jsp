<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib uri="http://java.sun.com/jsp/jstl/core"
prefix="c" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>My Exams</title>
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
      .status-badge {
        padding: 2px 6px;
        border-radius: 4px;
        font-size: 0.8em;
      }
      .published {
        background-color: #d4edda;
        color: #155724;
      }
      .draft {
        background-color: #fff3cd;
        color: #856404;
      }
    </style>
  </head>
  <body>
    <jsp:include page="../common/header.jsp" />
    <div style="display: flex; flex: 1">
      <jsp:include page="../common/sidebar.jsp" />
      <main>
        <h2>My Exams</h2>
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Title</th>
              <th>Start Time</th>
              <th>Status</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach var="exam" items="${exams}">
              <tr>
                <td>${exam.examId}</td>
                <td>${exam.title}</td>
                <td>${exam.startTime}</td>
                <td>
                  <c:choose>
                    <c:when test="${exam.published}">
                      <span class="status-badge published">PUBLISHED</span>
                    </c:when>
                    <c:otherwise>
                      <span class="status-badge draft">CREATED</span>
                    </c:otherwise>
                  </c:choose>
                </td>
                <td>
                  <a
                    href="controller?action=add_questions&exam_id=${exam.examId}"
                    class="action-link"
                    >Add Questions</a
                  >
                  <c:if test="${not exam.published}">
                    <a
                      href="controller?action=publish_exam&exam_id=${exam.examId}"
                      class="action-link"
                      onclick="return confirm('Publishing is final. Continue?')"
                      >Publish</a
                    >
                  </c:if>
                </td>
              </tr>
            </c:forEach>
            <c:if test="${empty exams}">
              <tr>
                <td colspan="5">No exams found.</td>
              </tr>
            </c:if>
          </tbody>
        </table>
      </main>
    </div>
    <jsp:include page="../common/footer.jsp" />
  </body>
</html>
