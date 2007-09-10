package org.crank.crud.controller.datasource;

import java.io.Serializable;
import java.util.List;

import org.crank.crud.criteria.Group;
import org.crank.crud.criteria.OrderBy;

public class DaoFilteringDataSource<T, PK extends Serializable> extends DaoDataSource<T, PK> implements FilteringDataSource {
    protected Group group = new Group();
    protected OrderBy[] orderBy;

    public DaoFilteringDataSource() {
        super();
    }

    public List list() {
        if (orderBy!=null) {
            return dao.find( orderBy, group );
        } else {
            return dao.find( group );
        }
    }

    public OrderBy[] orderBy() {
        return orderBy;
    }

    public void setOrderBy( OrderBy[] orderBy ) {
        this.orderBy = orderBy;
    }

    public Group group() {
        return group;
    }

    public int getCount() {
        return dao.count( group );
    }
    

}