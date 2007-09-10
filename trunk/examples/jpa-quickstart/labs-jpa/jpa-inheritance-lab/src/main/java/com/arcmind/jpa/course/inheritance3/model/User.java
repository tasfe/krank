package com.arcmind.jpa.course.inheritance3.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Table(name="IN3_USER")
@Entity(name="In3User")
//TODO Use PrimaryKeyJoinColumn create FK called USER_ID to point to Subject table
public class User extends Subject {
	@Column (name="USER_EMAIL")
	private String email;
	

	@ManyToOne ()
	@JoinColumn(name="FK_GROUP_ID")	
	private Group parentGroup;



	public User(String name, ContactInfo contactInfo) {
		super(name, contactInfo);
	}


	public User(String name) {
		super(name);
	}

	public User() {
	}


	public Group getParentGroup() {
		return parentGroup;
	}

	public void setParentGroup(Group parent) {
		this.parentGroup = parent;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}
}
