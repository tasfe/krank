package org.crank.validation.validators;

import junit.framework.TestCase;

import org.crank.validation.ValidationContext;
import org.crank.validation.ValidatorMessage;

public class DomainValidatorTest extends TestCase {

    private DomainValidator validator;

    public void setUp() {
        validator = new DomainValidator();
    }

	public void testDomainValidator() {
		
    	DomainParent parent = new DomainParent();
    	DomainChild child = new DomainChild();
    	
    	child.setName("First born");
    	child.setScaleFactor(40);
    	child.setParent(parent);
    	parent.getChildren().add(child);
    	validator.setRootObject(child);
        ValidatorMessage message = (ValidatorMessage) validator.validate(40, "scaleFactor");
        assertFalse(message.hasError());
        
        child = new DomainChild();
    	child.setName("Favorite child");
    	child.setScaleFactor(60);
    	child.setParent(parent);
    	parent.getChildren().add(child);
    	validator.setRootObject(child);
        message = (ValidatorMessage) validator.validate(60, "scaleFactor");
        assertFalse(message.hasError());
        
        child = new DomainChild();
    	child.setScaleFactor(0);
    	child.setParent(parent);
    	parent.getChildren().add(child);
    	validator.setRootObject(child);
        message = (ValidatorMessage) validator.validate("", "name");
        assertTrue(message.hasError());
        
        child = new DomainChild();
    	child.setName("Wild child");
    	child.setScaleFactor(30);
    	child.setParent(parent);
    	parent.getChildren().add(child);
    	validator.setRootObject(child);
        message = (ValidatorMessage) validator.validate(30, "scaleFactor");
        assertTrue(message.hasError());
        
    }
	
	// Validation context for the test
	static class TestValidationContext extends ValidationContext {

		@Override
		public Object getProposedPropertyValue(String propertyName) {
			return propertyName;
		}
		
	    @Override
	    protected void register(ValidationContext context) {
	        super.register(context);
	    }
	    
	    /**
	     * Free. Set yourself free. If you love something set it free.
	     * Free as in beer.
	     * 
	     * This method frees the context from the Thread local variable.
	     */
	    void free() {
	        super.register(null);
	    }
	    
	}

	
}
