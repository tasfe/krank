package com.arcmind.jpa.course.improved.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity(name = "ImprovedUser")

public class User {
	
	@OneToOne
	private ContactInfo contactInfo;

	@ManyToOne ()
	@JoinColumn(name="FK_GROUP_ID")	
	private Group parentGroup;

	@ManyToMany
	private List<Role> roles;


	@Id
	@GeneratedValue
	private Long id;
	private String name;



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

	public Group getParentGroup() {
		return parentGroup;
	}

	public void setParentGroup(Group parent) {
		this.parentGroup = parent;
	}
}
