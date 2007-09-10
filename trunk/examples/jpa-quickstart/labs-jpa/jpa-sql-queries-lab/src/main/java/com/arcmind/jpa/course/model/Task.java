package com.arcmind.jpa.course.model;

import java.io.Serializable;
import java.util.Formatter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
//import javax.persistence.SqlResultSetMapping;
//import javax.persistence.SqlResultSetMappings;
//import javax.persistence.EntityResult;
//import javax.persistence.FieldResult;

import javax.persistence.Version;


@Entity

//TODO Create SqlResultSetMappings 
// SqlResultSetMappings( 
// SqlResultSetMapping name = "TaskToDeletedTask", 
// name = "estimate", column = "est"
// name = "actual", column = "act"
// name = "id", column = "id")
// name = "name", column = "name"
// name = "version", column = "version" 
public class Task implements Serializable {
	
	@Id @GeneratedValue
	private Long id;

	@Version
	private int version;

	private String name;
	private int estimate;
	private int actual;
		
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getEstimate() {
		return estimate;
	}
	public void setEstimate(int estimate) {
		this.estimate = estimate;
	}
	public int getActual() {
		return actual;
	}
	public void setActual(int actual) {
		this.actual = actual;
	}

	public Task() {
	}
	
	public Task(String name, int estimate, int actual) {
		super();
		this.name = name;
		this.estimate = estimate;
		this.actual = actual;
	}
	
	
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String toString() {
		return (new Formatter()).format(
		"Task id=%s version=%s name=%s estimate=%s actual=%s", 
		id, version, name, estimate, actual).toString();
	}
	
}
