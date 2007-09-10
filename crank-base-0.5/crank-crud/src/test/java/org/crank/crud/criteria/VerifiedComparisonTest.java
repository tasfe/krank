package org.crank.crud.criteria;

import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;
import org.crank.crud.test.model.Employee;

public class VerifiedComparisonTest {

	@Test()
	public void testVerify() {
		
		VerifiedComparison comparison =
			new VerifiedComparison(Employee.class, "department.name",
					Operator.EQ, null);
		
		assertNotNull(comparison);
		
		try {
			comparison =
				new VerifiedComparison(Employee.class, "dept.name",
					Operator.EQ, null);
		} catch (RuntimeException ex) {
			assertTrue(true);
		}
	}

}
