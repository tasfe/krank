package org.crank.crud;

import static org.crank.crud.criteria.Example.*;
import static org.crank.crud.criteria.Comparison.*;
import static org.crank.crud.join.Fetch.*;
import static org.crank.crud.criteria.OrderBy.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.crank.crud.criteria.Comparison;
import org.crank.crud.criteria.Criterion;
import org.crank.crud.criteria.Group;
import org.crank.crud.test.DbUnitTestBase;
import org.crank.crud.test.dao.EmployeeDAO;
import org.crank.crud.test.model.Department;
import org.crank.crud.test.model.Employee;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;


/**
*
*  @version $Revision:$
*  @author Rick Hightower
*/
public class GenericDaoJpaTest extends DbUnitTestBase {
    private GenericDao<Employee, Long> employeeDao;
    private GenericDao<Department, Long> departmentDao;

    @Override
    public String getDataSetXml() {
        return "data/Employee.xml";
    }


    @Test
    public void testDeleteObject() throws Exception {
        Employee employee = (Employee) employeeDao.read( 1L );
        AssertJUnit.assertNotNull( employee );
        AssertJUnit.assertEquals( ( (Employee) employee ).getId(), new Long( 1 ) );
        employeeDao.delete( 1L );
        employee = (Employee) employeeDao.read( 1L );
        AssertJUnit.assertNull( employee );
        initPersistenceStuff();
    }


    @Test
    public void testFetchWithOrderBy() throws Exception {
        Employee employee = new Employee();
        employee.setFirstName( "Rick" );
        employeeDao.update( employee );
        Map<String, Object> params = new HashMap<String, Object>();
        List employees = employeeDao.find( params, new String[] { "firstName" });
        AssertJUnit.assertEquals( ( (Employee) employees.get( 0 ) ).getFirstName(), "Bob" );
        AssertJUnit.assertEquals( ( (Employee) employees.get( 1 ) ).getFirstName(), "Carlos" );
        initPersistenceStuff();
    }


    @Test
    public void testGetObject() {
        Employee employee = employeeDao.read( new Long(1) );
        AssertJUnit.assertNotNull( employee );
        AssertJUnit.assertEquals( employee.getId(), new Long( 1 ) );
        AssertJUnit.assertEquals(  "Rick", employee.getFirstName() );
    }

    @Test
    public void testGetObjects() {
        List employees = employeeDao.find( );
        AssertJUnit.assertNotNull( employees );
        AssertJUnit.assertEquals(9, employees.size() );
    }

    @Test
    public void testGetUpdateObjects() throws Exception {
        List<Employee> employees = employeeDao.find(  );
        AssertJUnit.assertNotNull( employees );
        AssertJUnit.assertEquals(9, employees.size() );
        for (Employee employee : employees) {
            employee.setFirstName( employee.getFirstName() + "Gak" );
        }
        for (Employee employee : employees) {
            employeeDao.update( employee );
        }

        AssertJUnit.assertNotNull( employees );
        AssertJUnit.assertEquals(9, employees.size());
        for (Employee employee : employees) {
            AssertJUnit.assertTrue( employee.getFirstName().contains( "Gak" ) );
        }
        initPersistenceStuff();
    }

