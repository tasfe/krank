/**
 * 
 */
package com.arcmind.codegen

import groovy.text.SimpleTemplateEngine


public class SpringJavaConfigCodeGen{
	
	List<JavaClass> classes
	File file
	boolean debug
	FileTemplateUtils templateUtil = new FileTemplateUtils()
	SimpleTemplateEngine engine = new SimpleTemplateEngine()
	ChangeSpec constantsChangeSpec = new ChangeSpec(startLocationMarker:"Entity Constants.", stopLocationMarker:"End Entity Constants.")
	ChangeSpec relationshipConstantChangeSpec = new ChangeSpec(startLocationMarker:"Relationship Constants.", stopLocationMarker:"End Relationship Constants.")
	ChangeSpec managedObjectsChangeSpec = new ChangeSpec(startLocationMarker:"Managed objects.", stopLocationMarker:"End Managed objects.")
	ChangeSpec crudChangeSpec = new ChangeSpec(startLocationMarker:"Crud adapters.", stopLocationMarker:"End Crud adapters.")
	ChangeSpec crudManyToManyChangeSpec = new ChangeSpec(startLocationMarker:"ManyToMany controllers.", stopLocationMarker:"End ManyToMany controllers.")
	
	
	String constantsTemplateText = '''
	private static String ${bean.name.toUpperCase()} = "${bean.name.unCap()}";'''
	String relationshipConstantTemplateText = '''
	private static String ${relationship.owner.name.toUpperCase()}_${relationship.name.toUpperCase()}_RELATIONSHIP = "${relationship.name}";'''
	String managedObjectsTemplateText = '''
			managedObjects.add(new CrudManagedObject(${bean.name}.class));'''
			
	String crudTemplateText = '''  
	@Bean(scope = DefaultScopes.SESSION)
	public JsfCrudAdapter<${bean.name}, Long> ${bean.name.unCap()}Crud() throws Exception {
		JsfCrudAdapter<${bean.name}, Long> adapter = (JsfCrudAdapter<${bean.name}, Long>) cruds().get(${bean.name.toUpperCase()});
		${relationships}
		return adapter;
	}
'''
	
	String crudManyToManyTemplateText = '''
	@Bean(scope = DefaultScopes.SESSION)
	public JsfSelectManyController<${relationship.relatedClass.name}, Long> ${relationship.owner.name.unCap()}To${relationship.name}Controller()
			throws Exception {
		JsfSelectManyController<${relationship.relatedClass.name}, Long> controller = new JsfSelectManyController<${relationship.relatedClass.name}, Long>(
				${relationship.relatedClass.name}.class, ${relationship.owner.name.toUpperCase()}_${relationship.name.toUpperCase()}_RELATIONSHIP, paginators().get(${relationship.relatedClass.name.toUpperCase()}), ${relationship.owner.name.unCap()}Crud()
						.getController());
		return controller;
	}
'''
	String oneToManyDetailControllerTemplateText = '''
		adapter.getController().addChild(${relationship.owner.name.toUpperCase()}_${relationship.name.toUpperCase()}_RELATIONSHIP, new JsfDetailController(${relationship.relatedClass.name}.class, true));'''
	
	public String processTemplate(ChangeSpec changeSpec, String template, relationships={""}, String indent="") {
		StringBuilder builder = new StringBuilder()
		builder << indent + "    /* ${changeSpec.startLocationMarker} */"
		classes.each{JavaClass cls -> builder << engine.createTemplate(template).make([bean:cls, relationships:relationships(cls)]).toString()  }
		builder << "\n" + indent +  "    /* ${changeSpec.stopLocationMarker}  */\n"
		return builder.toString()
	}
	
	public String getConstants() {
		processTemplate constantsChangeSpec, constantsTemplateText
	}
	
	public String getRelationshipConstants() {
		ChangeSpec changeSpec = relationshipConstantChangeSpec
		String template = relationshipConstantTemplateText
		StringBuilder builder = new StringBuilder()
		builder << "    /* ${changeSpec.startLocationMarker} */"
		classes.each{JavaClass cls -> 
				cls.relationships.each {Relationship relationship -> 
					if (relationship.type == RelationshipType.MANY_TO_MANY || relationship.type == RelationshipType.ONE_TO_MANY) {
						builder << engine.createTemplate(template).make([relationship:relationship]).toString()
					}
				}
		}
		builder << "\n    /* ${changeSpec.stopLocationMarker}  */\n"
		return builder.toString()
	}
	
	public String getManagedObjects() {
		processTemplate managedObjectsChangeSpec, managedObjectsTemplateText, {""}, "        "
	}
	
	
	public String getCrudControllers() {
		processTemplate crudChangeSpec, crudTemplateText, this.&getOneToManyDetailControllers
	}

	public String getOneToManyDetailControllers(JavaClass cls) {
		StringBuilder builder = new StringBuilder()
		cls.relationships.each {Relationship relationship -> 
			if (relationship.type == RelationshipType.ONE_TO_MANY) {
						builder << engine.createTemplate(oneToManyDetailControllerTemplateText).make([relationship:relationship]).toString()
			}
		}
		return builder.toString()
	}

	public String getManyToManyControllers() {
		ChangeSpec changeSpec = crudManyToManyChangeSpec
		String template = crudManyToManyTemplateText
		StringBuilder builder = new StringBuilder()
		builder << "    /* ${changeSpec.startLocationMarker} */"
		classes.each{JavaClass cls -> 
				cls.relationships.each {Relationship relationship -> 
					if (relationship.type == RelationshipType.MANY_TO_MANY) {
						builder << engine.createTemplate(template).make([relationship:relationship]).toString()
					}
				}
		}
		builder << "\n    /* ${changeSpec.stopLocationMarker}  */\n"
		return builder.toString()

	}


	public void process() {
		FileTemplateUtils templateUtil = new FileTemplateUtils()
		templateUtil.file = file
		constantsChangeSpec.replacementText = getConstants()
		relationshipConstantChangeSpec.replacementText = getRelationshipConstants()
		managedObjectsChangeSpec.replacementText = getManagedObjects()
		crudChangeSpec.replacementText = getCrudControllers()
		crudManyToManyChangeSpec.replacementText = getManyToManyControllers()
		templateUtil.changeSpecs << constantsChangeSpec
		templateUtil.changeSpecs << relationshipConstantChangeSpec
		templateUtil.changeSpecs << managedObjectsChangeSpec
		templateUtil.changeSpecs << crudChangeSpec
		templateUtil.changeSpecs << crudManyToManyChangeSpec
		templateUtil.process()
	}
	
	
}
