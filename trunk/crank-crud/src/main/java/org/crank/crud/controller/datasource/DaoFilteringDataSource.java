package org.crank.crud.controller.datasource;

import java.io.Serializable;
import java.util.List;

import org.crank.crud.criteria.Group;
import org.crank.crud.criteria.OrderBy;
import org.crank.crud.join.Join;

public class DaoFilteringDataSource<T, PK extends Serializable> extends DaoDataSource<T, PK> implements FilteringDataSource {
    protected Group group = new Group();
    protected OrderBy[] orderBy = new OrderBy[]{};
    protected Join[] fetches = new Join[]{};

    public DaoFilteringDataSource() {
        super();
    }

    public List<T> list() {
        return dao.find(this.fetches, this.orderBy, -1, -1, this.group);
    }

    public OrderBy[] orderBy() {
        return orderBy;
    }

    public void setOrderBy( OrderBy[] orderBy ) {
        this.orderBy = orderBy;
    }

    public Join[] fetches() {
        return fetches;  
    }

    public void setOrderBys( OrderBy... orderBy ) {
        this.orderBy = orderBy;
    }

    public Group group() {
        return group;
    }

    public int getCount() {
        return dao.count(fetches, group );
    }

	public void setFetches(Join... fetches) {
		this.fetches = fetches;
	}
    

}