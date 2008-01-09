package org.crank.crud.controller;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.logging.Logger;

import org.crank.crud.relationships.RelationshipManager;

public class DetailController<T extends Serializable, PK extends Serializable> extends CrudControllerBase<T, PK> {    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected RelationshipManager relationshipManager = new RelationshipManager();
    private boolean showDetails = false;
    private Collection<T> changedEntities = new HashSet<T>();
    
    public boolean isShowDetails() {
		return showDetails;
	}

	public void setShowDetails(boolean showDetails) {
		this.showDetails = showDetails;
	}

    public void setIdParam( String idParam ) {
        this.idParam = idParam;
    }


    public void setRelationshipManager( RelationshipManager relationshipManager ) {
        this.relationshipManager = relationshipManager;
    }

    public RelationshipManager getRelationshipManager() {
        return this.relationshipManager;
    }

    public DetailController () {
    }
    
    @SuppressWarnings("unchecked")
    public DetailController (final Class<T> entityClass) {
        this.entityClass = entityClass;
        relationshipManager.setEntityClass( entityClass );
    }

    @SuppressWarnings("unchecked")
    public DetailController (final CrudOperations<?> parent, final Class<T> entityClass) {
        this.entityClass = entityClass;
        setParent(parent);
        relationshipManager.setEntityClass( entityClass );
    }
     
    /*
     * searches for the top-level CrudController in the CrudOperation hierarchy
     * returns null if none found
     */
	private CrudController<?, ?> findCrudController() {
    	CrudController<?, ?> rv = null;
    	CrudOperations<?> ancestor = parent;
    	while ((ancestor != null)) {
    		if (ancestor instanceof CrudController) {
    			rv = (CrudController<?, ?>)ancestor;
    			break;
    		}
    		else if (ancestor instanceof CrudControllerBase) {
    			ancestor = ((CrudControllerBase<?, ?>)ancestor).parent;
    		}
    		else {
    			// unknown class type for ancestor
    			break;
    		}
    	}
    	return rv;
    }
    
    public CrudOutcome doCreate() {
        Object parent = (Object) this.parent.getEntity();

        /* Add this object to the parent. */
        relationshipManager.addToParent(parent, this.getEntity());
        this.state = CrudState.UNKNOWN;
        changedEntities.add((T)this.getEntity());
        return null;
    }

    public CrudOutcome doDelete() {
        /* Read the entity from the parent object. */
        this.entity = (T)relationshipManager.readEntityFromParent( parent.getEntity(), retrieveId());
        doDelete(entity);
        return null;
    }
    
    
    
    @Override
	protected void doDelete(T entity) {
       	Logger.getAnonymousLogger().info("doDelete() - deleting " + entity);
        relationshipManager.removeFromParent(parent.getEntity(), entity);
        changedEntities.add(entity);
	}

	public String getObjectId (Object row) {
        return this.relationshipManager.getObjectId( parent.getEntity(), row);
    }


    public CrudOutcome doLoadCreate() {
        init();
        createEntity();
        this.state = CrudState.ADD;
        return null;
    }

    public CrudOutcome doRead() {
        /* Initialize this form and subForms to their initial state. */
        init();

        /* Turn on addMode, turn off editMode. */
        this.state = CrudState.EDIT;

        String index = retrieveId();


        /* Read the entity from the parent object. */
        this.entity = (T)relationshipManager.readEntityFromParent( parent.getEntity(), index );
        
        return null;
    }

    protected CrudOutcome doUpdate() {
        this.state = CrudState.UNKNOWN;
        return null;
    }

    
    public CrudOutcome doCancel() {
        state = CrudState.UNKNOWN;
        cancelChildren();
        return null;
    }
        
    public void toggleShowDetails() {
    	showDetails = !showDetails;
    }

	@Override
	protected CrudOutcome doLoadListing() {
		return CrudOutcome.LISTING;
	}

	@Override
	public void setParent(CrudOperations<?> parent) {	
		super.setParent(parent);
        final CrudController<?, ?> controller = findCrudController();
        if (controller != null) {
        	/*
        	 * this crud listener makes sure that entities changed
        	 * by this detail controller are part of the persistent
        	 * context when the top-level operation is persisted.
        	 */
        	controller.addCrudControllerListener(new CrudControllerListenerAdapter() {
				@Override
				public void afterUpdate(CrudEvent event) {
					changedEntities.clear();
				}
				@Override
				public void afterCancel(CrudEvent event) {
					changedEntities.clear();
				}
				@Override
				public void beforeUpdate(CrudEvent event) {
					changedEntities = controller.manageRelated(changedEntities);
				}
			});
        }
	}
    
}
