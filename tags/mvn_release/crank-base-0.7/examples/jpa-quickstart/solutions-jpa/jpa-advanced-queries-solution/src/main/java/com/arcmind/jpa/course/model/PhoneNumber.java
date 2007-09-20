package com.arcmind.jpa.course.model;

import javax.persistence.Entity;

@Entity
public class PhoneNumber extends Identifiable{

	private String name;
	private String number;
	
	public PhoneNumber() {
		
	}
	public PhoneNumber(String name, String number) {
		super();
		this.name = name;
		this.number = number;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	
	@Override
	protected String name() {
		return this.getName();
	}
	
}
