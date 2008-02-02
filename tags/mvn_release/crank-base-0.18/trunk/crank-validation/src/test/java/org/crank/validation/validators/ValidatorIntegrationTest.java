package org.crank.validation.validators;

import org.crank.validation.FieldValidator;
import org.crank.validation.ValidatorMessage;
import org.crank.validation.ValidatorMessageHolder;
import org.crank.validation.ValidatorMessages;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


import junit.framework.TestCase;

/**
 * ValidatorIntegrationTest
 *
 * <p>
 * <small>
 * $File: //depot/projects/site/main/src/test/java/net/lmb/model/validator/impl/ValidatorIntegrationTest.java $ <br/>
 * $Change: 14195 $ submitted by $Author: rhightower $ at $DateTime: 2005/07/04 12:37:03 $
 * </small>
 * </p>
 * @author $Author: rhightower $
 * @version $Revision: #4 $
 */
public class ValidatorIntegrationTest extends TestCase {    


    private ApplicationContext applicationContext =
    new ClassPathXmlApplicationContext("/org/crank/validation/validators/validatorContext.xml");

    /**
     * 
     */
    @Override
    public void setUp() {
        //empty
    }

    public void testAreaCode()  throws Exception {
        FieldValidator validator = (FieldValidator) applicationContext.getBean("areaCode");

        validateErrorsPresent(validator, "ab", 2);
        validateErrorsPresent(validator, "777", 1);
        validateErrorsPresent(validator, "100", 1);
        validateErrorsPresent(validator, "301", 0);
    }

    public void testEmail()  throws Exception {
        FieldValidator validator = (FieldValidator) applicationContext.getBean("email");

        validateErrorsPresent(validator, "rick@rick.com", 0);
        validateErrorsPresent(validator, "rick.cic", 1);
        validateErrorsPresent(validator, "rick@rick", 1);
    }


    public void testReproduceJiraSRA120()  throws Exception {
        FieldValidator validator = (FieldValidator) applicationContext.getBean("address");

        validateErrorsPresent(validator, "address 123", 0);
        validateErrorsPresent(validator, "0 address", 0);
        validateErrorsPresent(validator, "123", 1);
        validateErrorsPresent(validator, "foo", 1);
    }

    private void validateErrorsPresent(FieldValidator validator, String value, final int expectedValidationErrors) {
        ValidatorMessageHolder messages = validator.validate(value, "");
        int errors = 0;
        if (messages instanceof ValidatorMessages) {
             errors = errorCount((ValidatorMessages) messages);
        } else {
            if ( ((ValidatorMessage) messages).hasError()) {
                errors = 1;
            }
        }
        assertEquals(expectedValidationErrors, errors);
    }

    private int errorCount(ValidatorMessages messages) {
        int errors = 0;
        for (ValidatorMessage message : messages) {
            if (message.hasError()) {
                errors++;
            }
        }
        return errors;
    }


}
