package org.crank.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import org.crank.core.CrankException;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;

public class TreeModelBuilderTest {
	class UserObject {
		String name;
		Object data;
		public UserObject(String name) {
			this.name = name;
		}
		public UserObject(String name, Object data) {
			this.data = data;
			this.name = name;
		}
	}
	class TestTreeModelBuilder extends AbstractTreeModelBuilder<DefaultTreeModel,DefaultMutableTreeNode> {

		@Override
		protected void addToNode(DefaultMutableTreeNode parent, DefaultMutableTreeNode child) {
			parent.add(child);			
		}

		@Override
		protected DefaultMutableTreeNode createFolder(String name) {
			DefaultMutableTreeNode mtn =  new DefaultMutableTreeNode(new UserObject(name));
			mtn.setAllowsChildren(true);
			return mtn;
		}

		@Override
		protected DefaultMutableTreeNode createNode(String name, Object data) {
			DefaultMutableTreeNode dmtn = new DefaultMutableTreeNode(new UserObject(name, data));
			dmtn.setAllowsChildren(true);
			return dmtn;
		}

		@Override
		protected DefaultMutableTreeNode createRoot(String name) {
			DefaultMutableTreeNode mtn =  new DefaultMutableTreeNode(new UserObject(name));
			mtn.setAllowsChildren(true);
			return mtn;
		}

