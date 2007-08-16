package org.crank.crud.relationships;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
//import static org.testng.AssertJUnit.*;

public class RelationshipManagerTest extends TestCase implements Serializable {
    
    public class Address implements Serializable {
        
        private String zipCode;
        private String name;
        private Long id;
        
        public Long getId() {
            return id;
        }
        public void setId( Long id ) {
            this.id = id;
        }
        public Address () {
            
        }
        public Address (String zipCode, String name) {
            this.name = name;
            this.zipCode = zipCode;
        }
        
        public String getName() {
            return name;
        }
        public void setName( String name ) {
            this.name = name;
        }
        public String getZipCode() {
            return zipCode;
        }
        public void setZipCode( String zipCode ) {
            this.zipCode = zipCode;
        }
    }

    public class Task implements Serializable {
        private String name;
        private Long id;

        public Long getId() {
            return id;
        }
        public void setId( Long id ) {
            this.id = id;
        }
        public Task() {
            
        }
        public Task( String name ) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName( String name ) {
            this.name = name;
        }
        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }
    
    public class Employee implements Serializable {
        private String firstName;
        private Department department;
        private Set<Task> tasks = new HashSet<Task>();
        private Map<String, Address> addressMap = new HashMap<String, Address>();
        private Long id;

        public Long getId() {
            return id;
        }
        public void setId( Long id ) {
            this.id = id;
        }
        public Employee (String firstName) {
            this.firstName = firstName;
        }
        public Department getDepartment() {
            return department;
        }

        public void setDepartment( Department department ) {
            this.department = department;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName( String firstName ) {
            this.firstName = firstName;
        }
        
//        public void addTask(Task task) {
//            this.tasks.add( task );
//        }
//        public void removeTask(Task task) {
//            this.tasks.remove( task );
//        }

        public Set<Task> getTasks() {
            return tasks;
        }
        public void setTasks( Set<Task> tasks ) {
            this.tasks = tasks;
        }
        public void addAddress( Address address ) {
            addressMap.put(address.getName(), address);
        }
        public void removeAddress( Address address ) {
            addressMap.remove( address.getName());
        }
        public Map<String, Address> getAddresses() {
            return addressMap;
        }
        public void setAddresses( Map<String, Address> addressMap ) {
            this.addressMap = addressMap;
        }
    }
    
    public class Department {
        private List<Employee> employees = new ArrayList<Employee>();

        public List<Employee> getEmployees() {
            return employees;
        }
        public void setEmployees( List<Employee> employees ) {
            this.employees = employees;
        }
        public void addEmployee(Employee employee) {
            employee.setDepartment( this );
            employees.add( employee );
        }
        public void removeEmployee(Employee employee) {
            employee.setDepartment( null );
            employees.remove(employee);
        }
    }
    
    private RelationshipManager departmentEmployeeManager;
    private RelationshipManager employeeTaskManager;
    private RelationshipManager employeeAddressManager;
    private Department department;
    private Employee employee;
        
    @BeforeMethod
    protected void setUp() throws Exception {
        employee = new Employee("Rick");
        employee.setId( 1L );
        department = new Department();
        department.addEmployee(employee);
        employee.getTasks().add( new Task("Task1") );
        employee.getTasks().add( new Task("Task2") );
        employee.addAddress(new Address("85748", "Home"));
        employee.addAddress(new Address("95053", "Work"));
        
        departmentEmployeeManager = new RelationshipManager();
        departmentEmployeeManager.setEntityClass( Employee.class );
        
        employeeTaskManager = new RelationshipManager();
        employeeTaskManager.setEntityClass( Task.class );

        employeeAddressManager = new RelationshipManager();
        employeeAddressManager.setEntityClass ( Address.class );
        employeeAddressManager.setChildCollectionProperty( "addresses" );
    }


    @Test
    public void testNullRelationship() {
        Employee emp = new Employee("Bob");
        emp.setTasks( null );
        employeeTaskManager = new RelationshipManager();
        employeeTaskManager.setEntityClass( Task.class );
        employeeTaskManager.addToParent( emp, new Task("Some task") );
        
    }

    @Test
    public void testList() {
        Serializable serializable = departmentEmployeeManager.readEntityFromParent(department, "1");
        assertNotNull(serializable);
        assertTrue(serializable instanceof Employee);
        Employee employee = (Employee) serializable;
        assertEquals( "Rick", employee.getFirstName() );
        
        Employee employee2 = new Employee("Danillo");
        departmentEmployeeManager.addToParent( department, employee2 );
        assertEquals("Danillo", department.getEmployees().get( 1 ).getFirstName());
        assertEquals("ix--1", departmentEmployeeManager.getObjectId( department, employee2));
        Employee employeex = (Employee) departmentEmployeeManager.readEntityFromParent( department, "ix--1" );
        assertEquals("Danillo", employeex.getFirstName());
        
        Employee employee3 = new Employee("Paul");
        employee3.setId( 66L );
        departmentEmployeeManager.addToParent( department, employee3 );
        assertEquals("66", departmentEmployeeManager.getObjectId( department, employee3));
        
        Employee employee4 = new Employee("Rick");
        employee3.setId( 77L );
        departmentEmployeeManager.addToParent( department, employee4 );
        assertEquals("77", departmentEmployeeManager.getObjectId( department, employee3));

        departmentEmployeeManager.removeFromParent( department, employee2 );
        assertEquals(3, department.getEmployees().size());
    }

    @Test
    public void testSet() {
        Serializable serializable = employeeTaskManager.readEntityFromParent(employee, "hc--" + "Task1".hashCode());
        assertNotNull(serializable);
        assertTrue(serializable instanceof Task);
        Task task = (Task) serializable;
        assertEquals( "Task1", task.getName() );

        Task task2 = new Task("Catch up with foobar");
        employeeTaskManager.addToParent( employee, task2 );
        assertTrue(employee.getTasks().contains( task2 ));

        assertEquals("hc--" + "Catch up with foobar".hashCode(), employeeTaskManager.getObjectId( employee, task2));
        Task taskx = (Task) employeeTaskManager.readEntityFromParent( employee, "hc--" + "Catch up with foobar".hashCode());
        assertEquals("Catch up with foobar", taskx.getName());
        
        employeeTaskManager.removeFromParent( employee, task2 );
        assertFalse(employee.getTasks().contains( task2 ));
        
    }

    @Test
    public void testMap() {
        Serializable serializable = employeeAddressManager.readEntityFromParent(employee, "Home");
        assertNotNull(serializable);
        assertTrue(serializable instanceof Address);
        Address address = (Address) serializable;
        assertEquals( "Home", address.getName() );
        
        Address address2 = new Address("97021", "VACATION");
        employeeAddressManager.addToParent( employee, address2);
        assertEquals("VACATION", employee.getAddresses().get( "VACATION" ).getName());
        
        assertEquals("VACATION", employeeAddressManager.getObjectId( employee, address2));
        Address addressx = (Address) employeeAddressManager.readEntityFromParent( employee, "VACATION");
        assertEquals("VACATION", addressx.getName());
        
        employeeAddressManager.removeFromParent( employee, address2 );
        assertNull(employee.getAddresses().get( "VACATION" ));
        
        
    }

}
