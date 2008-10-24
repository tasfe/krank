package org.crank.crud.controller.datasource;

import java.io.Serializable;
import java.util.List;

public class DaoFilteringPagingDataSource<T, PK extends Serializable> extends DaoFilteringDataSource<T, PK> implements FilteringPagingDataSource<T> {

    

    public List<?> list( int startItem, int numItems ) {
        return dao.find(this.selects, this.fetches, this.orderBy, startItem, numItems, this.group);
    }


}
