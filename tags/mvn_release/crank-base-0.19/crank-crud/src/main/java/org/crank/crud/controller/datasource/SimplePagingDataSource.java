package org.crank.crud.controller.datasource;

import java.util.List;

public class SimplePagingDataSource<T> implements PagingDataSource<T> {
    private List<T> list;
    
    public SimplePagingDataSource(final List<T> list) {
        this.list = list;
    }
    
    public int getCount() {
        return list.size();
    }

    public List<T> list( int startItem, int numItems ) {
        int end = startItem + numItems;
        if ((end) > list.size()) {
            end = list.size();
        }
        return list.subList( startItem, end );
    }

    public List<T> list() {
        return list;
    }

}
