package org.crank.security.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@SuppressWarnings("serial")
@Embeddable
public class Address implements Serializable {
	private String line1;
	private String line2;
	private String zip;
	private String state;

	public Address() {

	}

	public Address(String line1, String line2, String zip, String state) {
		this.line1 = line1;
		this.line2 = line2;
		this.zip = zip;
		this.state = state;
	}

	public String getLine1() {
		return line1;
	}

	public void setLine1(String line1) {
		this.line1 = line1;
	}

	public String getLine2() {
		return line2;
	}

	public void setLine2(String line2) {
		this.line2 = line2;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}
