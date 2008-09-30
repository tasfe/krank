package org.crank.web.validation.jsf.support;

import java.io.IOException;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

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
public class ValidationScriptGeneratorComponent extends UIOutput {

    /**
     * The main collborator, this class delegates most functionality to the
     * ValidationScriptReaper.
     */
    private ValidationScriptReaper validationScriptReaper = null;

    /** Holds the name of the form. */
    private String formName;

    /** Holds the object expression. */
    private String objectExpression = null;

    /**
     * Holds a list of propertyNames that we want to generate validation rule
     * calls for.
     */
    private String propertyNames;
    
    public String toString() {
        StringBuilder builder = new StringBuilder(100);
        builder.append("Form Name =");
        builder.append(formName);
        builder.append("\n");
        builder.append("Object Expression =");
        builder.append(_getObjectExpression());
        builder.append("\n");
        builder.append("Property Names =");
        builder.append(propertyNames);
        builder.append("\n");
        
        return builder.toString();
    }

    /** The type of component. */
    public final static String COMPONENT_TYPE = 
        "org.crank.ValidationScriptGenerator";

    public ValidationScriptGeneratorComponent() {
        setRendererType(null);
    }

    /**
     * Outputs the resources, and the validation form function calls.
     */
    @Override
    public void encodeEnd(FacesContext context) throws IOException {
        //ystem.out.println("ENCODE END " + this);
        ResponseWriter writer = context.getResponseWriter();
        encodeJavaScriptValidationLibraryInclude(writer);
        encodeValidateFormFunctionCalls(writer);
    }

    /** Save component state. */
    @Override
    public Object saveState(FacesContext context) {
        Object values[] = new Object[5];
        values[0] = super.saveState(context);
        values[1] = formName;
        values[2] = _getObjectExpression();
        values[3] = propertyNames;
        values[4] = parentClass;
        //ystem.out.println("SAVE STATE " + this);
        return ((Object) (values));
    }

    /** Restore component state. */
    @Override
    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        formName = (String) values[1];
        objectExpression = ((String) values[2]);
        propertyNames = (String) values[3];
        parentClass = (Class<?>)values[4]; 
        //ystem.out.println("RESTORE STATE " + this);
    }

    /**
     * This method encodes the call to include the JavaScript validation
     * library.
     * 
     * @param writer
     * @throws IOException
     */
    private void encodeJavaScriptValidationLibraryInclude(ResponseWriter writer)
            throws IOException {
        writer.write("<script language='JavaScript' SRC='"
                + ClientValidatorConstants.VALIDATOR_RESOURCE_VIEW_ID
                + ClientValidatorConstants.EXTENTION
                + "?name=clientvalidators&amp;type=js'></script>\n");
    }

    /**
     * This method encodes validation function calls for the form.
     * 
     * @param writer
     * @throws IOException
     */
    private void encodeValidateFormFunctionCalls(ResponseWriter writer)
            throws IOException {
        assert writer != null : "The writer is not null";
        assert propertyNames != null : "The propertyNames is not null";
        getValidationScriptReaper().outputFieldValidation(writer,
                findParentClass(), propertyNames.split(","), 
                getFormName());
    }

    private Class<?> parentClass = null;
    private Class<?> findParentClass() {
        if (parentClass==null) {
            parentClass = lookupParentObject().getClass();
        }
        return parentClass;
    }

    /**
     * Look up the parent object.
     * 
     * @return
     */
    @SuppressWarnings("deprecation")
    protected Object lookupParentObject() {
        assert _getObjectExpression() != null : "objectExpression is null";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Object parentObject = null;
        try {
            parentObject = facesContext.getApplication().evaluateExpressionGet(
                    facesContext, _getObjectExpression(), Object.class);
            if (parentObject == null) {
                parentObject = facesContext.getApplication().createValueBinding(
                        _getObjectExpression()).getValue(facesContext);
            }
        } catch (Exception ex) {
            parentObject = facesContext.getApplication().createValueBinding(
                    _getObjectExpression()).getValue(facesContext);

        }
        assert parentObject != null : "Parent object was found";
        return parentObject;
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
        if (formName == null && _getObjectExpression() != null) {
            int lastDotLocation = _getObjectExpression().lastIndexOf('.');
            String objectName = null;
            if (lastDotLocation!=-1) {
                objectName = _getObjectExpression().substring(lastDotLocation + 1,
                    _getObjectExpression().length() - 1);
            } else {
                objectName = _getObjectExpression().substring(2,
                        _getObjectExpression().length() - 1);
            }
            formName = objectName + "Form";
        }

        if (formName == null) {
            formName = JSFComponentTreeUtils.findForm(this).getClientId(
                    FacesContext.getCurrentInstance());
        }
        assert formName != null : "The formName is not null";
        return formName;
    }

    public void setFormName(String form) {
        this.formName = form;
    }

    public void setValidationScriptReaper(ValidationScriptReaper 
            validationScriptReaper) {
        this.validationScriptReaper = validationScriptReaper;
    }

    public String getObjectExpression() {
        return _getObjectExpression();
    }

    public void setObjectExpression(String objectExpression) {
        this.objectExpression = objectExpression;
    }

    public String getPropertyNames() {
        return propertyNames;
    }

    public void setPropertyNames(String propertyNames) {
        this.propertyNames = propertyNames;
    }


    /**
     * @return the objectExpression
     */
    private String _getObjectExpression() {
        if (objectExpression==null) {
            objectExpression = this.getValueExpression("objectExpression").getExpressionString();
        }
        return objectExpression;
    }

}