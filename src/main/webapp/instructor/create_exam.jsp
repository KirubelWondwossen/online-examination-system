<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>Create Exam</title>
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
      .form-group {
        margin-bottom: 1rem;
      }
      label {
        display: block;
        margin-bottom: 0.5rem;
        font-weight: bold;
      }
      input[type="text"],
      textarea,
      input[type="datetime-local"],
      input[type="number"] {
        width: 100%;
        padding: 0.5rem;
        box-sizing: border-box;
      }
      button {
        padding: 0.75rem 1.5rem;
        background-color: #007bff;
        color: white;
        border: none;
        cursor: pointer;
      }
    </style>
  </head>
  <body>
    <jsp:include page="../common/header.jsp" />
    <div style="display: flex; flex: 1">
      <jsp:include page="../common/sidebar.jsp" />
      <main>
        <h2>Create New Exam</h2>
        <form action="controller" method="post">
          <input type="hidden" name="action" value="create_exam" />

          <div class="form-group">
            <label>Exam Title</label>
            <input type="text" name="title" required />
          </div>

          <div class="form-group">
            <label>Description</label>
            <textarea name="description" rows="3"></textarea>
          </div>

          <div class="form-group">
            <label>Start Time</label>
            <input type="datetime-local" name="start_time" required />
          </div>

          <div class="form-group">
            <label>Duration (Minutes)</label>
            <input type="number" name="duration" min="1" value="60" required />
          </div>

          <button type="submit">Create Exam</button>
        </form>
      </main>
    </div>
    <jsp:include page="../common/footer.jsp" />
  </body>
</html>
