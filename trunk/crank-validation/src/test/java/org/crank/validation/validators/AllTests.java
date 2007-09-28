package org.crank.validation.validators;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * 
 *
 * <p>
 * <small>
 * $File: //depot/projects/site/main/src/test/java/net/lmb/model/validator/impl/AllTests.java $ <br/>
 * $Change: 14159 $ submitted by $Author: rhightower $ at $DateTime: 2005/07/01 11:09:02 $
 * </small>
 * </p>
 * @author $Author: rhightower $
 * @version $Revision: #3 $
 */
public final class AllTests {

	/**
	 * 
	 *
	 */
	private AllTests() {
		//empty
	}

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.crank.validation");
		//$JUnit-BEGIN$
		//$JUnit-END$
		return suite;
	}

}
