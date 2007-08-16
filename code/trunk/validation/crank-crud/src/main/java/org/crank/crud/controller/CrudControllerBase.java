package org.crank.crud.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.crank.annotations.design.AllowsConfigurationInjection;
import org.crank.annotations.design.ExpectsInjection;
import org.crank.annotations.design.OptionalInjection;
import org.crank.core.PropertiesUtil;
import org.crank.core.RequestParameterMapFinder;
import org.crank.crud.GenericDao;
import org.crank.web.RequestParameterMapFinderImpl;

public abstract class CrudControllerBase<T, PK extends Serializable> implements CrudOperations, Toggleable, Serializable {

    protected GenericDao<T, PK> dao;
    protected EntityLocator entityLocator;
    protected PropertiesUtil propertyUtil;
    protected String idPropertyName = "id";
    protected boolean readPopulated = true;
    protected Class<T> entityClass;
    protected CrudState state;
    protected Object entity;
    private Map<String, DetailController> children = new HashMap<String, DetailController>();
    private ToggleSupport toggleSupport = new ToggleSupport();
    private String name;
    protected CrudOperations parent;
    protected RequestParameterMapFinder requestParameterMapFinder = new RequestParameterMapFinderImpl();
    protected Map<String, Object> dynamicProperties = new HashMap<String, Object>();
    protected FileUploadHandler fileUploadHandler;
    


    public CrudControllerBase() {
        super();
    }

    public String getName() {
        return name != null ? name : CrudUtils.getClassEntityName(entityClass);
    }

    public void setName( String name ) {
        this.name = name;
    }

    /**
     * @see Toggleable#addToggleListener(ToggleListener)
     */
    public void addToggleListener( ToggleListener listener ) {
        toggleSupport.addToggleListener( listener );
    }

    /**
     * @see Toggleable#addToggleListener(ToggleListener)
     */
    public void removeToggleListener( ToggleListener listener ) {
        toggleSupport.removeToggleListener( listener );
    }

    /**
     * Fire and event to the listeners.
     *
     */
    protected void fireToggle() {
        toggleSupport.fireToggle();
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
    protected void cancelChildren() {
        if (children!=null) {
            for (DetailController detailController : children.values()) {
                detailController.cancel();
            }
        }
    }

    public void addChild( String name, DetailController detailController ) {
        this.children.put(name, detailController);
        if (detailController.getRelationshipManager().getChildCollectionProperty() == null ) {
            detailController.getRelationshipManager().setChildCollectionProperty( name );
        }
        detailController.setParent( this );
    }
    
    /**
     * Create a new entity. An Entity is the object we are managing.
     *
     */
    protected void createEntity() {
        try {
            this.entity = (Serializable) entityClass.newInstance();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public CrudOperations getParent() {
        return parent;
    }

    public void setParent( CrudOperations parent ) {
        this.parent = parent;
    }
    
    public boolean isShowListing() {
        return state != CrudState.ADD || state != CrudState.EDIT;
    }
    public boolean isShowForm() {
        return state == CrudState.ADD || state == CrudState.EDIT;
    }

    public void setRequestParameterMapFinder( RequestParameterMapFinder requestParameterMapFinder ) {
        this.requestParameterMapFinder = requestParameterMapFinder;
    }

    public Map<String, Object> getDynamicProperties() {
        return dynamicProperties;
    }

    public void setDynamicProperties( Map<String, Object> dynamicProperties ) {
        this.dynamicProperties = dynamicProperties;
    }

    public void setFileUploadHandler( FileUploadHandler fileUploadHandler ) {
        this.fileUploadHandler = fileUploadHandler;
    }
    
    /** Create an object. */
    public CrudOutcome create() {
        if (fileUploadHandler!=null) {
            fileUploadHandler.upload( this );
        }
        return doCreate();
    }
    /** Load a form to update an object. */
    public CrudOutcome update() {
        if (fileUploadHandler!=null) {
            fileUploadHandler.upload( this );
        }
        return doUpdate();
    }

    /** Create an object. */
    protected abstract CrudOutcome doCreate();

    /** Load a form to update an object. */
    protected abstract CrudOutcome doUpdate();
    
}