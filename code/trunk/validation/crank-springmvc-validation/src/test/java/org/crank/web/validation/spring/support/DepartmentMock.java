package org.crank.web.validation.spring.support;

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

	public void setName(String name) {
		this.name = name;
	}

}
