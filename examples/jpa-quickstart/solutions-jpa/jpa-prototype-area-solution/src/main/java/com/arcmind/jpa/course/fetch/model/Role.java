package com.arcmind.jpa.course.fetch.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;

@Entity(name="FetchRole")
@NamedQuery(name = "fetch.loadRole", 
		query = "select role from FetchRole role where role.name=:name")
public class Role {
	
	@ManyToMany(mappedBy="roles")
	private List<Group> groups;

	
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Role(String name) {
		this.name = name;
	}

	public Role() {
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

}
