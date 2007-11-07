/**
 * 
 */
package org.crank.web.validation.jsf.support;

import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;

import org.crank.validation.ValidationContext;

/** This class implements the ValidationCotext for JSF. 
 *  It was a pain to write.
 * */
class JSFValidationContext extends ValidationContext {
    
    /** The form that holds the fields. */
    private UIForm form;
    
    /** 
     * Create the JSFValidation context passing a input field.
     * @param input
     */
    public JSFValidationContext (UIInput input) {
        /* We need the form that holds this field. */
        this.form = JSFComponentTreeUtils.findForm(input);
    }
    
    /**
     * Register this bad boy so we can access it from our FieldValidators.
     */
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
    
    /**
     * Here is the magic of this class. We need to get the value
     * of a property in the validation phase before it gets applied to the
     * actual model object. Thus, we need to grab the value from the actual
     * component since at this point it is only a proposed value.
     * 
     * 
     */
    @Override
    public Object getProposedPropertyValue(String propertyName) {
        
        /* We have to find the component that holds the proposed value. */
        UIInput input = JSFComponentTreeUtils.findInput(form, propertyName);
        
        /* If we could not find it, complain since this means it is
         * likely missconfigured. */
        if (input==null) {
            throw new RuntimeException("Could not find the component" +
                    " for propertyName=" + propertyName + 
                    " for form=" + form.getId());
        }
    
        /* Extract the value from the input component 
         * force it to convert if needed. */
        return extractValueForceValidationIfNeeded(input);
    }

    
    /**
     * This method provides two bits of magic JSF programming.
     * 
     * This method extracts the value from the input component and 
     * forces the component to convert if needed. 
     * 
     * See, the problem is that the component may not be validated yet.
     * 
     * If it has not been validated yet, then the converter has not
     * been invoked yet and the value is null.
     * 
     * Thus, we need to force it to validate if the value is null.
     * 
     * @param input
     * @return
     */
    private Object extractValueForceValidationIfNeeded(UIInput input) {
        /* See if the value is null, if it is force conversion and validation. 
         * You have to understand the JSF lifecycle to understand why we
         * are doing this.
         * */
        if (input.getValue() == null){
            try {
                /* Force validation which will force conversion. */
                input.validate(FacesContext.getCurrentInstance());
            } catch (Exception ex) {
                /* We don't actually want the validation errors. 
                 * We just want the conversion to happen.
                 * JSF will call validate when it wants the actual
                 * error messages.
                 * */
                //ignore FacesExceptions
            }
        }
        return input.getValue();

    }
}