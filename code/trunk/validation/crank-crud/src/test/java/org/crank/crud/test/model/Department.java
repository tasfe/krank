package org.crank.crud.test.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

//import org.hibernate.annotations.Proxy;

@Entity(name="Department")
//@Proxy (lazy=true)
public class Department {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )	
	private Long id;
    private String name;
    
    
    //@org.hibernate.annotations.BatchSize(size = 20)
    @OneToMany (mappedBy="department")
//    , fetch=FetchType.EAGER)
//    @org.hibernate.annotations.Fetch (
//    		org.hibernate.annotations.FetchMode.JOIN
//    )
    private List<Employee> employees;

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


}
