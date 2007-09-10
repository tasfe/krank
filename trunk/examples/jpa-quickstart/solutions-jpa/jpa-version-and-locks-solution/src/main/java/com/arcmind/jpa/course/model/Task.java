package com.arcmind.jpa.course.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;


@Entity
public class Task implements Serializable {
	
	@Id @GeneratedValue
	private Long id;

	@Version
	private int version;

	private String name;
	private String description;
	private Date startDate;
	private Date endDate;
	private int estimate;
	private int actual;
	
	@SuppressWarnings("unused")
	private boolean complete;
	
	@Enumerated (value=EnumType.STRING)
	private TimeUnit unit = TimeUnit.DAY;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public int getEstimate() {
		return estimate;
	}
	public void setEstimate(int estimate) {
		this.estimate = estimate;
	}
	public TimeUnit getUnit() {
		return unit;
	}
	public void setUnit(TimeUnit unit) {
		this.unit = unit;
	}
	public int getActual() {
		return actual;
	}
	public void setActual(int actual) {
		this.actual = actual;
	}

	public Task() {
	}
	
	public Task(String name, String description, Date startDate,
			Date endDate, int estimate, int actual, TimeUnit unit) {
		super();
		this.name = name;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.estimate = estimate;
		this.actual = actual;
		this.unit = unit;
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
	
}
