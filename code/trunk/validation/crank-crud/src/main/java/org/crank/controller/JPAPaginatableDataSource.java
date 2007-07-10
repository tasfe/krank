package org.crank.controller;

import java.io.Serializable;
import java.util.List;

import org.crank.crud.GenericDao;

public class JPAPaginatableDataSource<T, PK extends Serializable> implements PaginatableDataSource{

    private GenericDao<T, Serializable> dao;

    public int getCount() {
        return dao.count();
    }

    public List list( int startItem, int numItems ) {
        return dao.find(startItem, numItems);
    }

    public void setDao( GenericDao<T, Serializable> dao ) {
        this.dao = dao;
    }

}
