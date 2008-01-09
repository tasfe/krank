package org.crank.crud.controller;

import java.util.EventListener;


public interface CrudControllerListener extends EventListener {
    public void afterUpdate(CrudEvent event);
    public void beforeUpdate(CrudEvent event);
    
    public void afterCreate(CrudEvent event);
    public void beforeCreate(CrudEvent event);
    
    public void afterLoadCreate(CrudEvent event);
    public void beforeLoadCreate(CrudEvent event);
    
    public void afterRead(CrudEvent event);
    public void beforeRead(CrudEvent event);
    
    public void afterDelete(CrudEvent event);
    public void beforeDelete(CrudEvent event);
    
    public void afterCancel(CrudEvent event);
    public void beforeCancel(CrudEvent event);
	public void afterLoadListing(CrudEvent event);
	public void beforeLoadListing(CrudEvent event);

}
