<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<jsp:directive.page import="com.arcmind.jpa.course.UserService"/>
<jsp:directive.page import="javax.naming.InitialContext"/>
<jsp:directive.page import="java.util.List"/>
<jsp:directive.page import="com.arcmind.jpa.course.User"/>
<jsp:directive.page import="java.util.Iterator"/>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%
InitialContext ctx = new InitialContext();
		
UserService userService = (UserService) ctx.lookup("UserServiceImpl/local");
List users = userService.list();

%>

<%
Iterator iter = users.iterator();
while (iter.hasNext()) {
	User user = (User) iter.next();
%>
	<%= user.getName() %>
<%
}
%>
</body>
</html>
<%!
UserService userService;



public static InitialContext getInitialContext() throws Exception {
	java.util.Hashtable props = getInitialContextProperties();
	return new InitialContext(props);
}

public static java.util.Hashtable getInitialContextProperties() {
	java.util.Hashtable props = new java.util.Hashtable();
	props.put("java.naming.factory.initial",
                     "org.jnp.interfaces.NamingContextFactory");
      props.put("java.naming.provider.url", "jnp://localhost:1099");
      props.put("java.naming.factory.url.pkgs",
                     "org.jboss.naming:org.jnp.interfaces");
	return props;
}
%>