package org.crank.crud.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
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
import javax.persistence.OneToOne;


import org.crank.annotations.validation.Email;
import org.crank.annotations.validation.LongRange;
import org.crank.annotations.validation.Phone;
import org.crank.annotations.validation.Required;
import org.crank.crud.model.PersistedFile;
import org.crank.crud.annotations.ToolTip;

@Entity
@NamedQueries( {
        @NamedQuery( name = "Employee.findEmployeesByDepartment", 
		     query = "SELECT employee FROM Employee employee " +
		     		" WHERE employee.department.name=?1" ),
        @NamedQuery( name = "Employee.readPopulated", 
                query = "SELECT DISTINCT employee FROM Employee employee " +
                        "LEFT OUTER JOIN FETCH employee.department " +
                        "LEFT OUTER JOIN FETCH employee.type " + 
                        "LEFT OUTER JOIN FETCH employee.tasks " +
                        "LEFT OUTER JOIN FETCH employee.contacts " +
                        "LEFT OUTER JOIN FETCH employee.directReports " +
                        "LEFT OUTER JOIN FETCH employee.roles " +
                        "WHERE employee.id=?1" ),
        @NamedQuery( name = "Employee.findInEmployeeIds", 
		     query = "SELECT o FROM Employee o  WHERE  o.id in  ( ?1 )" ),
        @NamedQuery( name = "Employee.findSalaryEmployees", 
		     query = "SELECT o FROM Employee o  WHERE o.status = " +
		     		" org.crank.crud.model.EmployeeStatus.SALARY" ),
        @NamedQuery( name = "Employee.findExcellentEmployees", 
		     query = "SELECT o FROM Employee o WHERE o.rank = " +
		     		" org.crank.crud.model.EmployeeRank.EXCELLENT" )

} )
public class Employee extends Person {
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private Long id;
    private boolean active;
    private int age;
    @ToolTip(value = "Do not use an AOL or Lotus Notes internal format.",
   	         labelValue = "Employee's company e-mail address.")
    private String email;
    private String phone;
    @Column (nullable=false)
    private Integer numberOfPromotions;
    private EmployeeStatus status = EmployeeStatus.SALARY;
    private Integer rank;
    @Required
    private Date dob;
    @Embedded
    private Address address;
    @OneToOne (cascade=CascadeType.ALL)
    @JoinColumn( name = "PARKING_SPOT_ID" )
    private ParkingSpot parkingSpot;
    
    @OneToMany( cascade = CascadeType.ALL )
    private Set<Task> tasks = new HashSet<Task>();
    @OneToMany( cascade = CascadeType.ALL )
    private Set<ContactInfo> contacts = new HashSet<ContactInfo>();
    @ManyToMany()
    @JoinTable(name="EMPLOYEE_ROLE", 
		    joinColumns={@JoinColumn(name="FK_EMPLOYEE_ID")},
			inverseJoinColumns={@JoinColumn(name="FK_ROLE_ID")})	    
    private Set<Role> roles = new HashSet<Role>();
    @ManyToOne(cascade=CascadeType.ALL)
    private Department department;
    @OneToMany( cascade = CascadeType.ALL )
    private Set<Employee> directReports = new HashSet<Employee>();
    @ManyToOne()
    private Specialty type;
    //@ManyToOne(optional = false)
    @ManyToOne()
    private Skill primarySkill;
    private PersistedFile file;

    @ManyToOne( )
    private Employee manager;
    
    public Employee() {
    	
    }
    
    public Employee(String aFirstName, String aLastName, Integer numberOfPromotions) {
    	this.setFirstName(aFirstName);
    	this.setLastName(aLastName);
    	this.setNumberOfPromotions(numberOfPromotions);
    	
    }
    
    public Set<Role> getRoles() {
		return roles;
	}

    @Required
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
        //System.out.println( "SET DEPARTMENT " + department );
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


    public String getPhone() {
        return phone;
    }


    @Email
    public void setEmail( String email ) {
        this.email = email;
    }
    
    @Required
    @LongRange( min = 18L, max = 135L )
    @ToolTip(value = "Age must be entered even though birthdate is supplied.",
    	     labelValue = "Employee's age at time of hire.")
    public void setAge( int age ) {
        this.age = age;
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

	public Specialty getType() {
		return type;
	}

	public void setType(Specialty type) {
		this.type = type;
	}

	public Set<Employee> getDirectReports() {
		return directReports;
	}

	public void setDirectReports(Set<Employee> directReports) {
		this.directReports = directReports;
	}

    public Employee getManager() {
        return manager;
    }

    public void setManager(Employee manager) {
        this.manager = manager;
    }

	public Skill getPrimarySkill() {
		return primarySkill;
	}

	public void setPrimarySkill(Skill primarySkill) {
		this.primarySkill = primarySkill;
	}

	public ParkingSpot getParkingSpot() {
		if (parkingSpot==null) {
			parkingSpot = new ParkingSpot();
		}
		return parkingSpot;
	}

	public void setParkingSpot(ParkingSpot parkingSpot) {
		this.parkingSpot = parkingSpot;
	}

}
