package org.crank.validation;

import junit.framework.TestCase;

public class ValidationContextTest extends TestCase {

	public void testGetBindingPath() {
		ValidationContext.create();
		ValidationContext context = ValidationContext.get();
		EmployeeMock employee = new EmployeeMock();
		
		context.pushObject(employee);
		context.pushProperty("department");
		context.pushProperty("address");
		context.pushProperty("line1");
		
		assertEquals("employeeMock.department.address.line1", 
				ValidationContext.getBindingPath());
		
		context.pop();

		assertEquals("employeeMock.department.address", 
				ValidationContext.getBindingPath());
		
		ValidationContext.destroy();

		assertEquals("", 
				ValidationContext.getBindingPath());
		
	}

}
