package org.crank.crud.controller;

import java.util.Map;


public interface FilterableDataPaginator extends DataPaginator {
    Map<String, FilterableProperty> getFilterableProperties();
    void filter();
    void clearAll();
    boolean isSorting();
    boolean isFiltering();
    void disableSorts();
    void disableFilters();
}
