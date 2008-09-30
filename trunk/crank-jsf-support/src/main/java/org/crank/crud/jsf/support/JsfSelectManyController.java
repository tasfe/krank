package org.crank.crud.jsf.support;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import org.crank.crud.controller.CrudOperations;
import org.crank.crud.controller.FilterablePageable;
import org.crank.crud.controller.Row;
import org.crank.crud.controller.SelectManyController;

public class JsfSelectManyController<T extends Serializable, PK extends Serializable> 
		extends SelectManyController<T, PK> {
	
    private DataModel model = new ListDataModel();
	
    @SuppressWarnings("unchecked")
	public JsfSelectManyController (Class<T> clazz, 
    		String propertyName, FilterablePageable pageable, 
    		CrudOperations crudController) {
    	super(clazz, propertyName, pageable,  crudController);
    }

    @SuppressWarnings("unchecked")
	public DataModel getModel() {
        /* Note if you wire in events from paginators, you will only have to change this
         * when there is a next page event.
         */
        List page = getPaginator().getPage();
        List<Row> wrappedList = new ArrayList<Row>(page.size());
        for (Object rowData : page) {
            Row row = new Row();
            row.setObject( rowData );
            if (getManager().isSelected(rowData)) {
            	row.setSelected(true);
            }
            wrappedList.add(row);
        }
        model.setWrappedData( wrappedList );
        return model;
    }

	public void setModel(DataModel model) {
		this.model = model;
	}
	
	@SuppressWarnings("unchecked")
	public Set<Object> getSelectedEntities() {
        List<Row> list = (List<Row>) model.getWrappedData();
        Set<Object> selectedList = new LinkedHashSet<Object>(10);
        if (list==null || list.size()==0) {
        	return selectedList;
        }
        for (Row row : list){
            if (row.isSelected()) {
                selectedList.add( row.getObject() );
            }
        }
        return selectedList;
    }

	@SuppressWarnings("unchecked")
	public Set<Object> getEntitiesInView() {
        List<Row> list = (List<Row>) model.getWrappedData();
        Set<Object> selectedList = new LinkedHashSet<Object>(10);
        if (list==null || list.size()==0) {
        	return selectedList;
        }        
        for (Row row : list){
           selectedList.add( row.getObject() );
        }
        return selectedList;
    }

}
