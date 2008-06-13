package org.crank.crud.jsf.support;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

import org.crank.crud.controller.CrudController;
import org.crank.crud.controller.CrudControllerListenerAdapter;
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
import org.crank.crud.criteria.Select;
import org.crank.message.MessageUtils;

/**
 * This class adapts a CrudController to the JSF world.
 * @author Rick Hightower
 *  "I'll never be afraid of some pilsner fresh fat (guy) who eats donut hamburgers and only gets exercise when he plays World of Warcraft on a DDR pad." --Zed
 *
 * @param <T> Type of entity that we are providing CRUD for.
 * @param <PK> Primary key type.
 */
public class JsfCrudAdapter<T extends Serializable, PK extends Serializable> implements EntityLocator<T>, Serializable {
	private static final long serialVersionUID = 1L;
	private FilterablePageable paginator;
    private DataModel model = new ListDataModel();
    private CrudOperations<T> controller;
	private List<T> page;
    private String[] availableProperties;
    private String[] properties;
    private List<SelectItem> availablePropertyList;
    private boolean propertyEditorOpen;


    public JsfCrudAdapter() {
        
    }


    public JsfCrudAdapter(FilterablePageable filterablePageable, CrudOperations<T> crudController) {
        this.paginator = filterablePageable;
        this.controller = crudController;
        crudController.addCrudControllerListener( new CancelListener() );
        if (crudController instanceof CrudController) {
        	((CrudController<T, PK>)crudController).setEntityLocator( this );
        }
        if (this.controller != null) {
        	setupCrudControllerWiring();
        }
        if (this.paginator!=null) {
        	setupPaginatorEventWiring();
            if (this.paginator.getPropertyNames()!=null) {
                this.setAvailableProperties(this.paginator.getPropertyNames().toArray(new String[this.paginator.getPropertyNames().size()]));
            }
        }
    }

    public boolean isPropertyEditorOpen() {
        return propertyEditorOpen;
    }

    public void setPropertyEditorOpen(boolean propertyEditorOpen) {
        this.propertyEditorOpen = propertyEditorOpen;
    }

    public boolean isPropertiesEditable() {
        return availableProperties!=null && availableProperties.length>0;
    }

    public List<SelectItem> getAvailablePropertyItems() {
        if (availablePropertyList==null) {
            String[] props = this.getAvailableProperties();

            availablePropertyList = new ArrayList<SelectItem>(props.length);
            for (String propertyName : props) {
                availablePropertyList.add(new SelectItem(propertyName, MessageUtils.createLabel(propertyName)));
            }
        }
        return availablePropertyList;
    }

    public void openPropertiesEditor() {
            propertyEditorOpen = true;
    }

    public void closePropertiesEditor() {
            propertyEditorOpen = false;
            this.paginator.clearAll();
    }


    public String[] getAvailableProperties() {
        return availableProperties;
    }

    public void setAvailableProperties(String[] availableProperties) {
        this.availableProperties = availableProperties;
    }

    public String[] getProperties() {
        return properties;
    }

    public void setProperties(String[] properties) {
        this.properties = properties;
    }
    
    private void setupCrudControllerWiring() {
		((Toggleable)this.controller).addToggleListener( new ToggleListener(){
            public void toggle( ToggleEvent event ) {
                JsfCrudAdapter.this.crudChanged();
            }} );

        this.controller.addCrudControllerListener(new CrudControllerListenerAdapter(){
			public void afterCreate(CrudEvent event) {
				getPage();
			}

			public void afterDelete(CrudEvent event) {
				getPage();
			}

			public void afterUpdate(CrudEvent event) {
				getPage();
			}

			public void beforeCancel(CrudEvent event) {
				FacesContext.getCurrentInstance().renderResponse();
			}

        	});
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
    @SuppressWarnings("unchecked")
	public T getEntity() {
        /** If the selected entity is not equal return it and set it to null. */
       if (selectedEntity!=null) {
    	   Object tmp = selectedEntity;
    	   selectedEntity = null;
    	   return (T) tmp;
       }
       else if (model.isRowAvailable()) {
           return (T) ((Row)model.getRowData()).getObject();
       }
       else {
           return null;
       }
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
        	Row row = null;
        	
        	/* If the rowData is an Object array it means we have select statements. */
        	if (rowData instanceof Object[]) {
        		Object [] columns = (Object[]) rowData;
        		/* First column is always the entity in question. */
        		row = new Row(columns[0]);
        		
        		/* Extract the rest of the columns. Skip the first one, which is the entity.*/
        		List<Select> selects = paginator.getSelects();
        		for (int index = 1; index < columns.length; index++) {
        			row.putInMap(selects.get(index-1).getName(), columns[index]);
        		}
        	} else {
        		row = new Row(rowData);
        	}
        	wrappedList.add(row);
        }
        model.setWrappedData( wrappedList );
        return model;
    }
    
    public void clear () {
    	page = null;
    }

    public void setModel( DataModel model ) {
        this.model = model;
    }

    public CrudOperations<T> getController() {
        return controller;
    }

    public FilterablePageable getPaginator() {
        return paginator;
    }

    @SuppressWarnings("unchecked")
    public List<T> getSelectedEntities() {
        List<Row> list = (List<Row>) model.getWrappedData();
        List<T> selectedList = new ArrayList<T>(Math.max(list.size(), 10));
        for (Row row : list){
            if (row.isSelected()) {
                selectedList.add( (T)row.getObject() );
            }
        }
        return selectedList;
    }

    class CancelListener extends CrudControllerListenerAdapter {
       @Override
        public void afterCancel( CrudEvent event ) {
            clear(); // ensure that entity edit that was canceled is resync'd with DB
        }
        
    }
}
