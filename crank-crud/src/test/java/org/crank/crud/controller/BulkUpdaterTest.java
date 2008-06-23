package org.crank.crud.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.crank.crud.GenericDao;

import junit.framework.TestCase;
import static org.easymock.EasyMock.*;

public class BulkUpdaterTest extends TestCase {
	private BulkUpdaterController bulkUpdater;
	private List<Employee> employees = null;
	
	enum EmployeeType  {
		HOURLY, SALARY;
	}
	class Employee {
		String firstName;
		Date dob;
		EmployeeType type;
		short age;
		int empId;
		Float avg;
		
		Employee(String firstName, Date dob, EmployeeType type, short age, int empId, Float avg) {
			this.firstName = firstName;
			this.dob = dob;
			this.type = type;
			this.age = age;
			this.empId = empId;
			this.avg = avg;
		}
		public String getFirstName() {
			return firstName;
		}
		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}
		public Date getDob() {
			return dob;
		}
		public void setDob(Date dob) {
			this.dob = dob;
		}
		public EmployeeType getType() {
			return type;
		}
		public void setType(EmployeeType type) {
			this.type = type;
		}
		public short getAge() {
			return age;
		}
		public void setAge(short age) {
			this.age = age;
		}
		public int getEmpId() {
			return empId;
		}
		public void setEmpId(int empId) {
			this.empId = empId;
		}
		public Float getAvg() {
			return avg;
		}
		public void setAvg(Float avg) {
			this.avg = avg;
		}
		
		
	}
	
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
		bulkUpdater.getProperties().put("firstName", "badBoy");
		bulkUpdater.getProperties().put("type", EmployeeType.SALARY.toString());
		bulkUpdater.getProperties().put("age", "10");
		bulkUpdater.getProperties().put("empId", "11");
		bulkUpdater.getProperties().put("avg", "1.7f");


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
