package org.crank.crud.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.crank.annotations.design.AllowsConfigurationInjection;
import org.crank.annotations.design.ExpectsInjection;
import org.crank.annotations.design.OptionalInjection;
import org.crank.core.PropertiesUtil;
import org.crank.crud.GenericDao;

/**
 * Controls CRUD operations from an application.
 * @author Rick
 *
 * @param <T> Entity type
 * @param <PK> Entity primary key
 * @see CrudOperations
 * @see Toggleable
 */
public class CrudController<T, PK extends Serializable> implements CrudOperations, Toggleable {
    private GenericDao<T, PK> dao;
    private EntityLocator entityLocator;
    private PropertiesUtil propertyUtil;
    private String idPropertyName = "id";
    private boolean readPopulated = true;
    private Class<T> entityClass;
    private CrudState state;    
    private T entity;
    private Map<String, DetailController> children = new HashMap<String, DetailController>(); 
    private ToggleSupport toggleSupport = new ToggleSupport();
    private String name;
    
    public String getName() {
        return name != null ? name : CrudUtils.getClassEntityName(entityClass);
    }

    public void setName( String name ) {
        this.name = name;
    }
    

    /**
     * @see Toggleable#addToggleListener(ToggleListener)
     */
    public void addToggleListener(ToggleListener listener) {
        toggleSupport.addToggleListener( listener );
    }
    /**
     * @see Toggleable#addToggleListener(ToggleListener)
     */    
    public void removeToggleListener(ToggleListener listener) {
        toggleSupport.removeToggleListener( listener );
    }

    /**
     * Fire and event to the listeners.
     *
     */
    private void fireToggle() {
        toggleSupport.fireToggle();
    }

    public CrudController () {
        
    }
    
    /**
     * Creates a new instance of the entity class and sets the entity to this new instance.
     * @see CrudOperations#loadCreate()
     */
    public CrudOutcome loadCreate() {
        init();
        try {
            entity = entityClass.newInstance();
            this.state = CrudState.ADD;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return CrudOutcome.FORM;
    }

    /**
     * Create a new instance in the database.
     * Notify listeners that the entity was created.
     * @see CrudOperations#create()
     * @retrun outcome
     */
    public CrudOutcome create() {
        dao.create(entity);
        this.state = CrudState.UNKNOWN;
        fireToggle();
        return CrudOutcome.LISTING;
    }

    /**
     * Delete the entity from the data store.
     * Notify listeners that the model changed.
     * @see CrudOperations#delete()
     * @retrun outcome
     */
    @SuppressWarnings("unchecked")    
    public CrudOutcome delete() {
        doDelete((T)getCurrentEntity());        
        fireToggle();
        return CrudOutcome.LISTING;
    }

    @SuppressWarnings("unchecked")
    private void doDelete(Object entity) {
        dao.delete( (PK) propertyUtil.getPropertyValue( idPropertyName, entity ));
    }
    
    public CrudOutcome deleteSelected() {
        List listToDelete = this.getSelectedEntities();
        /* You could change this to delete a list of ids. */
        for (Object entity : listToDelete) {
            doDelete( entity );
        }
        fireToggle();        
        return CrudOutcome.LISTING;        
    }

    /**
     * Read the entity from the data store. If the readPopulated flag is set, read 
     * the entity fully populated. You need to read the entity fully populated for 
     * master detail forms and such.
     * @see CrudOperations#read()
     * @retrun outcome
     */
    @SuppressWarnings("unchecked")
    public CrudOutcome read() {
        init();
        entity = (T)getCurrentEntity();
        state = CrudState.EDIT; 
        if (readPopulated) {
            entity = dao.readPopulated( (PK) propertyUtil.getPropertyValue( idPropertyName, entity ));
        }
        return CrudOutcome.FORM;
    }

    private Object getCurrentEntity() {
        return entityLocator.getEntity();
    }

    private List getSelectedEntities() {
        return entityLocator.getSelectedEntities();
    }

    /**
     * Update the entity in the data store. 
     * Notify listeners that the entity was updated.
     * @see CrudOperations#update()
     * @return outcome
     */
    public CrudOutcome update() {
    	dao.update(entity);
        state = CrudState.UNKNOWN; 
        fireToggle();
        return CrudOutcome.LISTING;
    }

    /** 
     * @see CrudOperations#getEntity()
     */
    public Serializable getEntity() {
        return (Serializable) entity;
    }



    public void setDao( GenericDao<T, PK> dao ) {
        this.dao = dao;
    }

    @ExpectsInjection
    public void setEntityLocator( EntityLocator entityLocator ) {
        this.entityLocator = entityLocator;
    }

    @ExpectsInjection    
    public void setPropertyUtil( PropertiesUtil propertyUtil ) {
        this.propertyUtil = propertyUtil;
    }

    @OptionalInjection
    public void setIdPropertyName( String idPropertyName ) {
        this.idPropertyName = idPropertyName;
    }

    @AllowsConfigurationInjection
    public void setReadPopulated( boolean readPopulated ) {
        this.readPopulated = readPopulated;
    }

    @AllowsConfigurationInjection
    public void setEntityClass( Class<T> entityClass ) {
        this.entityClass = entityClass;
    }
    
    public Class<T> getEntityClass() {
        return entityClass;
    }

    public CrudState getState() {
        return state;
    }
    public CrudOutcome cancel() {
        state = CrudState.UNKNOWN;
        cancelChildren();
        return CrudOutcome.LISTING;
    }
    public Map<String, DetailController> getChildren() {
        return children;
    }
    public void setChildren( Map<String, DetailController> children ) {
        this.children = children;
    }
    public void init() {
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
    
    
}
