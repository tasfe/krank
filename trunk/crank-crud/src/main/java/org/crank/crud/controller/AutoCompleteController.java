package org.crank.crud.controller;

import java.io.Serializable;
import java.util.List;

import org.crank.crud.controller.CrudControllerListener;
import org.crank.crud.controller.CrudEvent;
import org.crank.crud.controller.datasource.FilteringDataSource;
import org.crank.crud.criteria.OrderBy;
import org.crank.crud.criteria.OrderDirection;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import static org.crank.crud.criteria.Comparison.startsLike;
import static org.crank.crud.criteria.Comparison.eq;

public class AutoCompleteController <T, PK extends Serializable>  {

    private String propertyName;
    private String fieldName;
    private String value;
    private FilteringDataSource dataSource;
    private SelectSupport selectSupport = new SelectSupport();
    private CrudControllerBase<T, PK> controller; 

	public void setDataSource( FilteringDataSource dataSource ) {
        this.dataSource = dataSource;
    }
    
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public AutoCompleteController() {
		
	}
	
    public AutoCompleteController(Class clazz, String propertyName, CrudOperations crudController, 
    		FilteringDataSource dataSource, String fieldName) {
    	this.controller = (CrudControllerBase<T, PK>) crudController;
        this.dataSource = dataSource;
        this.propertyName = propertyName;
        this.fieldName = fieldName;
    	
		controller.addCrudControllerListener(new CrudControllerListener() {

			public void afterCancel(CrudEvent event) {
			}

			public void afterCreate(CrudEvent event) {
				handleReadEvent(event);
			}

			public void afterDelete(CrudEvent event) {
			}

			public void afterLoadCreate(CrudEvent event) {
                setValue(null);
			}

			public void afterLoadListing(CrudEvent event) {
			}

			public void afterRead(CrudEvent event) {
				handleReadEvent(event);
			}

			public void afterUpdate(CrudEvent event) {
			}

			public void beforeCancel(CrudEvent event) {
			}

			public void beforeCreate(CrudEvent event) {
				handleCreateUpdate(event);
			}

			public void beforeDelete(CrudEvent event) {
			}

			public void beforeLoadCreate(CrudEvent event) {
			}

			public void beforeLoadListing(CrudEvent event) {
			}

			public void beforeRead(CrudEvent event) {
			}

			public void beforeUpdate(CrudEvent event) {
				handleCreateUpdate(event);
			}}
		);
   	
    }
    
    public List autocomplete(Object suggest) {

        String pref = (String)suggest;
        
        return getList(pref);
    }



	/**
	 * Local helper method to lookup the many to one object and then associate it with the event's entity
	 * @param event
	 */
	private void handleCreateUpdate(CrudEvent event) {
        BeanWrapper entity = new BeanWrapperImpl( event.getEntity() );
        Object newValue = null;
        if ((value != null) && !"".equals(value)) {
	        List<?> list = getListExact(value);
	        if (list.size()==1) {
		        newValue = list.get(0);
	        }
        }
        entity.setPropertyValue(fieldName, newValue);
        selectSupport.fireSelect(newValue);
	}

	/**
	 * Local helper method to perform wiring of the existing crud entity value into the local value
	 * For example, for an Employee entity which has a Specialty entity as property "specialty"...
	 * and the Specialty entity has a "name" property, then the
	 * fieldName="specialty" and propertyName="name" would grab the value from the controller entity as
	 * Employee.specialty.name
	 * @param event
	 */
	private void handleReadEvent(CrudEvent event) {
        BeanWrapper entity = new BeanWrapperImpl( event.getEntity() );
        Object fieldValue = entity.getPropertyValue(fieldName);
        
    	this.value = null;
    	
        if (fieldValue != null) {
        	BeanWrapper entityProp = new BeanWrapperImpl(fieldValue);
            this.value = (String) entityProp.getPropertyValue(propertyName);
        }
	}

	/**
	 * Local helper method to perform a criteria lookup on the instance's data source
	 * @param pref
	 * @return
	 */
	private List getList(String pref) {
		OrderBy orderBy = new OrderBy(propertyName, OrderDirection.ASC);
		
        /* Clear the comparison group b/c we are about to recreate it */
        dataSource.group().clear();
        
        /* Add the criteria */
        dataSource.group().add(startsLike(propertyName,pref));
        
        /* Set the orderBy list. */
        dataSource.setOrderBy( new OrderBy[]{orderBy} );
        
        return dataSource.list();
	}

	/**
	 * Local helper method to perform an exact criteria lookup on the instance's data source
	 * @param pref
	 * @return
	 */
	private List getListExact(String pref) {
		OrderBy orderBy = new OrderBy(propertyName, OrderDirection.ASC);
		
        /* Clear the comparison group b/c we are about to recreate it */
        dataSource.group().clear();
        
        /* Add the criteria */
        dataSource.group().add(eq(propertyName,pref));
        
        /* Set the orderBy list. */
        dataSource.setOrderBy( new OrderBy[]{orderBy} );
        
        return dataSource.list();
	}

}
