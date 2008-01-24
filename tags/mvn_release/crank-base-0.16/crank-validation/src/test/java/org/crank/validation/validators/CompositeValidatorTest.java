package org.crank.validation.validators;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;
import java.util.ArrayList;
import java.util.List;

import org.crank.validation.FieldValidator;
import org.crank.validation.ValidatorMessage;
import org.crank.validation.ValidatorMessages;

/**
 * Test composite validator.
 *
 * <p>
 * <small>
 * $File: //depot/projects/site/main/src/test/java/net/lmb/model/validator/impl/CompositeValidatorTest.java $ <br/>
 * $Change: 14144 $ submitted by $Author: rhightower $ at $DateTime: 2005/06/30 18:01:41 $
 * </small>
 * </p>
 * @author $Author: rhightower $
 * @version $Revision: #1 $
 */
public class CompositeValidatorTest {

    private CompositeValidator validator = new CompositeValidator();
    private List<FieldValidator> validatorList = new ArrayList<FieldValidator>();
    private int count = 0;
    

    private FieldValidator pass1 = new FieldValidator() {

        public ValidatorMessage validate(Object object, String fieldLabel) {
            return new ValidatorMessage();
        }

    };
    class Fail implements FieldValidator {

        public ValidatorMessage validate(Object object, String fieldLabel) {
            count ++;
            ValidatorMessage message = new ValidatorMessage();
            message.setHasError( true );
            return message;
        }

    };
    Fail fail = new Fail();


    @BeforeMethod
    protected void setUp() throws Exception {
        count = 0;
        validator = new CompositeValidator();
        validatorList = new ArrayList<FieldValidator>();
        validatorList.add(pass1);
        validatorList.add(pass1);
        validator.setValidatorList(validatorList);
    }

    @Test()
	public void testCompositePass() {

        ValidatorMessages messages = (ValidatorMessages) validator.validate("crap", "fieldLabel");
        assertNotNull(messages);

        for (ValidatorMessage message : messages) {
            assertNotNull(message);
            assertFalse(message.hasError());
        }

    }

    @Test()
    public void testCompositeWithRequired() {

        RequiredValidator requiredValidator = new RequiredValidator();
        StopOnRuleValidator onRuleValidator = new StopOnRuleValidator();
        validatorList.add( requiredValidator );
        validatorList.add( onRuleValidator );
        validatorList.add( fail );
        validatorList.add( fail );
        validator.setValidatorList(validatorList);
        
        ValidatorMessages messages = (ValidatorMessages) validator.validate("crap", "fieldLabel");
        assertNotNull(messages);

        assertEquals( 1, count );
    }
    @Test
    public void testCompositeWithRequired1() {

        RequiredValidator requiredValidator = new RequiredValidator();
        StopOnRuleValidator onRuleValidator = new StopOnRuleValidator();
        onRuleValidator.setRuleName( "fail" );
        validatorList.add( requiredValidator );
        validatorList.add( onRuleValidator );
        validatorList.add( fail );
        validatorList.add( fail );
        validator.setValidatorList(validatorList);
        
        ValidatorMessages messages = (ValidatorMessages) validator.validate("crap", "fieldLabel");
        assertNotNull(messages);

        assertEquals( 1, count );
    }
    @Test
    public void testCompositeWithRequired2() {

        StopOnRuleValidator onRuleValidator = new StopOnRuleValidator();
        onRuleValidator.setRuleName( "fail" );
        validatorList.add( onRuleValidator );
        validatorList.add( fail );
        validatorList.add( fail );
        validator.setValidatorList(validatorList);
        
        ValidatorMessages messages = (ValidatorMessages) validator.validate("crap", "fieldLabel");
        assertNotNull(messages);

        assertEquals( 1, count );
    }
    @Test    
    public void testCompositeWithRequired3() {

        StopOnRuleValidator onRuleValidator = new StopOnRuleValidator();
        onRuleValidator.setRuleName( "fail" );
        validatorList.add( onRuleValidator );
        validatorList.add( fail );
        validatorList.add( fail );
        validator.setValidatorList(validatorList);
        
        ValidatorMessages messages = (ValidatorMessages) validator.validate("", "fieldLabel");
        assertNotNull(messages);

        assertEquals( 0, count );
    }
    @Test
    public void testCompositeWithRequired4() {

        StopOnRuleValidator onRuleValidator = new StopOnRuleValidator();
        onRuleValidator.setRuleName( "fail" );
        validatorList.add( onRuleValidator );
        validatorList.add( fail );
        validatorList.add( fail );
        validator.setValidatorList(validatorList);
        
        ValidatorMessages messages = (ValidatorMessages) validator.validate(null, "fieldLabel");
        assertNotNull(messages);

        assertEquals( 0, count );
    }
    @Test
    public void testCompositeWithRequired5() {

        StopOnRuleValidator onRuleValidator = new StopOnRuleValidator();
        onRuleValidator.setRuleName( "fail" );
        validatorList.add( onRuleValidator );
        validatorList.add( fail );
        validatorList.add( fail );
        validator.setValidatorList(validatorList);
        validator.setStopOnBlank( false );
        
        ValidatorMessages messages = (ValidatorMessages) validator.validate(null, "fieldLabel");
        assertNotNull(messages);

        assertEquals( 1, count );
    }

}
