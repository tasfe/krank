package org.crank.crud.controller;

import java.io.Serializable;
import java.util.Collection;

/**
 * Controls CRUD operations from an application.
 * @author Rick
 *
 * @param <T> Entity type
 * @param <PK> Entity primary key
 * @see CrudOperations
 * @see Toggleable
 */
public class CrudController<T extends Serializable, PK extends Serializable> extends CrudControllerBase<T, PK>  {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CrudController () {
        
    }
    
    /**
     * Update the entity in the data store. 
     * Notify listeners that the entity was updated.
     * @see CrudOperations#update()
     * @return outcome
     */
    @SuppressWarnings("unchecked")
    public CrudOutcome doUpdate() {
        entity = dao.update(entity);
        state = CrudState.UNKNOWN; 
        fireToggle();
        return CrudOutcome.LISTING;
    }
    
    /**
     * Creates a new instance of the entity class and sets the entity to this new instance.
     * @see CrudOperations#loadCreate()
     */
    @SuppressWarnings("unchecked")
    public CrudOutcome doLoadCreate() {
        init();
        createEntity();
        this.state = CrudState.ADD;
        return CrudOutcome.FORM;
    }

    /**
     * Create a new instance in the database.
     * Notify listeners that the entity was created.
     * @see CrudOperations#create()
     * @return outcome
     */
    @SuppressWarnings({ "unchecked", "deprecation" })
    public CrudOutcome doCreate() {
    	if (CrudOperations.ADD_BY_MERGE.equals(addStrategy)) {
    		dao.merge(entity);
    	} else {
    		dao.create(entity);
    	}
        this.state = CrudState.UNKNOWN;
        fireToggle();
        return CrudOutcome.LISTING;
    }

    /**
     * Delete the entity from the data store.
     * Notify listeners that the model changed.
     * @see CrudOperations#delete()
     * @return outcome
     */
    @SuppressWarnings("unchecked")    
    public CrudOutcome doDelete() {
        doDelete(getCurrentEntity());        
        fireToggle();
        return CrudOutcome.LISTING;
    }


   /**
     * Read the entity from the data store. If the readPopulated flag is set, read 
     * the entity fully populated. You need to read the entity fully populated for 
     * master detail forms and such.
     * @see CrudOperations#read()
     * @return outcome
     */
    @SuppressWarnings("unchecked")
    public CrudOutcome doRead() {
        init();
        PK id = null;
        
        String sId = this.retrieveId();
        if (sId==null) {
        	entity = getCurrentEntity();
        	id = (PK) propertyUtil.getPropertyValue( idPropertyName, entity );
        } else {
        	id = (PK) Long.valueOf(sId);
        }
        state = CrudState.EDIT; 
        if (readPopulated) {
            entity = dao.readPopulated( id );
        }
        return CrudOutcome.FORM;
    }

    protected T getCurrentEntity() {
        return entityLocator.getEntity();
    }


    public CrudOutcome doCancel() {
        state = CrudState.UNKNOWN;
        cancelChildren();
        return CrudOutcome.LISTING;
    }

	@Override
	protected CrudOutcome doLoadListing() {
		return CrudOutcome.LISTING;
	}

	@Override
	public CrudOutcome deleteSelected() {
		super.deleteSelected();
		return CrudOutcome.LISTING;
	}
    
    public <RE> Collection<RE> manageRelated(Collection<RE> relatedEntities) {
    	return dao.mergeRelated(relatedEntities);
    }
}
