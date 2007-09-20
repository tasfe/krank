package com.arcmind.jpa.course.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;


@NamedQuery(name = "loadGroup", 
		query = "select group from Group "
	+ " group where group.name = :name")	

@Entity @Table(name="USER_GROUP")
@PrimaryKeyJoinColumn(name="GROUP_ID")
public class Group extends Subject{

	/* NOTE @OrderBy("name ASC") was removed
	 * We had to remove this because name field is 
	 * in the Subject table not the user table.
	 */
	@OneToMany(mappedBy="parentGroup",
		cascade={CascadeType.ALL})
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

	public Group(String name, User... someUsers) {
		super(name);
		for (User user : someUsers) {
			this.addUser(user);
		}
	}
	
	public Group() {

	}


	
}
