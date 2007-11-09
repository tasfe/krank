package org.crank.crud.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.crank.annotations.design.AllowsConfigurationInjection;
import org.crank.annotations.design.ExpectsInjection;
import org.crank.annotations.design.OptionalInjection;
import org.crank.core.CrankValidationException;
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
    @SuppressWarnings("unchecked")
	private Map<String, DetailController> children = new HashMap<String, DetailController>();
    private ToggleSupport toggleSupport = new ToggleSupport();
    private String name;
    protected CrudOperations parent;
    protected RequestParameterMapFinder requestParameterMapFinder = new RequestParameterMapFinderImpl();
    protected Map<String, Object> dynamicProperties = new CrankMap();
    protected FileUploadHandler fileUploadHandler;
	protected String idParam = "id";
    


    public CrudControllerBase() {
        super();
    }

    public String getName() {
        return name != null ? name : CrudUtils.getClassEntityName(entityClass);
    }

    public String getNameUpperCase() {
        return getName().toUpperCase();
    }

    public String getNamePlural() {
        String name = getName();
        if (name.endsWith("s")) {
        	return name + "es";
        } else {
        	return name + "s";
        }
    }
    
    public String getNamePluralAndUpperCase() {
    	return getNamePlural().toUpperCase();
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
     * Fire an event to the Toggle listeners.
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
            for (CrudControllerBase<T, PK> detailController : children.values()) {
                detailController.init();
            }
        }
    }

    /** Inject this parent into all Children. It's fathers day. Give all the children a daddy. */
    private void parentChildren() {
        if (children!=null) {
            for (CrudControllerBase<T, PK> detailController : children.values()) {
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
            for (CrudControllerBase<T, PK> detailController : children.values()) {
                detailController.cancel();
            }
        }
    }

    public CrudControllerBase<T, PK> addChild( String name, DetailController detailController ) {
        this.children.put(name, detailController);
        if (detailController.getRelationshipManager().getChildCollectionProperty() == null ) {
            detailController.getRelationshipManager().setChildCollectionProperty( name );
        }
        detailController.setParent( this );
        return detailController;
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
        fireBeforeCreate();
        CrudOutcome outcome = doCreate();
        fireAfterCreate();
        return outcome;
    }

    /** Load create an object. */
    public CrudOutcome loadCreate() {
    	try {
	        fireBeforeLoadCreate();
	        CrudOutcome outcome = doLoadCreate();
	        fireAfterLoadCreate();
	        return outcome;
    	} catch (CrankValidationException e) {
    		e.printStackTrace();
    	}
    	return null;
    }
    
    /** Update an object. */
    public CrudOutcome update() {
        if (fileUploadHandler!=null) {
            fileUploadHandler.upload( this );
        }
        try {
	        fireBeforeUpdate();
	        CrudOutcome outcome = doUpdate();
	        fireAfterUpdate();
	        return outcome;
		} catch (CrankValidationException e) {
			e.printStackTrace();
		}
		return null;
    }

    /** Update an object. */
    public CrudOutcome delete() {
        fireBeforeDelete();
        CrudOutcome outcome = doDelete();
        fireAfterDelete();
        return outcome;
    }

    /** Update an object. */
    public CrudOutcome read() {
        fireBeforeRead();
        CrudOutcome outcome = doRead();
        fireAfterRead();
        return outcome;
    }

    /** Load Listing. */
    public CrudOutcome loadListing() {
        fireBeforeLoadListing();
        CrudOutcome outcome = doLoadListing();
        fireAfterLoadListing();
        return outcome;
    }


	/** Update an object. */
    public CrudOutcome cancel() {
        fireBeforeCancel();
        CrudOutcome outcome = doCancel();
        fireAfterCancel();
        return outcome;
    }

    protected abstract CrudOutcome doCancel();

    /** Create an object. */
    protected abstract CrudOutcome doCreate();

    /** Update an object. */
    protected abstract CrudOutcome doUpdate();

    /** Delete an object. */
    protected abstract CrudOutcome doDelete();

    /** Read an object. */
    protected abstract CrudOutcome doRead();

    /** Read an object. */
    protected abstract CrudOutcome doLoadCreate();
    
    /** Read an object. */
    protected abstract CrudOutcome doLoadListing();
    

    protected String retrieveId() {
		String[] params = this.requestParameterMapFinder.getMap().get( this.idParam );
		if (params!=null && params.length > 0) {
			return params[0];
		} else {
			return null;
		}
	}
	
    private List<CrudControllerListener> listeners = new ArrayList<CrudControllerListener>();
	
    public void addCrudControllerListener(CrudControllerListener listener) {
    		listeners.add(listener);
    }
    public void removeCrudControllerListener(CrudControllerListener listener) {
    		listeners.remove(listener);
    }

    protected void fireAfterUpdate() {
        CrudEvent event = new CrudEvent(this, this.entity);
        for (CrudControllerListener ccl : listeners) {
            ccl.afterUpdate(event);
        }
    }
    protected void fireBeforeUpdate() {
        CrudEvent event = new CrudEvent(this, this.entity);
        for (CrudControllerListener ccl : listeners) {
            ccl.beforeUpdate(event);
        }
    }

    protected void fireBeforeCreate() {
        CrudEvent event = new CrudEvent(this, this.entity);
        for (CrudControllerListener ccl : listeners) {
            ccl.beforeCreate(event);
        }
    }

    protected void fireAfterCreate() {
        CrudEvent event = new CrudEvent(this, this.entity);
        for (CrudControllerListener ccl : listeners) {
            ccl.afterCreate(event);
        }
    }

    protected void fireBeforeLoadCreate() {
        CrudEvent event = new CrudEvent(this, this.entity);
        for (CrudControllerListener ccl : listeners) {
            ccl.beforeLoadCreate(event);
        }
    }

    protected void fireAfterLoadCreate() {
        CrudEvent event = new CrudEvent(this, this.entity);
        for (CrudControllerListener ccl : listeners) {
            ccl.afterLoadCreate(event);
        }
    }

    protected void fireBeforeRead() {
        CrudEvent event = new CrudEvent(this, this.entity);
        for (CrudControllerListener ccl : listeners) {
            ccl.beforeRead(event);
        }
    }

    protected void fireAfterRead() {
        CrudEvent event = new CrudEvent(this, this.entity);
        for (CrudControllerListener ccl : listeners) {
            ccl.afterRead(event);
        }
    }

    protected void fireBeforeDelete() {
        CrudEvent event = new CrudEvent(this, this.entity);
        for (CrudControllerListener ccl : listeners) {
            ccl.beforeDelete(event);
        }
    }

    protected void fireAfterDelete() {
        CrudEvent event = new CrudEvent(this, this.entity);
        for (CrudControllerListener ccl : listeners) {
            ccl.afterDelete(event);
        }
    }
    


    protected void fireBeforeCancel() {
        CrudEvent event = new CrudEvent(this, this.entity);
        for (CrudControllerListener ccl : listeners) {
            ccl.beforeCancel(event);
        }
    }

    protected void fireAfterCancel() {
        CrudEvent event = new CrudEvent(this, this.entity);
        for (CrudControllerListener ccl : listeners) {
            ccl.afterCancel(event);
        }
    }
    
    private void fireAfterLoadListing() {
        CrudEvent event = new CrudEvent(this, this.entity);
        for (CrudControllerListener ccl : listeners) {
            ccl.afterLoadListing(event);
        }
	}

	private void fireBeforeLoadListing() {
        CrudEvent event = new CrudEvent(this, this.entity);
        for (CrudControllerListener ccl : listeners) {
            ccl.beforeLoadListing(event);
        }
	}
    

}