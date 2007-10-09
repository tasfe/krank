package org.crank.validation.validators;


import java.util.ResourceBundle;

import org.crank.core.ResourceBundleLocator;
import org.crank.validation.ValidatorMessage;
import org.crank.validation.validators.LongRangeValidator;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;

public class LongRangeValidatorTest {
	private LongRangeValidator longRangeValidator;
    private ResourceBundle bundle = ResourceBundle.getBundle(TestConstants.TEST_BUNDLE);
    private ResourceBundleLocator resourceBundleLocator = new ResourceBundleLocator() {
		public ResourceBundle getBundle() {
			return bundle;
		}
    };
	
	@BeforeMethod
	protected void setUp() throws Exception {
		longRangeValidator = new LongRangeValidator();
		longRangeValidator.setResourceBundleLocator(resourceBundleLocator);
		longRangeValidator.init();
	}

	@AfterMethod
	protected void tearDown() throws Exception {
	}

	@Test()
	public void testValidateWithLong() {
		longRangeValidator.setMax(100L);
		longRangeValidator.setMin(10L);
		ValidatorMessage message = 
			(ValidatorMessage) longRangeValidator.validate(new Long(21), "label");
		assertEquals(false, message.hasError());
		
		message = (ValidatorMessage) longRangeValidator.validate(new Long(101), 
				"label");
		assertEquals(true, message.hasError());

		message = (ValidatorMessage) longRangeValidator.validate(new Long(9),
				"label");
		assertEquals(true, message.hasError());
		
		//ystem.out.println(message.getDetail());
		//ystem.out.println(message.getSummary());
	}

	@Test()
	public void testValidateWithInteger() {
		longRangeValidator.setMax(100L);
		longRangeValidator.setMin(10L);
		ValidatorMessage message = (ValidatorMessage) longRangeValidator.validate(new Integer(21), "label");
		assertEquals(false, message.hasError());
		
		message = (ValidatorMessage) longRangeValidator.validate(new Integer(101), "label");
		assertEquals(true, message.hasError());

		message = (ValidatorMessage) longRangeValidator.validate(new Integer(9), "label");
		assertEquals(true, message.hasError());
		
	}

}
