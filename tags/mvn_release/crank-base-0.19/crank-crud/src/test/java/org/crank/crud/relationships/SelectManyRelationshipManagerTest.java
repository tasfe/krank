package org.crank.crud.relationships;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


import junit.framework.TestCase;

public class SelectManyRelationshipManagerTest extends TestCase {
	
	private SelectManyRelationshipManager manager;
	private UserMock user;
	private RoleMock suRole = new RoleMock(1L, "su");
	
	
	@BeforeMethod
	public void setUp () {
		manager = new SelectManyRelationshipManager();
		user = new UserMock();
		manager.setParentObject(user);
		manager.setChildCollectionProperty("roles");
		manager.setAddToParentMethodName("addRole");
		manager.setRemoveFromParentMethodName("removeRole");
		manager.setLabelProperty("name");
		manager.setIdProperty("id");
		
	}

	@Test
	public void testProcess() {
		List<RoleMock> list = new ArrayList<RoleMock>();
		list.add(new RoleMock(1L, "su"));
		list.add(new RoleMock(2L, "foo"));
		manager.process(new LinkedHashSet<Object>(list), new LinkedHashSet<Object>());
		
		assertEquals(2, user.getRoles().size());
		assertEquals("su, foo", manager.getCollectionLabelString());
	}

	@Test
	public void testProcessComplex() {
		user.addRole(new RoleMock(1L, "su"));
		
		List<RoleMock> selectedList = new ArrayList<RoleMock>();
		selectedList.add(new RoleMock(1L, "su"));
		
		List<RoleMock> window = new ArrayList<RoleMock>();
		window.add(new RoleMock(1L, "su"));
		window.add(new RoleMock(2L, "bar"));

		
		manager.process(new LinkedHashSet<Object>(selectedList), new LinkedHashSet<Object>(window));
		
		assertEquals(1, user.getRoles().size());
		assertEquals("su", manager.getCollectionLabelString());
	}

	@Test
	public void testIsSelected() {
		user.addRole(suRole);
		user.addRole(new RoleMock(2L, "admin"));

		assertTrue(manager.isSelected(suRole));
		RoleMock someRole = new RoleMock(666L, "SomeRole");
		assertFalse(manager.isSelected(someRole));
	}
	
	@Test
	public void testGetCollectionLabelString() {
		user.addRole(suRole);
		user.addRole(new RoleMock(2L, "admin"));
		
		assertEquals("su, admin", manager.getCollectionLabelString());
	}

	@Test
	public void testGetCollectionLabelOneItem() {
		user.addRole(new RoleMock(2L, "a"));
		
		assertEquals("a", manager.getCollectionLabelString());
	}
	
	@Test
	public void testGetCollectionLabelNoItems() {
		
		assertEquals("", manager.getCollectionLabelString());
	}

	public static class UserMock {
		private List<RoleMock> roles = new ArrayList<RoleMock>();
		

		public List<RoleMock> getRoles() {
			return roles;
		}

		public void setRoles(List<RoleMock> roles) {
			this.roles = roles;
		}
		
		public void addRole(RoleMock role) {
			roles.add(role);
		}
		
		public void removeRole(RoleMock role) {
			roles.remove(role);
		}
	}
	public static class RoleMock {
		private Long id;
		private String name;
		public Long getId() {
			return id;
		}
		public RoleMock(Long id, String name) {
			super();
			this.id = id;
			this.name = name;
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
		
		public boolean equals(Object other) {
			if (!(other instanceof RoleMock)) {
				return false;
			}
			RoleMock otherRole = (RoleMock)other;
			if (otherRole.id!=null && this.id  == null) {
				return false;
			}
			if (this.id !=null && otherRole.id == null) {
				return false;
			}
			
			if (this.id != null) {
				return this.id.equals(otherRole.id);
			} else {
				return this.name.equals(otherRole.name);
			}
			
		}
		
		public int hashCode() {
			return ("" + id + ":" + name).hashCode();
		}
	
	}
	

}
