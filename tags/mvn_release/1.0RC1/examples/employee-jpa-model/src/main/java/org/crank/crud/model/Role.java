package org.crank.crud.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;


@SuppressWarnings("serial")
@Entity
@NamedQueries( {
    @NamedQuery( name = "Role.findInRoleIds", query = "SELECT o FROM Role o  WHERE  o.id in  ( ? )" ),
    @NamedQuery( name = "Role.readPopulated", 
            query = "SELECT DISTINCT role FROM Role role " +
                    "LEFT OUTER JOIN FETCH role.employees " + 
                    "WHERE role.id=?1" )
} )
public class Role implements Serializable {

	public Role () {
		
	}
	
	public Role (Long id, String name) {
		this.id = id;
		this.name = name;
	}
	
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private Long id;
    
    @Column( nullable = false, length = 32 )
    private String name;

    @ManyToMany()
    private Set<Employee> employees = new HashSet<Employee>();
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

	public Set<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(Set<Employee> employees) {
		this.employees = employees;
	}
	
	public void addEmployee(Employee employee) {
		this.employees.add(employee);
	}

	public void removeEmployee(Employee employee) {
		this.employees.remove(this);
	}

	public boolean equals(Object other) {
		if (!(other instanceof Role)) {
			return false;
		}
		Role otherRole = (Role)other;
		if (otherRole.id!=null && this.id  == null) {
			return false;
		}
		if (this.id !=null && otherRole.id == null) {
			return false;
		}
		
		if (this.id != null) {
			return this.id.equals(otherRole.id);
		} else {
			return this.name.equals(otherRole.name);
		}
		
	}
	
	public int hashCode() {
		return ("" + id + ":" + name).hashCode();
	}
	
}
