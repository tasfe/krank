<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>

<html>
<body>
<h1>ACCESS DENIED</h1>
 <c:if test="${not empty param.login_error}">
      <font color="red">
        Your login attempt was not successful, try again.<br/><br/>
        Reason: <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>.
      </font>
    </c:if>
</body>
</html>