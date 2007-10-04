package com.arcmind.jpa.course.improved.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;


@NamedQuery(name = "improved.loadGroup", 
		query = "select group from ImprovedGroup "
	+ " group where group.name = :name")
	
@Entity (name="ImprovedGroup")
public class Group {

	@OrderBy("name ASC")
	@OneToMany(mappedBy="parentGroup")
	//private Set<User> users;
	private List<User> users;

//	@OneToMany
//	@JoinColumn(name="FK_GROUP_ID")
	
//	 @JoinTable(name="GROUP_ROLE",
//				joinColumns={@JoinColumn(name="FK_GROUP_ID")},
//				inverseJoinColumns={@JoinColumn(name="FK_ROLE_ID")})	
	@ManyToMany
	private List<Role> roles;
	
	
	
	public void addUser(User user) {
		user.setParentGroup(this);
		getUsers().add(user);		
	}
	
	public void removeUser(User user) {
		user.setParentGroup(null);
		users.remove(user);
	}




	
	
	
	@Id
	@GeneratedValue
	private Long id;
	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

//	public Set<User> getUsers() {
//		if (users == null) {
//			users = new java.util.HashSet<User>();
//		}
//		return users;
//	}
//
//	public void setUsers(Set<User> users) {
//		this.users = users;
//	}

	public List<User> getUsers() {
	if (users == null) {
		users = new ArrayList<User>();
	}
	return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<Role> getRoles() {
		if (roles == null) {
			roles = new ArrayList<Role>();
		}
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Group(String name) {
		super();
		this.name = name;
	}

	public Group() {

	}

	
}
