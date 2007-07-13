package org.crank.crud.controller.datasource;

import java.util.List;

public class SimplePagingDataSource implements PagingDataSource {
    private List list;
    
    public SimplePagingDataSource(final List list) {
        this.list = list;
    }
    
    public int getCount() {
        return list.size();
    }

    public List list( int startItem, int numItems ) {
        int end = startItem + numItems;
        if ((end) > list.size()) {
            end = list.size();
        }
        return list.subList( startItem, end );
    }

}
