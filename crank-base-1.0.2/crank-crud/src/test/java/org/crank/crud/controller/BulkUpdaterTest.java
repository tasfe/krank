package org.crank.crud.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.crank.crud.GenericDao;
import junit.framework.TestCase;
import static org.easymock.EasyMock.*;

@SuppressWarnings("unchecked")
public class BulkUpdaterTest extends TestCase {
	private BulkUpdaterController<Employee> bulkUpdater;
	private List<Employee> employees = null;
	
	
	public void setUp() {
		employees = new ArrayList<Employee>();
		employees.add(new Employee("Rick", new Date(), EmployeeType.HOURLY, (short)5, 5, 1.1f));
		
		bulkUpdater = new BulkUpdaterController<Employee>();
		bulkUpdater.setType(Employee.class);
		EntityLocator el = createMock(EntityLocator.class);
		expect(el.getSelectedEntities()).andReturn(employees);
		replay(el);
		bulkUpdater.setEntityLocator(el);
		GenericDao repo = createMock(GenericDao.class);
		bulkUpdater.setRepo(repo);
	}
	public void testProcess() {
		bulkUpdater.getPrototype().setFirstName("badBoy");
		bulkUpdater.getPrototype().setType(EmployeeType.SALARY);
		bulkUpdater.getPrototype().setAge((short)10);
		bulkUpdater.getPrototype().setEmpId(11);
		bulkUpdater.getPrototype().setAvg(1.7f);
        
        bulkUpdater.getUseProperties().put("firstName", true);
		bulkUpdater.getUseProperties().put("type", true);
		bulkUpdater.getUseProperties().put("age", true);
		bulkUpdater.getUseProperties().put("empId", true);
		bulkUpdater.getUseProperties().put("avg", true);

        bulkUpdater.process();
		assertEquals("badBoy", employees.get(0).getFirstName());
		assertEquals(EmployeeType.SALARY, employees.get(0).getType());
		assertEquals(10, employees.get(0).getAge());
		assertEquals(11, employees.get(0).getEmpId());
		assertEquals(1.7f, employees.get(0).getAvg());
	}
	

}
