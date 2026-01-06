<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib uri="http://java.sun.com/jsp/jstl/core"
prefix="c" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>My Results</title>
    <style>
      body {
        font-family: sans-serif;
        display: flex;
        flex-direction: column;
        min-height: 100vh;
        margin: 0;
      }
      main {
        flex: 1;
        padding: 2rem;
      }
      table {
        width: 100%;
        border-collapse: collapse;
        margin-top: 1rem;
      }
      th,
      td {
        border: 1px solid #ddd;
        padding: 0.8rem;
        text-align: left;
      }
      th {
        background-color: #f4f4f4;
      }
      .status-graded {
        color: green;
        font-weight: bold;
      }
      .status-submitted {
        color: orange;
        font-weight: bold;
      }
      .status-progress {
        color: grey;
        font-style: italic;
      }
      .btn-secondary {
        padding: 0.5rem 1rem;
        background-color: #6c757d;
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
        <h2>My Exam History</h2>
        <a href="controller?action=dashboard" class="btn-secondary"
          >&larr; Back to Dashboard</a
        >

        <table>
          <thead>
            <tr>
              <th>Exam Title</th>
              <th>Date Taken</th>
              <th>Score</th>
              <th>Status</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach var="h" items="${history}">
              <tr>
                <td>${examMap[h.examId].title}</td>
                <td>${h.startTime}</td>
                <td>
                  <c:choose>
                    <c:when test="${h.status == 'GRADED'}"
                      >${h.totalScore}</c:when
                    >
                    <c:otherwise>--</c:otherwise>
                  </c:choose>
                </td>
                <td>
                  <c:choose>
                    <c:when test="${examMap[h.examId].resultPublished}">
                      <c:choose>
                        <c:when test="${h.status == 'GRADED'}"
                          >${h.totalScore}</c:when
                        >
                        <c:otherwise>--</c:otherwise>
                      </c:choose>
                    </c:when>
                    <c:otherwise>
                      <span style="color: grey; font-style: italic"
                        >Pending Publication</span
                      >
                    </c:otherwise>
                  </c:choose>
                </td>
                <td>
                  <c:choose>
                    <c:when test="${not examMap[h.examId].resultPublished}">
                      <span style="color: #6c757d; font-weight: bold"
                        >Completed - Awaiting Publication</span
                      >
                    </c:when>
                    <c:when test="${h.status == 'GRADED'}"
                      ><span class="status-graded">Graded</span></c:when
                    >
                    <c:when test="${h.status == 'SUBMITTED'}"
                      ><span class="status-submitted"
                        >Pending Grading</span
                      ></c:when
                    >
                    <c:otherwise
                      ><span class="status-progress"
                        >In Progress</span
                      ></c:otherwise
                    >
                  </c:choose>
                </td>
              </tr>
            </c:forEach>
            <c:if test="${empty history}">
              <tr>
                <td colspan="4">No exam history found.</td>
              </tr>
            </c:if>
          </tbody>
        </table>
      </main>
    </div>
    <jsp:include page="../common/footer.jsp" />
  </body>
</html>
