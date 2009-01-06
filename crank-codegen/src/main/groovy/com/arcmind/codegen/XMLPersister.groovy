package com.arcmind.codegen

import groovy.xml.MarkupBuilder


/** Reads and writes editable XML files. This allows the end user to override what we guessed in 
 *  the generation process.
 *  @author richardhightower
 */
public class XMLPersister{

	List <Table> tables
	List <JavaClass> classes
	File outputDir = new File("./target")
	String fileName = "codegen.xml"
	boolean debug
	
	/* Persist our classes and tables (and their hierarchy) to the XML file. */
	void persist() {
		List <Table> _tables = tables
		List <JavaClass> _classes = classes 
		BufferedWriter bWriter = new File (outputDir, fileName).newWriter()
		bWriter.withWriter {writer ->
            def xmlDocument = new MarkupBuilder(writer)
            xmlDocument.codeGen(){

				classes {
					_classes.each {JavaClass c -> c.with {
                            if (debug) println "Writing class ${name}"
						'class'(name: name, packageName: packageName, tableName: table.name) {
                                /* Write the id for the Java class. */
							'id'(name: id.name, column: id.column.name, className: id.javaClass.name, 
									packageName: id.javaClass.packageName, primitive: id.javaClass.primitive)
                                /* Write out the properties for the Java class. */
                                properties { c.properties.each {JavaProperty p -> p.with {
                                            property (name: p.name, column: column.name, className: javaClass.name,
                                                packageName: javaClass.packageName, primitive: javaClass.primitive)
                                        }}}//properties
                                /* Write out the relationships for the Java class. */
                                relationships { c.relationships.each {Relationship r -> r.with {
                                            relationship (name: r.name, type: type.toString()) {
									'relatedClass' (name: relatedClass.name, packageName: relatedClass.packageName)
                                                key?.with {
										'key'(primaryKeyColumn: primaryKey.name,
                                                        primaryKeyTable: primaryKey.table.name,
                                                        foriegnKeyColumn: foriegnKey.name,
                                                        foriegnKeyTable: foriegnKey.table.name,
                                                        imported: imported)
                                                }//key.with
                                            }//relationship
                                        }}}//relationships
                            }//'class'
                        }}//classes.each
				}//classes

                tables {
					_tables.each {Table t ->
                        if (debug) println "Writing table ${t.name}"
                        table(name: t.name) {
                            primaryKeys {
                                t.primaryKeys.each{String pk ->
                                    primaryKey(name:pk)
                                }//primaryKeys.each
                            }//primaryKeys
                            columns { t.columns.each { Column c -> c.with {
                                        column (name: name, type: type, typeName: typeName, nullable: nullable, primaryKey: primaryKey)
                                    }/*columns.each*/ }}
                            exportedKeys {t.exportedKeys.each { Key k -> k.with {
                                        key(primaryKeyColumn: primaryKey.name,
                                            primaryKeyTable: primaryKey.table.name,
                                            foriegnKeyColumn: foriegnKey.name,
                                            foriegnKeyTable: foriegnKey.table.name)
                                    }}}//exportedKeys
                            importedKeys {t.importedKeys.each { Key k -> k.with {
                                        key(primaryKeyColumn: primaryKey.name,
                                            primaryKeyTable: primaryKey.table.name,
                                            foriegnKeyColumn: foriegnKey.name,
                                            foriegnKeyTable: foriegnKey.table.name)
                                    }}}//importedKeys
                        }//table
					}//tables.each
				}//tables
            }//codeGen
		}//withWriter
	}//persist
	
