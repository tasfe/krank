package org.crank.crud;

import org.crank.crud.criteria.Comparison;
import org.crank.crud.criteria.Select;
import org.crank.crud.join.Join;
import org.crank.crud.test.DbUnitTestBase;
import org.crank.crud.test.model.Department;
import org.crank.crud.test.model.Employee;
import org.crank.crud.test.model.PetClinicInquiry;
import org.crank.crud.test.model.PetClinicLead;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.List;
import static org.testng.AssertJUnit.fail;
import static org.testng.AssertJUnit.assertEquals;

/**
 * 
 * @version $Revision$
 * @author Rick Hightower
 */
public class GenericDaoJpa2Test extends DbUnitTestBase {
	private GenericDao<Employee, Long> employeeDao;
	private GenericDao<Department, Long> departmentDao;


	private GenericDao<PetClinicInquiry, Long> petClinicInquiryDao;

	private GenericDao<PetClinicLead, Long> petClinicLeadDao;

	
	private EmployeeDataCreationUtility creationUtility = new EmployeeDataCreationUtility();
	private List<Employee> testEmployees;

	@Override
	public String getDataSetXml() {
		return "data/Employee.xml";
	}
	
	@BeforeClass (dependsOnGroups={"initPersist"})
	public void setupEmployeeData() {
		creationUtility.init(employeeDao, departmentDao);
		creationUtility.setupEmployeeData();
		testEmployees = creationUtility.getTestEmployees();
	}

	@AfterClass 
	public void deleteTestEmployeeData() {
		try {
			employeeDao.delete(testEmployees);
		} catch(Exception ex) {
			
		}
	}
    
	@Test (groups="reads")
    public void testFailure() {
		long time = System.currentTimeMillis();
		PetClinicInquiry inquiry = new PetClinicInquiry();
		inquiry.setName("inq1");
		inquiry.setBb("ricky" + time);
		this.petClinicInquiryDao.store(inquiry);
		
		PetClinicLead lead = new PetClinicLead();
		lead.setInquiry(inquiry);
		
		this.petClinicLeadDao.store(lead);
		try {
			this.petClinicLeadDao.find(Comparison.eq("inquiry.bb", "ricky" + time));
			fail();
		} catch (Exception ex) {
			/* Unable to run query : SELECT  o 
			 * FROM PetClinicLead o  WHERE  o.inquiry.bb = :inquiry_bb */
			/* java.sql.SQLException: 
			 * Column not found: INQUIRY1_2_.BB in statement
			 * SQL 
			 * select petclinicl0_.id as id4_, petclinicl0_1_.name as name4_, 
			 * petclinicl0_1_.INQUIRY_ID as INQUIRY3_4_ 
			 * from PetClinicLead petclinicl0_ 
			 * inner join BASE_LEAD petclinicl0_1_ 
			 * on petclinicl0_.id=petclinicl0_1_.id, 
			 * BASE_INQUIRY inquiry1_ 
			 * where petclinicl0_1_.INQUIRY_ID=inquiry1_.id 
			 * and inquiry1_2_.bb=?] */			
		}
		
		this.petClinicLeadDao.delete(lead);
		this.petClinicInquiryDao.delete(inquiry);
		
		
    }

	@Test (groups="reads")
    public void testSuccess() {
		long time = System.currentTimeMillis();
		PetClinicInquiry inquiry = new PetClinicInquiry();
		inquiry.setName("inq1");
		inquiry.setBb("ticky" + time);
		petClinicInquiryDao.store(inquiry);
		
		PetClinicLead lead = new PetClinicLead();
		lead.setInquiry(inquiry);
		lead.setName("golden boy" + time);
		
		petClinicLeadDao.store(lead);
		
		List<Object[]> results = petClinicLeadDao.find(Select.select(Select.select("inquiry")), 
					Join.join(Join.entityJoin("PetClinicInquiry", "inquiry")),
					null,
					0,
					1000,
					Comparison.eq("inquiry.bb", true, "ticky" + time));
		
		Object[] objects = results.get(0);
		lead = (PetClinicLead) objects[0];
		inquiry = (PetClinicInquiry) objects[1];
		assertEquals("golden boy" + time, lead.getName());
		assertEquals("inq1", inquiry.getName());
		
		//SELECT  o, inquiry FROM PetClinicLead o, PetClinicInquiry inquiry WHERE  inquiry.bb = :inquiry_bb 
		petClinicLeadDao.delete(lead);
		petClinicInquiryDao.delete(inquiry);
		
		
    }
	
	@Test (groups="updates")
    public void testUpdateWithStore() {
		Employee employee = testEmployees.get(0);
		employee.setFirstName("monkey");
		employeeDao.store(employee);
		employee = employeeDao.read(employee.getId());
		assert employee.getFirstName().equals("monkey");
	}
	
	@Test (groups="updates")
    public void testCreateEmployeeWithStore() {
		Employee employee = new Employee();
		employee.setLastName("boy");
		employee.setFirstName("monkey");
		employeeDao.store(employee);
		employee = employeeDao.read(employee.getId());
		assert employee.getFirstName().equals("monkey");
		assert employee.getLastName().equals("boy");
	}



//    @Test (groups="updates")
//	public void testUpdateSupport() {
//        System.out.println("WE GOT THIS FAR ----------------------------------- 1");
//        EmployeeDAO employeeDAO = (EmployeeDAO) this.employeeDao;
//		List<Employee> employees = employeeDAO
//				.findEmployeesByDepartment("Engineering");
//		AssertJUnit.assertTrue(employees.size() > 0);
//
//        System.out.println("WE GOT THIS FAR ----------------------------------- 2");
//
//        try {
//            long size = employees.size();
//
//            long recordsEffected = employeeDAO
//                .updateEmployeeActiveStatusByDepartment("Engineering", true);
//            assertEquals(size, recordsEffected);
//
//        }catch (Exception ex) {
//            ex.printStackTrace();
//            System.out.println("WE GOT THIS FAR ----------------------------------- 3.5");
//        }
//
//        System.out.println("WE GOT THIS FAR ----------------------------------- 3");
//
//
//    }

//    @Test (groups="updates")
//	public void testDeleteSupport() {
//		EmployeeDAO employeeDAO = (EmployeeDAO) this.employeeDao;
//		List<Employee> employees = employeeDAO
//				.findEmployeesByDepartment("Engineering");
//		AssertJUnit.assertTrue(employees.size() > 0);
//
//        employeeDAO
//                .deleteEmployeesByDepartment("Engineering");
//        employees = employeeDAO
//				.findEmployeesByDepartment("Engineering");
//        AssertJUnit.assertTrue(employees.size() == 0);
//
//    }

	
    public void setEmployeeDao(final GenericDao<Employee, Long> baseJpaDao) {
		this.employeeDao = baseJpaDao;
	}

	public void setDepartmentDao(GenericDao<Department, Long> departmentDao) {
		this.departmentDao = departmentDao;
	}


	public void setPetClinicInquiryDao(
			GenericDao<PetClinicInquiry, Long> petClinicInquiryDao) {
		this.petClinicInquiryDao = petClinicInquiryDao;
	}


	public void setPetClinicLeadDao(GenericDao<PetClinicLead, Long> petClinicLeadDao) {
		this.petClinicLeadDao = petClinicLeadDao;
	}
	
}
