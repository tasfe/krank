package org.crank.crud.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@NamedQueries( {
    @NamedQuery(name="Department.readPopulated",
            query="SELECT DISTINCT department FROM Department department " +
                    " LEFT JOIN department.employees AS employee" +
                    " LEFT JOIN employee.tasks " +
                    " LEFT JOIN employee.contacts " +
                    " WHERE department.id=?1"),
    @NamedQuery( name = "Department.findDepartmentNamed", 
               		     query = "SELECT o FROM Department o  WHERE  o.name = ?1" )
})
public class Department implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue( strategy = GenerationType.AUTO )	
    private Long id;
    private String name;
    
    
    @OneToMany (mappedBy="department", cascade=CascadeType.ALL)
    private List<Employee> employees = null;

    public List<Employee> getEmployees() {
        return employees;
    }

    public void addEmployee (Employee employee) {
        employee.setDepartment( this );
        this.employees.add( employee );
    }

    public void removeEmployee (Employee employee) {
        employee.setDepartment( null );
        employees.remove( employee );
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

    @Override
    public boolean equals( Object arg0 ) {
        if (arg0 == null) {
            return false;
        }
        if (arg0 instanceof Department) {
            Department otherDepartment = (Department) arg0;
            return otherDepartment.getId().equals( this.getId() );
        }
        return super.equals( arg0 );
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    public String toString() {
        return name;
    }
}
