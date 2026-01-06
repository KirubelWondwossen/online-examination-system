<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib uri="http://java.sun.com/jsp/jstl/core"
prefix="c" %>
<link
  rel="stylesheet"
  href="${pageContext.request.contextPath}/css/style.css"
/>
<header
  style="
    background: #333;
    color: white;
    padding: 1rem 2rem;
    display: flex;
    justify-content: space-between;
    align-items: center;
  "
>
  <h3 style="margin: 0">Online Examination System</h3>
  <div>
    <c:if test="${not empty sessionScope.user}">
      <span>Welcome, ${sessionScope.user.username}</span>
      <a
        href="controller?action=logout"
        style="color: #ccc; margin-left: 1rem; text-decoration: none"
        >Logout</a
      >
    </c:if>
  </div>
</header>
<hr style="margin: 0" />
