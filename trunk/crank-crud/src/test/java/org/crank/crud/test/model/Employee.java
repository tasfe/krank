package org.crank.crud.test.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

@Entity(name="Employee")
@NamedQueries( {
	@NamedQuery(name="Employee.findEmployeesByDepartment",
			query="SELECT employee FROM Employee employee WHERE employee.department.name=?1"),
	@NamedQuery(name="Employee.readPopulated",
			query="SELECT employee FROM  Employee employee JOIN FETCH employee.department WHERE employee.id=?1"),
	@NamedQuery(name="Employee.findInEmployeeIds",
			query="SELECT o FROM Employee o  WHERE  o.id in  ( ?1 )"),
    @NamedQuery(name="Employee.findSalaryEmployees",
				query="SELECT o FROM Employee o  WHERE  o.status = org.crank.crud.test.model.EmployeeStatus.SALARY")
				,
	@NamedQuery(name="Employee.findExcellentEmployees",
				query="SELECT o FROM Employee o  WHERE  o.rank = org.crank.crud.test.model.EmployeeRank.EXCELLENT"
	)
	
})
public class Employee {

	@OneToMany
	private List<Task> tasks = new ArrayList<Task>();
    public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public Employee() {
        
    }
    
    public Employee(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )	
	private Long id;

	private String firstName;
	
	private String lastName;
    
    @Column (length=81)
    private String description;
	
    private boolean active;
	
	private int age;
	
    @Column (nullable=false)
    private Integer numberOfPromotions = 0;
	
	private EmployeeStatus status;
	
	private Address address;
	
	private Integer rank;
	
	@Column (name="department_id", insertable=false, updatable=false)
	private Long departmentId;
	
	@ManyToOne(fetch=FetchType.LAZY, optional=true)
	private Department department;
	
	@ManyToOne(fetch=FetchType.LAZY, optional=true)
	private Department clientDepartment;
	
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Integer getNumberOfPromotions() {
		return numberOfPromotions;
	}

	public void setNumberOfPromotions(Integer numberOfPromotions) {
		this.numberOfPromotions = numberOfPromotions;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public EmployeeStatus getStatus() {
		return status;
	}

	public void setStatus(EmployeeStatus status) {
		this.status = status;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

    public String getDescription() {
        return description;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress( Address address ) {
        this.address = address;
    }
    
	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public Department getClientDepartment() {
		return clientDepartment;
	}

	public void setClientDepartment(Department clientDepartment) {
		this.clientDepartment = clientDepartment;
	}
    
}
