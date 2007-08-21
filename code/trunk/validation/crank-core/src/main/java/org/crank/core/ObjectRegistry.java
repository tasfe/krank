package org.crank.core;



public interface ObjectRegistry {
	public Object getObject(String name);
    
    public Object getObjectReturnNullIfMissing(String name);

	public Object getObject(String string, Class<?> clazz);
	
	public void resolveCollaborators(Object object);

	public Object[] getObjectsByType(Class<?> clazz);

    public Object convertObject(Object value, Class<?> clazz);
}
