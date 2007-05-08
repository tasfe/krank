package org.crank.crud.test.model;

import javax.persistence.Entity;
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
					query="from Employee employee join fetch employee.department where employee.id=?")
	
})
public class Employee {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )	
	private Long id;

	private String firstName;
	
	private String lastName;
	
	private boolean active;
	
	private int age;
	
	private Integer numberOfPromotions;
	
	@ManyToOne
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
}
