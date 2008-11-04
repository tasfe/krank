package org.crank.crud.controller.datasource;

public interface FilteringPagingDataSource<T> extends PagingDataSource<T>, FilteringDataSource<T> {
}
