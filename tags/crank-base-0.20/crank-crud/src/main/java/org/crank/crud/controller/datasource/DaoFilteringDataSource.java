package org.crank.crud.controller.datasource;

import java.io.Serializable;
import java.util.List;

import org.crank.crud.criteria.Group;
import org.crank.crud.criteria.OrderBy;
import org.crank.crud.criteria.Select;
import org.crank.crud.join.Join;

public class DaoFilteringDataSource<T, PK extends Serializable> extends DaoDataSource<T, PK> implements FilteringDataSource<T> {
    protected Group group = new Group();
    protected OrderBy[] orderBy = new OrderBy[]{};
    protected Join[] fetches = new Join[]{};
    protected Select[] selects = new Select[]{};

    public DaoFilteringDataSource() {
        super();
    }

    public List<?> list() {
        return dao.find(this.selects, this.fetches, this.orderBy, -1, -1, this.group);
    }

    public OrderBy[] orderBy() {
        return orderBy;
    }

    public void setOrderBy( OrderBy[] orderBy ) {
        this.orderBy = orderBy;
    }

    @Deprecated
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

    @Deprecated
	public void setFetches(Join... fetches) {
		this.fetches = fetches;
	}

	public Join[] joins() {
		// TODO Auto-generated method stub
		return fetches;  
	}

	public void setJoins(Join[] fetches) {
		this.fetches = fetches;		
	}

	public Select[] selects() {
		return selects;
	}

	public void setSelects(Select[] selects) {
		this.selects = selects;
	}
    

}