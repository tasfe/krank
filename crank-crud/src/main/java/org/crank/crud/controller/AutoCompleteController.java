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

public class AutoCompleteController <T, PK extends Serializable>  implements Selectable {

    private String propertyName;
    private String fieldName;
    private String value;
    private FilteringDataSource dataSource;
    private CrudControllerBase<T, PK> controller; 
    private SelectSupport selectable = new SelectSupport();

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
	
    public AutoCompleteController(Class sourceClass, String sourceProperty,  
    		FilteringDataSource dataSource, CrudOperations targetCrudController, 
    		String targetProperty) {
    	this.controller = (CrudControllerBase<T, PK>) targetCrudController;
        this.dataSource = dataSource;
        this.propertyName = sourceProperty;
        this.fieldName = targetProperty;
    	
        if (controller!=null) {
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
   	
    }
    
    public List autocomplete(Object suggest) {

        String pref = (String)suggest;
        
        return getList(pref);
    }



	/**
	 * Local helper method to lookup the many to one object and then associate it with the event's entity
	 * @param event
	 */
	protected void handleCreateUpdate(CrudEvent event) {
        BeanWrapper entity = new BeanWrapperImpl( event.getEntity() );
        Object newValue = null;
        if ((value != null) && !"".equals(value)) {
	        List<?> list = getListExact(value);
	        if (list.size()==1) {
		        newValue = list.get(0);
	        } else {
	        	StringBuilder msg = new StringBuilder();
	        	msg.append("Unable to match '");
	        	msg.append(fieldName);
	        	msg.append("' to selection '");
	        	msg.append(value);
	        	msg.append("'.");
	        	throw new IllegalArgumentException(msg.toString());
	        }
        }
        entity.setPropertyValue(fieldName, newValue);
        selectable.fireSelect(newValue);
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

	public void addSelectListener(SelectListener listener) {
		selectable.addSelectListener(listener);
		
	}

	public void removeSelectListener(SelectListener listener) {
		selectable.removeSelectListener(listener);
		
	}

	public FilteringDataSource getDataSource() {
		return dataSource;
	}

}
