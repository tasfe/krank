package org.crank.controller;

import java.util.List;

public class SimplePaginatableDataSource implements PaginatableDataSource {
    private List list;
    
    public SimplePaginatableDataSource(final List list) {
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
