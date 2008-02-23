package org.crank.crud.controller.datasource;

import org.crank.crud.criteria.Group;
import org.crank.crud.criteria.OrderBy;
import org.crank.crud.criteria.Select;
import org.crank.crud.join.Join;

public interface FilteringDataSource<T> extends DataSource<T>{
    Group group();
    OrderBy[] orderBy();
    void setOrderBy( OrderBy[] orderBy );
    
    @Deprecated
    Join[] fetches();
    @Deprecated
    void setFetches( Join[] fetches );
    
    Join[] joins();
    void setJoins( Join[] fetches );
    
    Select[] selects();
    void setSelects( Select[] selects );

}
