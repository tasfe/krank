package org.crank.crud.controller;

import org.crank.crud.criteria.Group;

public class FilterableDataPaginatorImpl extends DataPaginatorImpl implements FilterableDataPaginator {
    
    public FilterableDataPaginatorImpl() {
        super();
    }

    public FilterableDataPaginatorImpl( FilterablePaginatableDataSource dataSource ) {
        super( (PaginatableDataSource) dataSource );
    }

    public Group group() {
        return ((FilterablePaginatableDataSource)this.dataSource).group();
    }
}
