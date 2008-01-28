package org.crank.crud.controller;

import org.crank.crud.criteria.Criterion;
import org.crank.crud.criteria.OrderBy;
import org.crank.crud.criteria.Select;
import org.crank.crud.join.Join;

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
    @SuppressWarnings("unchecked")
	Class getType();
    void addCriterion(Criterion criterion);
    List<Criterion> getCriteria();
    void addFilteringListener(FilteringListener listener);
    void removeFilteringListener(FilteringListener listener);
    void addOrderBy(OrderBy orderBy);
    List<Join> getJoins();
    void setJoins(List<Join> joins);
    void addSelect(Select select);
    @SuppressWarnings("unchecked")
	void addFilterableEntityJoin(Class entityClass, String entityName, String alias, String properties[], String joinBy);

    @Deprecated
    List<Join> getFetches();
    @Deprecated
    void setFetches(List<Join> fetches);
}
