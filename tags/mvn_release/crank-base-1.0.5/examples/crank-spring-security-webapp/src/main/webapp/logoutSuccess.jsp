<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<html>
    <head>
        <title>Logged out</title>
    </head>
    <body>
        <f:view>
            <h:form id="form">
              <h:panelGrid id="grid" columns="1">
                <h:outputText id="output1" value="You have successfully logged out."/>
              </h:panelGrid>
            </h:form>
        </f:view>
    </body>
</html>
