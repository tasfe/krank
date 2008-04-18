package org.crank.web.validation.spring.support;

import junit.framework.TestCase;

public class SpringValidatorContextTest extends TestCase {

	public void testGetBindingPath() {
		SpringValidatorContext.create();
		SpringValidatorContext context = SpringValidatorContext.get();
		EmployeeMock employee = new EmployeeMock();
		
		context.pushObject(employee);
		context.pushProperty("department");
		context.pushProperty("address");
		context.pushProperty("line1");
		
		assertEquals("employeeMock.department.address.line1", 
				SpringValidatorContext.getBindingPath());
		
		context.pop();

		assertEquals("employeeMock.department.address", 
				SpringValidatorContext.getBindingPath());
		
		SpringValidatorContext.destroy();

		assertEquals("", 
				SpringValidatorContext.getBindingPath());
		
	}

}
