package com.arcmind.jpa.course.simple.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Table(name = "SimpleModelUser")

@Entity(name = "SimpleUser")
public class User {
	
	@OneToOne
	private ContactInfo contactInfo;

	@Id
	@GeneratedValue
	private Long id;
	private String name;


	@ManyToMany
	private List<Role> roles;

	public User(String name, ContactInfo contactInfo) {
		super();
		this.name = name;
		this.contactInfo = contactInfo;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public User(String name) {
		this.name = name;
	}

	public User() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ContactInfo getContactInfo() {
		return contactInfo;
	}

	public void setContactInfo(ContactInfo contactInfo) {
		this.contactInfo = contactInfo;
	}
}
