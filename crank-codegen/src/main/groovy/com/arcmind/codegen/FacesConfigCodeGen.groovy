/**
 * 
 */
package com.arcmind.codegen

import groovy.text.SimpleTemplateEngine


public class FacesConfigCodeGen{
	
	List<JavaClass> classes
	File file
	boolean debug
	FileTemplateUtils templateUtil = new FileTemplateUtils()
	SimpleTemplateEngine engine = new SimpleTemplateEngine()
	ChangeSpec mainPageLinksChangeSpec = new ChangeSpec(startLocationMarker:"Main Page Links (codegen)",
	stopLocationMarker:"End of Main Page Links (codegen)")
	ChangeSpec navigationCrudChangeSpec = new ChangeSpec(startLocationMarker:"Navigation goals for CRUD",
	stopLocationMarker:"End Navigation goals for CRUD")
	ChangeSpec crudConverterChangeSpec = new ChangeSpec(startLocationMarker:"Crud Converters",
	stopLocationMarker:"End Crud Converters")
	
	
	String mainPageLinksTemplateText = '''
    <navigation-case>
         <from-outcome>${cls.name.plural().toUpperCase()}</from-outcome>
         <to-view-id>/pages/crud/${cls.name.unCap()}Listing.xhtml</to-view-id>
     </navigation-case>
'''
	
	String navigationCrudTemplateText = '''  
<navigation-rule>
  	<from-view-id>/pages/crud/${cls.name.unCap()}Listing.xhtml</from-view-id>
  	<navigation-case>
  		<from-outcome>FORM</from-outcome>
  		<to-view-id>/pages/crud/${cls.name.unCap()}Form.xhtml</to-view-id>
  	</navigation-case>  	
  </navigation-rule>
  <navigation-rule>
  	<from-view-id>/pages/crud/${cls.name.unCap()}Form.xhtml</from-view-id>  
  	<navigation-case>
  		<from-outcome>LISTING</from-outcome>
  		<to-view-id>/pages/crud/${cls.name.unCap()}Listing.xhtml</to-view-id>
  	</navigation-case>
  </navigation-rule>
	'''
	
	String crudConverterTemplateText = '''
  <converter>
  	<converter-for-class>${cls.packageName}.${cls.name}</converter-for-class>
  	<converter-class>org.crank.crud.jsf.support.EntityConverter</converter-class>
  </converter>
'''
	
	public String processTemplate(ChangeSpec changeSpec, String template) {
		StringBuilder builder = new StringBuilder()
		builder << "    <!-- ${changeSpec.startLocationMarker} -->"
		classes.each{JavaClass cls -> builder << engine.createTemplate(template).make([cls:cls]).toString()  }
		builder << "  <!-- ${changeSpec.stopLocationMarker}  -->\n"
		return builder.toString()
	}
	
	public String getCrudNavigation() {
		processTemplate navigationCrudChangeSpec, navigationCrudTemplateText
	}
	
	public String getCrudConverter() {
		processTemplate crudConverterChangeSpec, crudConverterTemplateText
		
	}
	
	public String getPageLinks() {
		processTemplate mainPageLinksChangeSpec, mainPageLinksTemplateText
	}
	
	public void process() {
		FileTemplateUtils templateUtil = new FileTemplateUtils()
		templateUtil.file = file
		mainPageLinksChangeSpec.replacementText = getPageLinks()
		navigationCrudChangeSpec.replacementText = getCrudNavigation()
		crudConverterChangeSpec.replacementText = getCrudConverter()
		templateUtil.changeSpecs << mainPageLinksChangeSpec
		templateUtil.changeSpecs << navigationCrudChangeSpec
		templateUtil.changeSpecs << crudConverterChangeSpec
		templateUtil.process()
	}
	
	
}
