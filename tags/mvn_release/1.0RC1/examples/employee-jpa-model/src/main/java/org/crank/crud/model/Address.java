package org.crank.crud.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@SuppressWarnings("serial")
@Embeddable
public class Address implements Serializable {

	private String line_1;
	private String line2;
	private String zipCode;

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getLine_1() {
		return line_1;
	}

	public void setLine_1(String line_1) {
		this.line_1 = line_1;
	}

	public String getLine2() {
		return line2;
	}

	public void setLine2(String line2) {
		this.line2 = line2;
	}
}
