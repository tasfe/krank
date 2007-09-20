package com.arcmind.jpa.course.inheritance.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
//import javax.persistence.DiscriminatorColumn;
//import javax.persistence.DiscriminatorType;
//import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
//import javax.persistence.Inheritance;
//import javax.persistence.InheritanceType;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Table(name="IN_SUBJECT")
@Entity(name="InSubject")
//TODO Setup an Inheritance strategy of type SINGLE_TABLE 
//TODO setup DiscriminatorColumn called SUBJECT_TYPE of type STRING 
public abstract class Subject implements Serializable {

	@Id @GeneratedValue @Column(name="SUBJECT_ID")
	private Long id;
	
	@Column (name="SUBJECT_NAME")
	private String name;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="IN_SUBJECT_ROLE", 
			    joinColumns={@JoinColumn(name="FK_SUBJECT_ID")},
				inverseJoinColumns={@JoinColumn(name="FK_ROLE_ID")})	
	private Set<Role> roles;
	
	@OneToOne(cascade={CascadeType.ALL})
	@JoinColumn(name="SUBJECT_CONTACT_INFO_ID")
	private ContactInfo contactInfo;
	
	public Subject (String name, ContactInfo contactInfo) {
		this.name = name;
		this.contactInfo = contactInfo;
	}

	public Subject (String name) {
		this.name = name;
	}
	
	public Set<Role> getRoles() {
		if (roles == null) {
			roles = new java.util.HashSet<Role>();
		}
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	

	public Subject () {
		
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ContactInfo getContactInfo() {
		return contactInfo;
	}

	public void setContactInfo(ContactInfo contactInfo) {
		this.contactInfo = contactInfo;
	}
	
}
