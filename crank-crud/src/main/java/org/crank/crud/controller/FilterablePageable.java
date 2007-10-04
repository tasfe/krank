package org.crank.crud.controller;

import java.util.Map;
import java.util.List;

import org.crank.crud.criteria.Criterion;


public interface FilterablePageable extends Pageable {
    Map<String, FilterableProperty> getFilterableProperties();
    void filter();
    void clearAll();
    boolean isSorting();
    boolean isFiltering();
    void disableSorts();
    void disableFilters();
    Class getType();
    void addCriterion(Criterion criterion);
    List<Criterion> getCriteria();
    void addFilteringListener(FilteringListener listener);
    void removeFilteringListener(FilteringListener listener);

}
