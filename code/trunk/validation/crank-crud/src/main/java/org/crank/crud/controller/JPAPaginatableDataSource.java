package org.crank.crud.controller;

import java.io.Serializable;
import java.util.List;

import org.crank.crud.GenericDao;

public class JPAPaginatableDataSource<T, PK extends Serializable> implements PaginatableDataSource{

    private GenericDao<T, PK> dao;

    public int getCount() {
        return dao.count();
    }

    public List list( int startItem, int numItems ) {
        return dao.find(startItem, numItems);
    }

    public void setDao( GenericDao<T, PK> dao ) {
        this.dao = dao;
    }

}
