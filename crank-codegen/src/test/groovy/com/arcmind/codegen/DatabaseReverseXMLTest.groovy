/**
 * 
 */
package com.arcmind.codegen

import junit.framework.TestCase


/**
 * @author richardhightower
 *
 */
public class DatabaseReverseXMLTest extends TestCase{

    static JdbcUtils jdbcUtils = new JdbcUtils(url:"jdbc:hsqldb:file:/db-codeGen/testdb",
            userName:"sa", password:"", driver:"org.hsqldb.jdbcDriver")
    DataBaseMetaDataReader reader = new DataBaseMetaDataReader(debug:true)
    JavaModelGenerator modelGen = new JavaModelGenerator()
    JPACodeGenerator codeGen = new JPACodeGenerator()
    XMLPersister persister = new XMLPersister()

    static {
    	try {
	    	File dbDir = new File("/db-codeGen")
	    	dbDir.eachFile {File file ->
	    		file.delete()
	    	}
    	} catch(ex) {
    		
    	}
    	jdbcUtils.executeScript(sqlDDL)
    }
	
	static String sqlDDL = """
DROP TABLE EMPLOYEE IF EXISTS;
DROP TABLE Department IF EXISTS;
DROP TABLE ROLE IF EXISTS;

CREATE TABLE Department (
  ID INTEGER IDENTITY,
  name VARCHAR(30),
  PRIMARY KEY (id)
);
CREATE TABLE EMPLOYEE (
  EMP_ID INTEGER IDENTITY,
  FK_DEPARTMENT_ID INTEGER NULL,  
  firstName VARCHAR(30),
  LAST_NAME VARCHAR(30),
  PRIMARY KEY (EMP_ID),
  FOREIGN KEY (FK_DEPARTMENT_ID) REFERENCES Department(ID)  
);

CREATE TABLE ROLE (
		ROLE_ID INTEGER IDENTITY,
		NAME VARCHAR(30),
		PRIMARY KEY (ROLE_ID)
		
);
CREATE TABLE ROLE_EMPLOYEE (
		FK_ROLE_ID INTEGER,
		FK_EMP_ID INTEGER,
		
		FOREIGN KEY (FK_ROLE_ID) REFERENCES ROLE(ROLE_ID), 
		FOREIGN KEY (FK_EMP_ID) REFERENCES EMPLOYEE(EMP_ID)  
);
""";

	public void setUp () {
	}
	
	public void testTest() {
		use(StringCategory) { 
			reader.jdbcUtils = jdbcUtils
	        reader.processDB()
	        
	        modelGen.tables = reader.tables 
	        modelGen.packageName="org.crank.codegentest"
	        modelGen.convertTablesToJavaClasses()
	        
	        codeGen.classes = modelGen.classes
	        codeGen.rootDir = new File("./target/src/main/java")
	        codeGen.writeClassFiles()
	 
	        persister.tables = reader.tables
	        persister.classes = modelGen.classes
	        persister.persist()
	        persister.read()

            persister.classes.each {JavaClass jc -> jc.validateClassAsModel()}

	        persister.fileName = "codeGenTest.xml"
	        persister.persist()
	        
	        assertEquals(new File(persister.outputDir, "codeGen.xml").text, new File(persister.outputDir, "codeGenTest.xml").text)
		}
	}
}