	/* Read our model from XML. */
	void read() {
		def codeGen = new XmlSlurper().parse(new File (outputDir, fileName))
		Map<String,Table> tableMap = [:]
        readTables(tableMap, codeGen)
        readClasses(tableMap, codeGen)
	}	
    void readTables(Map<String,Table> tableMap, codeGen) {
    	if (debug) println "Reading tables"
		tables = []
       
		codeGen.tables.table.each {tbl ->
			Table table = new Table(name: tbl.@name)
			tables << table
            tableMap[tbl.@name.toString()] = table
		}//tables        
		codeGen.tables.table.each {tbl ->

            Table table =  tableMap[tbl.@name.toString()]
            assert table!=null
            tbl.primaryKeys.primaryKey.each {
				table.primaryKeys << it.@name.toString()
			}//primaryKeys
			tbl.columns.column.each { c ->
				table.columns << new Column(table: table, name: c.@name, 
                    type: Integer.valueOf(c.@type.toString()), typeName: c.@typeName,
                    nullable: Boolean.valueOf(c.@nullable.toString()),
                    primaryKey: Boolean.valueOf(c.@primaryKey.toString()))
			}//columns
			tbl.exportedKeys.key.each {k ->
				Key key = new Key(imported:false)
				key.primaryKey = new Column(name: k.@primaryKeyColumn, table: tableMap[k.@primaryKeyTable.toString()])
				key.foriegnKey = new Column(name: k.@foriegnKeyColumn, table: tableMap[k.@foriegnKeyTable.toString()])
				table.exportedKeys << key
			}//exportedKeys
			tbl.importedKeys.key.each {k ->
				Key key = new Key(imported:true)
				key.primaryKey = new Column(name: k.@primaryKeyColumn, table: tableMap[k.@primaryKeyTable.toString()])
				key.foriegnKey = new Column(name: k.@foriegnKeyColumn, table: tableMap[k.@foriegnKeyTable.toString()])
				table.importedKeys << key
			}//importedKeys
        }

    }
	

	
	/** Read the classes from the XML document. */
	void readClasses(Map<String,Table> tableMap, codeGen) {
		if (debug) println "Reading classes"
		classes = []
		Map<String,JavaClass> classMap = [:]
		/* Read the classes. */
		codeGen.classes.'class'.each {cls ->
			JavaClass clz = new JavaClass(name: cls.@name, packageName: cls.@packageName)
			classMap[clz.name]=clz
			classes << clz
			clz.table = tableMap[cls.@tableName.toString()]
			/* Read the id. */
			clz.id = new JavaProperty(name: cls.id.@name)
			clz.id.javaClass = new JavaClass(name: cls.id.@className, packageName: cls.id.@packageName, 
                primitive: Boolean.valueOf(cls.id.@primitive.toString()))
			clz.id.column = clz.table.columns.find{it.name==cls.id.@column.toString()}
			/* Read the properties. */
			cls.properties.property.each { prop ->
				JavaProperty jp = new JavaProperty(name: prop.@name)
				JavaClass jc = new JavaClass(name: prop.@className, packageName: prop.@packageName, 
                    primitive: Boolean.valueOf(prop.@primitive.toString()))
				jp.javaClass = jc
				jp.column = clz.table.columns.find{it.name==prop.@column.toString()}
				clz.properties << jp
			}
		}
		
		readRelationships(classMap, tableMap, codeGen)
		
	}//readClasses
	
	
	void readRelationships(Map<String,JavaClass> classMap, Map<String,Table> tableMap, codeGen) {
		if (debug) println "Reading relationships from classes"
		/** Read the relationships from the XML document. */
		codeGen.classes.'class'.each {cls ->
            cls.relationships.relationship.each { rel ->
                /* Create the relationship. */
                Relationship relationship = new Relationship(name: rel.@name, type: Enum.valueOf(RelationshipType.class,
                        rel.@type.toString()))
                /* Lookup the actual class object based on the class element. */
                JavaClass clz = classMap[cls.@name.toString()]
                clz.relationships << relationship
                relationship.owner = clz
			
                /* Create the related class based on the relationhip element. */
                relationship.relatedClass = classMap[rel.relatedClass.@name.toString()]
                if (relationship.relatedClass==null) {	
                	relationship.relatedClass = new JavaClass(name: rel.relatedClass.@name, packageName: rel.relatedClass.@packageName)
                }
			
                /* Pull out the information that we need to look up the correct tables and columns. */
                String primaryKeyTable = rel.key.@primaryKeyTable.toString()
                String primaryKeyColumn = rel.key.@primaryKeyColumn.toString()
                boolean imported = Boolean.valueOf(rel.key.@imported.toString())
                String foriegnKeyTable = rel.key.@foriegnKeyTable.toString()
                String foriegnKeyCoumn = rel.key.@foriegnKeyColumn.toString()
			
                /* Lookup the keys based on looking up the table and the right key from the right source. */
                if (!imported) {
                    relationship.key = tableMap[primaryKeyTable].exportedKeys.find{it.primaryKey.name==primaryKeyColumn}
                } else if (imported) {
                    relationship.key = tableMap[foriegnKeyTable].importedKeys.find{it.foriegnKey.name==foriegnKeyCoumn}
                }
            }//relationships
		}//classes
	}//readRelationships
}