package org.crank.crud.controller.datasource;

import org.crank.crud.criteria.Group;
import org.crank.crud.criteria.OrderBy;
import org.crank.crud.join.Join;

public interface FilteringDataSource<T> extends DataSource<T>{
    Group group();
    OrderBy[] orderBy();
    void setOrderBy( OrderBy[] orderBy );
    Join[] fetches();
    void setFetches( Join[] fetches );

}
