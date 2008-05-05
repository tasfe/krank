package org.crank.validation;

import org.crank.annotations.validation.Regex;
import org.crank.annotations.validation.Required;
import org.crank.validation.AddressMock;
import org.crank.validation.DepartmentMock;

public class EmployeeMock {
	
	private String firstName;
	private String lastName;
	private AddressMock address = new AddressMock();
	private DepartmentMock department;
	
	
	public DepartmentMock getDepartment() {
		return department;
	}

	public void setDepartment(DepartmentMock department) {
		this.department = department;
	}

	public String getFirstName() {
		return firstName;
	}
	
	@Required (summaryMessage="First name is required", detailMessage="required")
    @Regex (match="Rick", summaryMessage="Hello", detailMessage="regex message")
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

	public AddressMock getAddress() {
		return address;
	}

	public void setAddress(AddressMock address) {
		this.address = address;
	}

}
