


package org.crank.examples.employeetask;

import java.io.Serializable;
import javax.persistence.Entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity  @Table(name="EMPLOYEE") 
public class Employee implements Serializable {
    /** ID */
    @Id @Column (name="EMP_ID") @GeneratedValue( strategy = GenerationType.AUTO )
    private Long id;

    /* ------- Relationships ------ */
    @ManyToOne(cascade={CascadeType.MERGE, CascadeType.REFRESH}) @JoinColumn(name="FK_DEPARTMENT_ID")
    private Department department;
    
    /* ------ Properties ----- */
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
    
    public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}
   
}
