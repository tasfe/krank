/**
 * 
 */
package com.arcmind.codegen
import java.sql.*



/** Takes the table hierarchy produced from DataBaseMetaDataReader or read from an XML file 
 *  and generates a hierarchy of JavaClass, JavaProperty and Relationship model objects.
 *  @author richardhightower
 */
public class JavaModelGenerator{
    /** List of Java classes calculated from tables. */
    List <JavaClass> classes = []
    /** Table names to process. */
    Set <String> tableNames = []    
    /** Map of tables to Java classes */
    HashMap <String, JavaClass> tableToJavaClassMap = [:]
    /** Map of Java classes to tables */
    HashMap <String,Table> javaClassToTableMap = [:]
    /** Tables */
    List<Table> tables
    /** The name of the packageName that we will be using. */
    String packageName
    boolean debug
    boolean trace
	
    /**
     *   Convert the columns to Java properties.
     */
    boolean convertColumnsToJavaProperties(JavaClass javaClass, Table table){
        javaClass.properties = table.columns.collect {Column column ->

            String propertyName = generateName(column.name).unCap()
            JavaProperty property = new JavaProperty(name:propertyName, column:column)
            javaClass.columnNameToPropertyMap[column.name]=property
            property.javaClass = convertColumnToJavaClass (column)
            property
        }
        List<JavaProperty> idProps = javaClass.properties.findAll{JavaProperty javaProperty -> javaProperty.column.primaryKey==true}
        if (idProps.size() == 1) {
            javaClass.id = idProps[0]
            javaClass.properties.remove(javaClass.id)
            return true
        } else {
            return false
        }
    }

    /** Convert the column type to the equivalent Java class/type.
     */
    JavaClass convertColumnToJavaClass(Column column) {
        switch (column.type) {
            case [Types.BINARY, Types.VARBINARY, Types.LONGVARBINARY]:
            return new JavaClass(name:"byte[]", packageName:"java.lang")
            case [Types.VARCHAR, Types.CHAR, Types.LONGNVARCHAR, Types.CLOB, Types.LONGVARCHAR]:
            return new JavaClass(name:"String", packageName:"java.lang")
            case [Types.FLOAT, Types.DOUBLE]:
            return column.nullable ?
            new JavaClass(name:"Double", packageName:"java.lang") :
            new JavaClass(name:"double", primitive:true)
            case Types.REAL:
            return column.nullable ?
            new JavaClass(name:"Float", packageName:"java.lang") :
            new JavaClass(name:"float", primitive:true)
            case Types.BIGINT:
            return column.nullable || column.primaryKey ?
            new JavaClass(name:"Long", packageName:"java.lang") :
            new JavaClass(name:"long", primitive:true)
            case Types.SMALLINT:
            return column.nullable ?
            new JavaClass(name:"Short", packageName:"java.lang") :
            new JavaClass(name:"short", primitive:true)
            case Types.TINYINT:
            return column.nullable ?
            new JavaClass(name:"Byte", packageName:"java.lang") :
            new JavaClass(name:"byte", primitive:true)
            case Types.BIT:
            return column.nullable ?
            new JavaClass(name:"Boolean", packageName:"java.lang") :
            new JavaClass(name:"boolean", primitive:true)
            case [Types.NUMERIC, Types.DECIMAL]:
            return new JavaClass(name:"BigDecimal", packageName:"java.math")
            case [Types.DATE, Types.TIME, Types.TIMESTAMP]:
            return new JavaClass(name:"Date", packageName:"java.util")
            case [Types.INTEGER] :
            return column.nullable || column.primaryKey ?
            new JavaClass(name:"Integer", packageName:"java.lang") :
            new JavaClass(name:"int", primitive:true)
            default:
            println "Unable to map type for column " + column
            return new JavaClass(name:"Object", packageName:"java.lang")
        }
    }

    String generateName(dbName) {
    	String name
        if (dbName.contains("_")) {
        	name = dbName.split("_").collect{ String namePart -> namePart.capAndLower() }.join()
        } else {
        	if (dbName.isAllUppers()) {
        		name = dbName.capAndLower()
        	} else {
        		name = dbName.cap()
        	}
        }
    	name
    }
    
    /**
     *  Convert the Database tables into Java classes.
     */
    def convertTablesToJavaClasses(){
        tables.each{Table table ->
            String className = generateName(table.name)
            if (!tableNames.empty && !tableNames.contains(table.name)) {
            	return;
            }
            JavaClass javaClass = new JavaClass(name:className, packageName:packageName, table:table)
            if (convertColumnsToJavaProperties(javaClass, table)) {
                javaClassToTableMap[javaClass.name]=table
                tableToJavaClassMap[table.name]=javaClass
            	classes << javaClass
            }
            
        }
        classes.each{JavaClass javaClass ->
         	convertKeysToRelationships(javaClass)
        }
    }
    def processExportedKey(Key key, JavaClass javaClass) {
  		Key theKey = key
 		RelationshipType type = null
 		JavaClass relatedClass = this.tableToJavaClassMap[key.foriegnKey.table.name]
 		if (relatedClass == null) {
 			/* If the related class is not found, then this may be a ManyToManyRelationship */
 			Table joinTable = key.foriegnKey.table
 			if (joinTable.columns.size()==2){
 				Column otherColumn = joinTable.columns.find{it.name != key.foriegnKey.name}
 				theKey = joinTable.importedKeys.find{it.foriegnKey.name == otherColumn.name}
 				relatedClass = this.tableToJavaClassMap[theKey.primaryKey.table.name]
 				type = RelationshipType.MANY_TO_MANY
 			} else {
 				return	
 			}
        } else {
        	type = RelationshipType.ONE_TO_MANY
        }
 		String relationshipName = relatedClass.name.unCap()
        relationshipName = relationshipName.endsWith('s') ? relationshipName + "es" : relationshipName + "s"
 		javaClass.relationships << 
        new Relationship(name:relationshipName, relatedClass:relatedClass, key:theKey, type:type)
    	 
    }
    def convertKeysToRelationships(JavaClass javaClass) {
     	/* Note: Exported keys are fkeys in other tables that are pointing to this table. */
     	javaClass.table.exportedKeys.each { Key key ->
     		try {
     			processExportedKey(key, javaClass)
     		} catch (Exception ex) {
                println "UNABLE TO PROCESS KEY ${ex.message}"
                ByteArrayOutputStream bos = new ByteArrayOutputStream()
                PrintStream stream = new PrintStream(bos)
                ex.printStackTrace(stream)
                println bos.toString()
     		}
     	}
     	/* Note: Imported keys are the keys that correlate to columns in this Classes table. 
     	 * Imported keys list the foriegn keys in this table
     	 * */
        javaClass.table.importedKeys.each { Key key ->
 			JavaClass relatedClass = this.tableToJavaClassMap[key.primaryKey.table.name]
     		if (relatedClass == null) {
     			return
            }
 			String relationshipName = key.foriegnKey.name

            relationshipName = relationshipName - "_ID"
            relationshipName = relationshipName - "FK_"
            relationshipName = generateName(relationshipName).unCap()
            if (relationshipName.endsWith("Id")) {
                relationshipName = relationshipName - "Id"
            }
 			javaClass.relationships << 
            new Relationship(name:relationshipName, relatedClass:relatedClass, key:key, type:RelationshipType.MANY_TO_ONE)
 			javaClass.properties.remove(javaClass.columnNameToPropertyMap[key.foriegnKey.name])
     	}
    }

	
	
}
