package org.crank.crud.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import org.crank.annotations.validation.Email;
import org.crank.annotations.validation.LongRange;
import org.crank.annotations.validation.Phone;
import org.crank.annotations.validation.Required;
import org.crank.crud.model.PersistedFile;
import org.hibernate.annotations.Cascade;

@Entity
@NamedQueries( {
        @NamedQuery( name = "Employee.findEmployeesByDepartment", query = "from Employee employee where employee.department.name=?" ),
        @NamedQuery( name = "Employee.readPopulated", 
                query = "select distinct employee from Employee employee " +
                        "left outer join fetch employee.department " + 
                        "left outer join fetch employee.tasks " +
                        "left outer join fetch employee.contacts " +
                        "left outer join fetch employee.directReports " +
                        "left outer join fetch employee.roles " +
                        "where employee.id=?" ),
        @NamedQuery( name = "Employee.findInEmployeeIds", query = "SELECT o FROM Employee o  WHERE  o.id in  ( ? )" ),
        @NamedQuery( name = "Employee.findSalaryEmployees", query = "SELECT o FROM Employee o  WHERE  o.status = org.crank.crud.model.EmployeeStatus.SALARY" ),
        @NamedQuery( name = "Employee.findExcellentEmployees", query = "SELECT o FROM Employee o  WHERE  o.rank = org.crank.crud.model.EmployeeRank.EXCELLENT" )

} )
public class Employee extends Person {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private Long id;

    private boolean active;

    private int age;

    private String email;

    private String phone;

    @Column (nullable=false)
    private Integer numberOfPromotions;

    private EmployeeStatus status;

    private Integer rank;

    private Date dob;
    
    @Embedded
    private Address address;

    @OneToMany( cascade = CascadeType.ALL )
    @Cascade({org.hibernate.annotations.CascadeType.DELETE_ORPHAN})    
    private Set<Task> tasks = new HashSet<Task>();
    
    @OneToMany( cascade = CascadeType.ALL )
    @Cascade({org.hibernate.annotations.CascadeType.DELETE_ORPHAN})    
    private Set<ContactInfo> contacts = new HashSet<ContactInfo>();

	@ManyToMany()
	@JoinTable(name="EMPLOYEE_ROLE", 
			    joinColumns={@JoinColumn(name="FK_EMPLOYEE_ID")},
				inverseJoinColumns={@JoinColumn(name="FK_ROLE_ID")})	    
    private Set<Role> roles = new HashSet<Role>();

    @ManyToOne( )
    private Department department;
    
    @ManyToOne( )
    private Employee manager;
    

    @OneToMany( cascade = CascadeType.ALL )
    @Cascade({org.hibernate.annotations.CascadeType.DELETE_ORPHAN})    
    private Set<Employee> directReports = new HashSet<Employee>();
    
    
    
    @ManyToOne()
    private Specialty specialty;
    
    private PersistedFile file;

    public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public PersistedFile getFile() {
        return file;
    }

    public void setFile( PersistedFile file ) {
        this.file = file;
    }

    public void addTask( Task task ) {
        this.tasks.add( task );
    }
    
    public void addRole(Role role) {
    	this.roles.add(role);
    }
    
    public void removeRole(Role role) {
    	this.roles.remove(role);
    }

    public void removeTask( Task task ) {
        this.tasks.remove( task );
    }

    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }


    public Department getDepartment() {
        return department;
    }

    public void setDepartment( Department department ) {
        System.out.println( "SET DEPARTMENT " + department );
        this.department = department;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive( boolean active ) {
        this.active = active;
    }

    public int getAge() {
        return age;
    }

    @Required
    @LongRange( min = 18L, max = 135L )
    public void setAge( int age ) {
        this.age = age;
    }

    public Integer getNumberOfPromotions() {
        return numberOfPromotions;
    }

    public void setNumberOfPromotions( Integer numberOfPromotions ) {
        this.numberOfPromotions = numberOfPromotions;
    }


    public EmployeeStatus getStatus() {
        return status;
    }

    public void setStatus( EmployeeStatus status ) {
        this.status = status;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank( Integer rank ) {
        this.rank = rank;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob( Date dob ) {
        this.dob = dob;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks( Set<Task> tasks ) {
        this.tasks = tasks;
    }


    public String getEmail() {
        return email;
    }

    @Email
    public void setEmail( String email ) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    @Phone
    public void setPhone( String phone ) {
        this.phone = phone;
    }

    public Address getAddress() {
        if (address == null) {
            address = new Address();
        }
        return address;
    }

    public void setAddress( Address address ) {
        this.address = address;
    }
    
    public void addDirectReport(Employee employee) {
    	employee.setManager(this);
    	directReports.add(employee);
    }

    public void removeDirectReport(Employee employee) {
    	directReports.remove(employee);
    }
    
    
    public Set<ContactInfo> getContacts() {
        return contacts;
    }

    public void setContacts( Set<ContactInfo> contacts ) {
        this.contacts = contacts;
    }

	public Specialty getSpecialty() {
		return specialty;
	}

	public void setSpecialty(Specialty specialty) {
		this.specialty = specialty;
	}

	public Employee getManager() {
		return manager;
	}

	public void setManager(Employee manager) {
		this.manager = manager;
	}

	public Set<Employee> getDirectReports() {
		return directReports;
	}

	public void setDirectReports(Set<Employee> directReports) {
		this.directReports = directReports;
	}
}
