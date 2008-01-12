package org.crank.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import org.crank.crud.GenericDao;
import org.crank.crud.controller.CrudOperations;
import org.crank.crud.controller.Row;
import org.crank.crud.criteria.Comparison;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeanWrapper;


@SuppressWarnings("serial")
public class SelectManyByIdController<PT extends Serializable, T, PK extends Serializable> implements Serializable {
	private GenericDao<T, PK> repo;
	
	private CrudOperations<PT> parentCrudController;
	private DataModel modelTags = new ListDataModel();
	private String targetProperty;
	private String sourceProperty="id";

	public void setRepo(GenericDao<T, PK> dao) {
		this.repo = dao;
	}
	
	protected Set<T> getSelectedChildren() {
		BeanWrapper parent = getParent();
		List<T> children = (List<T>) repo.find(Comparison.eq(targetProperty, parent.getPropertyValue(sourceProperty) ));
		return new TreeSet<T>(children);
	}

	private BeanWrapper getParent() {
		return new BeanWrapperImpl(parentCrudController.getEntity());
	}
	
	public DataModel getAvailableTags() {
		List<Row> availableTags = new ArrayList<Row>();
		Set<T> selectedTags = getSelectedChildren();
		List<T> allTags = (List<T>) repo.find();
		for (T availableTag : allTags) {
			Row row = new Row();
			row.setObject(availableTag);
			if (selectedTags.contains(availableTag)) {
				row.setSelected(true);
			}
			availableTags.add(row);
		}
		modelTags.setWrappedData(availableTags);
		return modelTags;
	}
	
	@SuppressWarnings("unchecked")
	public void process() {
		List<Row> availableTags = (List<Row>) modelTags.getWrappedData();
		List<T> tagsToProcess = new ArrayList<T>();
		for (Row row : availableTags) {
			T tag = (T) row.getObject();
			BeanWrapper child = new BeanWrapperImpl(tag);
			BeanWrapper parent = getParent();
			tagsToProcess.add(tag);
			PK parentId = (PK) parent.getPropertyValue(sourceProperty);

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

	public void setParentCrudController(
			CrudOperations<PT> crudOperations) {
		this.parentCrudController = crudOperations;
	}

	public CrudOperations<PT> getParentCrudController() {
		return parentCrudController;
	}

	public void setTargetProperty(String targetProperty) {
		this.targetProperty = targetProperty;
	}

	public void setSourceProperty(String sourceProperty) {
		this.sourceProperty = sourceProperty;
	}
	
}
