package org.crank.crud.controller;

import java.io.Serializable;
import java.util.Map;

/**
 * Controls CRUD operations from an application.
 * @author Rick Hightower
 *
 */
public interface CrudOperations<T extends Serializable> extends Toggleable {
	/** Delete method constant for deleting by id only. */
	static final String DELETE_BY_ID = "BY_ID"; 
	/** Delete method constant for deleting by entity only. */
	static final String DELETE_BY_ENTITY = "BY_ENTITY";
	/** Add method constant for adding by create. */
	static final String ADD_BY_CREATE = "BY_CREATE";
	/** Add method constant for adding by merge. */
	static final String ADD_BY_MERGE = "BY_MERGE";
	
    /** Create an object. */
    CrudOutcome create();
    /** Load a form to update an object. */
    CrudOutcome update();
    /** Delete an object. */

    /** Load the form to create an object. */
    CrudOutcome loadCreate();
    
    /** Load the list. */
    CrudOutcome loadListing();
    
    /** Load the form to edit an object. */
    CrudOutcome read();
    CrudOutcome delete();
    /** Expose object for creation and updating. Allows object to be edited in the form.*/
    T getEntity();
    /** Cancel. */
    CrudOutcome cancel();
    /** */
    String getName();
    /** */
    CrudOutcome deleteSelected();
    /** */
    Class<T> getEntityClass();
    /** */
    CrudState getState();
    /** */
    Map<String, DetailController<? extends Serializable, ? extends Serializable>> getChildren();
    /** */
    void init();
    /** */
    <CE extends Serializable, CEPK extends Serializable> CrudControllerBase<CE, CEPK> addChild (String name, DetailController<CE,CEPK> detailController);
    /** */
    boolean isShowListing();
    /** */
    boolean isShowForm();
    /** */
    Map<String, Object> getDynamicProperties();
    
    void addCrudControllerListener(CrudControllerListener listener);
    void removeCrudControllerListener(CrudControllerListener listener);
    public void setDeleteStrategy(String value);
    
}
