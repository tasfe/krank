package org.crank.crud.jsf.support;

import java.util.List;

import javax.faces.model.SelectItem;

import org.crank.core.spring.support.SelectItemUtils;
import org.crank.crud.controller.datasource.DataSource;

public class SelectItemGenerator {
    
    private SelectItemUtils selectItemUtils = new SelectItemUtils();
    private DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource( DataSource dataSource ) {
        this.dataSource = dataSource;
    }

    public void setSelectItemUtils( SelectItemUtils itemUtils ) {
        this.selectItemUtils = itemUtils;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getList() {
        return selectItemUtils.createSelectItems( dataSource.list() );
    }
    
    @SuppressWarnings("unchecked")
    public List<SelectItem> getListOptional() {
        return selectItemUtils.createSelectItems( dataSource.list(), true );
    }

}
