package org.crank.crud.controller;

import org.crank.crud.criteria.Group;

public interface FilterablePaginatableDataSource extends PaginatableDataSource{
    Group group();
}
