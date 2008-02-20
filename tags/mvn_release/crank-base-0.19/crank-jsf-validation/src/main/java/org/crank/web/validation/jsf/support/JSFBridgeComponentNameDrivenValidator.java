package org.crank.web.validation.jsf.support;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;

import org.crank.annotations.design.DependsOnJSF;
import org.crank.core.CrankContext;
import org.crank.core.ObjectNotFound;
import org.crank.core.ObjectRegistry;
import org.crank.validation.FieldValidator;



/**
 *
 * <p>
 * Bridges from the Crank validator world to the JSF world.
 * This allows us to easily associated validators with JSF component ids.
 * </p>
 * @author Rick Hightower
 */
@DependsOnJSF
public class JSFBridgeComponentNameDrivenValidator extends AbstractJSFBridgeValidator implements Validator {


	/** Looks up the validator based on the id of the component. 
	 * It first tries to find the validator using the component id if it 
	 * does not find it with that
	 * it uses the clientId.
	 */
    protected FieldValidator findValidatorAndFieldName(
    		FacesContext facesContext, UIComponent component,
    		String [] fieldName) {
        /* Try to read the validator out of the Spring context.
         * First lookup with id, if you don't find it with component id, use the client id.
         */
    	ObjectRegistry applicationContext = CrankContext.getObjectRegistry();
        String componentId = component.getId();
        FieldValidator validator;
        
        try {
        	/* Look up the more specific validator, e.g., EmployeeForm.age. */
            String clientId = component.getClientId(facesContext);
            validator = (FieldValidator) applicationContext.getObject(clientId);
        } catch (ObjectNotFound oe) {
        	/* Look up the less specific validator, e.g., age. */
            validator = (FieldValidator) applicationContext.getObject(componentId);
        }
        return validator;
    }

    @Override
    protected void cleanup() {
        // TODO Auto-generated method stub
        
    }

}
