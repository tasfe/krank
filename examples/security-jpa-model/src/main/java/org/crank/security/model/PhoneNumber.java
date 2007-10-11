package org.crank.security.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="IN3_PHONE_NUMBER")
@Entity(name="In3PhoneNumber")
public class PhoneNumber {

	@Id @GeneratedValue
	private Long id;
	private String name;
	private String number;
	
	public PhoneNumber() {
		
	}
	public PhoneNumber(String name, String number) {
		super();
		this.name = name;
		this.number = number;
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
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
}
