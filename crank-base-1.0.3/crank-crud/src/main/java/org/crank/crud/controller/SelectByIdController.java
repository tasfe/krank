package org.crank.crud.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


import org.crank.crud.GenericDao;
import org.crank.crud.criteria.Comparison;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public abstract class SelectByIdController<PT extends Serializable, T extends Serializable, PK extends Serializable> {

	/** This is used by the GUI to look up entity name and label and such. */
	protected Class<T> entityClass;
	protected String relationshipName;
	protected GenericDao<T, PK> repo;
	protected FilterablePageable paginator;
	protected CrudOperations<PT> parentCrudController;
	protected String targetProperty;
	protected String sourceProperty = "id";


    protected String childSourceProperty = "id";
    protected boolean show;

	public SelectByIdController() {
		super();
	}

	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

	public void cancel() {
		this.show = false;
	}

	public void showSelection() {
		this.show = true;
	}

    private CrudController<T, PK> cc;

    @SuppressWarnings("unchecked")
	public void init() {


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

        if (parentCrudController instanceof CrudController) {
            cc = (CrudController<T, PK>) parentCrudController;
		    cc.setCreateOutcome(CrudOutcome.FORM);
		    cc.setUseEntityAsCurrent(true);
		    cc.setAddStrategy(CrudOperations.ADD_BY_MERGE);
        }

        parentCrudController.addCrudControllerListener(new CrudControllerListenerAdapter(){
	
			public void afterCreate(CrudEvent event) {
                if (cc!=null) {
                    /* Force form to reload in edit mode. */
				    cc.stayOnForm();
                }
            }
	
			public void afterLoadCreate(CrudEvent event) {
				prepareModelChoices();				
			}
	
			public void afterRead(CrudEvent event) {
				prepareModelChoices();
			}
	
			});
	}

	@SuppressWarnings("unchecked")
	protected void prepareModelChoices() {
		Set<T> selectedObjects = this.getSelectedChildren();

        List<T> allObjects = (List<T>) paginator.getPage();
		List<Row> availableObjects = new ArrayList<Row>(allObjects.size());
		for (T availableObject : allObjects) {
			Row row = new Row();
			row.setObject(availableObject);
            for (T selectedObject : selectedObjects) {
                BeanWrapper selectedObjectWrapper = new BeanWrapperImpl(selectedObject);
                if (row.get(childSourceProperty).equals(selectedObjectWrapper.getPropertyValue(childSourceProperty))) {
                    row.setSelected(true);
                }
            }            
			availableObjects.add(row);
		}
		prepareModelChoices(availableObjects);
	}
	abstract protected void prepareModelChoices(List<Row> availableTags);

	public void setRepo(GenericDao<T, PK> dao) {
		this.repo = dao;
	}

	protected Set<T> getSelectedChildren() {
		BeanWrapper parent = getParent();
		PK parentId = getParentId(parent);
		if (parentId != null) {
			List<T> children = repo.find(Comparison.eq(targetProperty, parentId ));
			return new TreeSet<T>(children);
		} else {
			return Collections.emptySet();
		}
	}

	protected BeanWrapper getParent() {
		return new BeanWrapperImpl(parentCrudController.getEntity());
	}

	@SuppressWarnings("unchecked")
	public void process() {
		List<Row> availableTags = getRows();
		List<T> tagsToProcess = new ArrayList<T>(availableTags.size());
		BeanWrapper parent = getParent();
		PK parentId = getParentId(parent);
		BeanWrapper child = new BeanWrapperImpl();
		for (Row row : availableTags) {
			T tag = (T) row.getObject();
			child.setWrappedInstance(tag);
			tagsToProcess.add(tag);
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

	
	abstract protected List<Row> getRows();

	@SuppressWarnings("unchecked")
	protected PK getParentId(BeanWrapper parent) {
		PK parentId = (PK) parent.getPropertyValue(sourceProperty);
		return parentId;
	}

	protected PK getParentId() {
		return getParentId(getParent());
	}

	public void setParentCrudController(CrudOperations<PT> crudOperations) {
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

    public void setChildSourceProperty(String childSourceProperty) {
        this.childSourceProperty = childSourceProperty;
    }
    
}