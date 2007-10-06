package org.crank.crud.controller.datasource;

import org.crank.crud.criteria.Group;
import org.crank.crud.criteria.OrderBy;
import org.crank.crud.join.Fetch;

public interface FilteringDataSource extends DataSource{
    Group group();
    OrderBy[] orderBy();
    void setOrderBy( OrderBy[] orderBy );
    Fetch[] fetches();
    void setFetches( Fetch[] fetches );

}
