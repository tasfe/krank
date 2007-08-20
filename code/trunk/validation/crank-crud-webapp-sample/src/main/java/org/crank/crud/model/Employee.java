package org.crank.crud.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
//import javax.persistence.FetchType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import org.crank.annotations.validation.Email;
import org.crank.annotations.validation.Length;
import org.crank.annotations.validation.LongRange;
import org.crank.annotations.validation.Phone;
import org.crank.annotations.validation.ProperNoun;
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
                        "where employee.id=?" ),
        @NamedQuery( name = "Employee.findInEmployeeIds", query = "SELECT o FROM Employee o  WHERE  o.id in  ( ? )" ),
        @NamedQuery( name = "Employee.findSalaryEmployees", query = "SELECT o FROM Employee o  WHERE  o.status = org.crank.crud.model.EmployeeStatus.SALARY" ),
        @NamedQuery( name = "Employee.findExcellentEmployees", query = "SELECT o FROM Employee o  WHERE  o.rank = org.crank.crud.model.EmployeeRank.EXCELLENT" )

} )
public class Employee implements Serializable {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private Long id;

    @Column( nullable = false, length = 32 )
    private String firstName;

    private String lastName;

    @Column( length = 150 )
    private String description;

    private boolean active;

    private int age;

    private String email;

    private String phone;

    @Column (nullable=false)
    private Integer numberOfPromotions;

    private EmployeeStatus status;

    private Integer rank;

    private Date dob;
    
    private Address address;

    @OneToMany( cascade = CascadeType.ALL )
    @Cascade({org.hibernate.annotations.CascadeType.DELETE_ORPHAN})    
    private Set<Task> tasks = new HashSet<Task>();
    
    @OneToMany( cascade = CascadeType.ALL )
    @Cascade({org.hibernate.annotations.CascadeType.DELETE_ORPHAN})    
    private Set<ContactInfo> contacts = new HashSet<ContactInfo>();

    @ManyToOne( )
    private Department department;

    private PersistedFile file;

    public PersistedFile getFile() {
        return file;
    }

    public void setFile( PersistedFile file ) {
        this.file = file;
    }

    public void addTask( Task task ) {
        this.tasks.add( task );
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

    public String getFirstName() {
        return firstName;
    }

    @Required
    @ProperNoun
    //@Length( min = 2, max = 35 )
    public void setFirstName( String firstName ) {
        this.firstName = firstName;
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

    public String getLastName() {
        return lastName;
    }

    @Required
    @ProperNoun
    @Length( min = 2, max = 35 )
    public void setLastName( String lastName ) {
        this.lastName = lastName;
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

    public String getDescription() {
        return description;
    }

    public void setDescription( String description ) {
        this.description = description;
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

    public Set<ContactInfo> getContacts() {
        return contacts;
    }

    public void setContacts( Set<ContactInfo> contacts ) {
        this.contacts = contacts;
    }
}
