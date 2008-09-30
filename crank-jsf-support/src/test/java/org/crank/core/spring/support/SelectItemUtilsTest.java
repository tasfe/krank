package org.crank.core.spring.support;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.testng.annotations.Test;

import junit.framework.TestCase;

public class SelectItemUtilsTest extends TestCase {
	
	class Department {
		private Long id;
		private String name;
		public Department(Long id, String name) {
			super();
			this.id = id;
			this.name = name;
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testIt() {
		SelectItemUtils utils = new SelectItemUtils();
	    List<Department> list = new ArrayList<Department>();
	    list.add(new Department(1l, "Foo Bar"));
		List<SelectItem> createSelectItems = utils.createSelectItems(list);
		assertEquals("Foo Bar", createSelectItems.get(0).getLabel());
		assertEquals("1", createSelectItems.get(0).getValue());
	}

}
