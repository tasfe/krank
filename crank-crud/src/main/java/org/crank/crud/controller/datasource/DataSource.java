package org.crank.crud.controller.datasource;

import java.util.List;

public interface DataSource<T> {
    public List<?> list();
}
