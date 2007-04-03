package org.crank.web.validation.spring.support;

import org.crank.annotations.validation.Required;

public class EmployeeMock {
	
	private String firstName;
	private String lastName;
	
	
	public String getFirstName() {
		return firstName;
	}
	
	@Required (summaryMessage="First name is required", detailMessage="required")
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	
	@Required (summaryMessage="Last name is required", detailMessage="last required")
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
