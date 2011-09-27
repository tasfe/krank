package org.crank.validation;

import org.crank.annotations.validation.Required;
import org.crank.validation.AddressMock;

public class DepartmentMock {
	
	private String name;
	private AddressMock address; 

	public AddressMock getAddress() {
		return address;
	}

	public void setAddress(AddressMock address) {
		this.address = address;
	}

	public String getName() {
		return name;
	}

    @Required (summaryMessage="name is required", detailMessage="required")
    public void setName(String name) {
		this.name = name;
	}

}
