package org.crank.crud.controller;

import java.io.Serializable;
import java.util.List;

import org.crank.crud.QueryHint;

@SuppressWarnings("serial")
public class CrudManagedObject implements Serializable {
	private Class<?> idType = Long.class;
    private Class<?> entityType;
    private Class<?> daoInterface;
    private String name;
    private boolean needsConverter;
    private boolean needsDropDownSupport;
    private boolean transactionalController;
    private String newSelect = null;
	private List<QueryHint<?>> queryHints;
    private String [] propertyNames;

    public String[] getPropertyNames() {
        return propertyNames;
    }

    public void setPropertyNames(String... propertyNames) {
        this.propertyNames = propertyNames;
    }

    public List<QueryHint<?>> getQueryHints() {
		return queryHints;
	}
	public void setQueryHints(List<QueryHint<?>> queryHints) {
		this.queryHints = queryHints;
	}
	public String getNewSelect() {
		return newSelect;
	}
	public void setNewSelect(String newSelect) {
		this.newSelect = newSelect;
	}
	public CrudManagedObject () {
        
    }
    
    public CrudManagedObject (final Class<?> entityType) {
        this.entityType = entityType;
    }


    public static CrudManagedObject createWithEntityNameAndProperties(String name, final Class<?> entityType, final String... propertyNames) {
        CrudManagedObject crudManagedObject = new CrudManagedObject();
        crudManagedObject.name = name;
        crudManagedObject.entityType = entityType;
        crudManagedObject.propertyNames = propertyNames;
        return crudManagedObject;
    }

    public static CrudManagedObject createWithProperties(final Class<?> entityType, final String... propertyNames) {
        CrudManagedObject crudManagedObject = new CrudManagedObject();
        crudManagedObject.entityType = entityType;
        crudManagedObject.propertyNames = propertyNames;        
        return crudManagedObject;
    }

    public static CrudManagedObject createWithPropertiesAndDAO(final Class<?> entityType, final Class<?> daoInterface, final String... propertyNames) {
        CrudManagedObject crudManagedObject = new CrudManagedObject();
        crudManagedObject.entityType = entityType;
        crudManagedObject.propertyNames = propertyNames;
        crudManagedObject.daoInterface = daoInterface;
        return crudManagedObject;
    }

    public static CrudManagedObject createWithNameAndDAOAndProperties(final String name, final Class<?> entityType, 
    		final Class<?> daoInterface, final String... propertyNames) {
        CrudManagedObject crudManagedObject = new CrudManagedObject();
        crudManagedObject.entityType = entityType;
        crudManagedObject.propertyNames = propertyNames;
        crudManagedObject.daoInterface = daoInterface;
        crudManagedObject.name = name;
        return crudManagedObject;
    }



    public CrudManagedObject (final Class<?> entityType, final Class<?> daoInterface) {
        this.entityType = entityType;
        this.daoInterface = daoInterface;
    }
    public CrudManagedObject (final Class<?> entityType, final String name, final Class<?> daoInterface) {
        this.entityType = entityType;
        this.name = name;
        this.daoInterface = daoInterface;
    }
    public CrudManagedObject (final Class<?> entityType, final Class<?> idType, final String name, 
    		final Class<?> daoInterface) {
        this.entityType = entityType;
        this.idType = idType;
        this.name = name;
        this.daoInterface = daoInterface;
    }

    public String getName() {
        return name == null ? CrudUtils.getClassEntityName(entityType) : name; 
    }
    public void setName( String name ) {
        this.name = name;
    }
    public Class<?> getEntityType() {
        return entityType;
    }
    public void setEntityType( Class<?> entityType ) {
        this.entityType = entityType;
    }
    public Class<?> getIdType() {
        return idType;
    }
    public void setIdType( Class<?> idType ) {
        this.idType = idType;
    }
    public Class<?> getDaoInterface() {
        return daoInterface;
    }
    public void setDaoInterface( Class<?> daoInterface ) {
        this.daoInterface = daoInterface;
    }
    public boolean isNeedsConverter() {
        return needsConverter;
    }
    public void setNeedsConverter( boolean needsConverter ) {
        this.needsConverter = needsConverter;
    }
    public boolean isNeedsDropDownSupport() {
        return needsDropDownSupport;
    }
    public void setNeedsDropDownSupport( boolean needsDropDownSupport ) {
        this.needsDropDownSupport = needsDropDownSupport;
    }
	public boolean isTransactionalController() {
		return transactionalController;
	}
	public void setTransactionalController(boolean transactionalController) {
		this.transactionalController = transactionalController;
	}
}
