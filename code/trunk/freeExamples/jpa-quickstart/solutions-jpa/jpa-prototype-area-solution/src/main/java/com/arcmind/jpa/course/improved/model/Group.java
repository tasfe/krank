package com.arcmind.jpa.course.improved.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
//import javax.persistence.JoinColumn;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@NamedQuery(name = "improved.loadGroup", 
		query = "select group from ImprovedGroup "
	+ " group where group.name = :name")
	
@Table(name = "ImprovedGroup")		
@Entity (name="ImprovedGroup")

public class Group {
	
	public void addUser(User user) {
		user.setParentGroup(this);
		getUsers().add(user);		
	}
	
	public void removeUser(User user) {
		user.setParentGroup(null);
		users.remove(user);
	}

	@ManyToMany
	private List<Role> roles;



//	@OneToMany
//	@JoinColumn(name="FK_GROUP_ID")
	@OneToMany(mappedBy="parentGroup")
	private List<User> users;
	
	
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

	public List<User> getUsers() {
		if (users == null) {
			users = new ArrayList<User>();
		}
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	@JoinTable(name="GROUP_ROLE",
			joinColumns={@JoinColumn(name="FK_GROUP_ID")},
			inverseJoinColumns={@JoinColumn(name="FK_ROLE_ID")}
			)
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
