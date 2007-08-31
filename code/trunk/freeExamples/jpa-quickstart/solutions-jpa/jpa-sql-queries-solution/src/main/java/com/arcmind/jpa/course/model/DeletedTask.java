package com.arcmind.jpa.course.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Formatter;

import javax.persistence.ColumnResult;
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
				name = "TaskToDeletedTaskPlusFooAndDelDate",
				entities = {
							@EntityResult(entityClass = Task.class, 
									fields={@FieldResult(name = "estimate", column = "est"),
											@FieldResult(name = "actual", column = "act"),
											@FieldResult(name = "id", column = "id"),
											@FieldResult(name = "name", column = "name"),
											@FieldResult(name = "version", column = "version")
									}
							)
				}, 
				columns = {
						@ColumnResult(name="foo"),
						@ColumnResult(name="delDate"),
				}
				
	)
})
public class DeletedTask implements Serializable {
	
	@Id @GeneratedValue
	private Long id;

	@Version
	private int version;

	private String name;
	private int est;
	private int act;
	private Date deleteDate = new Date();
		
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public DeletedTask() {
	}
	
	public DeletedTask(String name, int estimate, int actual) {
		super();
		this.name = name;
		this.est = estimate;
		this.act = actual;
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
		id, version, name, est, act).toString();
	}
	public int getEst() {
		return est;
	}
	public void setEst(int est) {
		this.est = est;
	}
	public int getAct() {
		return act;
	}
	public void setAct(int act) {
		this.act = act;
	}
	public Date getDeleteDate() {
		return deleteDate;
	}
	public void setDeleteDate(Date deleteDate) {
		this.deleteDate = deleteDate;
	}
	
}
