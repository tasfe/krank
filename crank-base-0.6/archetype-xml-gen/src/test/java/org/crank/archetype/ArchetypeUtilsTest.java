package org.crank.archetype;

import java.io.File;

import junit.framework.TestCase;

/**
 * Unit test for simple App.
 */
public class ArchetypeUtilsTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ArchetypeUtilsTest( String testName ) {
        super( testName );
    }

    /**
     * 
     */
    public void testGenerateXML() throws Exception {
        String xml = ArchetypeUtils.generateXML("C:\\krank\\validation\\validation\\crank-crud-webapp-sample", "foo");
        assertTrue(xml.contains( "<source filtered='false'>src/main/resources/META-INF/persistence.xml</source>" ));
        
        File dir = new File ("/tmp/archetypetest/");
        File sample = new File("C:\\krank\\validation\\validation\\crank-crud-webapp-sample");

        
        ArchetypeUtils.main( "-o=foo", "-d=bar");
        ArchetypeUtils.main( "-i=someId");
        
        ArchetypeUtils.main( "-o=" + dir.getCanonicalFile().toString(), 
                "-d="+ sample.getCanonicalPath().toString(),
                "-i="+ "sample");
        
        
        File outputFile = new File(dir, "archetype.xml");
        assertTrue(outputFile.exists());
        

    }
}
