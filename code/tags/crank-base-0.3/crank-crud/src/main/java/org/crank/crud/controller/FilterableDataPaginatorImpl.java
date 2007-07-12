package org.crank.crud.controller;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.crank.crud.controller.datasource.FilterablePaginatableDataSource;
import org.crank.crud.controller.datasource.PaginatableDataSource;
import org.crank.crud.criteria.OrderBy;

public class FilterableDataPaginatorImpl extends DataPaginatorImpl implements FilterableDataPaginator, Serializable {
    private Map<String, FilterableProperty> filterableProperties = null;
    private Class type;
    
    public FilterableDataPaginatorImpl() {
        super();
    }

    private FilterablePaginatableDataSource filterablePaginatableDataSource() {
        return (FilterablePaginatableDataSource) this.dataSource;
    }
    public FilterableDataPaginatorImpl( FilterablePaginatableDataSource dataSource, Class type) {
        super( (PaginatableDataSource) dataSource );
        this.type = type;
        createFilterProperties(  );
    }

    private void createFilterProperties(  ) {
        filterableProperties = new HashMap<String, FilterableProperty>();
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo( type );
        } catch (IntrospectionException ie) {
            throw new RuntimeException(ie);
        }
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor: propertyDescriptors) {
            FilterableProperty filterableProperty = new FilterableProperty(propertyDescriptor.getName(), propertyDescriptor.getPropertyType());
            filterableProperties.put( propertyDescriptor.getName(), filterableProperty );
        }
    }

    public void filter() {
        /* Clear the comparision group b/c we are about to recreate it */
        filterablePaginatableDataSource().group().clear();
        
        /* OrderBy collection list. */
        List<OrderBy> orderBy = new ArrayList<OrderBy>();
        
        /* Iterator through the filters. */
        Collection<FilterableProperty> values = filterableProperties.values();
        for (FilterableProperty fp : values) {
            /* Add the comparison to the group. */
            if (fp.getComparison().isEnabled()) {
                filterablePaginatableDataSource().group().add( fp.getComparison() );
            }
            
            /* Add the order by clause to the list. */
            if (fp.getOrderBy().isEnabled()) {
                orderBy.add( fp.getOrderBy() );
            }
        }
        
        /* Set the orderBy list. */
        filterablePaginatableDataSource().setOrderBy( orderBy.toArray(new OrderBy[orderBy.size()]) );
        reset();
    }
    
    public void clearAll() {
        filterableProperties.clear();
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
        Collection<FilterableProperty> values = filterableProperties.values();
        for (FilterableProperty fp : values) {
            fp.getOrderBy().setEnabled( false );
        }
    }

    public void disableFilters() {
        Collection<FilterableProperty> values = filterableProperties.values();
        for (FilterableProperty fp : values) {
            fp.getComparison().setEnabled( false );
        }
    }

    public Map<String, FilterableProperty> getFilterableProperties() {
        return filterableProperties;
    }

}
