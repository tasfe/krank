package org.crank.core.spring.support;

import java.util.HashMap;
import java.util.Map;
import static org.testng.AssertJUnit.*;

import org.testng.annotations.Test;

public class SpringBeanWrapperPropertiesUtilTest {
	@Test
	public void testCopyProperties() {
		SpringBeanWrapperPropertiesUtil sbwpu = new SpringBeanWrapperPropertiesUtil();
		TestSupport testObject = new TestSupport();
		Map<String, Object> valuesMap = new HashMap<String, Object>();
		valuesMap.put("prop1", "test");
		valuesMap.put("prop2", true);
		valuesMap.put("prop3", 5.5f);
		sbwpu.copyProperties(testObject, valuesMap);
		assertEquals("test", testObject.getProp1());
		assertEquals(true, testObject.isProp2());
		assertEquals(5.5f, testObject.getProp3());
	}
	
	@Test
	public void testGetObjectPropertiesAsMap() {
		SpringBeanWrapperPropertiesUtil sbwpu = new SpringBeanWrapperPropertiesUtil();
		TestSupport testObject = new TestSupport();
		testObject.setProp1("test");
		testObject.setProp2(true);
		testObject.setProp3(5.5f);
		Map<String, Object> valuesMap = sbwpu.getObjectPropertiesAsMap(testObject);
		assertEquals("test", valuesMap.get("prop1"));
		assertEquals(true, valuesMap.get("prop2"));
		assertEquals(5.5f, valuesMap.get("prop3"));
	}
	
	@Test
	public void testGetPropertyValue() {
		SpringBeanWrapperPropertiesUtil sbwpu = new SpringBeanWrapperPropertiesUtil();
		TestSupport testObject = new TestSupport();
		testObject.setProp1("test");
		testObject.setProp2(true);
		testObject.setProp3(5.5f);
		assertEquals("test", sbwpu.getPropertyValue("prop1", testObject));
		assertEquals(true, sbwpu.getPropertyValue("prop2", testObject));
		assertEquals(5.5f, sbwpu.getPropertyValue("prop3", testObject));
	}
	
	
	class TestSupport {
		private String prop1;
		private boolean prop2;
		private float prop3;
		public String getProp1() {
			return prop1;
		}
		public void setProp1(String prop1) {
			this.prop1 = prop1;
		}
		public boolean isProp2() {
			return prop2;
		}
		public void setProp2(boolean prop2) {
			this.prop2 = prop2;
		}
		public float getProp3() {
			return prop3;
		}
		public void setProp3(float prop3) {
			this.prop3 = prop3;
		}
	}
}
