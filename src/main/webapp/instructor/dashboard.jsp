<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>Instructor Dashboard</title>
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
        background-color: #28a745;
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
        <h2>Instructor Dashboard</h2>
        <div class="card">
          <h3>Exam Management</h3>
          <p>Create and manage your exams.</p>
          <a href="controller?action=create_exam" class="btn"
            >Create New Exam</a
          >
          <a
            href="controller?action=view_exams"
            class="btn"
            style="background-color: #17a2b8"
            >View My Exams</a
          >
        </div>
      </main>
    </div>
    <jsp:include page="../common/footer.jsp" />
  </body>
</html>
