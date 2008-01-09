package org.crank.crud.jsf.support;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//import javax.faces.context.FacesContext;
//import javax.faces.event.PhaseEvent;
//import javax.faces.event.PhaseId;
//import javax.faces.event.PhaseListener;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import org.crank.crud.controller.CrudControllerBase;
import org.crank.crud.controller.CrudOperations;
//import org.crank.crud.controller.CrudUtils;
import org.crank.crud.controller.FilterablePageable;
import org.crank.crud.controller.Row;
import org.crank.crud.controller.SelectListener;
import org.crank.crud.controller.SelectSupport;
import org.crank.crud.controller.Selectable;
//import org.crank.message.MessageManagerUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class JsfSelectOneListingController<T extends Serializable, PK extends Serializable> implements Selectable{
	
	/** Used to paginate over the listing and allow the user to select. */
	private FilterablePageable paginator;
	/** Represents the table's data model. */
    private DataModel model = new ListDataModel();
    /** Should the selection listing show. */
    private boolean show;
    /** Target property. */
    private String targetPropertyName;
    private Class entityClass;
	private String idProperty="id";
	private String labelProperty="name";
	private String sourcePropertyName = null;
	private SelectSupport selectSupport = new SelectSupport();
	private CrudControllerBase<T, PK> controller;	
    
//	{
//		FacesContext.getCurrentInstance().getViewRoot().addPhaseListener(
//		new PhaseListener() {
//
//			public void afterPhase(PhaseEvent event) {
//			}
//
//			public void beforePhase(PhaseEvent event) {
//				if (parentEntity == null) {
//					return;
//				}
//				BeanWrapper wrappedParentEntity = new BeanWrapperImpl(parentEntity);
//				
//				if (CrudUtils.isRequired(parentEntity.getClass(), targetPropertyName)){
//					Object newValue = wrappedParentEntity.getPropertyValue(targetPropertyName);
//					if (newValue==null) {
//						MessageManagerUtils.getCurrentInstance().addErrorMessage("GAK");
//					}
//				}
//				
//			}
//
//			public PhaseId getPhaseId() {
//				return PhaseId.PROCESS_VALIDATIONS;
//			}
//			
//		}
//		);
//	}
	
	public JsfSelectOneListingController (Class entityClass, String propertyName, FilterablePageable pageable, CrudOperations crudController) {
    	this.paginator = pageable;
    	controller = (CrudControllerBase<T, PK>) crudController;
    	this.targetPropertyName = propertyName;
    	this.entityClass = entityClass;
    }

	public JsfSelectOneListingController (Class entityClass, String propertyName, FilterablePageable pageable, CrudOperations crudController, String sourceProperty) {
    	this(entityClass, propertyName, pageable, crudController);
    	this.sourcePropertyName = sourceProperty;
    }

	public JsfSelectOneListingController (Class entityClass, FilterablePageable pageable) {
		this(entityClass, null, null, pageable);
    }

	private Object parentEntity = null;
	public JsfSelectOneListingController (Class entityClass, Object parentEntity, String controllerProperty, FilterablePageable pageable) {
		this.entityClass = entityClass;
		this.paginator = pageable;
		this.parentEntity = parentEntity;
    	this.targetPropertyName = controllerProperty;
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
		
		/* If no source property is found than the value property is set to the selected object. */
		Object valueProperty = valueBean;
		
		/* If the sourceProperty is found, 
		 * then get the current value of the source property from the selected row. */
		if ((sourcePropertyName != null) && !"".equals(sourcePropertyName)) {
	    	BeanWrapper valueWrapper = new BeanWrapperImpl(valueBean);
	    	valueProperty = valueWrapper.getPropertyValue(this.sourcePropertyName);
		}

		/* Try to find the target property. */
		BeanWrapper wrappedParentEntity =  null;
		/* If the parentEnity is not equal to null then it is the object that owns the target property. */
    	if (this.parentEntity != null) {
    		wrappedParentEntity = new BeanWrapperImpl(this.parentEntity);
    	} else {
    		/* If the parentEntity was null then try to the controller as the target object. */
    		if (controller!=null) {
    			wrappedParentEntity = new BeanWrapperImpl(controller.getEntity());
    		}
    	}
		
    	/* If we found a target object, then use the target property to set the new value. */
    	if (wrappedParentEntity!=null) {
    		wrappedParentEntity.setPropertyValue(this.targetPropertyName, valueProperty);
    	}
    	
		selectSupport.fireSelect(valueProperty);
		FacesContext.getCurrentInstance().renderResponse();
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
		return targetPropertyName;
	}


	public void setPropertyName(String propertyName) {
		this.targetPropertyName = propertyName;
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
