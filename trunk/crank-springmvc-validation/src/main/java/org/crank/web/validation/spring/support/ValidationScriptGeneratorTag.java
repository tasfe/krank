package org.crank.web.validation.spring.support;


import java.io.IOException;
import java.io.Writer;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.crank.core.CrankContext;
import org.crank.web.validation.ClientValidatorConstants;
import org.crank.web.validation.ValidationScriptReaper;

/**
 * ValidationScriptGeneratorComponent figures out the classname by parsing the
 * Universal EL expression that are passed and looking up the parent object.
 * 
 * It then passes this informaiton to the validation script reaper which looks
 * up the validation rules and outputs them to the browser.
 * 
 * @author Rick Hightower
 */
public class ValidationScriptGeneratorTag extends SimpleTagSupport {

    /**
     * The main collborator, this class delegates most functionality to the
     * ValidationScriptReaper.
     */
    private ValidationScriptReaper validationScriptReaper = null;

    /** Holds the name of the bean. */
    private String beanName;

    /** Holds the name of the form. */
    private String formName;

    /**
     * Holds a list of propertyNames that we want to generate validation rule
     * calls for.
     */
    private String propertyNames;
    
    public String toString() {
        StringBuilder builder = new StringBuilder(100);
        builder.append("Form Name =");
        //builder.append(formName);
        builder.append("\n");
        builder.append("Property Names =");
        builder.append(propertyNames);
        builder.append("\n");
        
        return builder.toString();
    }


    public void doTag() throws JspException, IOException {
    	JspWriter writer = getJspContext().getOut();
        encodeJavaScriptValidationLibraryInclude(writer);
        encodeValidateFormFunctionCalls(writer);
    }    

    /**
     * This method encodes the call to include the JavaScript validation
     * library.
     * 
     * @param writer
     * @throws IOException
     */
    private void encodeJavaScriptValidationLibraryInclude(Writer writer)
            throws IOException {
        writer.write("<script language='JavaScript' SRC='"
                + ClientValidatorConstants.VALIDATOR_RESOURCE_VIEW_ID
                + ".crank"
                + "?name=clientvalidators&amp;type=js'></script>\n");
    }

    /**
     * This method encodes validation function calls for the form.
     * 
     * @param writer
     * @throws IOException
     */
    private void encodeValidateFormFunctionCalls(Writer writer)
            throws IOException {
        assert writer != null : "The writer is not null";
        assert propertyNames != null : "The propertyNames is not null";
        getValidationScriptReaper().outputFieldValidation(writer,
                findParentClass(), propertyNames.split(","), 
                getFormName());
    }

   @SuppressWarnings("unchecked")
   private Class findParentClass() {
    	Object object = getJspContext().findAttribute(beanName);
        assert object!=null;
    	return object.getClass();
    }


    /**
     * This allow the reaper to be injected or looked up.
     * 
     * @return
     */
    protected ValidationScriptReaper getValidationScriptReaper() {
        if (validationScriptReaper == null) {
            CrankContext.getObjectRegistry().resolveCollaborators(this);
        }
        assert validationScriptReaper != null : "Validation Reaper was found";
        return validationScriptReaper;
    }

    /**
     * Figure out the formname if it is missing by finding the form in the tree
     * and returning its name.
     */
    private String getFormName() {
    	if (formName == null) {
    		assert beanName != null;
    		return beanName + "Form";
    	}
    	return formName;
    }


    public void setValidationScriptReaper(ValidationScriptReaper 
            validationScriptReaper) {
        this.validationScriptReaper = validationScriptReaper;
    }


    public String getPropertyNames() {
        return propertyNames;
    }

    public void setPropertyNames(String propertyNames) {
        this.propertyNames = propertyNames;
    }


	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}


	public String getBeanName() {
		return beanName;
	}


	public void setFormName(String formName) {
		this.formName = formName;
	}

}