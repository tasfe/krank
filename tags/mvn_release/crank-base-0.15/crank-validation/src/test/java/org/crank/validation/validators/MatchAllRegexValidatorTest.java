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
public class MatchAllRegexValidatorTest {

    private MatchAllRegexValidator validator = new MatchAllRegexValidator();
    private ResourceBundle bundle = ResourceBundle.getBundle(TestConstants.TEST_BUNDLE);
    private ResourceBundleLocator resourceBundleLocator = new ResourceBundleLocator() {
		public ResourceBundle getBundle() {
			return bundle;
		}
    };

    @BeforeMethod
    public void setUp() {
        validator = new MatchAllRegexValidator();
        validator.setMatches( new String[] {"[a-zA-Z]+", "\\d+"});
        validator.setResourceBundleLocator(resourceBundleLocator);
        validator.setDetailMessage("validator.regex.detail");
        validator.setSummaryMessage("validator.regex.summary");
    }
    @Test()
	public void testRegexValidatorWithError() {
        ValidatorMessage message = (ValidatorMessage) validator.validate("aaa", "");
        assertTrue(message.hasError());
        
        
        message = (ValidatorMessage) validator.validate("123", "");
        assertTrue(message.hasError());
        
        message = (ValidatorMessage) validator.validate("123 Main", "");
        assertFalse(message.hasError());
        
    }

}
