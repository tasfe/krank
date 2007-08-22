package com.arcmind.jpa.course.improved.model;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.OneToOne;

@Entity(name = "ImprovedContactInfo")

public class ContactInfo {

	@OneToOne(mappedBy = "contactInfo")
	private User user;
	
	
	private Address address;

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
	private Address workAddress;


	@Id
	@GeneratedValue
	private Long id;
	private String phone;
	private String firstName;
	private String lastName;

	public ContactInfo(String phone, String firstName, String lastName,
			Address address, Address workAddress) {
		super();
		this.phone = phone;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.workAddress = workAddress;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	public Address getWorkAddress() {
		return workAddress;
	}

	public void setWorkAddress(Address workAddress) {
		this.workAddress = workAddress;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
