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
    
    String mainPageLinksTemplateText = '''    <navigation-case>
         <from-outcome>${cls.name.plural().toUpperCase()}</from-outcome>
         <to-view-id>/pages/crud/${cls.name.unCap()}Listing.xhtml</to-view-id>
     </navigation-case>
'''
    
	String navigationCrudTemplateText = '''  <navigation-rule>
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
  	
	public String getCrudNavigation() {
    	StringBuilder builder = new StringBuilder()
    	builder << "\n    <!-- Navigation goals for CRUD -->\n"
    	classes.each{JavaClass cls ->
    		builder << engine.createTemplate(navigationCrudTemplateText).make([cls:cls]).toString() 
    	}
    	builder << "  <!-- End Navigation goals for CRUD  -->\n"
    	return builder.toString()
	}

	public String getPageLinks() {
    	StringBuilder builder = new StringBuilder()
    	builder << "\n    <!-- Main Page Links (codegen) -->\n"
    	classes.each{JavaClass cls ->
    		builder << engine.createTemplate(mainPageLinksTemplateText).make([cls:cls]).toString() 
    	}
    	builder << "    <!-- End of Main Page Links (codegen) -->\n"
    	return builder.toString()
	}
	
    public void process() {
        FileTemplateUtils templateUtil = new FileTemplateUtils()
    	templateUtil.file = file
    	mainPageLinksChangeSpec.replacementText = getPageLinks()
    	navigationCrudChangeSpec.replacementText = getCrudNavigation()
    	templateUtil.changeSpecs << mainPageLinksChangeSpec
    	templateUtil.changeSpecs << navigationCrudChangeSpec
    	templateUtil.process()
    }
	
	
}
