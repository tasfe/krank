package org.crank.crud.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public abstract class SelectManyByIdController
	<PT extends Serializable, T extends Serializable, PK extends Serializable> 
	extends SelectByIdController<PT, T, PK>{

	@SuppressWarnings("unchecked")
	public void process() {
		List<Row> availableTags = getRows();
		List<T> tagsToProcess = new ArrayList<T>();
		for (Row row : availableTags) {
			T tag = (T) row.getObject();
			BeanWrapper child = new BeanWrapperImpl(tag);
			BeanWrapper parent = getParent();
			tagsToProcess.add(tag);
			PK parentId = (PK) getParentId(parent);
	
			if (row.isSelected()) {
				child.setPropertyValue(targetProperty, parentId);
			} else {
				
				if (parentId!= null && parentId.equals(child.getPropertyValue(targetProperty))) {
					child.setPropertyValue(targetProperty, null);
				}
			}
		}
		this.repo.merge((List<T>)tagsToProcess);
	}
}