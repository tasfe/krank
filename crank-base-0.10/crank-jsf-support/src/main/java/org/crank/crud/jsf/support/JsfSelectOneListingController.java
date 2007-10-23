package org.crank.crud.jsf.support;

import java.io.Serializable;

import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import org.crank.crud.controller.CrudControllerBase;
import org.crank.crud.controller.CrudOperations;
import org.crank.crud.controller.FilterablePageable;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class JsfSelectOneListingController<T, PK extends Serializable> {
	
	private FilterablePageable paginator;
    private DataModel model = new ListDataModel();
    private CrudControllerBase<T, PK> controller;
    private boolean show;
    private String propertyName;
    private Class entityClass;
	private String idProperty="id";
	private String labelProperty="name";
	private String sourceProperty = null;
    
	
	public JsfSelectOneListingController (Class entityClass, String propertyName, FilterablePageable pageable, CrudOperations crudController, String sourceProperty) {
    	this.paginator = pageable;
    	this.controller = (CrudControllerBase<T, PK>) crudController;
    	this.propertyName = propertyName;
    	this.entityClass = entityClass;
    	this.sourceProperty = sourceProperty;
    }


	public JsfSelectOneListingController (Class entityClass, String propertyName, FilterablePageable pageable, CrudOperations crudController) {
    	this.paginator = pageable;
    	this.controller = (CrudControllerBase<T, PK>) crudController;
    	this.propertyName = propertyName;
    	this.entityClass = entityClass;
    }


	public FilterablePageable getPaginator() {
		return paginator;
	}

	public void setPaginator(FilterablePageable paginator) {
		this.paginator = paginator;
	}

    @SuppressWarnings("unchecked")
	public DataModel getModel() {
        /* Note if you wire in events from paginators, you will only have to change this
         * when there is a next page event.
         */
        model.setWrappedData( paginator.getPage() );
        return model;
    }

	public void setModel(DataModel model) {
		this.model = model;
	}
	
	public void select () {
    	BeanWrapper wrapper = new BeanWrapperImpl(this.controller.getEntity());
		Object value = this.model.getRowData();
		
		if ((sourceProperty != null) && !"".equals(sourceProperty)) {
	    	BeanWrapper valueWrapper = new BeanWrapperImpl(value);
	    	wrapper.setPropertyValue(this.propertyName, valueWrapper.getPropertyValue(this.sourceProperty));
		} else {
			wrapper.setPropertyValue(this.propertyName, value);
		}
		this.show = false;
	}
	
	public void cancel () {
		this.show = false;
	}
	
	public void showSelection() {
		this.show = true;
	}


	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

	public CrudControllerBase<T, PK> getController() {
		return controller;
	}

	public void setController(CrudControllerBase<T, PK> controller) {
		this.controller = controller;
	}


	public Class getEntityClass() {
		return entityClass;
	}


	public void setEntityClass(Class entityClass) {
		this.entityClass = entityClass;
	}    

    public String getPropertyName() {
		return propertyName;
	}


	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}


	public String getIdProperty() {
		return idProperty;
	}


	public void setIdProperty(String idProperty) {
		this.idProperty = idProperty;
	}


	public String getLabelProperty() {
		return labelProperty;
	}


	public void setLabelProperty(String labelProperty) {
		this.labelProperty = labelProperty;
	}
	
}
