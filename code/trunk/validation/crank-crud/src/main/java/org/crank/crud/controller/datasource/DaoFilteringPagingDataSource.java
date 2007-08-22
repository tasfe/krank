package org.crank.crud.controller.datasource;

import java.io.Serializable;
import java.util.List;

public class DaoFilteringPagingDataSource<T, PK extends Serializable> extends DaoFilteringDataSource<T, PK> implements FilteringPagingDataSource{

    

    public List list( int startItem, int numItems ) {
        if (orderBy!=null) {
            return dao.find( orderBy, startItem, numItems, group );
        } else {
            return dao.find( startItem, numItems, group );
        }
    }


}
