package org.crank.crud.controller;

import java.io.Serializable;
import java.util.Set;

import org.crank.crud.relationships.SelectManyRelationshipManager;

public abstract class SelectManyController<T extends Serializable, PK extends Serializable> {
	
	private SelectManyRelationshipManager manager;
    private FilterablePageable paginator;
    private CrudControllerBase<T, PK> controller;
    private boolean show;
	
    public SelectManyController (Class clazz, String propertyName, FilterablePageable pageable, CrudOperations<T> crudController) {
    	this.paginator = pageable;
    	this.controller = (CrudControllerBase<T, PK>) crudController;
    	
		manager = new SelectManyRelationshipManager();
		manager.setEntityClass(clazz);
		manager.setChildCollectionProperty(propertyName);
		
		controller.addCrudControllerListener(new CrudControllerListener() {

			public void afterCancel(CrudEvent event) {
			}

			public void afterCreate(CrudEvent event) {
			}

			public void afterDelete(CrudEvent event) {
			}

			public void afterLoadCreate(CrudEvent event) {
                initEntity();
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

        paginator.addPaginationListener(new PaginationListener(){
            public void pagination(PaginationEvent pe) {
                processPaginationEvent();
            }
        });

        paginator.addFilteringListener(new FilteringListener(){
            public void beforeFilter(FilteringEvent fe) {
                processPaginationEvent();
            }

            public void afterFilter(FilteringEvent fe) {
            }
        });
        
        initEntity();
    	
    }

    public void processPaginationEvent() {
        this.manager.setParentObject(controller.getEntity());
        this.manager.process(getSelectedEntities(), getEntitiesInView());
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

	public void process () {
		this.manager.setParentObject(controller.getEntity());
		this.manager.process(getSelectedEntities(), getEntitiesInView());
		this.show = false;
	}
	
	public void cancel () {
		this.show = false;
	}
	
	public void showSelection() {
		this.show = true;
	}

	@SuppressWarnings("unchecked")
	public abstract Set<Object> getSelectedEntities();

	@SuppressWarnings("unchecked")
	public abstract Set<Object> getEntitiesInView();

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
