package org.crank.crud.controller;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public abstract class SelectOneByIdController
	<PT extends Serializable, T extends Serializable, PK extends Serializable> 
	extends SelectByIdController<PT, T, PK>{

	private T clickedItem;
	
	@SuppressWarnings("unchecked")
	public void process() {
		BeanWrapper child = new BeanWrapperImpl(clickedItem);
		child.setPropertyValue(super.targetProperty, getParentId());
		Set<T> unselectedChildren = getSelectedChildren();
		for (T unSelect : unselectedChildren) {
			BeanWrapper unChild = new BeanWrapperImpl(unSelect);
			unChild.setPropertyValue(targetProperty, null);
		}
		repo.merge(unselectedChildren);
		repo.merge(clickedItem);		
		this.setShow(false);
	}

	public T getClickedItem() {
		return clickedItem;
	}

	public void setClickedItem(T clickedItem) {
		this.clickedItem = clickedItem;
	}
	
	public T getCurrentChild() {
		
		Iterator<T> iter = this.getSelectedChildren().iterator();
		if (iter.hasNext()) {
			return iter.next();
		} else {
			return null;
		}
		
	}

}