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
    public void testQueryRelatedField() {
        GenericDaoJpa<Employee, Long> localDao = new GenericDaoJpa<Employee, Long>();
        Map<String, Object> florp = new HashMap<String, Object>();
        florp.put("foo.bar", "bar");
        String gimp = localDao.constructQueryString( "Glorp", florp, null );
        AssertJUnit.assertEquals( "SELECT o FROM Glorp o  WHERE o.foo.bar = :foo_bar", gimp );
    }

    @Test
    public void testQueryBuildingEmptyParam() {
        GenericDaoJpa<Employee, Long> localDao = new GenericDaoJpa<Employee, Long>();
        Map<String, Object> florp = new HashMap<String, Object>();
        String gimp = localDao.constructQueryString( "Glorp", florp, null );
        AssertJUnit.assertEquals( "SELECT o FROM Glorp o ", gimp );
    }

    @Test
    public void testQueryBuildingSingleParam() {
        GenericDaoJpa<Employee, Long> localDao = new GenericDaoJpa<Employee, Long>();
        Map<String, Object> florp = new HashMap<String, Object>();
        florp.put( "gimp", new Integer( 14 ) );
        String gimp = localDao.constructQueryString( "Glorp", florp, null );
        AssertJUnit.assertEquals( "SELECT o FROM Glorp o  WHERE o.gimp = :gimp", gimp );
    }

    @Test
    public void testQueryBuildingDoubleParam() {
        Map<String, Object> florp = new HashMap<String, Object>();
        GenericDaoJpa<Employee, Long> localDao = new GenericDaoJpa<Employee, Long>();
        florp.put( "gimp", new Integer( 14 ) );
        florp.put( "gomp", new Integer( 14 ) );
        String gimp = localDao.constructQueryString( "Glorp", florp, null );
        AssertJUnit.assertEquals( 0, gimp.indexOf( "SELECT" ) );
        AssertJUnit.assertTrue( gimp.contains( ":gimp" ) );
        AssertJUnit.assertTrue( gimp.contains( ":gomp" ) );
    }

    @Test
    public void testQueryBuildingDoubleParamOrderBy() {
        Map<String, Object> florp = new HashMap<String, Object>();
        GenericDaoJpa<Employee, Long> localDao = new GenericDaoJpa<Employee, Long>();
        florp.put( "gimp", new Integer( 14 ) );
        florp.put( "gomp", new Integer( 14 ) );
        String orderBy[] = new String[] { "gimp" };
        String gimp = localDao.constructQueryString( "Glorp", florp, orderBy );
        AssertJUnit.assertEquals( 0, gimp.indexOf( "SELECT" ) );
        AssertJUnit.assertTrue( gimp.contains( ":gimp" ) );
        AssertJUnit.assertTrue( gimp.contains( ":gomp" ) );
        AssertJUnit.assertTrue( gimp.contains( "ORDER BY gimp" ) );
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
        AssertJUnit.assertTrue( employees.size() == 3 );
    }

    @Test
    public void testGetUpdateObjects() throws Exception {
        List<Employee> employees = genericDao.find(  );
        AssertJUnit.assertNotNull( employees );
        AssertJUnit.assertTrue( employees.size() == 3 );
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
    	String string = dao.constructQueryString(group, false);
    	AssertJUnit.assertEquals(" WHERE  o.firstName = :firstName  AND  o.lastName = :lastName  AND  (  o.foo = :foo  OR  o.baz = :baz  ) ", string);

		group = and(
				eq("firstName", "Rick"), eq("lastName", "Hightower"), 
				or(
					eq("foo", "bar")
				)
			  );    	
    	string = dao.constructQueryString(group, false);
    	AssertJUnit.assertEquals(" WHERE  o.firstName = :firstName  AND  o.lastName = :lastName  AND  (  o.foo = :foo  ) ", string);

		group = and( eq("firstName", "Rick") );    	
    	string = dao.constructQueryString(group, false);
    	AssertJUnit.assertEquals(" WHERE  o.firstName = :firstName ", string);
    	
    }
    
    @Test 
    public void testFindByCriteria () {
    	
    	List<Employee> employees = genericDao.find(
    								eq("department.name", "Engineering")
    							   );
    	AssertJUnit.assertTrue(employees.size() > 0);

    	employees = genericDao.find(
    					eq("department.name", "Engineering"),
    					or(
    							startsLike("firstName", "Rick")
    					)
    				);
    	AssertJUnit.assertTrue(employees.size() > 0);
    	
    	employees = genericDao.find(
    				or (
    					eq("department.name", "Engineering"), like("firstName", "Ri")
    				)
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

    }
    
    public void setGenericDao( final GenericDao<Employee, Long> baseJpaDao ) {
        this.genericDao = baseJpaDao;
    }

}
