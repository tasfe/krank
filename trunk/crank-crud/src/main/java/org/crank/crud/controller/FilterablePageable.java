package org.crank.crud.controller;

import java.util.Map;

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
}
