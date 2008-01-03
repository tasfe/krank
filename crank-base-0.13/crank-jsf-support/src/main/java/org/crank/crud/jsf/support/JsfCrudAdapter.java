package org.crank.crud.jsf.support;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import org.crank.crud.controller.CrudController;
import org.crank.crud.controller.CrudControllerListener;
import org.crank.crud.controller.CrudEvent;
import org.crank.crud.controller.CrudOperations;
import org.crank.crud.controller.EntityLocator;
import org.crank.crud.controller.FilterablePageable;
import org.crank.crud.controller.FilteringEvent;
import org.crank.crud.controller.FilteringListener;
import org.crank.crud.controller.PaginationEvent;
import org.crank.crud.controller.PaginationListener;
import org.crank.crud.controller.Row;
import org.crank.crud.controller.ToggleEvent;
import org.crank.crud.controller.ToggleListener;
import org.crank.crud.controller.Toggleable;

/**
 * This class adapts a CrudController to the JSF world.
 * @author Rick Hightower
 *
 * @param <T> Type of entity that we are providing CRUD for.
 * @param <PK> Primary key type.
 */
@SuppressWarnings("unchecked")
public class JsfCrudAdapter<T, PK extends Serializable> implements EntityLocator, Serializable {
	private static final long serialVersionUID = 1L;
	private FilterablePageable paginator;
    private DataModel model = new ListDataModel();
    private CrudOperations controller;
	private List page;

    public JsfCrudAdapter() {
        
    }

    public JsfCrudAdapter(FilterablePageable filterablePageable, CrudOperations crudController) {
        this.paginator = filterablePageable;
        this.controller = crudController;
        if (crudController instanceof CrudController) {
        	((CrudController)crudController).setEntityLocator( this );
        }
        if (this.controller != null) {
        	setupCrudControllerWiring();
        }
        if (this.paginator!=null) {
        	setupPaginatorEventWiring();
        }
    }

	private void setupCrudControllerWiring() {
		((Toggleable)this.controller).addToggleListener( new ToggleListener(){
            public void toggle( ToggleEvent event ) {
                JsfCrudAdapter.this.crudChanged();
            }} );

        this.controller.addCrudControllerListener(new CrudControllerListener(){

			public void afterCancel(CrudEvent event) {
			}

			public void afterCreate(CrudEvent event) {
				getPage();
			}

			public void afterDelete(CrudEvent event) {
				getPage();
			}

			public void afterLoadCreate(CrudEvent event) {
			}

			public void afterLoadListing(CrudEvent event) {
			}

			public void afterRead(CrudEvent event) {
			}

			public void afterUpdate(CrudEvent event) {
				getPage();
			}

			public void beforeCancel(CrudEvent event) {
				FacesContext.getCurrentInstance().renderResponse();
			}

			public void beforeCreate(CrudEvent event) {
			}

			public void beforeDelete(CrudEvent event) {
			}

			public void beforeLoadCreate(CrudEvent event) {
			}

			public void beforeLoadListing(CrudEvent event) {
			}

			public void beforeRead(CrudEvent event) {
			}

			public void beforeUpdate(CrudEvent event) {
			}});
	}

	private void setupPaginatorEventWiring() {
		/* Registers for events. */
        this.paginator.addFilteringListener(new FilteringListener(){

			public void afterFilter(FilteringEvent fe) {
				getPage();
			}

			public void beforeFilter(FilteringEvent fe) {
			}});
        
        this.paginator.addPaginationListener(new PaginationListener(){
			public void pagination(PaginationEvent pe) {
				getPage();
			}});
	}
    
    protected void getPage() {
    	page = paginator.getPage();
    }
    
    /**
     * @see EntityLocator#getEntity()
     */
    public Serializable getEntity() {
        /** If the selected entity is not equal return it and set it to null. */
       if (selectedEntity!=null) {
    	   Object tmp = selectedEntity;
    	   selectedEntity = null;
    	   return (Serializable) tmp;
       }
       return (Serializable) ((Row)model.getRowData()).getObject();
    }
    
    private Object selectedEntity;
    public void setSelectedEnity(Object selectedEntity) {
    	this.selectedEntity = selectedEntity;
    }

    private void crudChanged() {
        paginator.reset();
    }

    public DataModel getModel() {
    	if (page == null) {
    		page = paginator.getPage();
    	}
        /* Note if you wire in events from paginators, you will only have to change this
         * when there is a next page event.
         */
        List<Row> wrappedList = new ArrayList<Row>(page.size());
        for (Object rowData : page) {
            wrappedList.add(new Row(rowData));
        }
        model.setWrappedData( wrappedList );
        return model;
    }

    public void setModel( DataModel model ) {
        this.model = model;
    }

    public CrudOperations getController() {
        return controller;
    }

    public FilterablePageable getPaginator() {
        return paginator;
    }

    @SuppressWarnings("unchecked")
    public List getSelectedEntities() {
        List<Row> list = (List<Row>) model.getWrappedData();
        List selectedList = new ArrayList(10);
        for (Row row : list){
            if (row.isSelected()) {
                selectedList.add( row.getObject() );
            }
        }
        return selectedList;
    }

}
