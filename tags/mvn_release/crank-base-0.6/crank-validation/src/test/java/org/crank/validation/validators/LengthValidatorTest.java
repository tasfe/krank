package org.crank.validation.validators;

import static org.testng.AssertJUnit.*;
import java.util.ResourceBundle;

import org.crank.core.ResourceBundleLocator;
import org.crank.validation.ValidatorMessage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * 
 *
 * <p>
 * </p>
 * @author Rick Hightower
 */
public class LengthValidatorTest {

    private LengthValidator validator = new LengthValidator();
    private ResourceBundle bundle = ResourceBundle.getBundle(TestConstants.TEST_BUNDLE);
    private ResourceBundleLocator resourceBundleLocator = new ResourceBundleLocator() {
		public ResourceBundle getBundle() {
			return bundle;
		}
    };

    @BeforeMethod
    public void setUp() {
        validator = new LengthValidator();
        validator.setResourceBundleLocator(resourceBundleLocator);
        validator.setDetailMessage("validator.length.detail");
        validator.setSummaryMessage("validator.length.summary");
    }
    @Test()
	public void test() {
        ValidatorMessage message = (ValidatorMessage) validator.validate("", "");
        assertFalse(message.hasError());

        validator.setMin( 2L );
        message = (ValidatorMessage) validator.validate("Ri", "");
        assertFalse("Ri is just two letters and should pass", message.hasError());

        validator.setMin( 5L );
        message = (ValidatorMessage) validator.validate("Ri", "");
        assertTrue("Ri is does not have 5 letters", message.hasError());
    
        validator.setMin( 5L );
        message = (ValidatorMessage) validator.validate("", "");
        assertTrue("Ri is does not have 5 letters", message.hasError());
        
        
        validator.setMin( 0L );
        validator.setMax( 10L );
        message = (ValidatorMessage) validator.validate("RichardMNixonIII", "");
        assertTrue(message.hasError());

        validator.setMax( 10L );
        message = (ValidatorMessage) validator.validate("R", "");
        assertFalse(message.hasError());

    }

}
