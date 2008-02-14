package org.crank.crud.controller;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.logging.Logger;

import org.crank.crud.relationships.RelationshipManager;

/**
 * 
 * @author Rick Hightower, Tom Cellucci
 *
 * @param <T> Entity type
 * @param <PK> primary key type
 */

public class DetailController<T extends Serializable, PK extends Serializable> extends CrudControllerBase<T, PK> {    
	private static final long serialVersionUID = 1L;
	
	/** manage the relationship with the parent entity object. */
	protected RelationshipManager relationshipManager = new RelationshipManager();
	
    private boolean showDetails = false;
    
    /** Always synchronize to the database, can't be rolled back by hitting cancel. */
    private boolean forceUpdate = false;
    
    /** Because we needed to keep track of the items that were modified; they need to become part of the persistent context
     * when the parent entity is saved or if the parent collection isn't the owner
     * there was a case where you'd remove an parent-child relationship
     * but it would never stick because the child was the owner
     * updating the parent wouldn't save your change
     * @author Tom Cellucci
     */
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
     * Searches for the top-level CrudController in the CrudOperation hierarchy
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
        
        if (forceUpdate) {
        	checkDao();
        	dao.merge(this.getEntity());
        } else {
        	changedEntities.add((T)this.getEntity());        	
        }
        return null;
    }

    private final void checkDao() {
    	if (dao==null) {
    		throw new RuntimeException("If force merge is set to true, then the dao object must already be set");
    	}
	}

	@SuppressWarnings("unchecked")
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
        if (forceUpdate) {
        	checkDao();
        	dao.merge(entity);        	
        } else {
        	changedEntities.add(entity);
        }
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

    @SuppressWarnings("unchecked")
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
        if (forceUpdate) {
        	checkDao();
        	dao.merge(entity);        	
        }
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

	public boolean isForceUpdate() {
		return forceUpdate;
	}

	public void setForceUpdate(boolean forceUpdate) {
		this.forceUpdate = forceUpdate;
	}
    
}
