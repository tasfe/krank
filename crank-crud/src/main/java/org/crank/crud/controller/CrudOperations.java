package org.crank.crud.controller;

import java.io.Serializable;
import java.util.Map;

/**
 * Controls CRUD operations from an application.
 * @author Rick Hightower
 *
 */
public interface CrudOperations extends Toggleable {
	/** Delete method constant for deleting by id only. */
	static final String DELETE_BY_ID = "BY_ID"; 
	/** Delete method constant for deleting by entity only. */
	static final String DELETE_BY_ENTITY = "BY_ENTITY";
	
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
    Serializable getEntity();
    /** Cancel. */
    CrudOutcome cancel();
    /** */
    String getName();
    /** */
    CrudOutcome deleteSelected();
    /** */
    Class getEntityClass();
    /** */
    CrudState getState();
    /** */
    Map<String, DetailController> getChildren();
    /** */
    void init();
    /** */
    CrudControllerBase addChild (String name, DetailController detailController);
    /** */
    boolean isShowListing();
    /** */
    boolean isShowForm();
    /** */
    Map<String, Object> getDynamicProperties();
    
    void addCrudControllerListener(CrudControllerListener listener);
    void removeCrudControllerListener(CrudControllerListener listener);
    
}
