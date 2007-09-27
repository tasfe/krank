package org.crank.crud.controller;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.*;

import org.crank.crud.controller.datasource.FilteringPagingDataSource;
import org.crank.crud.controller.datasource.PagingDataSource;
import org.crank.crud.criteria.Criterion;
import org.crank.crud.criteria.OrderBy;
import org.crank.core.CrankException;

public class FilteringPaginator extends Paginator implements FilterablePageable, Serializable {
    private Map<String, FilterableProperty> filterableProperties = null;
    private List<Criterion> criteria;
    private Class type;
    
    private String name;
    
    private int sequence;

    public String getName() {
        return (name != null ? name : CrudUtils.getClassEntityName(type)) + "Paginator";
    }

    public void setName( String name ) {
        this.name = name;
    }
    
    public FilteringPaginator() {
        super();
    }

    private FilteringPagingDataSource filterablePaginatableDataSource() {
        return (FilteringPagingDataSource) this.dataSource;
    }
    public FilteringPaginator( FilteringPagingDataSource dataSource, Class type) {
        super( (PagingDataSource) dataSource );
        this.type = type;
        createFilterProperties();
    }

    private void createFilterProperties( ) {
        filterableProperties = new HashMap<String, FilterableProperty>();
        createFilterProperties( type, null );
    }

    private class FPToggleListener implements ToggleListener, Serializable {
        private String property;
        public String getProperty() {
            return property;
        }
        public void setProperty( String property ) {
            this.property = property;
        }
        public FPToggleListener(String property) {
            this.property = property;
        }
        public FPToggleListener() {
        }
        public void toggle( ToggleEvent event ) {
            if (event.getSource() instanceof OrderBy) {
                OrderBy orderBy = (OrderBy) event.getSource();
                orderBy.setSequence( sequence );
                sequence++;
            }
            filter();
        }
    } 
    private Set visitorSet = new HashSet();

    private void createFilterProperties( final Class theType, final String propertyName ) {
        String key = null;

        try {

        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo( theType );
        } catch (IntrospectionException ie) {
            throw new RuntimeException(ie);
        }
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor: propertyDescriptors) {

            String property = null;
            if (propertyName != null) {
                property = propertyName + "." +  propertyDescriptor.getName();
            } else {
                property = propertyDescriptor.getName();
            }
            String parentClassName = theType.getClass().getName();
            String childClassName = propertyDescriptor.getPropertyType().getName();
            key = parentClassName + "." + childClassName + "." + propertyDescriptor.getName();

            if (!visitorSet.contains(key)) {

                FilterableProperty filterableProperty = new FilterableProperty(property, propertyDescriptor.getPropertyType());

                filterableProperties.put( property, filterableProperty );
                //!theType.equals( propertyDescriptor.getPropertyType() does not allow circular dependencies which plagues us.
                if (CrudUtils.isEntity( propertyDescriptor.getPropertyType() ) && !theType.equals( propertyDescriptor.getPropertyType() )) {
                    createFilterProperties( propertyDescriptor.getPropertyType(), property );
                } else if (CrudUtils.isEmbeddable( propertyDescriptor.getPropertyType() ) && !theType.equals( propertyDescriptor.getPropertyType() )) {
                    createFilterProperties( propertyDescriptor.getPropertyType(), property );
                }

                filterableProperty.addToggleListener(new FPToggleListener(property));
                visitorSet.add(key);
            }
        }
        } catch (Exception ce) {
             String message  = (new Formatter()).format("Problem Calling createFilterProperties args=(theType=%s, propertyName=%s), key=%s", new Object[]{theType, propertyName, key}).toString();
             throw new RuntimeException(message, ce);
        }
    }

    public void filter() {
        /* Clear the comparison group b/c we are about to recreate it */
        filterablePaginatableDataSource().group().clear();
        
        /* OrderBy collection list. */
        List<OrderBy> orderBys = new ArrayList<OrderBy>();
        
        /* Iterator through the filters. */
        Collection<FilterableProperty> values = filterableProperties.values();
        for (FilterableProperty fp : values) {
            /* Add the comparison to the group. */
            if (fp.getComparison().isEnabled() && fp.getComparison().getValue()!=null) {
                filterablePaginatableDataSource().group().add( fp.getComparison() );
            }
            
            /* Add the order by clause to the list. */
            if (fp.getOrderBy().isEnabled()) {
                orderBys.add( fp.getOrderBy() );
            }
        }
        
        Collections.sort( orderBys,  new Comparator<OrderBy> (){
            public int compare( OrderBy ob1, OrderBy ob2 ) {
                return ob1.getSequence().compareTo( ob2.getSequence() );
            }});
        /* Set the orderBy list. */
        filterablePaginatableDataSource().setOrderBy( orderBys.toArray(new OrderBy[orderBys.size()]) );
        
        
        if (criteria!=null && criteria.size() >0) {
        	for (Criterion criterion : criteria) {
        		filterablePaginatableDataSource().group().add(criterion);
        	}
        }
        
        reset();
    }
    
    public void clearAll() {
        filterableProperties.clear();
        sequence = 0;
        createFilterProperties(  );
        filter();
    }
    public boolean isSorting() {
        Collection<FilterableProperty> values = filterableProperties.values();
        for (FilterableProperty fp : values) {
            if (fp.getOrderBy().isEnabled()) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isFiltering() {
        Collection<FilterableProperty> values = filterableProperties.values();
        for (FilterableProperty fp : values) {
            if (fp.getComparison().isEnabled()) {
                return true;
            }
        }
        return false;
    }

    public void disableSorts() {
        sequence = 0;
        Collection<FilterableProperty> values = filterableProperties.values();
        for (FilterableProperty fp : values) {
            fp.getOrderBy().setEnabled( false );
        }
        filter();
    }

    public void disableFilters() {
        Collection<FilterableProperty> values = filterableProperties.values();
        for (FilterableProperty fp : values) {
            fp.getComparison().setEnabled( false );
        }
        filter();
    }

    public Map<String, FilterableProperty> getFilterableProperties() {
        return filterableProperties;
    }

    public Class getType() {
        return type;
    }

    public void setType( Class type ) {
        this.type = type;
    }

    public void addCriterion(Criterion criterion) {
    	List<Criterion> criteriaList = getCriteria();
    	criteriaList.add(criterion);
    }
	public List<Criterion> getCriteria() {
		if (criteria==null) {
			criteria = new ArrayList<Criterion>();
		}
		return criteria;
	}

}
