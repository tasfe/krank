package org.crank.crud.controller.datasource;

import java.util.List;

public interface PagingDataSource extends DataSource {
    public List list(int startItem, int numItems);
    public int getCount();
}
