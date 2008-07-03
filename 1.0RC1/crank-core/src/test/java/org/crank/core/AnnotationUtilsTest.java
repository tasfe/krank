package org.crank.core;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Collection;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;


public class AnnotationUtilsTest {

    private Set<String> packageNames = new HashSet<String>() ; 

    @BeforeMethod
    protected void setUp() throws Exception {
        packageNames = new HashSet<String>();
        packageNames.add( "org.crank.annotations" );
    }


    @Test()
    public void testGetAnnotationDataForProperty() {
        List<AnnotationData> annotationDataForProperty =
            AnnotationUtils.getAnnotationDataForProperty( Employee.class, "age", false, packageNames);
        assertNotNull( annotationDataForProperty );
        assertTrue( annotationDataForProperty.size() > 0);


        annotationDataForProperty =
            AnnotationUtils.getAnnotationDataForProperty( Employee.class, "weight", false, packageNames);
        assertNotNull( annotationDataForProperty );
        assertTrue( annotationDataForProperty.size() > 0);

    }

    @Test()
    public void testGetAnnotationDataForField() {
        List<AnnotationData> annotationDataForProperty =
            AnnotationUtils.getAnnotationDataForField( Employee.class, "iq", packageNames);
        assertNotNull( annotationDataForProperty );
        assertTrue( annotationDataForProperty.size() > 0);
        
        annotationDataForProperty =
            AnnotationUtils.getAnnotationDataForField( Employee.class, "height", packageNames);
        assertNotNull( annotationDataForProperty );
        assertTrue( annotationDataForProperty.size() > 0);
        
    }

    @Test()
    public void testGetAnnotationDataForFieldorProperty() {
        Collection<AnnotationData> annotationDataForProperty =
            AnnotationUtils.getAnnotationDataForFieldAndProperty( Employee.class, "iq", packageNames);
        assertNotNull( annotationDataForProperty );
        assertTrue( annotationDataForProperty.size() > 0);

        annotationDataForProperty =
            AnnotationUtils.getAnnotationDataForFieldAndProperty( Employee.class, "weight", packageNames);
        assertNotNull( annotationDataForProperty );
        assertTrue( annotationDataForProperty.size() > 0);

    }

}
