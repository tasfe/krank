package org.crank.examples.employeetask;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity  @Table(name="EMPLOYEE") 
@NamedQueries( {
		@NamedQuery(name = "Employee.readPopulated", query = "SELECT DISTINCT employee FROM Employee employee "
				//				+ " LEFT JOIN FETCH employee.roles"
				+ " LEFT JOIN FETCH employee.department"
				+ " WHERE employee.id=?1")
		})
public class Employee implements Serializable {
    /** ID */
    @Id @Column(name="EMP_ID") 
    @GeneratedValue( strategy = GenerationType.AUTO )
    private Long id;

    /* ------- Relationships ------ */
   
    @ManyToMany @JoinColumn(name="FK_ROLE_ID") @JoinTable(name="ROLE_EMPLOYEE")
    private Set <Role> roles = new HashSet<Role>();
   
    @ManyToOne (cascade = {CascadeType.REFRESH, CascadeType.MERGE}) @JoinColumn(name="FK_DEPARTMENT_ID")
    private Department department;
   

    /** Properties's fields */
   
    
    private String firstName;
   
    @Column(name="LAST_NAME")
    private String lastName;
   

    public Employee () {

    }

    public void setId(Long id) {
    	this.id = id;
    }
    public Long getId() {
    	return id;
    }

   

    public Set<Role> getRoles() {
        return this.roles;
    }

    public void setRoles(Set<Role> roles ) {
        this.roles = roles;
    }
    
    public void addRole(Role role) {
    	
    	roles.add(role);
    }

    public void removeRole(Role role) {
    	
    	roles.remove(role);
    }
 

    

    public Department getDepartment() {
        return this.department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    


   
    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
   
    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
   
}
