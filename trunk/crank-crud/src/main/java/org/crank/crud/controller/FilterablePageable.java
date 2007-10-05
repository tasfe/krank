package org.crank.crud.controller;

import org.crank.crud.criteria.Criterion;
import org.crank.crud.criteria.OrderBy;

import java.util.List;
import java.util.Map;


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
    void addOrderBy(OrderBy orderBy);

}
