<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<html>
<head>
<title>Hello World</title>
</head>
<body>
<f:view>
	<f:verbatim>
		<h2>
	</f:verbatim>
	<h:outputText value="Congrats #{sessionScope.SPRING_SECURITY_LAST_USERNAME}" />
	
	<f:verbatim>
		</h2>
	</f:verbatim>

	<h:form id="form2">
		<h:panelGrid columns="1">
			<h:commandLink id="link1" action="back">
				<h:outputText id="linkText" value="Home" />
			</h:commandLink>

			<h:commandLink id="link2" action="logout">
				<h:outputText id="logout" value="Logout" />
			</h:commandLink>
		</h:panelGrid>

	</h:form>
</f:view>
</body>
</html>
