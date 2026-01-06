<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib uri="http://java.sun.com/jsp/jstl/core"
prefix="c" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>Login - Online Examination System</title>
    <style>
      body {
        font-family: sans-serif;
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh;
        background-color: #f0f2f5;
      }
      .login-container {
        background: white;
        padding: 2rem;
        border-radius: 8px;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        width: 300px;
      }
      .form-group {
        margin-bottom: 1rem;
      }
      label {
        display: block;
        margin-bottom: 0.5rem;
      }
      input {
        width: 100%;
        padding: 0.5rem;
        border: 1px solid #ccc;
        border-radius: 4px;
        box-sizing: border-box;
      }
      button {
        width: 100%;
        padding: 0.75rem;
        background-color: #007bff;
        color: white;
        border: none;
        border-radius: 4px;
        cursor: pointer;
      }
      button:hover {
        background-color: #0056b3;
      }
      .error {
        color: red;
        margin-bottom: 1rem;
        font-size: 0.9rem;
      }
    </style>
  </head>
  <body>
    <div class="login-container">
      <h2 style="text-align: center">Login</h2>
      <c:if test="${not empty error}">
        <div class="error">${error}</div>
      </c:if>
      <form action="controller" method="post">
        <input type="hidden" name="action" value="login" />
        <div class="form-group">
          <label>Username</label>
          <input type="text" name="username" required />
        </div>
        <div class="form-group">
          <label>Password</label>
          <input type="password" name="password" required />
        </div>
        <button type="submit">Sign In</button>
      </form>
      <p style="text-align: center; font-size: 0.8rem; color: #666">
        Default Users:<br />
        admin / admin123<br />
        inst1 / inst123<br />
        stud1 / stud123
      </p>
    </div>
  </body>
</html>
