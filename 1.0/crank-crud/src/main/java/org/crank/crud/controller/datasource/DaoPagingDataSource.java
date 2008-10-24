package org.crank.crud.controller.datasource;

import java.io.Serializable;
import java.util.List;

import org.crank.crud.GenericDao;

public class DaoPagingDataSource<T, PK extends Serializable> implements PagingDataSource<T>{

    private GenericDao<T, PK> dao;

    public int getCount() {
        return dao.count();
    }

    public List<T> list( int startItem, int numItems ) {
        return dao.find(startItem, numItems);
    }

    public void setDao( GenericDao<T, PK> dao ) {
        this.dao = dao;
    }

    public List<T> list() {
        return dao.find();
    }

}
