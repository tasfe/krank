package com.arcmind.jpa.course.improved.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

//TODO: Name this entity as "ImprovedUser"
public class User {

	// TODO: Make the relationship to the parent bi-directional	
	private Group parentGroup;

	// TODO: Correctly annotate this relationship	
	private List<Role> roles;

	// TODO: Correctly annotate this relationship	
	private ContactInfo contactInfo;

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
