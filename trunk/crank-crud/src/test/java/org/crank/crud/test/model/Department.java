package org.crank.crud.test.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

//import org.hibernate.annotations.Proxy;

@Entity(name="Department")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Department {

    public Department() {
        
    }
    public Department(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
	private Long id;
    private String name;

    
    //@org.hibernate.annotations.BatchSize(size = 20)
    @OneToMany (mappedBy="department", cascade={CascadeType.ALL})
//    , fetch=FetchType.EAGER)
//    @org.hibernate.annotations.Fetch (
//    		org.hibernate.annotations.FetchMode.JOIN
//    )
    private List<Employee> employees;

	public List<Employee> getEmployees() {
		
		if (employees==null) {
			employees = new ArrayList<Employee>();
		}
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
