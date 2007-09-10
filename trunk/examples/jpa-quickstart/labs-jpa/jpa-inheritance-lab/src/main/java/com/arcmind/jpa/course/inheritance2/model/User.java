package com.arcmind.jpa.course.inheritance2.model;

import java.util.Set;

//import javax.persistence.AssociationOverrides;
//import javax.persistence.AssociationOverride;
//import javax.persistence.AttributeOverrides;
//import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Table(name="IN2_USER") @Entity(name="In2User")
//TODO Change this to subclass Subject.
//TODO Change the name of the name's property COLUMN to USER_NAME
//TODO Extra Credit: Change the name of the FKEY Column that points to ContactInfo to USER_TO_CONTACT_INFO_ID 

//HINT
//@AttributeOverrides 
//@AttributeOverride 
//column=@Column(name="USER_NAME")
//HINT
//@AssociationOverrides @AssociationOverride
//		     joinColumns={@JoinColumn(name="USER_TO_CONTACT_INFO")
public class User { //extends Subject {
	//TODO delete the id, name and contactInfo, field and property.
	private Long id;
	private ContactInfo contactInfo;
	private String email;	
	private Group parentGroup;
	private Set<Role> roles;
	private String name;

	//TODO Delete this property
	@Id @GeneratedValue () @Column(name="USER_ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	//TODO delete this property
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	//TODO Use the roles from the super class.
	//TODO Move these annotations to a new set of methods below.
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="IN2_USER_ROLE", 
       joinColumns={@JoinColumn(name="FK_USER_ID")},
       inverseJoinColumns={@JoinColumn(
    		                name="FK_U_ROLE_ID")})	
	public Set<Role> getRoles() {
		if (roles == null) {
			roles = new java.util.HashSet<Role>();
		}
		return roles;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	//TODO move the annotations from getRoles to here.
	//TODO uncomment property
//	public Set<Role> getUserRoles() {
//		return super.getRoles();
//	}
//
//
//	public void setUserRoles(Set<Role> roles) {
//		super.setRoles(roles);
//	}

	@Column (name="USER_EMAIL")	
	public String getEmail() {
		return email;
	}
	
	@ManyToOne ()
	@JoinColumn(name="FK_GROUP_ID")	
	public Group getParentGroup() {
		return parentGroup;
	}


	
	public User(String name, ContactInfo contactInfo) {
		this.name = name;
		this.contactInfo = contactInfo;
		//super(name, contactInfo);		
	}


	public User(String name) {
		//super(name);
	}

	public User() {
	}

	//TODO Delete this property (annotations and all)
	@OneToOne(cascade={CascadeType.ALL})
	@JoinColumn(name="SUBJECT_CONTACT_INFO_ID")
	public ContactInfo getContactInfo() {
		return contactInfo;
	}

	public void setContactInfo(ContactInfo contactInfo) {
		this.contactInfo = contactInfo;
	}


	public void setParentGroup(Group parent) {
		this.parentGroup = parent;
	}



	public void setEmail(String email) {
		this.email = email;
	}

}
