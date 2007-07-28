package org.crank.crud.controller;

import java.io.Serializable;

/**
 * Controls CRUD operations from an application.
 * @author Rick Hightower
 *
 */
public interface CrudOperations {
    /** Create an object. */
    CrudOutcome create();
    /** Load the form to create an object. */
    CrudOutcome loadCreate();
    /** Load the form to edit an objectt. */
    CrudOutcome read();
    /** Load a form to update an object. */
    CrudOutcome update();
    /** Delete an object. */
    CrudOutcome delete();
    /** Expose object for creation and updating. Allows object to be edited in the form.*/
    Serializable getEntity();
    /** Cancel. */
    CrudOutcome cancel();

}
