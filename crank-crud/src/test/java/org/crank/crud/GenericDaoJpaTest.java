package org.crank.crud;

import static org.crank.crud.criteria.Comparison.between;
import static org.crank.crud.criteria.Comparison.eq;
import static org.crank.crud.criteria.Comparison.in;
import static org.crank.crud.criteria.Comparison.like;
import static org.crank.crud.criteria.Comparison.startsLike;
import static org.crank.crud.criteria.Example.like;
import static org.crank.crud.criteria.Group.and;
import static org.crank.crud.criteria.Group.or;
import static org.crank.crud.criteria.Group.orderBy;
import static org.crank.crud.criteria.OrderBy.asc;
import static org.crank.crud.criteria.OrderBy.desc;
import static org.crank.crud.criteria.OrderBy.orderBy;
import static org.crank.crud.join.Join.join;
import static org.crank.crud.join.Join.joinFetch;
import static org.crank.crud.join.Join.leftJoinFetch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.crank.crud.criteria.Comparison;
import org.crank.crud.criteria.Criterion;
import org.crank.crud.criteria.Example;
import org.crank.crud.criteria.Group;
import org.crank.crud.join.Join;
import org.crank.crud.join.JoinType;
import org.crank.crud.join.SimpleRelationshipJoin;
import org.crank.crud.test.DbUnitTestBase;
import org.crank.crud.test.dao.EmployeeDAO;
import org.crank.crud.test.model.Department;
import org.crank.crud.test.model.Employee;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

/**
 * 
 * @version $Revision:$
 * @author Rick Hightower
 */
public class GenericDaoJpaTest extends DbUnitTestBase {
	private GenericDao<Employee, Long> employeeDao;
	private GenericDao<Department, Long> departmentDao;
	private PlatformTransactionManager transactionManager;

	@Override
	public String getDataSetXml() {
		return "data/Employee.xml";
	}
 
	@Test
	public void testLazyEmployees() {
		Employee emp = employeeDao.read(1L);
		System.out.println(emp.getDepartmentId());
		System.out.println(emp.getDepartment().getId());
	}

	@Test
	public void testLazy() {
		List<Department> departments = departmentDao.find();
		assert departments.size() > 0;
		Department department = departments.get(0);
		assert department != null;
		List<Employee> employees = department.getEmployees();
		assert employees.size() > 0;
	}

	@Test
	public void testDeleteObject() throws Exception {
		Employee employee = (Employee) employeeDao.read(1L);
		AssertJUnit.assertNotNull(employee);
		AssertJUnit.assertEquals(((Employee) employee).getId(), new Long(1));
		employeeDao.delete(1L);
		employeeDao.flushAndClear();
		employee = (Employee) employeeDao.read(1L);
		AssertJUnit.assertNull(employee);
		employeeDao.flushAndClear();
		initPersistenceStuff();
		employee = (Employee) employeeDao.read(1L);
		AssertJUnit.assertNotNull(employee);
	}

	@Test
	public void testFetchWithOrderBy() throws Exception {
		Employee employee = new Employee();
		employee.setFirstName("Rick");
		employee.setNumberOfPromotions(1);
		employeeDao.update(employee);
		Map<String, Object> params = new HashMap<String, Object>();
		List<Employee> employees = employeeDao.find(params,
				new String[] { "firstName" });
		AssertJUnit.assertEquals(((Employee) employees.get(0)).getFirstName(),
				"Bob");
		AssertJUnit.assertEquals(((Employee) employees.get(1)).getFirstName(),
				"Carlos");
		employeeDao.flushAndClear();
		initPersistenceStuff();
	}

	@Test
	public void testGetObject() {
		Employee employee = employeeDao.read(new Long(1));
		AssertJUnit.assertNotNull(employee);
		AssertJUnit.assertEquals(employee.getId(), new Long(1));
		AssertJUnit.assertEquals("Rick", employee.getFirstName());
	}

	@Test
	public void testGetObjects() {
		List<Employee> employees = employeeDao.find();
		AssertJUnit.assertNotNull(employees);
		AssertJUnit.assertTrue(employees.size()>=14);
	}

