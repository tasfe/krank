package org.crank.web.validation.jsf.support;


import javax.faces.context.FacesContext;

import org.crank.annotations.design.DependsOnJSF;

/** 
 * Utility class.
 * @author Rick Hightower
 *
 */
class ValidatorData {
	protected boolean useBracketForPropertyExtraction;
	protected String parentObjectExpression;
	protected String property;
	protected int locationOfLastDot = -1;
	protected int locationOfLastBracket = -1;
	protected String propertyNameOfTheField;
	protected String expressionString;
    protected Object parentObject = null;
	void initialize() {
		if (locationOfLastBracket>locationOfLastDot) {
			useBracketForPropertyExtraction=true;
		}
	}
	private Class<?> parentClassOfTheField;

	public ValidatorData  (String expressionString, FacesContext facesContext, Class<?> parentClass, String fieldName, Object parentObject) {
        this.parentClassOfTheField = parentClass;
        this.propertyNameOfTheField = fieldName;
		this.expressionString = expressionString;
		extractParentObjectExpression();
        if (fieldName==null) {
            extractPropertyName();
        }
		this.parentObject = parentObject;
		if (this.parentObject==null) {
			lookupParentObject(facesContext);
		}
	}
	public ValidatorData  (String expressionString, FacesContext facesContext, Class<?> parentClass, String fieldName) {
        this.parentClassOfTheField = parentClass;
        this.propertyNameOfTheField = fieldName;
		this.expressionString = expressionString;
		extractParentObjectExpression();
        if (fieldName==null) {
            extractPropertyName();
        }
		/* We need the parentObject so we can read its meta-data. Use the expression to look
		 * up the parent object and then get its class. */
		lookupParentObject(facesContext);
		
		System.out.println("parentObject = " + (parentObject == null?"null" : parentObject.getClass().getName()));

		
	}

	
	/**
	 * This method can extract an parent expression from an expression.
	 * 
	 * For example: <br /> <br /> 
	 * It can take <br /> 
	 * <code>#{MyBean.foo}</code> <br />
	 * And covert it to 
	 * <code>#{MyBean}</code> <br /> <br />
	 * It can take <br /> 
	 * <code>#{MyBean[foo]}</code> <br />
	 * And covert it to 
	 * <code>#{MyBean}</code> <br /> <br />
	 * It can take <br /> 
	 * <code>#{Context['MyBean'].foo}</code> <br />
	 * And covert it to 
	 * <code>#{Context['MyBean']}</code> <br /> <br />
	 * @param expressionString
	 * @return
	 */
	protected void extractParentObjectExpression() {
		
		
		this.locationOfLastBracket = expressionString.lastIndexOf('[');
		this.locationOfLastDot = expressionString.lastIndexOf('.');
		this.initialize();
		if (this.useBracketForPropertyExtraction) {
			this.parentObjectExpression = expressionString.substring(0,this.locationOfLastBracket) + "}";
		} else {		
			
			this.parentObjectExpression = expressionString.substring(0,this.locationOfLastDot) + "}";
		}
		assert parentObjectExpression != null;
	}
	/**
	 * This method can extract a property name from an expression.
	 * 
	 * For example: <br /> <br /> 
	 * It can take <br /> 
	 * <code>#{MyBean.foo}</code> <br />
	 * And covert it to 
	 * <code>#{foo}</code> <br /> <br />
	 * It can take <br /> 
	 * <code>#{MyBean[foo]}</code> <br />
	 * And covert it to 
	 * <code>#{foo}</code> <br /> <br />
	 * It can take <br /> 
	 * <code>#{Context['MyBean'].foo}</code> <br />
	 * And covert it to 
	 * <code>#{foo}</code> <br /> <br />
	 * @param expressionString
	 * @return
	 */
	protected void extractPropertyName() {
		if (useBracketForPropertyExtraction) {
			propertyNameOfTheField = expressionString.substring(locationOfLastBracket+1,
					expressionString.lastIndexOf(']'));			
		}
		else {
			propertyNameOfTheField = expressionString.substring(locationOfLastDot+1,expressionString.length()-1);
		}
		assert propertyNameOfTheField != null;
	}
	/** This method looks up the parent object given a parentObjectExpression (Universal EL expression)
	 * 
	 * @param facesContext
	 * @param parentObjectExpression
	 * @return
	 */
	@DependsOnJSF
	@SuppressWarnings("deprecation")
	protected Object lookupParentObject(FacesContext facesContext) {
        if (parentClassOfTheField==null) {
    		if (parentObject!=null) {
    		    return parentObject;
            }
    		assert parentObjectExpression != null;
    		try {
    			parentObject = facesContext.getApplication().evaluateExpressionGet(facesContext, parentObjectExpression, Object.class);
    			// JSF 1.1
    			if (parentObject==null) {
    				parentObject = facesContext.getApplication().createValueBinding(parentObjectExpression).getValue(facesContext);
    			}
    			// JSF 1.2
    			if (parentObject==null) {
    				parentObject = facesContext.getApplication()
    				                           .getExpressionFactory()
    				                           .createValueExpression(facesContext.getELContext(), parentObjectExpression, parentClassOfTheField)
    				                           .getValue(facesContext.getELContext());
    			}
    			if (parentObject==null) {
    				parentObject = facesContext.getApplication()
    				                           .getExpressionFactory()
    				                           .createValueExpression(facesContext.getELContext(), parentObjectExpression, Object.class)
    				                           .getValue(facesContext.getELContext());
    			}
    		} catch (Exception ex) {
    			//System.out.println("All attempted methods failed!");
    			ex.printStackTrace();
    			assert facesContext != null;
    			parentObject = facesContext.getApplication().createValueBinding(parentObjectExpression).getValue(facesContext);
    			//System.out.println("Method in catch block = " + (parentObject==null?"failed":"passed"));
    			assert parentObject !=null;
    		}
        }
        
        if (parentObject!=null) {
        	this.parentClassOfTheField = parentObject.getClass();
        }
        return parentObject;
	}
	public Class<?> getParentClassOfTheField() {
		return parentClassOfTheField;
	}
	public String getPropertyNameOfTheField() {
		return propertyNameOfTheField;
	}
}