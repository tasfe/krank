package org.crank.crud.controller.datasource;

import org.crank.crud.criteria.Group;
import org.crank.crud.criteria.OrderBy;

public interface FilteringPagingDataSource extends PagingDataSource{
    Group group();
    OrderBy[] orderBy();
    void setOrderBy( OrderBy[] orderBy );
}
