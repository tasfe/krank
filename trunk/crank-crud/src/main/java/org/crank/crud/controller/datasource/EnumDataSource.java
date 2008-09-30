package org.crank.crud.controller.datasource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unchecked")
public class EnumDataSource<T> implements DataSource {
    private List list;
    
    private Class<?> clazz;
    
    public void setType(Class<?> clazz) {
    	this.clazz = clazz;
    	buildList();
    }
    
    public EnumDataSource() {
    	
    }
    
    public List list() {
        return list;
    }

	private void buildList() {

        try {
        	list = Arrays.asList(((Class<? extends Object>) clazz).getEnumConstants());
	        if (list == null) {
	        	list = new ArrayList();
	        	list.add("Null");
	        }
		} catch (Exception ex) {
			list = new ArrayList();
			list.add("Error");
			throw new RuntimeException( "Unable to get enums from class " + clazz.getClass(), ex );
		}
	}
	
}
