package org.crank.crud.criteria;

import org.crank.crud.test.model.Employee;
import org.testng.annotations.Test;


import junit.framework.TestCase;

public class VerifiedGroupTest extends TestCase {
	
	@Test
	public void test() {
		VerifiedGroup group = new VerifiedGroup(Employee.class);
		group.eq("firstName", "bar").eq("lastName", "foo").eq("department.name", "qa");
		assertEquals("VG_(AND [V_firstName_EQ_bar, V_lastName_EQ_foo, V_department.name_EQ_qa])", group.toString());
		
		try {
			group.eq("asfdasfdasfd", "bar").eq("lastName", "foo").eq("department.name", "qa");
			assertTrue(false);
		} catch (Exception e) {
			
		}

		try {
			group.eq("firstName", "bar").eq("lastName", "foo").eq("department.crap", "qa");
			assertTrue(false);
		} catch (Exception e) {
			
		}
		
	}

}