		@Override
		protected DefaultTreeModel createTreeModel(Object root) {
			return new DefaultTreeModel((TreeNode)root);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testEmptyList() {
		TestTreeModelBuilder builder = new TestTreeModelBuilder();
		try {
			builder.createTreeModelFromList(new ArrayList());
			assertTrue(false);
		} catch (CrankException e) {
			// expected; we haven't supplied any build directions
		}	
		
		builder.setTreeBuildDirections("Departments->this.name->employees.name");
		try {
			builder.createTreeModelFromList(null);
			assertTrue(false);
		} catch (CrankException e) {
			// expected; we haven't supplied any build directions
		}		
		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testCreateTreeModelFromList() {
		TestTreeModelBuilder builder = new TestTreeModelBuilder();
		builder.setTreeBuildDirections("Departments->this.name->employees.name,description");
		List model = new ArrayList();
		model.add(
				new TestSupport(
						"deptA", 
						Arrays.asList(new TestSupport[]{
								new TestSupport("employee one", "good"),
								new TestSupport("employee two", "really good"),
								new TestSupport("employee three", "average")
						})));
		DefaultTreeModel dtm = builder.createTreeModelFromList(model);
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)dtm.getRoot();
	    UserObject rootData = (UserObject)root.getUserObject();
	    assertEquals("Departments", rootData.name);
	    DefaultMutableTreeNode deptA = (DefaultMutableTreeNode)root.getChildAt(0);
	    UserObject deptAData = (UserObject) deptA.getUserObject();
	    assertEquals("deptA", deptAData.name);
	    DefaultMutableTreeNode emp1 = (DefaultMutableTreeNode) deptA.getChildAt(0);
	    UserObject emp1Data = (UserObject) emp1.getUserObject();
	    assertEquals("employee one good ", emp1Data.name);
	    DefaultMutableTreeNode emp2 = (DefaultMutableTreeNode) deptA.getChildAt(1);
	    UserObject emp2Data = (UserObject) emp2.getUserObject();
	    assertEquals("employee two really good ", emp2Data.name);
	    DefaultMutableTreeNode emp3 = (DefaultMutableTreeNode) deptA.getChildAt(2);
	    UserObject emp3Data = (UserObject) emp3.getUserObject();
	    assertEquals("employee three average ", emp3Data.name);
	}	
	
	@SuppressWarnings("unchecked")
	@Test
	public void testCreateTreeModelFromListNoRoot() {
		TestTreeModelBuilder builder = new TestTreeModelBuilder();
		builder.setTreeBuildDirections("Departments->this.name->employees.name,description");
		List model = new ArrayList();
		model.add(
				new TestSupport(
						"deptA", 
						Arrays.asList(new TestSupport[]{
								new TestSupport("employee one", "good"),
								new TestSupport("employee two", "really good"),
								new TestSupport("employee three", "average")
						})));
		builder.setNoRoot(true);
		assertTrue(builder.isNoRoot());
		DefaultTreeModel dtm = builder.createTreeModelFromList(model);
	    DefaultMutableTreeNode deptA = (DefaultMutableTreeNode)dtm.getRoot();
	    UserObject deptAData = (UserObject) deptA.getUserObject();
	    assertEquals("deptA", deptAData.name);
	    DefaultMutableTreeNode emp1 = (DefaultMutableTreeNode) deptA.getChildAt(0);
	    UserObject emp1Data = (UserObject) emp1.getUserObject();
	    assertEquals("employee one good ", emp1Data.name);
	    DefaultMutableTreeNode emp2 = (DefaultMutableTreeNode) deptA.getChildAt(1);
	    UserObject emp2Data = (UserObject) emp2.getUserObject();
	    assertEquals("employee two really good ", emp2Data.name);
	    DefaultMutableTreeNode emp3 = (DefaultMutableTreeNode) deptA.getChildAt(2);
	    UserObject emp3Data = (UserObject) emp3.getUserObject();
	    assertEquals("employee three average ", emp3Data.name);
	}		
	
	@SuppressWarnings("unchecked")
	@Test
	public void testCreateTreeModelFromListWithWildcard() {
		TestTreeModelBuilder builder = new TestTreeModelBuilder();
		builder.setTreeBuildDirections("Departments->this.name->employees.name*");
		List model = new ArrayList();
		model.add(
				new TestSupport(
						"deptA", 
						Arrays.asList(new TestSupport[]{
								new TestSupport("employee one", "good"),
								new TestSupport("employee two", "really good"),
								new TestSupport("employee three", "average"),
								new TestSupport(
										"group one",
										Arrays.asList(new TestSupport[]{
												new TestSupport("employee four", "average"),
												new TestSupport("employee five", "average")
										})

								)
						})));
		DefaultTreeModel dtm = builder.createTreeModelFromList(model);
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)dtm.getRoot();
	    UserObject rootData = (UserObject)root.getUserObject();
	    assertEquals("Departments", rootData.name);
	    DefaultMutableTreeNode deptA = (DefaultMutableTreeNode)root.getChildAt(0);
	    UserObject deptAData = (UserObject) deptA.getUserObject();
	    assertEquals("deptA", deptAData.name);
	    DefaultMutableTreeNode emp1 = (DefaultMutableTreeNode) deptA.getChildAt(0);
	    UserObject emp1Data = (UserObject) emp1.getUserObject();
	    assertEquals("employee one", emp1Data.name);
	    DefaultMutableTreeNode emp2 = (DefaultMutableTreeNode) deptA.getChildAt(1);
	    UserObject emp2Data = (UserObject) emp2.getUserObject();
	    assertEquals("employee two", emp2Data.name);
	    DefaultMutableTreeNode emp3 = (DefaultMutableTreeNode) deptA.getChildAt(2);
	    UserObject emp3Data = (UserObject) emp3.getUserObject();
	    assertEquals("employee three", emp3Data.name);
	    DefaultMutableTreeNode group1 = (DefaultMutableTreeNode) deptA.getChildAt(3);
	    UserObject group1Data = (UserObject) group1.getUserObject();
	    assertEquals("group one", group1Data.name);
	    DefaultMutableTreeNode emp4 = (DefaultMutableTreeNode) group1.getChildAt(0);
	    UserObject emp4Data = (UserObject) emp4.getUserObject();
	    assertEquals("employee four", emp4Data.name);
	    DefaultMutableTreeNode emp5 = (DefaultMutableTreeNode) group1.getChildAt(1);
	    UserObject emp5Data = (UserObject) emp5.getUserObject();
	    assertEquals("employee five", emp5Data.name);
	    
	}		
	

	class TestSupport {
		private String name;
		private String description;
		private List<TestSupport> employees = new ArrayList<TestSupport>();
		
		public TestSupport(String name) {
			this.name = name;
		}
		public TestSupport(String name, String description) {
			this.name = name;
			this.description = description;
		}
		public TestSupport(String name, List<TestSupport> employees) {
			this.name = name;
			this.employees.addAll(employees);
		}
		public TestSupport(String name, String description, List<TestSupport> employees) {
			this.name = name;
			this.description = description;
			this.employees.addAll(employees);
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public List<TestSupport> getEmployees() {
			return employees;
		}
		public void setEmployees(List<TestSupport> employees) {
			this.employees = employees;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
	}
}
