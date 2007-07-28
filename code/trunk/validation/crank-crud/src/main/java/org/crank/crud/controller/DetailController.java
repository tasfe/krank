package org.crank.crud.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.crank.core.RequestParameterMapFinder;
import org.crank.crud.relationships.RelationshipManager;
import org.crank.web.RequestParameterMapFinderImpl;

public class DetailController implements CrudOperations, Serializable {
    
    private Class entityClass;
    private RelationshipManager relationshipManager = new RelationshipManager();
    private Serializable entity;
    private CrudState state = CrudState.UNKNOWN;
    private CrudOperations parent;
    private Map<String, DetailController> children = new HashMap<String, DetailController>();
    private RequestParameterMapFinder requestParameterMapFinder = new RequestParameterMapFinderImpl();
    private String idParam = "id";

    public void setIdParam( String idParam ) {
        this.idParam = idParam;
    }

    public void setRequestParameterMapFinder( RequestParameterMapFinder requestParameterMapFinder ) {
        this.requestParameterMapFinder = requestParameterMapFinder;
    }

    public void setChildren( Map<String, DetailController> children ) {
        this.children = children;
    }

    public Map<String, DetailController> getChildren() {
        return children;
    }

    public CrudState getState() {
        return state;
    }

    public void setState( CrudState state ) {
        this.state = state;
    }

    public void setEntityClass( Class entityClass ) {
        this.entityClass = entityClass;
    }

    public void setParent( CrudOperations parent ) {
        this.parent = parent;
    }

    public void setRelationshipManager( RelationshipManager relationshipManager ) {
        this.relationshipManager = relationshipManager;
    }

    public DetailController () {
    }
    
    public DetailController (final Class entityClass) {
        this.entityClass = entityClass;
        relationshipManager.setEntityClass( entityClass );
    }

    public DetailController (final CrudOperations parent, final Class entityClass) {
        this.entityClass = entityClass;
        this.parent = parent;
        relationshipManager.setEntityClass( entityClass );
    }
    
    public CrudOutcome create() {
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

    public Serializable getEntity() {
        return entity;
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

    private String retrieveId() {
        return this.requestParameterMapFinder.getMap().get( this.idParam )[0];
    }

    public CrudOutcome update() {
        this.state = CrudState.UNKNOWN;
        return null;
    }

    /**
     * Create a new entity. An Entity is the object we are managing.
     *
     */
    private void createEntity() {
        try {
            this.entity = (Serializable) entityClass.newInstance();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public void init() {
        createEntity();
        this.state = CrudState.UNKNOWN;
        initDetailChildren();
        parentChildren();
    }
    
    
    private void initDetailChildren() {
        if (children!=null) {
            for (DetailController detailController : children.values()) {
                detailController.init();
            }
        }
    }
    
    
    /** Inject this parent into all Children. It's fathers day. Give all the children a daddy. */
    private void parentChildren() {
        if (children!=null) {
            for (DetailController detailController : children.values()) {
                detailController.setParent(this);
            }
        }
    }
    
    public CrudOutcome cancel() {
        state = CrudState.UNKNOWN;
        cancelChildren();
        return null;
    }
    
    /**
     * Call cancelSubForms on all children.
     *
     */
    private void cancelChildren() {
        if (children!=null) {
            for (DetailController detailController : children.values()) {
                detailController.cancel();
            }
        }
    }
    
    public void addChild (String name, DetailController detailController) {
        this.children.put(name, detailController);
        detailController.setParent( this );
    }
    
    public boolean isShowListing() {
        return state != CrudState.ADD || state != CrudState.EDIT;
    }
    public boolean isShowForm() {
        return state == CrudState.ADD || state == CrudState.EDIT;
    }

    public Class getEntityClass() {
        return entityClass;
    }

}
