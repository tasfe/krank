package org.crank.crud.jsf.support;

import org.crank.crud.controller.*;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class JsfSelectOneListingController <T extends Serializable, PK extends Serializable> extends SelectOneController<T, PK>  implements Selectable{

    private DataModel model = new ListDataModel();

    @SuppressWarnings("unchecked")
    public JsfSelectOneListingController (Class entityClass, String propertyName, FilterablePageable pageable, CrudOperations crudController) {
        super(entityClass, propertyName, pageable, crudController);
    }

	@SuppressWarnings("unchecked")
	public JsfSelectOneListingController (Class<?> entityClass, String propertyName, FilterablePageable pageable, CrudOperations crudController, String sourceProperty) {
        super(entityClass, propertyName, pageable, crudController, sourceProperty);
    }

	public JsfSelectOneListingController (Class<?> entityClass, FilterablePageable pageable) {
		super(entityClass, null, null, pageable);
    }


	public JsfSelectOneListingController (Class<?> entityClass, Object parentEntity, String controllerProperty, FilterablePageable pageable) {
        super(entityClass, parentEntity, controllerProperty, pageable);
    }

    public Row getSelectedRow() {
        return (Row) model.getRowData();
    }

    public void prepareUI() {
        FacesContext.getCurrentInstance().renderResponse();
    }


    @SuppressWarnings("unchecked")
	public DataModel getModel() {
        /* Note if you wire in events from paginators, you will only have to change this
         * when there is a next page event.
         */
        //model.setWrappedData( paginator.getPage() );
        List page = getPaginator().getPage();
        List<Row> wrappedList = new ArrayList<Row>(page.size());
        for (Object rowData : page) {
            Row row = new Row();
            row.setObject( rowData );
            wrappedList.add(row);
        }
        model.setWrappedData( wrappedList );

        return model;
    }

	public void setModel(DataModel model) {
		this.model = model;
	}
	


	
}
