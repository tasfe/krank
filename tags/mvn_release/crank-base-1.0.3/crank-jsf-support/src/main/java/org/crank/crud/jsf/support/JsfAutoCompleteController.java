package org.crank.crud.jsf.support;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import org.crank.core.CrankValidationException;
import org.crank.crud.controller.AutoCompleteController;
import org.crank.crud.controller.CrudEvent;
import org.crank.crud.controller.CrudOperations;
import org.crank.crud.controller.datasource.FilteringDataSource;

@SuppressWarnings("unchecked")
public class JsfAutoCompleteController extends AutoCompleteController {
	
    private UIInput component;

    public UIInput getComponent() {
		return null; // to ensure that duplicate id errors don't mysteriously popup
	}

	public void setComponent(UIInput component) {
		this.component = component;
	}

	public JsfAutoCompleteController() {
		super();
	}
	
	public void textChanged(ValueChangeEvent vce) {
		super.textChanged((String)vce.getNewValue());
	}
    public JsfAutoCompleteController(Class sourceClass, String sourceProperty,  
    		FilteringDataSource dataSource, CrudOperations targetCrudController, 
    		String targetProperty) {
    	super(sourceProperty, dataSource,  targetProperty, targetCrudController);
    }

	/**
	 * Local helper method to lookup the many to one object and then associate it with the event's entity
	 * @param event
	 */
	protected void handleCreateUpdate(CrudEvent event) {
     
        try {
			super.handleCreateUpdate(event);
		} catch (IllegalArgumentException e) {
        	FacesContext facesContext = FacesContext.getCurrentInstance();
        	FacesMessage message = new FacesMessage(e.getMessage());
        	message.setSeverity(FacesMessage.SEVERITY_ERROR);
        	component.setValid(false);
        	facesContext.addMessage(component.getClientId(facesContext), message);
        	facesContext.renderResponse();
        	throw new CrankValidationException(message.toString());
		}
	}


}
