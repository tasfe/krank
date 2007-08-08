package org.crank.validation.validators;

import static org.testng.AssertJUnit.*;
import java.util.ResourceBundle;

import org.crank.core.ResourceBundleLocator;
import org.crank.validation.ValidatorMessage;
import org.crank.validation.validators.RegexValidator;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * 
 *
 * <p>
 * </p>
 * @author Rick Hightower
 */
public class RegexValidatorTest {

    private RegexValidator validator = new RegexValidator();
    private ResourceBundle bundle = ResourceBundle.getBundle(TestConstants.TEST_BUNDLE);
    private ResourceBundleLocator resourceBundleLocator = new ResourceBundleLocator() {
		public ResourceBundle getBundle() {
			return bundle;
		}
    };

    @BeforeMethod
    public void setUp() {
        validator = new RegexValidator();
        validator.setMatch("");
        validator.setResourceBundleLocator(resourceBundleLocator);
        validator.setDetailMessage("validator.regex.detail");
        validator.setSummaryMessage("validator.regex.summary");
    }
    @Test()
	public void testRegexValidatorWithError() {
        ValidatorMessage message = (ValidatorMessage) validator.validate("crap", "");
        assertTrue("A validation error is expected; the string does "
                + "not match the pattern", message.hasError());
    }
    @Test()
	public void testRegexValidatorWithNoError() {
        validator.setMatch("^crap$");
        ValidatorMessage message = (ValidatorMessage) validator.validate("crap", "");
        assertFalse("No validation error is expected; the string "
                + "does match the pattern", message.hasError());
    }
    @Test()
	public void testRegexValidatorWithErrorNegated() {
        validator.setNegate(true);
        ValidatorMessage message = (ValidatorMessage) validator.validate("crap", "" );
        assertFalse("A validation errors is not expected; the string does "
                + "not match the pattern as expected", message.hasError());
    }
    @Test()
	public void testRegexValidatorWithNoErrorNegated() {
        validator.setNegate(true);
        validator.setMatch("^crap$");
        ValidatorMessage message = (ValidatorMessage) validator.validate("crap", "");
        assertTrue("A validation error is expected because the string does "
                + "match the pattern and it should not", message.hasError());
    }

    @Test()
	public void testLastNameRegex() {
        validator.setMatch("^([a-zA-Z]|[ -])*$");
        ValidatorMessage message = (ValidatorMessage) validator.validate("123", "");
        assertTrue(message.hasError());
        message = (ValidatorMessage) validator.validate("hhh hhh-", "");
        assertFalse(message.hasError());
        message = (ValidatorMessage) validator.validate("hhh hhh-1", "");
        assertTrue(message.hasError());

    }

    @Test()
	public void testAddressRegex() {
        validator.setMatch("^(\\d+ \\w+)|(\\w+ \\d+)$");
        ValidatorMessage message = (ValidatorMessage) validator.validate("123", "");
        assertTrue(message.hasError());
        message = (ValidatorMessage) validator.validate("Main", "");
        assertTrue(message.hasError());
        message = (ValidatorMessage) validator.validate("123 Main", "");
        assertFalse(message.hasError());
        message = (ValidatorMessage) validator.validate("Main 123", "");
        assertFalse(message.hasError());

    }

    @Test
    public void testOurAddress() {
        validator.setMatch("\\d+");
        ValidatorMessage message = (ValidatorMessage) validator.validate("123", "");
        assertFalse(message.hasError());
        
        validator.setMatch( "([a-zA-Z]|\\d+)+" );
        message = (ValidatorMessage) validator.validate("abc", "");
        assertFalse(message.hasError());

//        validator.setMatch( "([a-zA-Z]|\\d+)+" );
//        message = (ValidatorMessage) validator.validate("123 Main St", "");
//        assertFalse(message.hasError());
        
    }

    @Test
    public void testEmail() {
        validator.setMatch("^[_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.(([0-9]{1,3})|([a-zA-Z]{2,3})|(aero|coop|info|museum|name))$");
        ValidatorMessage message = (ValidatorMessage) validator.validate("rick@arc-mind.com", "");
        assertFalse(message.hasError());

        validator.setMatch("^[_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.(([0-9]{1,3})|([a-zA-Z]{2,3})|(aero|coop|info|museum|name))$");
        message = (ValidatorMessage) validator.validate("rick_arc-mind.com", "");
        assertTrue(message.hasError());
        
        
    }

}
