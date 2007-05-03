package org.crank.crud;

import static org.crank.crud.criteria.Example.*;
import static org.crank.crud.criteria.Comparison.*;
//import static org.crank.crud.criteria.Group.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    private GenericDao<Employee, Long> genericDao;

    @Override
    public String getDataSetXml() {
        return "data/Employee.xml";
    }


    @Test
    public void testDeleteObject() throws Exception {
        Employee employee = (Employee) genericDao.read( 1L );
        AssertJUnit.assertNotNull( employee );
        AssertJUnit.assertEquals( ( (Employee) employee ).getId(), new Long( 1 ) );
        genericDao.delete( 1L );
        employee = (Employee) genericDao.read( 1L );
        AssertJUnit.assertNull( employee );
        initPersistenceStuff();
    }


    @Test
    public void testFetchWithOrderBy() throws Exception {
        Employee employee = new Employee();
        employee.setFirstName( "Rick" );
        genericDao.update( employee );
        Map<String, Object> params = new HashMap<String, Object>();
        List employees = genericDao.find( params, new String[] { "firstName" });
        AssertJUnit.assertEquals( ( (Employee) employees.get( 0 ) ).getFirstName(), "Chris" );
        AssertJUnit.assertEquals( ( (Employee) employees.get( 1 ) ).getFirstName(), "Rick" );
        initPersistenceStuff();
    }


    @Test
    public void testGetObject() {
        Employee employee = genericDao.read( new Long(1) );
        AssertJUnit.assertNotNull( employee );
        AssertJUnit.assertEquals( employee.getId(), new Long( 1 ) );
        AssertJUnit.assertEquals(  "Rick", employee.getFirstName() );
    }

    @Test
    public void testGetObjects() {
        List employees = genericDao.find( );
        AssertJUnit.assertNotNull( employees );
        AssertJUnit.assertEquals(3, employees.size() );
    }

    @Test
    public void testGetUpdateObjects() throws Exception {
        List<Employee> employees = genericDao.find(  );
        AssertJUnit.assertNotNull( employees );
        AssertJUnit.assertEquals(3, employees.size() );
        for (Employee employee : employees) {
            employee.setFirstName( employee.getFirstName() + "Gak" );
        }
        for (Employee employee : employees) {
            genericDao.update( employee );
        }

        AssertJUnit.assertNotNull( employees );
        AssertJUnit.assertTrue( employees.size() == 3 );
        for (Employee employee : employees) {
            AssertJUnit.assertTrue( employee.getFirstName().contains( "Gak" ) );
        }
        initPersistenceStuff();
    }

    @Test
    public void testParameterQuery() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put( "firstName", "Rick" );
        List employees = genericDao.find( params );
        AssertJUnit.assertNotNull( employees );
        AssertJUnit.assertTrue( employees.size() > 0 );
    }

    @Test
    public void testSingleParameterQuery() {
        List employees = genericDao.find( "firstName", "Rick" );
        AssertJUnit.assertNotNull( employees );
        AssertJUnit.assertTrue( employees.size() > 0 );
    }
    

    @Test
    public void testFinderSupport () {
    	EmployeeDAO employeeDAO = (EmployeeDAO) this.genericDao;
    	List<Employee> employees = employeeDAO.findEmployeesByDepartment("Engineering");
    	AssertJUnit.assertTrue(employees.size() > 0);
    	
    }

    @Test
    public void testFindRelatedField () {
    	EmployeeDAO employeeDAO = (EmployeeDAO) this.genericDao;
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
    	
    	List<Employee> employees = genericDao.find(
    								eq("department.name", "Engineering")
    							   );
    	AssertJUnit.assertTrue(employees.size() > 0);

    	employees = genericDao.find(orderBy("firstName", 
    			                            "department.name"),
    					eq("department.name", "Engineering"),
    					or(
    							startsLike("firstName", "Rick")
    					)
    				);
    	AssertJUnit.assertTrue(employees.size() > 0);
    	
    	employees = genericDao.searchOrdered(
    			and(
				eq("department.name", "Engineering"),
				or(
						startsLike("firstName", "Rick")
				)), "firstName"
			);
    	AssertJUnit.assertTrue(employees.size() > 0);
	
    	employees = genericDao.find(
    				or (
    					eq("department.name", "Engineering"), like("firstName", "Ri")
    				)
    			);
    	AssertJUnit.assertTrue(employees.size() > 0);

    	employees = genericDao.find(
				or (
					in("age", 1, 2, 3, 4, 5, 6, 40)
				)
			);
    	AssertJUnit.assertTrue(employees.size() > 0);
    	
    }

    @Test 
    public void testBetween () {
    	
    	List<Employee> employees = genericDao.find(
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
		
    	
    	List<Employee> employees = genericDao.find(
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
    	employees = genericDao.find(like(employee));
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
        List<Employee> result = genericDao.find( attributes );
        AssertJUnit.assertEquals( result.size(), 2 );
    }

    public void setGenericDao( final GenericDao<Employee, Long> baseJpaDao ) {
        this.genericDao = baseJpaDao;
    }

}
