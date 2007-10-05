package org.crank.crud.controller.datasource;

import java.io.Serializable;
import java.util.List;

import org.crank.crud.criteria.Group;
import org.crank.crud.criteria.OrderBy;
import org.crank.crud.join.Fetch;

public class DaoFilteringDataSource<T, PK extends Serializable> extends DaoDataSource<T, PK> implements FilteringDataSource {
    protected Group group = new Group();
    protected OrderBy[] orderBy = new OrderBy[]{};
    protected Fetch[] fetches = new Fetch[]{};

    public DaoFilteringDataSource() {
        super();
    }

    public List<T> list() {
    	if (orderBy!=null && fetches!=null) {
    		return dao.find(fetches, orderBy, 0, 1000000, group);
    	} else if (orderBy!=null) {
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

    public void setOrderBys( OrderBy... orderBy ) {
        this.orderBy = orderBy;
    }

    public Group group() {
        return group;
    }

    public int getCount() {
        return dao.count( group );
    }

	public void setFetches(Fetch... fetches) {
		this.fetches = fetches;
	}
    

}