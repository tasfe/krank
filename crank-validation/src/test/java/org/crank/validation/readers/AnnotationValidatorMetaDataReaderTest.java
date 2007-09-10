package org.crank.validation.readers;

import java.util.List;

import org.crank.annotations.validation.LongRange;
import org.crank.annotations.validation.Range;
import org.crank.annotations.validation.Required;
import org.crank.validation.ValidatorMetaData;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;

public class AnnotationValidatorMetaDataReaderTest {

	private AnnotationValidatorMetaDataReader annotationValidatorMetaDataReader;
	@BeforeMethod
	protected void setUp() throws Exception {
		annotationValidatorMetaDataReader = new AnnotationValidatorMetaDataReader(); 
	}

	@AfterMethod
	protected void tearDown() throws Exception {
	}

	@Test()
	public void testReadMetaData() {
		List<ValidatorMetaData> list = annotationValidatorMetaDataReader.readMetaData(Employee.class, "age");
		assertNotNull(list);
		ValidatorMetaData required = list.get(0);
		assertEquals("required", required.getName());
		ValidatorMetaData range = list.get(1);
		assertEquals("range", range.getName());
		assertEquals("10",range.getProperties().get("max"));
		assertEquals("1",range.getProperties().get("min"));
		
	}

	@Test
	public void testReadMetaDataWithNonStringValues() {
		List<ValidatorMetaData> list = 
			annotationValidatorMetaDataReader.readMetaData(Employee.class, "iq");
		assertNotNull(list);
		assertEquals(2, list.size());
		ValidatorMetaData range = list.get(1);
		assertEquals("longRange", range.getName());
		assertEquals(100L,range.getProperties().get("max"));
		assertEquals(1L,range.getProperties().get("min"));
		
	}
	
}

class Employee {
	int age;
	long iq;

	public long getIq() {
		return iq;
	}


	@Required @LongRange(min=1L, max=100L)
	public void setIq(long iq) {
		this.iq = iq;
	}

	public int getAge() {
		return age;
	}

	@Required @Range (min="1", max="10")
	public void setAge(int age) {
		this.age = age;
	}
	
}
