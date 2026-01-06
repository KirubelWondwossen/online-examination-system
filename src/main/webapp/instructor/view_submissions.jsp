<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib uri="http://java.sun.com/jsp/jstl/core"
prefix="c" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>View Submissions</title>
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
      .btn {
        padding: 0.5rem 1rem;
        background-color: #007bff;
        color: white;
        text-decoration: none;
        border-radius: 4px;
      }
      .btn-secondary {
        background-color: #6c757d;
      }
    </style>
  </head>
  <body>
    <jsp:include page="../common/header.jsp" />
    <div style="display: flex; flex: 1">
      <jsp:include page="../common/sidebar.jsp" />
      <main>
        <h2>Student Submissions</h2>
        <a
          href="controller?action=view_exams"
          class="btn btn-secondary"
          style="margin-bottom: 1rem; display: inline-block"
          >&larr; Back to Exams</a
        >
        <a
          href="controller?action=publish_results&exam_id=${exam_id}"
          class="btn"
          style="background-color: #ffc107; color: black; margin-left: 10px"
          onclick="return confirm('Are you sure? Verify all grading is complete first.')"
        >
          Publish Results to Students
        </a>

        <table>
          <thead>
            <tr>
              <th>Student Name</th>
              <th>Status</th>
              <th>Submitted At</th>
              <th>Total Score</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach var="sub" items="${submissions}">
              <tr>
                <td>
                  ${studentMap[sub.studentId].fullName}
                  (${studentMap[sub.studentId].username})
                </td>
                <td>${sub.status}</td>
                <td>${sub.endTime != null ? sub.endTime : 'In Progress'}</td>
                <td>${sub.totalScore}</td>
                <td>
                  <c:if test="${sub.endTime != null}">
                    <a
                      href="controller?action=grade_exam&student_exam_id=${sub.studentExamId}"
                      class="btn"
                      >Grade / View</a
                    >
                  </c:if>
                  <c:if test="${sub.endTime == null}">
                    <span style="color: grey; font-style: italic"
                      >In Progress</span
                    >
                  </c:if>
                </td>
              </tr>
            </c:forEach>
            <c:if test="${empty submissions}">
              <tr>
                <td colspan="5">No submissions found.</td>
              </tr>
            </c:if>
          </tbody>
        </table>
      </main>
    </div>
    <jsp:include page="../common/footer.jsp" />
  </body>
</html>
