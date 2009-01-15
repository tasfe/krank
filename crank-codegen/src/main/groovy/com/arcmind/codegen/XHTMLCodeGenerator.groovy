package com.arcmind.codegen
import groovy.text.SimpleTemplateEngine

/**
 * Generates .java files from JavaClass models objects. 
 */
class XHTMLCodeGenerator implements CodeGenerator {
    List<JavaClass> classes
    /** The target output dir. Defaults to ./target */
    File rootDir = new File(".")
    String packageName //not used
    boolean debug
    SimpleTemplateEngine engine = new SimpleTemplateEngine()
    boolean use
    
    String oneToManyTemplate = '''
					<crank:detailListing
						detailController="#{${bean.name.unCap()}Crud.controller.children.${relationship.name}}" 
						propertyNames="firstName,lastName"
						parentForm="${bean.name.unCap()}Form"/>
'''
    String listingTemplate = '''
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:a4j="https://ajax4jsf.dev.java.net/ajax"
	xmlns:rich="http://richfaces.ajax4jsf.org/rich"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:crank="http://www.googlecode.com/crank">
<ui:composition template="/templates/layout.xhtml">
	<ui:define name="content">
		<crank:crudBreadCrumb crud="#{cruds.${bean.name.unCap()}.controller}" />
		<a4j:form id="${bean.name.unCap()}ListForm">
			<crank:listing propertyNames="${propertyNames}" parentForm="${bean.name.unCap()}ListForm" jsfCrudAdapter="#{cruds.${bean.name.unCap()}}"  />
		</a4j:form>
	</ui:define>
</ui:composition>
</html>
'''
    String formTemplate = '''
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:a4j="https://ajax4jsf.dev.java.net/ajax"
	xmlns:rich="http://richfaces.ajax4jsf.org/rich"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:crank="http://www.googlecode.com/crank">
<ui:composition template="/templates/layout.xhtml">
	<ui:define name="content">
		<crank:crudBreadCrumb crud="#{${bean.name.unCap()}Crud.controller}" />
		<c:choose>
			<c:when test='#{${bean.name.unCap()}.controller.state == "ADD"}'>
				<h:outputText value="Create Department" styleClass="pageTitle" />
			</c:when>
			<c:otherwise>
				<h:outputText
					value="Edit Department #{${bean.name.unCap()}Crud.controller.entity.name}"
					styleClass="pageTitle" />
			</c:otherwise>
		</c:choose>
		<a4j:form id="departmentForm">
			<rich:messages errorClass="pageErrorMessage" />
			<crank:form parentForm="departmentForm"
				crud="#{${bean.name.unCap()}Crud.controller}" propertyNames="${propertyNames}">
				${formBody}
			</crank:form>
		</a4j:form>
	</ui:define>
</ui:composition>
</html>
'''

	def doProcess(String fileNameSuffix, String template) {
        for (JavaClass bean in classes) {
        	
        	if (debug) println "Writing ${bean.name} listing xhtml"
            def binding = [bean:bean, formBody:generateBody(bean), propertyNames:generatePropertyNames(bean)]        
            String templateOutput = engine.createTemplate(template).make(binding).toString()
            rootDir.mkdirs()
            File listingFile = new File (rootDir, bean.name.unCap() + fileNameSuffix)
        	listingFile.newWriter().withWriter{BufferedWriter writer->
            	writer.write(templateOutput)
            }
        }
	
	}
	public void process() {
		doProcess("Listing.xhtml", listingTemplate)
		doProcess("Form.xhtml", formTemplate)
    }

	String generatePropertyNames(JavaClass bean) {
		List<String> builder = []
		bean.relationships.each{Relationship relationship ->
			switch (relationship.type) {
				case[RelationshipType.MANY_TO_ONE, RelationshipType.ONE_TO_ONE]:
					builder << relationship.name
			}
		}
		
		bean.properties.each {JavaProperty jp ->
			switch (jp.javaClass) {
				case [new JavaClass("String","java.lang"), new JavaClass("Integer","java.lang"), 
                new JavaClass("Long","java.lang"), new JavaClass("Byte", "java.lang"), 
                new JavaClass("Date", "java.util")]:
				builder << jp.name
			}
			if (jp.javaClass.primitive) {
				builder << jp.name
			}
		}
		
		builder.join(",")
	}
	
	String generateBody(JavaClass bean) {
		StringBuilder builder = new StringBuilder()
		bean.relationships.each {Relationship relationship ->
			if (relationship.type == RelationshipType.ONE_TO_MANY) {
				builder << engine.createTemplate(oneToManyTemplate).make([bean:bean, relationship:relationship]).toString()
			}
		}
		builder.toString()
	}
}