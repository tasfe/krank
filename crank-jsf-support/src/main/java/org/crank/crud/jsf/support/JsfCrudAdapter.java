package org.crank.crud.jsf.support;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import org.crank.crud.controller.CrudController;
import org.crank.crud.controller.CrudControllerBase;
import org.crank.crud.controller.CrudOperations;
import org.crank.crud.controller.EntityLocator;
import org.crank.crud.controller.FilterablePageable;
import org.crank.crud.controller.Row;
import org.crank.crud.controller.ToggleEvent;
import org.crank.crud.controller.ToggleListener;
import org.crank.crud.controller.Toggleable;

/**
 * This class adpats a CrudController to the JSF world.
 * @author Rick Hightower
 *
 * @param <T> Type of entity that we are providing CRUD for.
 * @param <PK> Primary key type.
 */
public class JsfCrudAdapter<T, PK extends Serializable> implements EntityLocator, Serializable {
	private static final long serialVersionUID = 1L;
	private FilterablePageable paginator;
    private DataModel model = new ListDataModel();
    private CrudControllerBase<T, PK> controller;

    public JsfCrudAdapter() {
        
    }

    public JsfCrudAdapter(FilterablePageable filterablePageable, CrudController<T, PK > crudController) {
        this.paginator = filterablePageable;
        this.controller = crudController;
        crudController.setEntityLocator( this );
        
        /* Registers for events. */
        ((Toggleable)crudController).addToggleListener( new ToggleListener(){
            public void toggle( ToggleEvent event ) {
                JsfCrudAdapter.this.crudChanged();
            }} );
    }
    
    /**
     * @see EntityLocator#getEntity()
     */
    public Serializable getEntity() {
       return (Serializable) ((Row)model.getRowData()).getObject();
    }

    private void crudChanged() {
        paginator.reset();
    }

    public DataModel getModel() {
        /* Note if you wire in events from paginators, you will only have to change this
         * when there is a next page event.
         */
        List page = paginator.getPage();
        List<Row> wrappedList = new ArrayList<Row>(page.size());
        for (Object rowData : page) {
            Row row = new Row();
            row.setObject( rowData );
            wrappedList.add(row);
        }
        model.setWrappedData( wrappedList );
        return model;
    }

    public void setModel( DataModel model ) {
        this.model = model;
    }

    public CrudOperations getController() {
        return controller;
    }

    public FilterablePageable getPaginator() {
        return paginator;
    }

    @SuppressWarnings("unchecked")
    public List getSelectedEntities() {
        List<Row> list = (List<Row>) model.getWrappedData();
        List selectedList = new ArrayList(10);
        for (Row row : list){
            if (row.isSelected()) {
                selectedList.add( row.getObject() );
            }
        }
        return selectedList;
    }

}
