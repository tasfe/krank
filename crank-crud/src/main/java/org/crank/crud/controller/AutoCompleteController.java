package org.crank.crud.controller;

import static org.crank.crud.criteria.Comparison.eq;
import static org.crank.crud.criteria.Comparison.startsLike;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.crank.crud.controller.datasource.FilteringDataSource;
import org.crank.crud.criteria.Group;
import org.crank.crud.criteria.OrderBy;
import org.crank.crud.criteria.OrderDirection;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class AutoCompleteController <S extends Serializable, T extends Serializable>  implements Selectable {

    private String propertyName;
    private String fieldName;
    private String value;
    private FilteringDataSource<S> dataSource;
    private CrudOperations<T> controller; 
    private SelectSupport selectable = new SelectSupport(this);
    private Group group = new Group();
    private boolean dataCached = false;
    private String cacheKey;
    private List<S> cachedData;


	public void setDataSource( FilteringDataSource<S> dataSource ) {
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
	
    public AutoCompleteController(String sourceProperty,  
    		FilteringDataSource<S> dataSource, String targetProperty,
    		CrudOperations<T> targetCrudController
    	) {
    	this.controller = targetCrudController;
        this.dataSource = dataSource;
        this.propertyName = sourceProperty;
        this.fieldName = targetProperty;
    	
        if (controller!=null) {
			controller.addCrudControllerListener(new CrudControllerListenerAdapter() {
				public void afterCreate(CrudEvent event) {
					handleReadEvent(event);
				}
	
				public void afterLoadCreate(CrudEvent event) {
	                setValue(null);
				}
	
				public void afterRead(CrudEvent event) {
					handleReadEvent(event);
				}
	
				public void beforeCreate(CrudEvent event) {
					handleCreateUpdate(event);
				}

				public void beforeUpdate(CrudEvent event) {
					handleCreateUpdate(event);
				}}
			);
        }
   	
    }
    
    public List<S> autocomplete(Object suggest) {
        List<S> rv = null;

        String pref = (String)suggest;
        if (isDataCached()) {
        	pref = pref.toUpperCase();
        	if (cacheKey != null && pref.startsWith(cacheKey)) {
        		List<S> tmp = new ArrayList<S>(cachedData.size());
     	       	BeanWrapper entityProp = null;
        		for (S s : cachedData) {
         	       	if (entityProp == null) {
         	       		entityProp = new BeanWrapperImpl(s);
         	       	}
         	       	else {
         	       		entityProp.setWrappedInstance(s);
         	       	}         	       	
                    Object beanPropertyValue = entityProp.getPropertyValue(propertyName);
                    if (beanPropertyValue != null) {
                    	String ucValue = beanPropertyValue.toString().toUpperCase();
                    	if (ucValue.startsWith(pref)) {
                    		tmp.add(s);
                    	}
                    }
          		}
        		rv = tmp;        		
        	}
        	else {
                rv = getList(pref);                  
        	}
            cachedData = rv;
            cacheKey = pref;
        }
        else {
            rv = getList(pref);
        }
        return rv;
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
	}
	private boolean found = true; 
	
	public boolean isFound() {
		return found;
	}

	public void setFound(boolean found) {
		this.found = found;
	}

	protected void textChanged(String value) {
		if (value == null || "".equals(value.trim())) {
			// If the text was changed to blank or null
			// There's no need to look for it.
			return;
		}
		List<?> list = getListExact(value);
		if (list.size() == 1) {
			Object newValue = list.get(0);
			selectable.fireSelect(newValue);
			found = true;
			
		} else {
			selectable.fireUnselect();
			found = false;
		}
	}

	/**
	 * Local helper method to perform wiring of the existing crud entity value
	 * into the local value For example, for an Employee entity which has a
	 * Specialty entity as property "specialty"... and the Specialty entity has
	 * a "name" property, then the fieldName="specialty" and propertyName="name"
	 * would grab the value from the controller entity as
	 * Employee.specialty.name
	 * 
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
	@SuppressWarnings("unchecked")
	private List<S> getList(String pref) {
		OrderBy orderBy = new OrderBy(propertyName, OrderDirection.ASC);
        /* Clear the comparison group b/c we are about to recreate it */
        dataSource.group().clear();
        
        /* Add the criteria */
        if (group.size() > 0) {
        	dataSource.group().add(startsLike(propertyName,pref)).add(this.group);
        } else {
        	dataSource.group().add(startsLike(propertyName,pref));
        }
        
        /* Set the orderBy list. */
        dataSource.setOrderBy( new OrderBy[]{orderBy} );
        
        return (List<S>) dataSource.list();
	}

	/**
	 * Local helper method to perform an exact criteria lookup on the instance's data source
	 * @param pref
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<S> getListExact(String pref) {
		OrderBy orderBy = new OrderBy(propertyName, OrderDirection.ASC);
		
        /* Clear the comparison group b/c we are about to recreate it */
        dataSource.group().clear();
        
        /* Add the criteria */
        dataSource.group().add(eq(propertyName,pref));
        
        /* Set the orderBy list. */
        dataSource.setOrderBy( new OrderBy[]{orderBy} );
        
        return (List<S>) dataSource.list();
	}

	public void addSelectListener(SelectListener listener) {
		selectable.addSelectListener(listener);
		
	}

	public void removeSelectListener(SelectListener listener) {
		selectable.removeSelectListener(listener);
		
	}

	public FilteringDataSource<S> getDataSource() {
		return dataSource;
	}

	public Group getGroup() {
		return group;
	}

	public boolean isDataCached() {
		return dataCached;
	}

	public void setDataCached(boolean dataCached) {
		this.dataCached = dataCached;
	}

}