	@Test
	public void testGetUpdateObjects() throws Exception {
		List<Employee> employees = employeeDao.find();
		AssertJUnit.assertNotNull(employees);
		AssertJUnit.assertEquals(14, employees.size());
		for (Employee employee : employees) {
			employee.setFirstName(employee.getFirstName() + "Gak");
			employeeDao.update(employee);
		}

		AssertJUnit.assertNotNull(employees);
		AssertJUnit.assertEquals(14, employees.size());
		for (Employee employee : employees) {
			AssertJUnit.assertTrue(employee.getFirstName().contains("Gak"));
		}
		employeeDao.flushAndClear();
		initPersistenceStuff();
		employees = employeeDao.find();
		for (Employee employee : employees) {
			AssertJUnit.assertTrue(!employee.getFirstName().contains("Gak"));
		}
	}

	@Test
	public void testParameterQuery() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("firstName", "Rick");
		List<Employee> employees = employeeDao.find(params);
		AssertJUnit.assertNotNull(employees);
		AssertJUnit.assertTrue(employees.size() > 0);
	}

	@Test
	public void testSingleParameterQuery() {
		List<Employee> employees = employeeDao.find("firstName", "Rick");
		AssertJUnit.assertNotNull(employees);
		AssertJUnit.assertTrue(employees.size() > 0);
	}

	@Test
	public void testFinderSupport() {
		EmployeeDAO employeeDAO = (EmployeeDAO) this.employeeDao;
		List<Employee> employees = employeeDAO
				.findEmployeesByDepartment("Engineering");
		AssertJUnit.assertTrue(employees.size() > 0);

	}

	@Test
	public void testFindRelatedField() {
		EmployeeDAO employeeDAO = (EmployeeDAO) this.employeeDao;
		List<Employee> employees = employeeDAO.find("department.name",
				"Engineering");
		AssertJUnit.assertTrue(employees.size() > 0);
	}
	
	@Test
	public void testFindRelatedField2() {
		EmployeeDAO employeeDAO = (EmployeeDAO) this.employeeDao;
		SimpleRelationshipJoin srj = new SimpleRelationshipJoin();
		srj.setAlias("foo");
		srj.setAliasedRelationship(true);
		srj.setRelationshipProperty("o.department");
		List<Employee> employees = employeeDAO.find(new Join[]{srj}, Comparison.eq("foo.name", true, "Engineering"));
		AssertJUnit.assertTrue(employees.size() > 0);
	}
	
	@Test
	public void testFetch2() {
		List<Employee> result = employeeDao.find(join(joinFetch("o.department", true, "foo")),Comparison.eq("foo.name", true, "Engineering"));
		AssertJUnit.assertTrue(result.size() > 0);
	}

	@Test
	public void testFetch3() {
		List<Employee> result = employeeDao.find(join(joinFetch("department")),Comparison.eq("department.name", true, "Engineering"));
		AssertJUnit.assertTrue(result.size() > 0);
	}
	
	@Test
	public void testFindRelatedFieldWithUnderBar() {
		EmployeeDAO employeeDAO = (EmployeeDAO) this.employeeDao;
		List<Employee> employees = employeeDAO.find("department_name",
				"Engineering");
		AssertJUnit.assertTrue(employees.size() > 0);
	}

	@Test
	public void testFindRelatedFieldWithSearchOrdered() {
		EmployeeDAO employeeDAO = (EmployeeDAO) this.employeeDao;
		List<Employee> employees = employeeDAO.searchOrdered(eq(
				"department.name", "Engineering"), "department.name");
		AssertJUnit.assertTrue(employees.size() > 0);
	}

	@Test
	public void testFindRelatedFieldWithSearchOrderedWithUnderBar() {
		EmployeeDAO employeeDAO = (EmployeeDAO) this.employeeDao;
		List<Employee> employees = employeeDAO.searchOrdered(eq(
				"department_name", "Engineering"), "department_name");
		AssertJUnit.assertTrue(employees.size() > 0);
	}

	@Test
	public void constructQueryString() {
		GenericDaoJpa<Employee, Long> dao = new GenericDaoJpa<Employee, Long>();
		Group group = and(eq("firstName", "Rick"), eq("lastName", "Hightower"),
				or(eq("foo", "bar"), eq("baz", "foo")));
		String string = dao.constructWhereClauseString(group, false);
		AssertJUnit
				.assertEquals(
						" WHERE  o.firstName = :firstName  AND  o.lastName = :lastName  AND  (  o.foo = :foo  OR  o.baz = :baz  ) ",
						string);

		group = and(eq("firstName", "Rick"), eq("lastName", "Hightower"),
				or(eq("foo", "bar")));
		string = dao.constructWhereClauseString(group, false);
		AssertJUnit
				.assertEquals(
						" WHERE  o.firstName = :firstName  AND  o.lastName = :lastName  AND  (  o.foo = :foo  ) ",
						string);

		group = and(eq("firstName", "Rick"));
		string = dao.constructWhereClauseString(group, false);
		AssertJUnit.assertEquals(" WHERE  o.firstName = :firstName ", string);

		group = and(between("age", 1, 100));
		string = dao.constructWhereClauseString(group, false);
		AssertJUnit.assertEquals(" WHERE  o.age BETWEEN :age_1 and :age_2 ",
				string);

		group = and(Employee.class, between("age", 1, 100));
		string = dao.constructWhereClauseString(group, false);
		AssertJUnit.assertEquals(" WHERE  o.age BETWEEN :age_1 and :age_2 ",
				string);

		Employee employee = new Employee();
		employee.setFirstName("Ric");
		employee.setAge(0);
		Department department = new Department();
		department.setName("Eng");
		employee.setDepartment(department);
		group = like(employee).excludeProperty("employees").excludeProperty(
				"numberOfPromotions");
		string = dao.constructWhereClauseString(group, false);
		AssertJUnit
				.assertEquals(
						" WHERE  o.active = :active  AND  (  o.department.name LIKE :department_name  )  AND  o.firstName LIKE :firstName  AND  o.tasks = :tasks ",
						string);

	}

	@Test
	public void testFindByCriteria() {

		List<Employee> employees = employeeDao.find(eq("department.name",
				"Engineering"));
		AssertJUnit.assertTrue(employees.size() > 0);

		employees = employeeDao.find(orderBy("firstName", "department.name"),
				eq("department.name", "Engineering"), or(startsLike(
						"firstName", "Rick")));
		AssertJUnit.assertTrue(employees.size() > 0);

		employees = employeeDao.searchOrdered(and(eq("department.name",
				"Engineering"), or(startsLike("firstName", "Rick"))),
				"firstName");
		AssertJUnit.assertTrue(employees.size() > 0);

		employees = employeeDao.find(or(eq("department.name", "Engineering"),
				like("firstName", "Ri")));
		AssertJUnit.assertTrue(employees.size() > 0);

		employees = employeeDao.find(or(in("age", 40, 1, 2, 3, 4, 5, 6)));
		AssertJUnit.assertTrue(employees.size() > 0);

		List<Criterion> criteria = new ArrayList<Criterion>();
		criteria.add(eq("department.name", "Engineering"));
		employees = employeeDao.find(criteria, orderBy("firstName"));
		AssertJUnit.assertTrue(employees.size() > 0);
	}

    @Test
    public void testFindByCriteriaIn() {

        List<Employee> employees = employeeDao.find(in("age", 1, 2, 3, 4, 5, 6));
        AssertJUnit.assertEquals(0, employees.size());

        employees = employeeDao.find(in("age", 40, 1, 2, 3, 4, 5, 6));
        AssertJUnit.assertEquals(14, employees.size());

    }

	@Test
	public void testBetween() {

		List<Employee> employees = employeeDao.find(between("age", 1, 100));
		AssertJUnit.assertTrue(employees.size() > 0);
	}

	@Test
	public void testQBE() {

		Employee employee = new Employee();
		employee.setActive(true);
		employee.setAge(40);
		employee.setNumberOfPromotions(1);
		employee.setFirstName("Rick");
		employee.setLastName("Rick");
		employee.setTasks(null);

		Example ex =  like(employee).excludeProperty("lastName").excludeProperty("tasks");
		System.out.println(ex);
		List<Employee> employees = employeeDao.find(ex);
		AssertJUnit.assertTrue(employees.size() > 0);

		employee = new Employee();
		employee.setFirstName("Ric%");
		employee.setLastName("High%");
		employee.setAge(40);
		employee.setActive(true);
		employee.setNumberOfPromotions(1);
		Department department = new Department();
		department.setName("Eng%");
		employee.setDepartment(department);
		employees = employeeDao.find(like(employee)
				.excludeProperty("employees").excludeProperty("tasks"));
		AssertJUnit.assertTrue(employees.size() > 0);

	}

	@Test
	public void testQueryBuildingNullParameter() {
		GenericDaoJpa<Employee, Long> localDao = new GenericDaoJpa<Employee, Long>();

		String gimp = localDao.constructWhereClauseString(
				and(eq("gimp", null)), true);
		AssertJUnit.assertEquals(" WHERE  o.gimp is null ", gimp);
	}

	@Test
	public void testNullParameterQueryExecution() {
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("lastName", null);
		List<Employee> result = employeeDao.find(attributes);
		AssertJUnit.assertEquals(9, result.size());
	}

	@Test
	public void testFetch() {
		List<Employee> result = employeeDao.find(join(joinFetch("department")),
				orderBy("firstName"), and());
		AssertJUnit.assertEquals(14, result.size());
		result = employeeDao.find(join(leftJoinFetch("department"),leftJoinFetch("tasks")),
				orderBy("firstName"), and());
		AssertJUnit.assertEquals(14, result.size());

	}

	@Test
	public void testFetchWithCriteria() {
		List<Employee> result = employeeDao.find(join(joinFetch("department",
				"dpt")), orderBy("firstName"), and(eq("dpt.name", true,
				"Engineering")));
		AssertJUnit.assertEquals(3, result.size());
	}

	@Test
	public void testReadFully() {
		Employee employee = employeeDao.readPopulated(1L);
		AssertJUnit.assertNotNull(employee);

		Department dept = (Department) departmentDao.readPopulated(1L);
		AssertJUnit.assertNotNull(dept);
	}

	@Test
	public void testPaginate() {
		int startPosition = 1;
		int maxResults = 2;
		List<Employee> employees = employeeDao.find(startPosition, maxResults);
		AssertJUnit.assertNotNull(employees);
		AssertJUnit.assertEquals(2, employees.size());

	}

	@Test
	public void testOrderBy() {
		List<Employee> employees = employeeDao.find(orderBy(asc("firstName")));
		AssertJUnit.assertNotNull(employees);
		AssertJUnit.assertEquals("Bob", employees.get(0).getFirstName());

		employees = employeeDao.find(orderBy(desc("firstName")));
		AssertJUnit.assertNotNull(employees);
		AssertJUnit.assertEquals("Vanilla", employees.get(0).getFirstName());
	}

	@Test
	public void testIn() {
		List<Long> ids = new ArrayList<Long>();
		ids.add(1L);
		ids.add(2L);
		List<Employee> employees = employeeDao.find(Comparison.in("id", ids));
        AssertJUnit.assertNotNull(employees);
        AssertJUnit.assertEquals(2,employees.size());

	}

	@Test
	public void testEagerNplusOne() {
		List<Department> depts = departmentDao.find(leftJoinFetch("employees"));
		AssertJUnit.assertNotNull(depts);
	}

	@Test
	public void testEmployees() {
		employeeDao.find();
	}
	
	@Test
	public void testReadExclusive() {
		
		TransactionTemplate transactionTemplate = 
			new TransactionTemplate(transactionManager);
		
		transactionTemplate.execute(
				new TransactionCallback() {
					public Object doInTransaction(TransactionStatus ts) {
						Employee employee =  employeeDao.readExclusive(1L);
						AssertJUnit.assertNotNull("Employee for id=1 not read.", employee);
						return null;
					}
				});		

	}

    @Test
    public void testEntityEquals () {
        Department department = departmentDao.update(new Department("r&d"));
        department = departmentDao.read(department.getId());
        Employee employee = new Employee("Rick", "Hightower");
        employee.setDepartment(department);
        department.getEmployees().add(employee);
        departmentDao.update(department);
        List<Employee> find = employeeDao.find(Comparison.eq("department", department));
        String firstname = find.get(0).getFirstName();
        AssertJUnit.assertEquals("Rick", firstname);
    }

    @Test
    public void testSameVarTwice () {
        List<Employee> find = employeeDao.find(
                or(Comparison.eq("firstName", "Rick"),
                   Comparison.eq("firstName", "Vanilla")
                )
        );
        AssertJUnit.assertEquals(2, find.size());        
    }
    
    @Test
    public void testPersistMultiple() {
        List<Employee> employees =Arrays.asList(
        		new Employee[] { 
        				new Employee("PersistMultipleOne", "Hightower"), 
        				new Employee("PersistMultipleTwo", "Hightower"), 
        				new Employee("PersistMultipleThree", "Hightower") 
        				}
        		);
        employeeDao.persist(employees);
         // clean up inserted data
        for (Employee e : employees) {
        	employeeDao.delete(e);
        }
    }
    
    @Test
    public void testStoreMultiple() {
        employeeDao.flushAndClear();
    	TransactionTemplate xTemplate = new TransactionTemplate(transactionManager);
    	xTemplate.execute(new TransactionCallback() {
		
			public Object doInTransaction(TransactionStatus arg0) {
		        List<Employee> employees =Arrays.asList(
		        		new Employee[] { 
		        				new Employee("StoreMultipleOne", "Hightower"), 
		        				new Employee("StoreMultipleTwo", "Hightower"), 
		        				new Employee("StoreMultipleThree", "Hightower") 
		        				}
		        		);
		        Collection<Employee> results = employeeDao.store(employees);
		         // clean up inserted data
		        for (Employee e : results) {
		        	employeeDao.delete(e);
		        }
		        employeeDao.flushAndClear();
		        
				employees = employeeDao.find();
				AssertJUnit.assertTrue(employees.size() > 0);
				// make a change to each of the employees in the 'managed' state
		        for (Employee e : employees) {
		        	e.setAge(e.getAge()+1);
		        }
		        employeeDao.store(employees);
		        return null;
			}
		
		});

    }    

    @Test
    public void testMergeMultiple() {
        List<Employee> employees =Arrays.asList(
        		new Employee[] { 
        				new Employee("MergeMultipleOne", "Hightower"), 
        				new Employee("MergeMultipleTwo", "Hightower"), 
        				new Employee("MergeMultipleThree", "Hightower") 
        				}
        		);
        Collection<Employee> results = employeeDao.merge(employees);
        
		AssertJUnit.assertTrue(results.size() > 0);
		// make a change to each of the employees in the 'managed' state
        for (Employee e : results) {
        	e.setAge(e.getAge()+1);
        }
        results = employeeDao.merge(results);
        
        // clean up inserted data
        for (Employee e : results) {
        	employeeDao.delete(e);
        }
        
		
    }    
    
    @Test
    public void testRefreshMultiple() {        
		List<Employee> employees = employeeDao.find();
		AssertJUnit.assertTrue(employees.size() > 0);
        employeeDao.refresh(employees);		
    } 
    
    @Test
    public void testRefreshByFind() {        
		List<Employee> employees = employeeDao.find();
		AssertJUnit.assertTrue(employees.size() > 0);
		
		for (Employee emp : employees) {
			String originalFirstName = emp.getFirstName();
			String modifiedFirstName = originalFirstName + System.currentTimeMillis();
			emp.setFirstName(modifiedFirstName );
	        AssertJUnit.assertEquals(modifiedFirstName, emp.getFirstName());
	        emp = employeeDao.refresh(emp.getId());					
	        AssertJUnit.assertEquals(originalFirstName, emp.getFirstName());
		}
    }     
    
    @Test
    public void testRefreshByMerge() {        
		List<Employee> employees = employeeDao.find();
		AssertJUnit.assertTrue(employees.size() > 0);
		
		for (Employee emp : employees) {
			String originalFirstName = emp.getFirstName();
			String modifiedFirstName = originalFirstName + System.currentTimeMillis();
			emp.setFirstName(modifiedFirstName );
	        AssertJUnit.assertEquals(modifiedFirstName, emp.getFirstName());
	        emp = employeeDao.refresh(emp);					
	        AssertJUnit.assertEquals(originalFirstName, emp.getFirstName());
		}
    }      
    
    
    @Test
    public void testDeleteMultiple() {        
    	employeeDao.flushAndClear();
        List<Employee> employees =Arrays.asList(
        		new Employee[] { 
        				new Employee("DeleteMultipleOne", "Hightower"), 
        				new Employee("DeleteMultipleTwo", "Hightower"), 
        				new Employee("DeleteMultipleThree", "Hightower") 
        				}
        		);
        employeeDao.persist(employees);
        employeeDao.delete(employees);		
    } 
    

    @Test
    public void testStartsLike() throws Exception {
	initPersistenceStuff();
	employeeDao.flushAndClear();
        List<Employee> list = employeeDao.find(
        		Comparison.startsLike("firstName", "Ri")        );
        AssertJUnit.assertEquals(5, list.size());
        list = employeeDao.find(
        		Comparison.startsLike("firstName", "C")        );
        AssertJUnit.assertEquals(2, list.size());
        list = employeeDao.find(
        		Comparison.startsLike("lastName", "High")        );
	for(Employee emp : list) {
	    System.err.println("id = " + emp.getId());
        }
        AssertJUnit.assertEquals(4, list.size());
        list = employeeDao.find(
        		Comparison.startsLike("firstName", "FizBot")        );
        AssertJUnit.assertEquals(0, list.size());
    }

    public void setEmployeeDao(final GenericDao<Employee, Long> baseJpaDao) {
		this.employeeDao = baseJpaDao;
	}

	public void setDepartmentDao(GenericDao<Department, Long> departmentDao) {
		this.departmentDao = departmentDao;
	}

	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}
}
