package org.crank.crud.controller;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.faces.model.DataModel;
import javax.servlet.http.HttpServletRequest;

import org.crank.core.CollectionUtils;
import org.crank.crud.GenericDao;
import org.crank.crud.controller.CrudControllerBase;
import org.crank.crud.controller.EntityLocator;
import org.crank.crud.controller.Row;
import org.crank.crud.dao.EmployeeDAO;
import org.crank.crud.dao.TagDAO;
import org.crank.crud.jsf.support.JsfSelectManyByIdController;
import org.crank.crud.model.Tag;
import org.crank.jsfspring.test.CrankMockObjects;
import org.crank.test.base.SpringTestNGBase;
import org.crank.test.base.SpringTestingUtility;
import org.crank.web.HttpRequestUtils;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.crank.crud.model.Employee;

import static org.testng.AssertJUnit.*;


public class SelectManyByIdControllerTest extends SpringTestNGBase {
	private JsfSelectManyByIdController<Employee, Tag, Long> tagController;
	private CrankMockObjects crankMockObjects;
	private Collection<Tag> testTags;
	private Employee testEmployee;
	private Employee otherEmployee;
	private DataModel availableTags;

	
	@SuppressWarnings("unchecked")
	private Map<String, GenericDao> repos;
	
	
	
	public void setTagController(JsfSelectManyByIdController<Employee, Tag, Long> controller) {
		this.tagController = controller;
	}
	@Override
	public String getModuleName() {
		return "crankWebExample";
	}
	
	@BeforeClass (groups="setup", dependsOnGroups="class-init")
	public void setupTags(){
		testTags = getTagRepo().store(Arrays.asList(new Tag [] {new Tag("one"), new Tag("two"), new Tag("three")}));
		testEmployee  = getEmployeeRepo().store(new Employee("Rick", "Hightower", 5));
		otherEmployee = getEmployeeRepo().store(new Employee("Foo", "Bar", 5));
		loadForm();
		availableTags = tagController.getAvailableChoices();
	}
	
	@AfterClass (groups="tearDown")
	public void deleteTags() {
		getTagRepo().delete(testTags);
		getEmployeeRepo().delete(testEmployee);
	}

	@Test 
	public void testInitsProperly() {
		assertNotNull("We have the available tags", availableTags);
		assertNotNull("We have the selected tags list", tagController.getSelectedChildren());
		assertEquals("No tags have been selected yet", 0, tagController.getSelectedChildren().size());
		assertEquals("We have 3 tags that are available", 3, availableTags.getRowCount());
		
	}
	@SuppressWarnings("unchecked")
	@Test (dependsOnMethods="testInitsProperly")
	public void testNormalCase() {
		
		/* Select two tags. */
		List<Row> rows = (List<Row>) availableTags.getWrappedData();
		rows.get(0).setSelected(true);
		rows.get(2).setSelected(true);
		
		/* Process the selection. */
		tagController.process();
		
		
		assertTrue(CollectionUtils.valueExists ("name", "one",  tagController.getSelectedChildren()));
		assertFalse(CollectionUtils.valueExists("name", "two",  tagController.getSelectedChildren()));
		assertTrue(CollectionUtils.valueExists ("name", "three",tagController.getSelectedChildren()));
		
		
	}

	@SuppressWarnings("unchecked")
	@Test 
	public void testAlreadyOwned() {
		/* Select two tags. */
		List<Row> rows = (List<Row>) availableTags.getWrappedData();
		rows.get(1).setSelected(true);
 		((Tag)rows.get(1).getObject()).setEmployeeId(otherEmployee.getId());
 		
 		rows.get(2).setSelected(false);
 		rows.get(0).setSelected(false);

		/* Process the selection. */
		tagController.process();
		
		assertFalse(CollectionUtils.valueExists ("name", "one",  tagController.getSelectedChildren()));
		assertTrue(CollectionUtils.valueExists("name", "two",  tagController.getSelectedChildren()));
		assertFalse(CollectionUtils.valueExists ("name", "three",tagController.getSelectedChildren()));
		
	}
	
	@SuppressWarnings("unchecked")
	private void loadForm() {
		crankMockObjects.getExternalContext().getRequestParameterMap().put("id", testEmployee.getId().toString());
		crankMockObjects.getRequest().addParameter("id", testEmployee.getId().toString());
		
		
		assertNotNull(tagController);
		
		CrudControllerBase<Employee, Long> controller = 
			(CrudControllerBase<Employee, Long>) tagController.getParentCrudController();
		controller.setEntityLocator(new EntityLocator<Employee>(){public Employee getEntity() {return null;}
			public List<Employee> getSelectedEntities() {
				// TODO Auto-generated method stub
				return null;
			}});
		tagController.getParentCrudController().read();
		
		
	}
	
	private TagDAO getTagRepo() {
		return (TagDAO) repos.get("Tag");
	}

	private EmployeeDAO getEmployeeRepo() {
		return (EmployeeDAO) repos.get("Employee");
	}
	
	@Override
	public List<String> getConfigLocations() {
		return new ArrayList<String>(Arrays.asList(new String [] {"classpath:applicationContext.xml"}));
	}

	public void setUpSpring() {
    	applicationContext = SpringTestingUtility.getContext( null, getConfigLocations(), contexts, false, getModuleName() );
    	crankMockObjects = new CrankMockObjects();
		try {
			crankMockObjects.setUp();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		crankMockObjects.setUpApplicationContextWithScopes(applicationContext);
        applicationContext.getBeanFactory().autowireBeanProperties( this,
                    AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false );
        
        HttpRequestUtils.setHttpRequest( (HttpServletRequest) crankMockObjects.getExternalContext().getRequest() );        
			
	}
	@SuppressWarnings("unchecked")
	public void setRepos(Map<String, GenericDao> repos) {
		this.repos = repos;
	}
//	public void setCruds(Map<String, JsfCrudAdapter<Employee, Long>> cruds) {
//		this.cruds = cruds;
//	}
	
	
}
