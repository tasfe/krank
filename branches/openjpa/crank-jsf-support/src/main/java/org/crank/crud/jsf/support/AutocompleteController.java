package org.crank.crud.jsf.support;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;

import org.crank.core.CrankValidationException;
import org.crank.crud.controller.CrudControllerListener;
import org.crank.crud.controller.CrudEvent;
import org.crank.crud.controller.datasource.FilteringDataSource;
import org.crank.crud.criteria.OrderBy;
import org.crank.crud.criteria.OrderDirection;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import static org.crank.crud.criteria.Comparison.startsLike;
import static org.crank.crud.criteria.Comparison.eq;

public class AutocompleteController implements CrudControllerListener {

    private String propertyName;
    private String fieldName;
    private String value;
    private FilteringDataSource dataSource;
    private UIInput component;

    public UIInput getComponent() {
		return null; // to ensure that duplicate id errors don't mysteriously popup
	}

	public void setComponent(UIInput component) {
		this.component = component;
	}

	public void setDataSource( FilteringDataSource dataSource ) {
        this.dataSource = dataSource;
    }
    
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

    public AutocompleteController() {
    	
    }
    
    public AutocompleteController( FilteringDataSource dataSource, String propertyName, String fieldName) {
        this.dataSource = dataSource;
        this.propertyName = propertyName;
        this.fieldName = fieldName;
    }
    

    public List autocomplete(Object suggest) {

        String pref = (String)suggest;
        
        return getList(pref);
    }

	public void afterCancel(CrudEvent event) {
		// This implementation does nothing here
	}

	public void afterCreate(CrudEvent event) {
		handleReadEvent(event);
	}

	public void afterDelete(CrudEvent event) {
		// This implementation does nothing here
	}

	public void afterRead(CrudEvent event) {
		handleReadEvent(event);
	}

	public void afterUpdate(CrudEvent event) {
		// This implementation does nothing here
	}

	public void beforeCancel(CrudEvent event) {
		// This implementation does nothing here
	}

	public void beforeCreate(CrudEvent event) {
		handleCreateUpdate(event);
	}

	public void beforeDelete(CrudEvent event) {
		// This implementation does nothing here
	}

	public void beforeRead(CrudEvent event) {
		// This implementation does nothing here
	}

	public void beforeUpdate(CrudEvent event) {
		handleCreateUpdate(event);
	}

	public void afterLoadCreate(CrudEvent event) {
		this.value = null;
	}

	public void beforeLoadCreate(CrudEvent event) {
		// This implementation does nothing here
	}
	
	/**
	 * Local helper method to lookup the many to one object and then associate it with the event's entity
	 * @param event
	 */
	private void handleCreateUpdate(CrudEvent event) {
        BeanWrapper entity = new BeanWrapperImpl( event.getEntity() );
        
        List list = getListExact(value);
        if (list.size()!=1) {
        	FacesContext facesContext = FacesContext.getCurrentInstance();
        	FacesMessage message = new FacesMessage("Invalid selection", "Unable to match '" + fieldName + "' selection to '" + value + "'");
        	message.setSeverity(FacesMessage.SEVERITY_ERROR);
        	component.setValid(false);
        	facesContext.addMessage(component.getClientId(facesContext), message);
        	facesContext.renderResponse();
        	throw new CrankValidationException(message.toString());
        } else {
	        Object newValue = list.get(0);
	        entity.setPropertyValue(fieldName, newValue);
        	component.setValid(true);
        }
	}

	/**
	 * Local helper method to perform wiring of the existing crud entity value into the local value
	 * For example, for an Employee entity which has a Specialty entity as property "specialty"...
	 * and the Specialty entity has a "name" property, then the
	 * fieldName="specialty" and propertyName="name" would grab the value from the controller entity as
	 * Employee.specialty.name
	 * @param event
	 */
	private void handleReadEvent(CrudEvent event) {
        BeanWrapper entity = new BeanWrapperImpl( event.getEntity() );
        Object fieldValue = entity.getPropertyValue(fieldName);
        
    	this.value = null;
    	
        if (fieldValue != null) {
        	BeanWrapper entityProp = new BeanWrapperImpl(fieldValue);
            this.value = (String) entityProp.getPropertyValue(propertyName);
        }
	}

	/**
	 * Local helper method to perform a criteria lookup on the instance's data source
	 * @param pref
	 * @return
	 */
	private List getList(String pref) {
		OrderBy orderBy = new OrderBy(propertyName, OrderDirection.ASC);
		
        /* Clear the comparison group b/c we are about to recreate it */
        dataSource.group().clear();
        
        /* Add the criteria */
        dataSource.group().add(startsLike(propertyName,pref));
        
        /* Set the orderBy list. */
        dataSource.setOrderBy( new OrderBy[]{orderBy} );
        
        return dataSource.list();
	}

	/**
	 * Local helper method to perform an exact criteria lookup on the instance's data source
	 * @param pref
	 * @return
	 */
	private List getListExact(String pref) {
		OrderBy orderBy = new OrderBy(propertyName, OrderDirection.ASC);
		
        /* Clear the comparison group b/c we are about to recreate it */
        dataSource.group().clear();
        
        /* Add the criteria */
        dataSource.group().add(eq(propertyName,pref));
        
        /* Set the orderBy list. */
        dataSource.setOrderBy( new OrderBy[]{orderBy} );
        
        return dataSource.list();
	}

	public void afterLoadListing(CrudEvent event) {
		// TODO Auto-generated method stub
		
	}

	public void beforeLoadListing(CrudEvent event) {
		// TODO Auto-generated method stub
		
	}

}
