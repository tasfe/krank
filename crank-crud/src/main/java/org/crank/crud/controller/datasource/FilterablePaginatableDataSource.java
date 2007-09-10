package org.crank.crud.controller.datasource;

import org.crank.crud.criteria.Group;
import org.crank.crud.criteria.OrderBy;

public interface FilterablePaginatableDataSource extends PaginatableDataSource{
    Group group();
    OrderBy[] orderBy();
    void setOrderBy( OrderBy[] orderBy );
}
