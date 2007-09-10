package org.crank.crud.controller;

import java.util.Map;


public interface FilterableDataPaginator extends DataPaginator {
    Map<String, FilterableProperty> getFilterableProperties();
    void filter();
}
