package org.crank.crud.relationships;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class SelectManyRelationshipManager extends RelationshipManager {
	private Object parentObject;
	private String idProperty="id";
	private String labelProperty="name";

	@SuppressWarnings("unchecked")
	public void process(List selectedRelatedEntities) {
		Object collection = getChildCollection(parentObject);
		
		if (collection instanceof Map) {
			((Map)collection).clear();
		} else {
			((Collection)collection).clear();
		}
		
		for (Object object : selectedRelatedEntities) {
			addToParent(parentObject, object);
		}
	}
	
	@SuppressWarnings("unchecked")
	public String getCollectionLabelString() {
		Object collection = getChildCollection(parentObject);
		Iterator iterator = null;
		StringBuilder builder = new StringBuilder(255);

		iterator = iterator(collection);
		
		
		while (iterator.hasNext()) {
			Object object = iterator.next();
			BeanWrapper wrapper = new BeanWrapperImpl(object);
			Object propertyValue = wrapper.getPropertyValue(labelProperty);
			builder.append(propertyValue.toString() + ", ");
		}
		String string = builder.toString();
		if (string.trim().length() >= 2) { 
			return string.substring(0, string.length() - 2);
		} else {
			return string;
		}

	}

	@SuppressWarnings("unchecked")
	private Iterator iterator(Object collection) {
		Iterator iterator = null;
		if (collection instanceof Map) {
			iterator = ((Map)collection).values().iterator();
		} else {
			iterator = ((Collection)collection).iterator();
		}
		return iterator;
	}

	public Object getParentObject() {
		return parentObject;
	}

	public void setParentObject(Object parentObject) {
		this.parentObject = parentObject;
	}
	
	public String getIdProperty() {
		return idProperty;
	}

	public void setIdProperty(String idProperty) {
		this.idProperty = idProperty;
	}

	public String getLabelProperty() {
		return labelProperty;
	}

	public void setLabelProperty(String labelProperty) {
		this.labelProperty = labelProperty;
	}

	@SuppressWarnings("unchecked")
	public boolean isSelected(Object object) {
		Object collection = getChildCollection(parentObject);
		Iterator iterator = iterator(collection);

		BeanWrapper wrapper = new BeanWrapperImpl();
		
		while(iterator.hasNext()) {
			Object next = iterator.next();
			wrapper.setWrappedInstance(next);
			Object id1 = wrapper.getPropertyValue(this.idProperty);
			wrapper.setWrappedInstance(object);
			Object id2 = wrapper.getPropertyValue(this.idProperty);
			if (id1.equals(id2)) {
				return true;
			}
			
		}
		
		return false;
	}
}
