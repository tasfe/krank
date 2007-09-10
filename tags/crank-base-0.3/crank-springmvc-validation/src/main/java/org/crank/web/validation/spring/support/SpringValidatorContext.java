package org.crank.web.validation.spring.support;

import java.util.Stack;

public class SpringValidatorContext {
	private Stack<String> bindingPath = new Stack<String>();
    private Object parentObject;
	private static ThreadLocal<SpringValidatorContext> validatorContext = new ThreadLocal<SpringValidatorContext>();

	private SpringValidatorContext () {
		
	}
	
	public static SpringValidatorContext create() {
		validatorContext.set(new SpringValidatorContext());
		return get();
	}
	
	public static SpringValidatorContext get () {
		return validatorContext.get();
	}

	public static void destroy() {
		validatorContext.set(null);
	}
	

	public static String getBindingPath() {
		if (validatorContext.get() != null) {
			return validatorContext.get().calculateBindingPath();
		}
		return "";
	}

	private String calculateBindingPath() {
		StringBuilder builder = new StringBuilder(255);
		int index = 0;
		for (String component : bindingPath) {
			index++;
			builder.append(component);
			if (index!=bindingPath.size()) {
				builder.append('.');
			}
		}
		return builder.toString();
	}

	public void pop () {
		bindingPath.pop();
	}

	public void pushProperty (final String component) {
		bindingPath.push(component);
	}
	public void pushObject (final Object object) {
		String simpleName = object.getClass().getSimpleName();
		simpleName = simpleName.substring(0,1).toLowerCase() + simpleName.substring(1,simpleName.length());
		bindingPath.push(simpleName);
	}

    public void setParentObject( Object object ) {
        this.parentObject = object;
        
    }

    public Object getParentObject() {
        return parentObject;
    }

}
