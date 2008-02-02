package org.crank.crud.relationships;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class SelectManyRelationshipManager extends RelationshipManager {
	private Object parentObject;
	private String idProperty="id";
	private String labelProperty="name";

	@SuppressWarnings("unchecked")
	public void process(Set<Object> selectedRelatedEntities, Set<Object> entitiesInView) {
		Object collection = getChildCollection(parentObject);
		if (collection == null) {
			return;
		}
		Iterator<Object> iterator = iterator(collection);
		
		Set currentValues = toSet(collection);
		
		
		while (iterator.hasNext()) {
			Object currentObject = iterator.next();
			
			/* If the current object is in the view, operate on it. */
			if (entitiesInView.contains(currentObject)) {
				/* If the current object was in the view but not selected then remove it. */
				if (! selectedRelatedEntities.contains(currentObject)) {
					removeFromParent(parentObject, currentObject);
				}
			}			
		}
		
		for (Object selected : selectedRelatedEntities) {
			if (!currentValues.contains(selected)) {
				addToParent(parentObject, selected);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public String getCollectionLabelString() {
		Object collection = getChildCollection(parentObject);
		if (collection == null) {
			return "";
		}
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
			iterator = new ArrayList(((Map)collection).values()).iterator();
		} else {
			iterator = new ArrayList(((Collection)collection)).iterator();
		}
		return iterator;
	}

	private Set toSet(Object collection) {
		if (collection instanceof Map) {
			return new LinkedHashSet(((Map)collection).values());
		} else if (collection instanceof Set) {
			return new LinkedHashSet((Set)collection);
		} else {
			return new LinkedHashSet((Collection)collection);
		}
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
		if (collection == null) {
			return false;
		}
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
