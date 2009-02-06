/**
 * 
 */
package org.codegen
import java.sql.*
import org.codegen.model.Table
import org.codegen.model.Relationship
import org.codegen.model.Key
import org.codegen.model.JavaProperty
import org.codegen.model.JavaClass
import org.codegen.model.Column
import org.codegen.model.RelationshipType


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
    Map <String, JavaClass> tableToJavaClassMap = [:]
    /** Map of Java classes to tables */
    Map <String,Table> javaClassToTableMap = [:]
    /** Tables */
    List<Table> tables
    /** The name of the packageName that we will be using. */
    String packageName
    String modifierPackageName="model"
    boolean debug
    boolean trace
    boolean needsSequence
	
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
    if (debug) println "Converting Database column to Java class Column name = ${column.name} Column type = ${column.type}/${column.typeName}"
    JavaClass returnValue = null
    switch (column.type) {
      case [Types.BINARY, Types.VARBINARY, Types.LONGVARBINARY, Types.BLOB]:
        returnValue = new JavaClass(name: "byte[]", packageName: "java.lang")
        break
      case [Types.VARCHAR, Types.CHAR, Types.CLOB, Types.LONGVARCHAR]:
        returnValue = new JavaClass(name: "String", packageName: "java.lang")
        break
      case Types.REAL:
        returnValue = column.nullable ?
          new JavaClass(name: "Float", packageName: "java.lang") :
          new JavaClass(name: "float", primitive: true)
        break
      case Types.BIGINT:
        returnValue = column.nullable || column.primaryKey ?
          new JavaClass(name: "Long", packageName: "java.lang") :
          new JavaClass(name: "long", primitive: true)
        break
      case Types.SMALLINT:
        returnValue = column.nullable ?
          new JavaClass(name: "Short", packageName: "java.lang") :
          new JavaClass(name: "short", primitive: true)
        break
      case Types.TINYINT:
        returnValue = column.nullable ?
          new JavaClass(name: "Byte", packageName: "java.lang") :
          new JavaClass(name: "byte", primitive: true)
        break
      case Types.BIT:
        returnValue = column.nullable ?
          new JavaClass(name: "Boolean", packageName: "java.lang") :
          new JavaClass(name: "boolean", primitive: true)
        break
      case [Types.NUMERIC, Types.DECIMAL, Types.FLOAT, Types.DOUBLE]:
        returnValue = new JavaClass(name: "BigDecimal", packageName: "java.math")
        break
      case Types.DATE:
        returnValue = new JavaClass(name: "Date", packageName: "java.util")
        break
      case Types.TIME:
        returnValue = new JavaClass(name: "Time", packageName: "java.sql")
        break
      case Types.TIMESTAMP:
        returnValue = new JavaClass(name: "Timestamp", packageName: "java.sql")
        break
      case Types.INTEGER:
        returnValue = column.nullable || column.primaryKey ?
          new JavaClass(name: "Integer", packageName: "java.lang") :
          new JavaClass(name: "int", primitive: true)
        break
      default:
         returnValue = returnJavaClassBasedOnColumnTypeName(column)
    }

    if (column.primaryKey && ([Types.NUMERIC, Types.DECIMAL, Types.FLOAT, Types.DOUBLE] as Set).contains(column.type)) {
      returnValue = new JavaClass(name: "Long", packageName: "java.lang")
    }
    /* This should work for Oracle and PostgreSQL style booleans. */
    if (column.typeName == "NUMBER" && column.size==1 && column.decimalDigits == 0){
         returnValue = column.nullable ?
           new JavaClass(name: "Boolean", packageName: "java.lang") :
           new JavaClass(name: "boolean", primitive: true)
    }
    /* It is intersting to note that typeName also hold UDT values so if a company uses UDTs
       it might make sense to put in reading a mapping file that maps UDT to java code right here.
     */
    returnValue
  }

  JavaClass returnJavaClassBasedOnColumnTypeName(Column column) {
    switch (column.typeName) {
      case "BINARY_FLOAT":
        return column.nullable ?
          new JavaClass(name: "Float", packageName: "java.lang") :
          new JavaClass(name: "float", primitive: true)

      case "BINARY_DOUBLE":
        return column.nullable ?
          new JavaClass(name: "Double", packageName: "java.lang") :
          new JavaClass(name: "double", primitive: true)

      default:
        println "Unable to map type for column " + column
        return new JavaClass(name: "Object", packageName: "java.lang")

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
     *  This is the main entry point into this class.
     *  It convert the Database tables into Java classes, columns into Java properties and keys into Relationships.
     */
    public void process() {
        convertTablesToJavaClasses()
        convertKeysIntoRelationships()
        marryRelationships()
    }


    def convertTablesToJavaClasses(){
        tables.each{Table table ->
            String className = generateName(table.name)
            println "generated className: ${className}"
            if (!tableNames.empty) {
                String name = tableNames.find{String name -> name.equalsIgnoreCase(table.name)}
                if (name == null) {
            	    return;
                }
            }
            JavaClass javaClass = new JavaClass(name:className, packageName:"${packageName}.${modifierPackageName}",
                    table:table, needsSequence: needsSequence, sequenceName: table.name + "_SEQ")
            if (convertColumnsToJavaProperties(javaClass, table)) {
                javaClassToTableMap[javaClass.name]=table
                tableToJavaClassMap[table.name]=javaClass
            	classes << javaClass
            }
        }
    }

    /*
     * This method finds the other side of the relationship.
     */
    def marryRelationships() {
        marryRelationships(classes)
    }


    def static marryRelationships(List <JavaClass> classes) {
        classes.each{JavaClass javaClass ->
     		javaClass.relationships.each { Relationship  r1 ->
     			if (r1.otherSide==null) {
     				if (r1.type == RelationshipType.MANY_TO_MANY) {
     					r1.otherSide = r1.relatedClass.relationships.find{Relationship r2 -> r2.owner==r1.relatedClass && r2.key.foriegnKey.table.name == r1.key.foriegnKey.table.name}
     				} else {
     					r1.otherSide = r1.relatedClass.relationships.find{Relationship r2 -> r2.owner==r1.relatedClass && r2.key.foriegnKey.name == r1.key.foriegnKey.name}
     				}
     				if (r1.otherSide==null) {
     					r1.bidirectional = false
     				} else {
     					r1.bidirectional = true
     				}
     			}
     		}
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
 		String relationshipNameSingular = relatedClass.name.unCap()
        String relationshipName = relationshipNameSingular.endsWith('s') ? relationshipNameSingular + "es" : relationshipNameSingular + "s"
 		javaClass.relationships << 
        new Relationship(name:relationshipName, singularName: relationshipNameSingular, relatedClass:relatedClass, key:theKey, type:type, owner:javaClass)
    	 
    }


    def convertKeysIntoRelationships() {
        classes.each{JavaClass javaClass ->
         	convertKeysToRelationships(javaClass)
        }        
    }

    def convertKeysToRelationships(JavaClass javaClass) {
     	/* Note: Exported keys are fkeys in other tables that are pointing to this table. */
     	javaClass.table.exportedKeys.each { Key key ->
     		try {
     			processExportedKey(key, javaClass)
     		} catch (Exception ex) {
     			if (trace) {
                    ex.printMe("Unable to process exported key", this.&pritnln)
     			}
     		}
     	}
     	/* Note: Imported keys are the keys that correlate to columns in this Classes table. 
     	 * Imported keys list the foriegn keys in this table
     	 * */
        javaClass.table.importedKeys.each { Key key ->
            try {
                processImportedKey(key, javaClass)
            } catch (Exception ex) {
                if (trace) {
                    ex.printMe("Unable to process import key", this.&println)
                }
            }

     	}
    }
    
    def processImportedKey(Key key, JavaClass javaClass) {
            if (!key.wellFormed) {
                println "The key was not well formed and we cannot process it."
                return
            }
        
            assert key
            assert key.foriegnKey
            assert key.primaryKey
            assert key.foriegnKey.table
            assert key.primaryKey.table

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
            new Relationship(name:relationshipName, relatedClass:relatedClass, key:key, type:RelationshipType.MANY_TO_ONE, owner:javaClass)
 			javaClass.properties.remove(javaClass.columnNameToPropertyMap[key.foriegnKey.name])
    	
    }

	
	
}
