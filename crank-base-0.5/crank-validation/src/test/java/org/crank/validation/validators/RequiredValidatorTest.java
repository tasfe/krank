package org.crank.validation.validators;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;
import java.util.Date;
import java.util.ResourceBundle;

import org.crank.core.ResourceBundleLocator;
import org.crank.validation.ValidatorMessage;
import org.crank.validation.validators.RequiredValidator;


/**
 * Required test.
 *
 * <p>
 * </p>
 * @author Rick Hightower
 */
public class RequiredValidatorTest {
    private RequiredValidator validator;
    private ResourceBundle bundle = ResourceBundle.getBundle(TestConstants.TEST_BUNDLE);
    private ResourceBundleLocator resourceBundleLocator = new ResourceBundleLocator() {
		public ResourceBundle getBundle() {
			return bundle;
		}
    };


    @BeforeTest
    public void setUp() {
        validator = new RequiredValidator();
        validator.setResourceBundleLocator(resourceBundleLocator);
    }

    @Test()
	public void testRequiredValidator() {
        ValidatorMessage message = (ValidatorMessage) validator.validate(null, "");
        assertTrue(message.hasError());
        message = (ValidatorMessage) validator.validate("", "");
        assertTrue(message.hasError());
        message = (ValidatorMessage) validator.validate("hello", "");
        assertFalse(message.hasError());
        message = (ValidatorMessage) validator.validate(new Date(), "");
        assertFalse(message.hasError());
    }
}
