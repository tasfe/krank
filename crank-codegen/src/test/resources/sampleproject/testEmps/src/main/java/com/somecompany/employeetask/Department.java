package com.somecompany.employeetask;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity  @Table(name="DEPARTMENT") 
@NamedQueries( {
		@NamedQuery(name = "Department.readPopulated", query = "SELECT DISTINCT department FROM Department department "
								+ " LEFT JOIN FETCH department.employees"
				+ " WHERE department.id=?1")
		})
public class Department implements Serializable {
    /** ID */
    @Id @Column(name="ID") 
    @GeneratedValue( strategy = GenerationType.AUTO )
    private Long id;

    /* ------- Relationships ------ */
   
    @OneToMany(mappedBy="department", cascade = CascadeType.ALL)
    private Set<Employee> employees = new HashSet<Employee>();
   

    /* Properties's fields */
  
  
    @Column(length=30) 
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
        return this.employees;
    }

    public void setEmployees(Set<Employee> employees ) {
        this.employees = employees;
    }
    
    public void addEmployee(Employee employee) {
    	employee.setDepartment(this);
    	employees.add(employee);
    }

    public void removeEmployee(Employee employee) {
    	employee.setDepartment(null);
    	employees.remove(employee);
    }
 

    


   
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
   

    public boolean equals(Object other) {
    	if (other==null) {
    		return false;
    	}
    	Department otherDepartment = (Department) other;
    	if (otherDepartment.id==null && this.id==null) {
    		return otherDepartment.hashCode() == this.hashCode();
    	} else if (this.id == null) {
    		return false;
    	} else {
    		return this.id.equals(otherDepartment.id);
    	}
    }
    
    public int hashCode() {
    	return id == null ? super.hashCode() : id.hashCode();
    }

}
