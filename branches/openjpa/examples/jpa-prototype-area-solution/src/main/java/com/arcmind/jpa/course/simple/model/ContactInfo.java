package com.arcmind.jpa.course.simple.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ContactInfo {

	private Address address;

	
	@Id
	@GeneratedValue
	private Long id;
	private String phone;
	private String firstName;
	private String lastName;

	public ContactInfo(String phone, String firstName, String lastName,
			Address address) {
		super();
		this.phone = phone;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
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
}
