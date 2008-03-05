package org.crank.crud.controller.datasource;

import java.util.List;

public interface PagingDataSource<T> extends DataSource<T> {
    public List<?> list(int startItem, int numItems);
    public int getCount();
}