    @Test
    public void testParameterQuery() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put( "firstName", "Rick" );
        List employees = employeeDao.find( params );
        AssertJUnit.assertNotNull( employees );
        AssertJUnit.assertTrue( employees.size() > 0 );
    }

    @Test
    public void testSingleParameterQuery() {
        List employees = employeeDao.find( "firstName", "Rick" );
        AssertJUnit.assertNotNull( employees );
        AssertJUnit.assertTrue( employees.size() > 0 );
    }
    

    @Test
    public void testFinderSupport () {
    	EmployeeDAO employeeDAO = (EmployeeDAO) this.employeeDao;
    	List<Employee> employees = employeeDAO.findEmployeesByDepartment("Engineering");
    	AssertJUnit.assertTrue(employees.size() > 0);
    	
    }

    @Test
    public void testFindRelatedField () {
    	EmployeeDAO employeeDAO = (EmployeeDAO) this.employeeDao;
    	List<Employee> employees = employeeDAO.find("department.name", "Engineering");
    	AssertJUnit.assertTrue(employees.size() > 0);
    }

    @Test 
    public void constructQueryString () {
    	GenericDaoJpa<Employee, Long> dao = new GenericDaoJpa<Employee, Long>();
		Group group = and(
				eq("firstName", "Rick"), eq("lastName", "Hightower"), 
				or(
					eq("foo", "bar"), eq("baz", "foo")
				)
			  );    	
    	String string = dao.constructWhereClauseString(group, false);
    	AssertJUnit.assertEquals(" WHERE  o.firstName = :firstName  AND  o.lastName = :lastName  AND  (  o.foo = :foo  OR  o.baz = :baz  ) ", string);

		group = and(
				eq("firstName", "Rick"), eq("lastName", "Hightower"), 
				or(
					eq("foo", "bar")
				)
			  );    	
    	string = dao.constructWhereClauseString(group, false);
    	AssertJUnit.assertEquals(" WHERE  o.firstName = :firstName  AND  o.lastName = :lastName  AND  (  o.foo = :foo  ) ", string);

		group = and( eq("firstName", "Rick") );    	
    	string = dao.constructWhereClauseString(group, false);
    	AssertJUnit.assertEquals(" WHERE  o.firstName = :firstName ", string);
    	
		group = and( between("age", 1, 100) );    	
    	string = dao.constructWhereClauseString(group, false);
    	AssertJUnit.assertEquals(" WHERE  o.age between :age1 and :age2 ", string);

		group = and(Employee.class, between("age", 1, 100) );    	
    	string = dao.constructWhereClauseString(group, false);
    	AssertJUnit.assertEquals(" WHERE  o.age between :age1 and :age2 ", string);

    	Employee employee = new Employee();
    	employee.setFirstName("Ric");
    	employee.setAge(0);
    	Department department = new Department();
    	department.setName("Eng");
    	employee.setDepartment(department);
    	group = like(employee);
    	string = dao.constructWhereClauseString(group, false);
    	AssertJUnit.assertEquals(
    			" WHERE  o.active = :active  AND  (  o.department.name like :department_name  )  AND  o.firstName like :firstName ", 
    			string);
    	
    }
    
    @Test 
    public void testFindByCriteria () {
    	
    	List<Employee> employees = employeeDao.find(
    								eq("department.name", "Engineering")
    							   );
    	AssertJUnit.assertTrue(employees.size() > 0);

    	employees = employeeDao.find(orderBy("firstName", 
    			                            "department.name"),
    					eq("department.name", "Engineering"),
    					or(
    							startsLike("firstName", "Rick")
    					)
    				);
    	AssertJUnit.assertTrue(employees.size() > 0);
    	
    	employees = employeeDao.searchOrdered(
    			and(
				eq("department.name", "Engineering"),
				or(
						startsLike("firstName", "Rick")
				)), "firstName"
			);
    	AssertJUnit.assertTrue(employees.size() > 0);
	
    	employees = employeeDao.find(
    				or (
    					eq("department.name", "Engineering"), like("firstName", "Ri")
    				)
    			);
    	AssertJUnit.assertTrue(employees.size() > 0);

    	employees = employeeDao.find(
				or (
					in("age", 1, 2, 3, 4, 5, 6, 40)
				)
			);
    	AssertJUnit.assertTrue(employees.size() > 0);
    	
        List<Criterion> criteria = new ArrayList<Criterion>();
        criteria.add( eq("department.name", "Engineering") );
        employees = employeeDao.find( criteria, orderBy("firstName") );
        AssertJUnit.assertTrue( employees.size() > 0 );
    }

    @Test 
    public void testBetween () {
    	
    	List<Employee> employees = employeeDao.find(
    								between("age", 1, 100)
    							   );
    	AssertJUnit.assertTrue(employees.size() > 0);
    }
    
    @Test 
    public void testQBE () {

		Employee employee = new Employee();
		employee.setActive(true);
		employee.setAge(40);
		employee.setFirstName("Rick");
		employee.setLastName("Rick");
		
    	
    	List<Employee> employees = employeeDao.find(
    			like(employee).excludeProperty("lastName")
    							   );
    	AssertJUnit.assertTrue(employees.size() > 0);

    	
    	employee = new Employee();
    	employee.setFirstName("Ric");
    	employee.setAge(0);
    	employee.setActive(true);
    	Department department = new Department();
    	department.setName("Eng");
    	employee.setDepartment(department);
    	employees = employeeDao.find(like(employee));
    	AssertJUnit.assertTrue(employees.size() > 0);
    	
    }
    
    @Test
    public void testQueryBuildingNullParameter() {
        GenericDaoJpa<Employee, Long> localDao = new GenericDaoJpa<Employee, Long>();
        
        String gimp = localDao.constructWhereClauseString(and(eq("gimp", null)), true);
        AssertJUnit.assertEquals(" WHERE  o.gimp is null ", gimp);
    }
    
    @Test
    public void testNullParameterQueryExecution() {
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put( "lastName", null );
        List<Employee> result = employeeDao.find( attributes );
        AssertJUnit.assertEquals( 6, result.size() );
    }

    @Test
    public void testFetch() {
        List<Employee> result = employeeDao.find( join(joinFetch("department")), orderBy("firstName"), and() );
        AssertJUnit.assertEquals( 9, result.size());
        result = employeeDao.find( join(leftJoinFetch("department")), orderBy("firstName"), and() );
        AssertJUnit.assertEquals( 9, result.size());
        
    }
    
    @Test
    public void testFetchWithCriteria(){
    	List<Employee> result = employeeDao.find( join(joinFetch("department","dpt")), orderBy("firstName"), and(eq("dpt.name",true,"Engineering")) );
        AssertJUnit.assertEquals( 3, result.size());
    }

    @Test
    public void testReadFully() {
    	Employee employee = employeeDao.readPopulated(1L);
    	AssertJUnit.assertNotNull( employee );
    	
    	Department dept = (Department) departmentDao.readPopulated(1L);
    	AssertJUnit.assertNotNull( dept );
    }

    @Test
    public void testPaginate() {
    	int startPosition = 1;
    	int maxResults = 2;
    	List<Employee> employees = employeeDao.find(startPosition, maxResults);
    	AssertJUnit.assertNotNull( employees );
    	AssertJUnit.assertEquals(2, employees.size());
    	
    }

    @Test
    public void testOrderBy() {
    	List<Employee> employees = employeeDao.find(orderBy(asc("firstName")));
    	AssertJUnit.assertNotNull(employees);
    	AssertJUnit.assertEquals("Bob", employees.get(0).getFirstName());

    	employees = employeeDao.find(orderBy(desc("firstName")));
    	AssertJUnit.assertNotNull(employees);
    	AssertJUnit.assertEquals("Scott", employees.get(0).getFirstName());

    }
    
    @Test 
    public void testIn () {
    	//EmployeeDAO employeeDAO = (EmployeeDAO) this.employeeDao;
    	List<Long> ids = new ArrayList<Long>();
    	ids.add(1L);
    	ids.add(2L);
    	//List<Employee> employees = employeeDAO.findInEmployeeIds(ids);
    	//List<Employee> employees = employeeDAO.findInEmployeeIds(new Long[] {1L, 2L});
    	//List<Employee> employees = employeeDao.find(Comparison.in("id", (Object) new Long [] {1L,2L}));
    	//Seems like named param works but positional does not.
    	List<Employee> employees = employeeDao.find(Comparison.in("id", ids));
    	AssertJUnit.assertNotNull(employees);
    }

    @Test
    public void testEagerNplusOne() {
//    	List<Department> depts = departmentDao.find();
//    	AssertJUnit.assertNotNull(depts);
    	
//    	Department department = departmentDao.read(1L);
//    	AssertJUnit.assertNotNull(department);
    	
    	List<Department> depts = departmentDao.find(leftJoinFetch("employees"));
    	AssertJUnit.assertNotNull(depts);
    }
    
    @Test
    public void testEmployees() {
        employeeDao.find();
    }

    public void setEmployeeDao( final GenericDao<Employee, Long> baseJpaDao ) {
        this.employeeDao = baseJpaDao;
    }


	public void setDepartmentDao(GenericDao<Department, Long> departmentDao) {
		this.departmentDao = departmentDao;
	}

}
