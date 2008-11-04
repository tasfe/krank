package org.crank.crud.controller.datasource;

import java.io.Serializable;
import java.util.List;

import org.crank.crud.GenericDao;

public class DaoDataSource<T, PK extends Serializable> implements DataSource<T> {
    protected GenericDao<T, PK> dao;

    public void setDao( GenericDao<T, PK> dao ) {
        this.dao = dao;
    }

    public List<?> list() {
        return dao.find(  );
    }
    
    public DaoDataSource() {
        super();
    }

}