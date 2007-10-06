package org.crank.crud.controller.datasource;

import java.io.Serializable;
import java.util.List;

public class DaoFilteringPagingDataSource<T, PK extends Serializable> extends DaoFilteringDataSource<T, PK> implements FilteringPagingDataSource{

    

    public List<T> list( int startItem, int numItems ) {
        return dao.find(this.fetches, this.orderBy, startItem, numItems, this.group);
    }


}
