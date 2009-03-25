<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:a4j="https://ajax4jsf.dev.java.net/ajax"
	xmlns:rich="http://richfaces.ajax4jsf.org/rich"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:crank="http://www.googlecode.com/crank"
	xmlns:validation="http://code.google.com/p/krank/validation"
	xmlns:t="http://myfaces.apache.org/tomahawk"
	>
	
<ui:composition template="/templates/layout.xhtml">
	<ui:define name="content">
                                       <h:form>
                                       <h:panelGrid columns="2">
                                       <h:outputLabel value="User Name" for="j_username" />
                                       <t:inputText id="j_username" forceId="true"
                                       value="#{loginBacking.userId}" size="40" maxlength="80"></t:inputText>
                                       <h:outputLabel value="Password" for="j_password" />
                                       <t:inputSecret id="j_password" forceId="true"
                                       value="#{loginBacking.password}" size="40" maxlength="80"
                                       redisplay="true"></t:inputSecret>
          </h:panelGrid>
    <h:commandButton action="login" value="Login" />
   <h:messages id="messages" layout="table" globalOnly="true"
                showSummary="true" showDetail="false" />
   </h:form>

</ui:define>
</ui:composition>
</html>

