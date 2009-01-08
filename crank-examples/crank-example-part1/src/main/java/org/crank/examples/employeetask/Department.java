


package org.crank.examples.employeetask;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;


@Entity  @Table(name="DEPARTMENT")
@NamedQueries( {
		@NamedQuery(name = "Department.readPopulated", query = "SELECT DISTINCT department FROM Department department "
				+ " LEFT JOIN FETCH department.employees"
				+ " WHERE department.id=?1")
		})
public class Department implements Serializable {
    /** ID */
    @Id @Column (name="ID") @GeneratedValue( strategy = GenerationType.AUTO )
    private Long id;
    

    /* ------- Relationships ------ */
    @OneToMany(mappedBy="department", cascade=CascadeType.ALL)
    private Set<Employee> employees = new HashSet<Employee>();


	/** Properties's fields */
    private String name;
   

    public Department () {

    }

    public void setId(Long id) {
    	this.id = id;
    }
    public Long getId() {
    	return id;
    }

   
    public Set<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(Set<Employee> employees) {
		this.employees = employees;
	}


   
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void addEmployee(Employee employee) {
    	System.out.printf("add %s",employee);
    	employee.setDepartment(this);
    	employees.add(employee);
    }
    public void removeEmployee(Employee employee) {
    	System.out.printf("remove %s",employee);
    	employee.setDepartment(null);
    	employees.remove(employee);
    }
   
}