package org.crank.crud.controller;

import org.crank.crud.criteria.Group;

public interface FilterableDataPaginator extends DataPaginator {
    Group group();
}
