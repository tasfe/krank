package org.crank.core;

import java.util.ArrayList;
import java.util.Collection;
import static org.testng.AssertJUnit.*;

import org.testng.annotations.Test;

public class MapUtilsTest {
	@SuppressWarnings("unchecked")
	@Test
	public void testConvertToMap() {
		Collection<TestSupport> items = new ArrayList<TestSupport>();
		items.add(new TestSupport(5, "number five"));
		items.add(new TestSupport(1, "number one"));
		items.add(new TestSupport(3, "number three"));

		{
			java.util.Map<Integer, TestSupport> beanLookupMap = MapUtils
					.convertListToMap("id", items);

			assertEquals("number five", beanLookupMap.get(5).getName());
			assertEquals("number one", beanLookupMap.get(1).getName());
			assertEquals("number three", beanLookupMap.get(3).getName());
		}
		{
			java.util.Map<String, TestSupport> beanLookupMap = MapUtils
					.convertListToMap("name", items);
			assertEquals(5, beanLookupMap.get("number five").getId());
			assertEquals(1, beanLookupMap.get("number one").getId());
			assertEquals(3, beanLookupMap.get("number three").getId());
		}
		{
			java.util.Map<Integer, TestSupport> beanLookupMap = MapUtils
					.convertArrayToMap("id", items.toArray());

			assertEquals("number five", beanLookupMap.get(5).getName());
			assertEquals("number one", beanLookupMap.get(1).getName());
			assertEquals("number three", beanLookupMap.get(3).getName());
		}
		
	}
	


	class TestSupport {
		int id;
		String name;

		TestSupport(int id, String name) {
			this.id = id;
			this.name = name;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}
