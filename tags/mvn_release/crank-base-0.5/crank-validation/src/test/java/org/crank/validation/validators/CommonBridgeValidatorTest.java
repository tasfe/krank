package org.crank.validation.validators;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;
import java.util.ResourceBundle;

import org.apache.commons.validator.EmailValidator;
import org.crank.core.ResourceBundleLocator;
import org.crank.validation.ValidatorMessage;

/**
 * Test the CommonBridge Validator.
 *
 * <p>
 * <small>
 * $File: //depot/projects/site/main/src/test/java/net/lmb/model/validator/impl/CommonBridgeValidatorTest.java $ <br/>
 * $Change: 14194 $ submitted by $Author: rhightower $ at $DateTime: 2005/07/04 12:28:43 $
 * </small>
 * </p>
 * @author $Author: rhightower $
 * @version $Revision: #1 $
 */
public class CommonBridgeValidatorTest {
    private CommonBridgeValidator validator;
    private ResourceBundle bundle = ResourceBundle.getBundle(TestConstants.TEST_BUNDLE);
    private ResourceBundleLocator resourceBundleLocator = new ResourceBundleLocator() {
		public ResourceBundle getBundle() {
			return bundle;
		}
    };
    /**
     * 
     */
    @BeforeMethod
    public void setUp() {
        this.validator = new CommonBridgeValidator();
        this.validator.setResourceBundleLocator(resourceBundleLocator);
        this.validator.setValidatorClass(EmailValidator.class);
        this.validator.setMethodName("isValid");
        this.validator.setFactoryMethod("getInstance");
        this.validator.setName("email");
        this.validator.init();

    }

    /**
     * 
     *
     */
    @Test()
	public void testAPIGoodEmail() {
       EmailValidator emailValidator = EmailValidator.getInstance();
       boolean valid = emailValidator.isValid("rhighotrwer@arcmind.com");
       assertTrue(valid);
    }
    /**
     * 
     *
     */
    @Test()
	public void testValidatorGoodEmail() {
       ValidatorMessage validatorMessage = (ValidatorMessage)
                       validator.validate("rhightower@arc-mind.com", "email");
       assertFalse(validatorMessage.hasError());
    }

}
