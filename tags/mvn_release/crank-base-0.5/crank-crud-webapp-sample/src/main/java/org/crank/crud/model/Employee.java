package org.crank.crud.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;


@Entity
@NamedQueries( {
	@NamedQuery(name="Employee.findEmployeesByDepartment",
			query="from Employee employee where employee.department.name=?"),
	@NamedQuery(name="Employee.readPopulated",
					query="from Employee employee join fetch employee.department where employee.id=?"),
	@NamedQuery(name="Employee.findInEmployeeIds",
							query="SELECT o FROM Employee o  WHERE  o.id in  ( ? )"),
    @NamedQuery(name="Employee.findSalaryEmployees",
				query="SELECT o FROM Employee o  WHERE  o.status = org.crank.crud.model.EmployeeStatus.SALARY")
				,
	@NamedQuery(name="Employee.findExcellentEmployees",
						query="SELECT o FROM Employee o  WHERE  o.rank = org.crank.crud.model.EmployeeRank.EXCELLENT"
	)
	
})
public class Employee implements Serializable{

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )	
	private Long id;

	private String firstName;
	
	private String lastName;
	
	private boolean active;
	
	private int age;
	
	private Integer numberOfPromotions;
	
	private EmployeeStatus status;
	
	private Integer rank;
	
    private Date dob;

    //@ManyToOne(fetch=FetchType.LAZY)
    @ManyToOne()
	private Department department;
    
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
        System.out.println("SET DEPARTMENT " + department);
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

    public Date getDob() {
        return dob;
    }

    public void setDob( Date dob ) {
        this.dob = dob;
    }
}
