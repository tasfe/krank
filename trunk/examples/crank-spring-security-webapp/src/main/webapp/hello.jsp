<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<html>
<head>
<title>Hello World</title>
</head>
<body>
<f:view>
	<h:form id="form">
		<h:panelGrid id="grid" columns="1">
			<h:outputText value="HELLO #{sessionScope.SECURITY_LAST_USERNAME}" /> 
			<h:outputText id="output1" value="Please enter the product id" />
			<h:inputText id="input1" value="#{shoppingBacking.productId}"
				required="true" />
			<h:commandButton id="button1" value="Buy"
				action="#{shoppingBacking.send}" />
			<h:message id="message1" for="input1" />

			<h:commandButton action="logout" value="Logout" immediate="true"/>
		</h:panelGrid>
	</h:form>
</f:view>
</body>
</html>
