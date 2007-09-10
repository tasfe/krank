package com.arcmind.jpa.course.inheritance2.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
//import javax.persistence.Column;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;
import javax.persistence.JoinColumn;
//import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
//import javax.persistence.Transient;


//TODO make this a MappedSuperclass
//TODO make roles property as Transient
public abstract class Subject implements Serializable {
	private Long id;
	private Set<Role> roles;
	private String name;
	private ContactInfo contactInfo;
	
	/* TODO Setup this an id. 
	@Id @GeneratedValue () @Column(name="SUBJECT_ID")*/
	public Long getId() {
		return id;
	}
	
	/* TODO Map this to the column SUBJECT_NAME. */
	public String getName() {
		return name;
	}

	/* TODO Mark as transient. */
	public Set<Role> getRoles() {
		if (roles == null) {
			roles = new java.util.HashSet<Role>();
		}
		return roles;
	}
	
	/* TODO Study this: What does this do? */
	@OneToOne(cascade={CascadeType.ALL})
	@JoinColumn(name="SUBJECT_CONTACT_INFO_ID")
	public ContactInfo getContactInfo() {
		return contactInfo;
	}

	

	public Subject (String name, ContactInfo contactInfo) {
		this.name = name;
		this.contactInfo = contactInfo;
	}
	
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Subject (String name) {
		this.name = name;
	}

	
	public Subject () {
	}
	
	public void setId(Long id) {
		this.id = id;
	}


	public void setName(String name) {
		this.name = name;
	}


	public void setContactInfo(ContactInfo contactInfo) {
		this.contactInfo = contactInfo;
	}
	
}
