<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>Create User</title>
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
        max-width: 500px;
        margin: 0 auto;
      }
      .form-group {
        margin-bottom: 1rem;
      }
      label {
        display: block;
        margin-bottom: 0.5rem;
      }
      input,
      select {
        width: 100%;
        padding: 0.5rem;
        box-sizing: border-box;
      }
      .btn {
        padding: 0.8rem 1.5rem;
        background-color: #007bff;
        color: white;
        border: none;
        cursor: pointer;
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
    <main>
      <h2>Create New User</h2>
      <c:if test="${not empty error}">
        <p class="error">${error}</p>
      </c:if>

      <form action="controller" method="post">
        <input type="hidden" name="action" value="create_user" />

        <div class="form-group">
          <label>Username:</label>
          <input type="text" name="username" required />
        </div>

        <div class="form-group">
          <label>Password:</label>
          <input type="password" name="password" required />
        </div>

        <div class="form-group">
          <label>Full Name:</label>
          <input type="text" name="full_name" required />
        </div>

        <div class="form-group">
          <label>Role:</label>
          <select name="role">
            <option value="STUDENT">Student</option>
            <option value="INSTRUCTOR">Instructor</option>
            <option value="ADMIN">Admin</option>
          </select>
        </div>

        <button type="submit" class="btn">Create User</button>
        <a href="controller?action=admin_users" style="margin-left: 1rem"
          >Cancel</a
        >
      </form>
    </main>
    <jsp:include page="../common/footer.jsp" />
  </body>
</html>
