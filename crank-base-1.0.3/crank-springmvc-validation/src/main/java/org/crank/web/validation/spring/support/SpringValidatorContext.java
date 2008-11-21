package org.crank.web.validation.spring.support;

import org.crank.validation.ValidationContext;


@Deprecated ()
public class SpringValidatorContext {
    private ValidationContext validationContext;

	private SpringValidatorContext () {
		
	}

    public static void create() {
        ValidationContext.create();
    }

    public static void destroy() {
        ValidationContext.destroy();
    }

    public static SpringValidatorContext get () {
        SpringValidatorContext context = new SpringValidatorContext();
        context.validationContext = ValidationContext.get();
        return context;
	}


	public String getBindingPath() {
        return ValidationContext.getBindingPath();
    }


    public Object getParentObject() {
        return validationContext.getParentObject();
    }

    public void pushProperty(String property) {
        validationContext.pushProperty(property);        
    }

    public void setParentObject(Object object) {
        validationContext.setParentObject(object);
    }

    public void pop() {
        validationContext.pop();
    }

}
