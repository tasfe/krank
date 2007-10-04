package com.arcmind.jpa.course.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Person implements Serializable {
	@Id @GeneratedValue
	private Long id;
	
	private String name;


	@OneToMany (cascade={CascadeType.ALL})
	private List<Task> tasks;



	public Person(String name, Task... todos) {
		this.name = name;
		getTasks();
		for (Task task : todos) {
			this.tasks.add(task);
		}
	}

	public Person() {
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
	
	public String toString() {
		return name;
	}
}
