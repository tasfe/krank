package com.arcmind.jpa.course.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name="USER_ID")
public class User extends Subject {
	@Column (name="USER_EMAIL")
	private String email;

	@ManyToOne ()
	@JoinColumn(name="FK_GROUP_ID")	
	private Group parentGroup;

	@OneToMany (cascade={CascadeType.ALL})
	@JoinColumn(name="FK_TASK_ID")
	private List<Task> tasks;



	public User(String name, ContactInfo contactInfo, Task... todos) {
		super(name, contactInfo);
		getTasks();
		for (Task task : todos) {
			this.tasks.add(task);
		}
	}


	public User(Long id, String name) {
		super(name);
		this.setId(id);
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


	public List<Task> getTasks() {
		if (tasks==null) {
			tasks = new ArrayList<Task>();
		}
		return tasks;
	}


	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
}
