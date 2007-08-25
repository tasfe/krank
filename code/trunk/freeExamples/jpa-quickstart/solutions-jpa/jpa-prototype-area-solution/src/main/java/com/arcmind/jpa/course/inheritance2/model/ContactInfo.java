package com.arcmind.jpa.course.inheritance2.model;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Table(name="IN2_CONTACT_INFO")
@Entity(name="In2ContactInfo")
public class ContactInfo {

	private Subject subject;
	
	@OneToOne(mappedBy="contactInfo")
	public Subject getSubject() {
		return subject;
	}
	
	private Long id;
	private Map<String, PhoneNumber> phoneNumbers; 
	private String firstName;
	private String lastName;
	private Address address;
	private Address workAddress;
	

	public void addPhoneNumber(PhoneNumber phoneNumber){
		if (phoneNumbers == null) {
			phoneNumbers = new HashMap<String, PhoneNumber>();
		}
		phoneNumbers.put(phoneNumber.getName(), phoneNumber);
	}

	
	@OneToMany(cascade={CascadeType.PERSIST, CascadeType.REMOVE})
	@JoinColumn(name="FK_CONTACT_INFO")
	@MapKey(name="name")
	public Map<String, PhoneNumber> getPhoneNumbers() {
		return phoneNumbers;
	}

	public void setPhoneNumbers(Map<String, PhoneNumber> phones) {
		this.phoneNumbers = phones;
	}

	
	public ContactInfo(String phone, String firstName, String lastName,
			Address address, Address workAddress) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.workAddress = workAddress;
	}


	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public ContactInfo() {

	}

	@Embedded
	@AttributeOverrides(value = {
			@AttributeOverride(name = "line1", 
					column = @Column(name = "work_Address_Line1")),
			@AttributeOverride(name = "line2", 
					column = @Column(name = "work_Address_Line2")),
			@AttributeOverride(name = "zip", 
					column = @Column(name = "work_Address_Zip")),
			@AttributeOverride(name = "state", 
					column = @Column(name = "work_Address_State")), })	
	public Address getWorkAddress() {
		return workAddress;
	}

	public void setWorkAddress(Address workAddress) {
		this.workAddress = workAddress;
	}


	public void setSubject(Subject subject) {
		this.subject = subject;
	}
}
