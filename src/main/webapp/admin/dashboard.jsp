<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib uri="http://java.sun.com/jsp/jstl/core"
prefix="c" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>Admin Dashboard</title>
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
      .btn {
        padding: 0.5rem 1rem;
        background-color: #007bff;
        color: white;
        text-decoration: none;
        border-radius: 4px;
      }
      .btn-danger {
        background-color: #dc3545;
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
    </style>
  </head>
  <body>
    <jsp:include page="../common/header.jsp" />
    <div style="display: flex; flex: 1">
      <jsp:include page="../common/sidebar.jsp" />
      <main>
        <h2>Admin Dashboard</h2>
        <p
          style="
            background-color: #f8d7da;
            color: #721c24;
            padding: 10px;
            border-radius: 4px;
            border: 1px solid #f5c6cb;
          "
        >
          <strong>Role Notice:</strong> The Admin role is strictly limited to
          <strong>User Management</strong>. Admins DO NOT have access to Exam
          Content, Grading, or Results to preserve academic integrity.
        </p>
        <p>Welcome, Admin!</p>

        <div style="margin-bottom: 1rem">
          <a href="controller?action=create_user" class="btn"
            >Create New User</a
          >
          <a
            href="controller?action=admin_users"
            class="btn"
            style="background-color: #28a745"
            >Manage Users</a
          >
        </div>

        <c:if test="${not empty users}">
          <h3>All Users</h3>
          <table>
            <thead>
              <tr>
                <th>ID</th>
                <th>Username</th>
                <th>Full Name</th>
                <th>Role</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="u" items="${users}">
                <tr>
                  <td>${u.userId}</td>
                  <td>${u.username}</td>
                  <td>${u.fullName}</td>
                  <td>${u.role}</td>
                  <td>
                    <c:if test="${u.role != 'ADMIN'}">
                      <a
                        href="controller?action=delete_user&user_id=${u.userId}"
                        class="btn btn-danger"
                        onclick="return confirm('Are you sure?')"
                        >Delete</a
                      >
                    </c:if>
                  </td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </c:if>
      </main>
    </div>
    <jsp:include page="../common/footer.jsp" />
  </body>
</html>
