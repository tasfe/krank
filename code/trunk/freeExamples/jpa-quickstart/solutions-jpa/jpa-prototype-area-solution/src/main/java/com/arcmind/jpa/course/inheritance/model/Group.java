package com.arcmind.jpa.course.inheritance.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.CascadeType;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;


@NamedQuery(name = "in.loadGroup", 
		query = "select group from InGroup "
	+ " group where group.name = :name")	
@Table(name="IN_GROUP")
@Entity(name="InGroup")
@DiscriminatorValue("GROUP")
public class Group extends Subject{


	@OrderBy("name ASC")
	@OneToMany(mappedBy="parentGroup",
		cascade={CascadeType.PERSIST, CascadeType.REMOVE})
	private Set<User> users;

	
	
	
	public void addUser(User user) {
		user.setParentGroup(this);
		getUsers().add(user);		
	}
	
	public void removeUser(User user) {
		user.setParentGroup(null);
		users.remove(user);
	}

	public Set<User> getUsers() {
		if (users == null) {
			users = new HashSet<User>();
		}
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}


	public Group(String name, ContactInfo info) {
		super(name);
	}

	public Group(String name) {
		super(name);
	}

	public Group() {

	}

	
}
