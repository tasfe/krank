package com.arcmind.jpa.course.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

@Entity
@Inheritance (strategy=InheritanceType.SINGLE_TABLE)
public abstract class Subject extends Identifiable {
	
	@OneToOne(cascade={CascadeType.ALL}, mappedBy="subject")
	private ContactInfo contactInfo;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="SUBJECT_ROLE", 
			    joinColumns={@JoinColumn(name="FK_SUBJECT_ID")},
				inverseJoinColumns={@JoinColumn(name="FK_ROLE_ID")})	
	private Set<Role> roles;

	@Column (name="SUBJECT_NAME")
	private String name;
	
	
	public Subject (String name, ContactInfo contactInfo) {
		this.name = name;
		this.contactInfo = contactInfo;
		if (contactInfo!=null) {
			contactInfo.setSubject(this);
		}
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
	
	public void addRole(Role role) {
		getRoles();
		this.roles.add(role);
	}
	
	

	public Subject () {
		
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
	
	@Override
	protected String name() {
		return this.getName();
	}
	
}
