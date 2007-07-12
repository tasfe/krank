package org.crank.crud.controller;

import java.io.Serializable;
import java.util.List;

import org.crank.crud.GenericDao;
import org.crank.crud.criteria.Group;

public class JPAFilterablePaginatableDataSource<T, PK extends Serializable> implements FilterablePaginatableDataSource{

    private GenericDao<T, PK> dao;
    private Group group = new Group();
    
    public Group group() {
        return group;
    }

    public int getCount() {
        return dao.count( group );
    }

    public List list( int startItem, int numItems ) {
        return dao.find( startItem, numItems, group );
    }

    public void setDao( GenericDao<T, PK> dao ) {
        this.dao = dao;
    }

}
