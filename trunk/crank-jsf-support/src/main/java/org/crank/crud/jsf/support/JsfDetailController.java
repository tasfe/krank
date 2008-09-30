package org.crank.crud.jsf.support;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import org.crank.crud.controller.CrudOperations;
import org.crank.crud.controller.CrudOutcome;
import org.crank.crud.controller.DetailController;
import org.crank.crud.controller.EntityLocator;
import org.crank.crud.controller.Row;
import org.crank.crud.controller.SelectListener;
import org.crank.crud.controller.SelectSupport;
import org.crank.crud.controller.Selectable;

@SuppressWarnings( { "unchecked", "serial" })
public class JsfDetailController<T extends Serializable, PK extends Serializable>
		extends DetailController<T, PK> implements Selectable, EntityLocator<T> {

	protected boolean allowsSelection = false;
	protected boolean magicMaps = true;
	protected DataModel model = new ListDataModel();
	protected SelectSupport selectSupport = new SelectSupport(this);

	protected Comparator orderByComparator;

	{
		super.entityLocator = this;
	}

	public JsfDetailController() {
		super();
	}

	public JsfDetailController(Class entityClass) {
		super(entityClass);
	}

	public JsfDetailController(Class entityClass, boolean allowSelection) {
		super(entityClass);
		this.allowsSelection = allowSelection;
	}

	public JsfDetailController(CrudOperations parent, Class entityClass) {
		super(parent, entityClass);
	}

	private UIPanel subForm;

	public UIPanel getSubForm() {
		return null;
	}

	public void setSubForm(UIPanel subForm) {
		this.subForm = subForm;
	}

	public DataModel getModel() {
		List<?> al = getRowData();
		if (allowsSelection || magicMaps) {
			al = wrapListElementsInRowObjects(al);
		}
		model.setWrappedData(al);
		return model;
	}

	public List<?> getRowData() {
		List<?> al = new ArrayList();
		if (parent.getEntity() != null) {

			Object object = this.relationshipManager
					.retrieveChildCollectionFromParentObject(
							parent.getEntity(), true);
			if (object instanceof List) {
				al = (List) object;
			} else if (object instanceof Set) {
				al = new ArrayList((Set) object);
			} else if (object instanceof Map) {
				Map map = (Map) object;
				al = new ArrayList(map.values());
			}
		}
		if (orderByComparator != null) {
			Collections.sort(al, orderByComparator);
		}
		return al;
	}

	private List wrapListElementsInRowObjects(List al) {
		ArrayList newList = new ArrayList(al.size());
		Iterator iterator = al.iterator();
		while (iterator.hasNext()) {
			newList.add(new Row(iterator.next()));

		}
		return newList;
	}

	/**
	 * Blank out the component fields so users don't see old values.
	 * 
	 */
	private void blankOutInputComponentFields() {
		if (subForm != null) {
			if (subForm.getChildren() != null) {
				Iterator<UIComponent> iterator = subForm.getChildren()
						.iterator();
				while (iterator.hasNext()) {
					Object comp = iterator.next();
					if (comp instanceof UIInput) {
						/* Reset the submittedValue for this component. */
						((UIInput) comp).setSubmittedValue(null);
					}
				}
			}
		}
	}

	@Override
	public CrudOutcome cancel() {
		/* Skip the rest of the phases past "Apply Request Values" */
		FacesContext.getCurrentInstance().renderResponse();
		blankOutInputComponentFields();
		return super.cancel();
	}

	public Comparator getOrderByComparator() {
		return orderByComparator;
	}

	public void setOrderByComparator(Comparator orderBy) {
		this.orderByComparator = orderBy;
	}

	public boolean isAllowsSelection() {
		return allowsSelection;
	}

	public void setAllowsSelection(boolean allowsSelection) {
		this.allowsSelection = allowsSelection;
	}

	public void processSelection() {
		if (!allowsSelection) {
			throw new RuntimeException(
					"The allowSelection property must be set");
		}

		selectSupport.fireSelect(getSelectedEntities());
	}

	public void addSelectListener(SelectListener listener) {
		if (!allowsSelection) {
			throw new RuntimeException(
					"The allowSelection property must be set");
		}

		selectSupport.addSelectListener(listener);
	}

	public void removeSelectListener(SelectListener listener) {
		if (!allowsSelection) {
			throw new RuntimeException(
					"The allowSelection property must be set");
		}

		selectSupport.removeSelectListener(listener);
	}

	public List<T> getSelectedEntities() {
		if (!allowsSelection) {
			throw new RuntimeException(
					"The allowSelection property must be set");
		}
		List<Row> list = (List<Row>) model.getWrappedData();
		List<T> selectedList = new ArrayList<T>(Math.max(list.size(), 10));
		for (Row row : list) {
			if (row.isSelected()) {
				selectedList.add((T) row.getObject());
			}
		}
		return selectedList;
	}

	public void setMagicMaps(boolean magicMaps) {
		this.magicMaps = magicMaps;
	}

}
