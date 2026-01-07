<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>Student Dashboard</title>
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
      .card {
        border: 1px solid #ddd;
        padding: 1.5rem;
        border-radius: 8px;
        margin-bottom: 1rem;
      }
      .btn {
        display: inline-block;
        padding: 0.5rem 1rem;
        background-color: #007bff;
        color: white;
        text-decoration: none;
        border-radius: 4px;
      }
    </style>
  </head>
  <body>
    <jsp:include page="../common/header.jsp" />
    <div style="display: flex; flex: 1">
      <jsp:include page="../common/sidebar.jsp" />
      <main>
        <h2>Student Dashboard</h2>
        <div class="card">
          <h3>Available Exams</h3>
          <p>View and take published exams.</p>
          <a href="controller?action=student_exam_list" class="btn"
            >View Exams</a
          >
        </div>
        <div class="card">
          <h3>My Results</h3>
          <p>View your exam history and grades.</p>
          <!-- Reuse the exam list or a specific results action -->
          <a
            href="controller?action=view_results"
            class="btn"
            style="background-color: #6c757d"
            >View History</a
          >
        </div>
      </main>
    </div>
    <jsp:include page="../common/footer.jsp" />
  </body>
</html>
