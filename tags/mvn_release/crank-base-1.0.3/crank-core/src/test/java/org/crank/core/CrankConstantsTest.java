package org.crank.core;
import static org.testng.AssertJUnit.assertNotNull;

import org.testng.annotations.Test;
public class CrankConstantsTest {
	@Test(groups="unittest")
	public void testConstants() {
		assertNotNull(CrankConstants.FRAMEWORK_DELIM);
		assertNotNull(CrankConstants.FRAMEWORK_PREFIX);
		assertNotNull(CrankConstants.LOG);
		assertNotNull(CrankConstants.OBJECT_REGISTRY);
	}
}
