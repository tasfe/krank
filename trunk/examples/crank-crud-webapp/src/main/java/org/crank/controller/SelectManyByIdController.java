package org.crank.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import org.crank.crud.GenericDao;
import org.crank.crud.controller.CrudController;
import org.crank.crud.controller.CrudControllerListener;
import org.crank.crud.controller.CrudEvent;
import org.crank.crud.controller.CrudOperations;
import org.crank.crud.controller.CrudOutcome;
import org.crank.crud.controller.CrudUtils;
import org.crank.crud.controller.FilterablePageable;
import org.crank.crud.controller.FilteringEvent;
import org.crank.crud.controller.FilteringListener;
import org.crank.crud.controller.PaginationEvent;
import org.crank.crud.controller.PaginationListener;
import org.crank.crud.controller.Row;
import org.crank.crud.criteria.Comparison;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeanWrapper;


@SuppressWarnings("serial")
public class SelectManyByIdController<PT extends Serializable, T extends Serializable, PK extends Serializable> implements Serializable {

	/** This is used by the GUI to look up entity name and label and such. */
	private Class<T> entityClass;
	private String relationshipName;
	private GenericDao<T, PK> repo;
	private FilterablePageable paginator;
	private CrudOperations<PT> parentCrudController;
	private DataModel modelChoices = new ListDataModel();
	private String targetProperty;
	private String sourceProperty="id";
	private boolean show;

	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

	public void cancel () {
		this.show = false;
	}
	
	public void showSelection() {
		this.show = true;
	}
	
	public SelectManyByIdController(){
		
	}
	
	@SuppressWarnings("unchecked")
	public void init() {
		final CrudController<T, PK> cc = (CrudController<T, PK>) parentCrudController;
		cc.setCreateOutcome(CrudOutcome.FORM);
		cc.setUseEntityAsCurrent(true);
		cc.setAddStrategy(CrudOperations.ADD_BY_MERGE);
		
		paginator.addFilteringListener(new FilteringListener(){

			public void afterFilter(FilteringEvent fe) {
				prepareModelChoices();
			}

			public void beforeFilter(FilteringEvent fe) {
				prepareModelChoices();
			}});
		
		paginator.addPaginationListener(new PaginationListener(){

			public void pagination(PaginationEvent pe) {
				prepareModelChoices();
			}});
		
		parentCrudController.addCrudControllerListener(new CrudControllerListener(){

			public void afterCancel(CrudEvent event) {
			}

			public void afterCreate(CrudEvent event) {
				/* Force form to reload in edit mode. */
				cc.stayOnForm();
			}

			public void afterDelete(CrudEvent event) {
			}

			@SuppressWarnings("unchecked")
			public void afterLoadCreate(CrudEvent event) {
				System.out.println("LOAD CREATE FOR " + parentCrudController);
				prepareModelChoices();				
			}

			public void afterLoadListing(CrudEvent event) {
			}

			public void afterRead(CrudEvent event) {
				System.out.println("READ FOR " + parentCrudController);
				prepareModelChoices();
			}

			public void afterUpdate(CrudEvent event) {
			}

			public void beforeCancel(CrudEvent event) {
			}

			public void beforeCreate(CrudEvent event) {
			}

			public void beforeDelete(CrudEvent event) {
			}

			public void beforeLoadCreate(CrudEvent event) {
			}

			public void beforeLoadListing(CrudEvent event) {
			}

			public void beforeRead(CrudEvent event) {
			}

			public void beforeUpdate(CrudEvent event) {
			}});
	}
	
	@SuppressWarnings("unchecked")
	protected void prepareModelChoices() {
		List<Row> availableTags = new ArrayList<Row>();
		Set<T> selectedTags = (Set<T>) new TreeSet<T>(this.getSelectedChildren());
		List<T> allTags = (List<T>) paginator.getPage();
		for (T availableTag : allTags) {
			Row row = new Row();
			row.setObject(availableTag);
			if (selectedTags.contains(availableTag)) {
				row.setSelected(true);
			}
			availableTags.add(row);
		}
		modelChoices.setWrappedData(availableTags);
	}
	
	public void setRepo(GenericDao<T, PK> dao) {
		this.repo = dao;
	}
	
	
	@SuppressWarnings("unchecked")
	protected Set<T> getSelectedChildren() {
		BeanWrapper parent = getParent();
		PK parentId = getParentId(parent);
		if (parentId != null) {
			List<T> children = (List<T>) repo.find(Comparison.eq(targetProperty, parentId ));
			return new TreeSet<T>(children);
		} else {
			return Collections.emptySet();
		}
	}

	protected BeanWrapper getParent() {
		return new BeanWrapperImpl(parentCrudController.getEntity());
	}
	
	public DataModel getAvailableChoices() {
		if (modelChoices.getWrappedData()==null) {
			prepareModelChoices();
		}
		return modelChoices;
	}
	
	@SuppressWarnings("unchecked")
	public void process() {
		List<Row> availableTags = (List<Row>) modelChoices.getWrappedData();
		List<T> tagsToProcess = new ArrayList<T>();
		for (Row row : availableTags) {
			T tag = (T) row.getObject();
			BeanWrapper child = new BeanWrapperImpl(tag);
			BeanWrapper parent = getParent();
			tagsToProcess.add(tag);
			PK parentId = getParentId(parent);

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

	@SuppressWarnings("unchecked")
	private PK getParentId(BeanWrapper parent) {
		PK parentId = (PK) parent.getPropertyValue(sourceProperty);
		return parentId;
	}
	private PK getParentId() {
		return getParentId(getParent());
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

	public void setPaginator(FilterablePageable paginator) {
		this.paginator = paginator;
	}

	public String getRelationshipName() {
		if (relationshipName==null) {
			if (this.entityClass!=null) {
				return CrudUtils.getClassEntityName(this.entityClass);
			} else {
				return "related";
			}
		} else {
			return relationshipName;
		}
	}
	public void setRelationshipName(String relationshipName) {
		this.relationshipName = relationshipName;
	}
	public Class<T> getEntityClass() {
		return entityClass;
	}
	public void setEntityClass(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	public FilterablePageable getPaginator() {
		return paginator;
	}
	
	public boolean isRendered() {
		
		return getParentId()!=null;
	}

	
}
