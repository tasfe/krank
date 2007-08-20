package com.arcmind.jpa.course.simple.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@NamedQuery(name = "loadGroup", query = "select group from Group "
		+ " group where group.name = :name")

@Table(name = "UserGroup")		
@Entity
public class Group {

	@OneToMany
	private List<User> users;

	@ManyToMany
	private List<Role> roles;

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
