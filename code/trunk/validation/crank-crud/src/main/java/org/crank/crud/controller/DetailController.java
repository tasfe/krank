package org.crank.crud.controller;

import java.io.Serializable;

import org.crank.crud.relationships.RelationshipManager;

public class DetailController<T, PK extends Serializable> extends CrudControllerBase<T, PK> {
    
    protected RelationshipManager relationshipManager = new RelationshipManager();
    private boolean showDetails = false;
    
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
    public DetailController (final Class entityClass) {
        this.entityClass = entityClass;
        relationshipManager.setEntityClass( entityClass );
    }

    @SuppressWarnings("unchecked")
    public DetailController (final CrudOperations parent, final Class entityClass) {
        this.entityClass = entityClass;
        this.parent = parent;
        relationshipManager.setEntityClass( entityClass );
    }
    
    public CrudOutcome doCreate() {
        Object parent = (Object) this.parent.getEntity();

        /* Add this object to the parent. */
        relationshipManager.addToParent(parent, this.getEntity());
        this.state = CrudState.UNKNOWN;
        
        return null;
    }

    public CrudOutcome delete() {
        /* Read the entity from the parent object. */
        this.entity = relationshipManager.readEntityFromParent( parent.getEntity(), retrieveId());
        /* Now kill it. This only succeeds if they don't click cancel.*/
        relationshipManager.removeFromParent(parent.getEntity(), this.getEntity());
        
        return null;
    }
    
    public String getObjectId (Object row) {
        return this.relationshipManager.getObjectId( parent.getEntity(), row);
    }


    public CrudOutcome loadCreate() {
        init();
        createEntity();
        this.state = CrudState.ADD;
        return null;
    }

    public CrudOutcome read() {
        /* Initialize this form and subForms to their initial state. */
        init();

        /* Turn on addMode, turn off editMode. */
        this.state = CrudState.EDIT;

        String index = retrieveId();


        /* Read the entity from the parent object. */
        this.entity = relationshipManager.readEntityFromParent( parent.getEntity(), index );
        
        return null;
    }

    protected CrudOutcome doUpdate() {
        this.state = CrudState.UNKNOWN;
        return null;
    }

    
    public CrudOutcome cancel() {
        state = CrudState.UNKNOWN;
        cancelChildren();
        return CrudOutcome.LISTING;
    }
        
    public CrudOutcome deleteSelected() {
        throw new UnsupportedOperationException();
    }
    
    public void toggleShowDetails() {
    	showDetails = !showDetails;
    }

}
