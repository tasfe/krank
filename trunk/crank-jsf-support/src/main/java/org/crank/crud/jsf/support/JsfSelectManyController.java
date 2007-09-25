package org.crank.crud.jsf.support;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import org.crank.crud.controller.CrudControllerBase;
import org.crank.crud.controller.CrudControllerListener;
import org.crank.crud.controller.CrudEvent;
import org.crank.crud.controller.CrudOperations;
import org.crank.crud.controller.FilterablePageable;
import org.crank.crud.controller.Row;
import org.crank.crud.relationships.SelectManyRelationshipManager;

public class JsfSelectManyController<T, PK extends Serializable> {
	
	private SelectManyRelationshipManager manager;
    private FilterablePageable paginator;
    private DataModel model = new ListDataModel();
    private CrudControllerBase<T, PK> controller;
    private boolean show;
	
    public JsfSelectManyController (Class clazz, FilterablePageable pageable, CrudOperations crudController) {
    	this.paginator = pageable;
    	this.controller = (CrudControllerBase<T, PK>) crudController;
    	
		manager = new SelectManyRelationshipManager();
		manager.setEntityClass(clazz);
		
		controller.addCrudControllerListener(new CrudControllerListener() {

			public void afterCancel(CrudEvent event) {
			}

			public void afterCreate(CrudEvent event) {
			}

			public void afterDelete(CrudEvent event) {
			}

			public void afterLoadCreate(CrudEvent event) {
			}

			public void afterLoadListing(CrudEvent event) {
				initEntity();
			}

			public void afterRead(CrudEvent event) {
				initEntity();
			}

			public void afterUpdate(CrudEvent event) {
			}

			public void beforeCancel(CrudEvent event) {
			}

			public void beforeCreate(CrudEvent event) {
				initEntity();
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
				initEntity();
			}}
		);
    	
    }

    public void initEntity() {
    	manager.setParentObject(controller.getEntity());
    }
	public SelectManyRelationshipManager getManager() {
		return manager;
	}

	public void setManager(SelectManyRelationshipManager manager) {
		this.manager = manager;
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
        List page = paginator.getPage();
        List<Row> wrappedList = new ArrayList<Row>(page.size());
        for (Object rowData : page) {
            Row row = new Row();
            row.setObject( rowData );
            if (manager.isSelected(rowData)) {
            	row.setSelected(true);
            }
            wrappedList.add(row);
        }
        model.setWrappedData( wrappedList );
        return model;
    }

	public void setModel(DataModel model) {
		this.model = model;
	}
	
	public void process () {
		this.manager.setParentObject(controller.getEntity());
		this.manager.process(getSelectedEntities());
		this.show = false;
	}
	
	public void cancel () {
		this.show = false;
	}
	
	public void showSelection() {
		this.show = true;
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

    

}
