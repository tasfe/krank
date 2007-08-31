package com.arcmind.jpa.course.model;

import java.io.Serializable;
import java.util.Formatter;

import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Version;

@Entity
@SqlResultSetMappings({
@SqlResultSetMapping(
	name = "TwoEntities",
	entities = {
				@EntityResult(entityClass = Task.class, 
						fields={@FieldResult(name = "estimate", column = "est"),
								@FieldResult(name = "actual", column = "act"),
								@FieldResult(name = "id", column = "tid"),
								@FieldResult(name = "name", column = "tname"),
								@FieldResult(name = "version", column = "tversion")
						}
				)
				//TODO Add @EntityResult Person.class
				//HINT name = "id", column = "pid"
				//HINT name = "name", column = "pname"
	}	
)})

//TODO Add NamedNativeQuery named peopleAndTasks that uses TwoEntities result mappings
//  query="select p.id as pid, t.id as tid, p.name as pname, " +
//	" t.version as tversion, t.name as tname, t.estimate as est, " +
//	" t.actual as act " + 
//    " from Person p inner join Person_Task tasks  on p.id=tasks.Person_id " +  
//    " inner join Task t  on tasks.tasks_id=t.id"
public class TaskHistory implements Serializable {
	
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

	public TaskHistory() {
	}
	
	public TaskHistory(String name, int estimate, int actual) {
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
