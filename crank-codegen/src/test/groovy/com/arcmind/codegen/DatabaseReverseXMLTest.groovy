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
    DataBaseMetaDataReader reader = new DataBaseMetaDataReader()
    JavaModelGenerator modelGen = new JavaModelGenerator()
    CodeGenerator codeGen = new CodeGenerator()
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
	
	static String sqlDDL = '''
DROP TABLE EMPLOYEE IF EXISTS;
DROP TABLE Department IF EXISTS;

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
''';

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
	        codeGen.writeClassFiles()
	 
	        persister.tables = reader.tables
	        persister.classes = modelGen.classes
	        persister.persist()
	        persister.read()
	        persister.fileName = "codeGenTest.xml"
	        persister.persist()
	        
	        assertEquals(new File(persister.outputDir, "codeGen.xml").text, new File(persister.outputDir, "codeGenTest.xml").text)
		}
	}
}
