package com.arcmind.jpa.course.inheritance2.model;

import java.util.Set;

import javax.persistence.AttributeOverrides;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Table(name="IN2_USER") @Entity(name="In2User")
@AttributeOverrides (
		value={@AttributeOverride(name="name", 
				column=@Column(name="USER_NAME"))})
public class User extends Subject {
	private String email;	
	private Group parentGroup;

	@Column (name="USER_EMAIL")	
	public String getEmail() {
		return email;
	}
	
	@ManyToOne ()
	@JoinColumn(name="FK_GROUP_ID")	
	public Group getParentGroup() {
		return parentGroup;
	}

	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="IN2_USER_ROLE", 
       joinColumns={@JoinColumn(name="FK_USER_ID")},
       inverseJoinColumns={@JoinColumn(
    		                name="FK_U_ROLE_ID")})	
	public Set<Role> getUserRoles() {
		return super.getRoles();
	}

	
	public void setUserRoles(Set<Role> roles) {
		super.setRoles(roles);
	}

	
	public User(String name, ContactInfo contactInfo) {
		super(name, contactInfo);		
	}


	public User(String name) {
		super(name);
	}

	public User() {
	}




	public void setParentGroup(Group parent) {
		this.parentGroup = parent;
	}



	public void setEmail(String email) {
		this.email = email;
	}
}
