package com.arcmind.jpa.course.inheritance2.model;

import java.util.HashSet;
import java.util.Set;

//import javax.persistence.AssociationOverride;
//import javax.persistence.AssociationOverrides;
//import javax.persistence.AttributeOverride;
//import javax.persistence.AttributeOverrides;
//import javax.persistence.Column;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;


@NamedQuery(name = "in2.loadGroup", 
		query = "select group from In2Group "
	+ " group where group.name = :name")	
	
@Table(name="IN2_GROUP")
@Entity(name="In2Group")
//TODO Change this to subclass Subject.
//TODO Change the name of the name's property COLUMN to GROUP_NAME
//TODO Extra Credit: Change the name of the FKEY Column that points to ContactInfo to GROUP_TO_CONTACT_INFO_ID 
public class Group  { //extends Subject{
	//TODO Delete the id, name and contactInfo
	private Long id;
	private String name;

	private Set<User> users;
	
	private Set<Role> roles;
	
	@Id @GeneratedValue () @Column(name="USER_ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToMany(fetch=FetchType.EAGER, targetEntity=Role.class)
	@JoinTable(name="IN2_GROUP_ROLE", 
			    joinColumns={@JoinColumn(name="FK_GROUP_ID")},
				inverseJoinColumns={@JoinColumn(name="FK_G_ROLE_ID")})	
	public Set<Role> getRoles() {
		if (roles == null) {
			roles = new java.util.HashSet<Role>();
		}
		return roles;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	

	@OrderBy("name ASC")
	@OneToMany(mappedBy="parentGroup",
		cascade={CascadeType.PERSIST, CascadeType.REMOVE})
	public Set<User> getUsers() {
		if (users == null) {
			users = new HashSet<User>();
		}
		return users;
	}

//	public Set<Role> getGroupRoles() {
//		return super.getRoles();
//	}
//	public void setGroupRoles(Set<Role> roles) {
//		super.setRoles(roles);
//	}
	
	
	

	public void setUsers(Set<User> users) {
		this.users = users;
	}
	
	
	public void addUser(User user) {
		user.setParentGroup(this);
		getUsers().add(user);		
	}
	
	public void removeUser(User user) {
		user.setParentGroup(null);
		users.remove(user);
	}


	public Group(String name) {
		//TODO uncomment this.
		//super(name)
	}

	public Group() {

	}
	
	// TODO Delete name property
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	
}
//@AttributeOverrides (
//value={@AttributeOverride(name="name", 
//		column=@Column(name="GROUP_NAME"))})
//@AssociationOverrides (value={
//   @AssociationOverride(name="contactInfo",
//     joinColumns={@JoinColumn(name="GROUP_TO_CONTACT_INFO_ID")})
//  }
//)
