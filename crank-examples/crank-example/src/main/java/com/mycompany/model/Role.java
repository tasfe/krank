
package com.mycompany.model;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@Entity  @Table(name="ROLE") 
@NamedQueries( {
		@NamedQuery(name = "Role.readPopulated", query = "SELECT DISTINCT role FROM Role role "
								+ " LEFT JOIN FETCH role.employees"
				+ " WHERE role.id=?1")
		})
public class Role implements Serializable {
    /** ID */
    @Id @Column(name="ROLE_ID")
    
    @GeneratedValue( strategy = GenerationType.AUTO )
    private Long id;

    /* ------- Relationships ------ */ 
   
    @ManyToMany
    @JoinTable(name="ROLE_EMPLOYEE",
    		joinColumns=@JoinColumn(name="FK_ROLE_ID"),
    		inverseJoinColumns=@JoinColumn(name="FK_EMP_ID"))	
    private Set <Employee> employees = new HashSet<Employee>();
   
   /* ------- End Relationships ------ */ 




    /* Properties's fields */
  
  
    @Column(name="NAME", nullable=false, length=30) 
    private String name;
    
    /* End of Properties's fields */


    public Role () {

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
    	
    	employees.add(employee);
    }

    public void removeEmployee(Employee employee) {
    	
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
    	Role otherRole = (Role) other;
    	if (otherRole.id==null && this.id==null) {
    		return otherRole.hashCode() == this.hashCode();
    	} else if (this.id == null) {
    		return false;
    	} else {
    		return this.id.equals(otherRole.id);
    	}
    }
    
    public int hashCode() {
    	return id == null ? super.hashCode() : id.hashCode();
    }

}
