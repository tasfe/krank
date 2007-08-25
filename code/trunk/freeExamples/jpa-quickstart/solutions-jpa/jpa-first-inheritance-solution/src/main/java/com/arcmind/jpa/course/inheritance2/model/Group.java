package com.arcmind.jpa.course.inheritance2.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
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
@AttributeOverrides (
		value={@AttributeOverride(name="name", 
				column=@Column(name="GROUP_NAME"))})
public class Group extends Subject{
	private Set<User> users;

	@OrderBy("name ASC")
	@OneToMany(mappedBy="parentGroup",
		cascade={CascadeType.PERSIST, CascadeType.REMOVE})
	public Set<User> getUsers() {
		if (users == null) {
			users = new HashSet<User>();
		}
		return users;
	}
	@ManyToMany(fetch=FetchType.EAGER, targetEntity=Role.class)
	@JoinTable(name="IN2_GROUP_ROLE", 
			    joinColumns={@JoinColumn(name="FK_GROUP_ID")},
				inverseJoinColumns={@JoinColumn(name="FK_G_ROLE_ID")})	
	public Set<Role> getGroupRoles() {
		return super.getRoles();
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	

	public void setGroupRoles(Set<Role> roles) {
		super.setRoles(roles);
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
		super(name);
	}

	public Group() {

	}

	
}
