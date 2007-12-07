package org.crank.crud.jsf.support;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import org.crank.crud.controller.CrudControllerBase;
import org.crank.crud.controller.CrudOperations;
import org.crank.crud.controller.FilterablePageable;
import org.crank.crud.controller.Row;
import org.crank.crud.controller.SelectListener;
import org.crank.crud.controller.SelectSupport;
import org.crank.crud.controller.Selectable;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class JsfSelectOneListingController<T, PK extends Serializable> implements Selectable{
	
	private FilterablePageable paginator;
    private DataModel model = new ListDataModel();
    private boolean show;
    private String propertyName;
    private Class entityClass;
	private String idProperty="id";
	private String labelProperty="name";
	private String sourceProperty = null;
	private SelectSupport selectSupport = new SelectSupport();
	private CrudControllerBase<T, PK> controller;	
    
	
	public JsfSelectOneListingController (Class entityClass, String propertyName, FilterablePageable pageable, CrudOperations crudController) {
    	this.paginator = pageable;
    	controller = (CrudControllerBase<T, PK>) crudController;
    	this.propertyName = propertyName;
    	this.entityClass = entityClass;
    }

	public JsfSelectOneListingController (Class entityClass, String propertyName, FilterablePageable pageable, CrudOperations crudController, String sourceProperty) {
    	this(entityClass, propertyName, pageable, crudController);
    	this.sourceProperty = sourceProperty;
    }

	public JsfSelectOneListingController (Class entityClass, FilterablePageable pageable) {
		this(entityClass, null, null, pageable);
    }

	private Object parentEntity = null;
	public JsfSelectOneListingController (Class entityClass, Object parentEntity, String controllerProperty, FilterablePageable pageable) {
		this.entityClass = entityClass;
		this.paginator = pageable;
		this.parentEntity = parentEntity;
    	this.propertyName = controllerProperty;
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
        //model.setWrappedData( paginator.getPage() );
        List page = getPaginator().getPage();
        List<Row> wrappedList = new ArrayList<Row>(page.size());
        for (Object rowData : page) {
            Row row = new Row();
            row.setObject( rowData );
            wrappedList.add(row);
        }
        model.setWrappedData( wrappedList );

        return model;
    }

	public void setModel(DataModel model) {
		this.model = model;
	}
	
	public void select () {
		Row selectedRow = (Row) this.model.getRowData();
		Object valueBean = selectedRow.getObject();
		Object value = valueBean;
		if ((sourceProperty != null) && !"".equals(sourceProperty)) {
	    	BeanWrapper valueWrapper = new BeanWrapperImpl(valueBean);
	    	value = valueWrapper.getPropertyValue(this.sourceProperty);
		}

		BeanWrapper wrappedParentEntity =  null;
    	if (this.parentEntity != null) {
    		wrappedParentEntity = new BeanWrapperImpl(this.parentEntity);
    	} else {
    		wrappedParentEntity = new BeanWrapperImpl(controller.getEntity());
    	}
		
    	wrappedParentEntity.setPropertyValue(this.propertyName, value);
    	
		selectSupport.fireSelect(value);
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

	public void addSelectListener(SelectListener listener) {
		selectSupport.addSelectListener(listener);
	}


	public void removeSelectListener(SelectListener listener) {
		selectSupport.removeSelectListener(listener);
	}


	
}
