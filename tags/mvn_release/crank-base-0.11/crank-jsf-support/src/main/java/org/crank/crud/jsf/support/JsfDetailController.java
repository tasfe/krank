package org.crank.crud.jsf.support;

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

public class JsfDetailController extends DetailController{
    protected DataModel model = new ListDataModel();

    protected Comparator orderByComparator;
    
    public JsfDetailController() {
        super();
    }

    public JsfDetailController( Class entityClass ) {
        super( entityClass );
    }

    public JsfDetailController( CrudOperations parent, Class entityClass ) {
        super( parent, entityClass );
    }

    private UIPanel subForm;

    public UIPanel getSubForm() {
        return null;
    }

    public void setSubForm( UIPanel subForm ) {
        this.subForm = subForm;
    }
    
    @SuppressWarnings("unchecked")
    public DataModel getModel() {
    	List al = new ArrayList();
        if (parent.getEntity() != null) {
            Object object = this.relationshipManager.retrieveChildCollectionFromParentObject(parent.getEntity(), true);
            if (object instanceof List) {
            	al = (List) object; 
            } else if (object instanceof Set) {
                al =  new ArrayList((Set)object);
            } else if (object instanceof Map)  {
                Map map = (Map) object;
                al =  new ArrayList(map.values());
            }
            if (orderByComparator != null) {
            	Collections.sort(al, orderByComparator);
            }
        }
        model.setWrappedData( al );
        return model;
    }

    /**
     * Blank out the component fields so users don't see old values.
     *
     */
    private void blankOutInputComponentFields() {
    	if (subForm != null){
    		if (subForm.getChildren() != null) {
    	        Iterator<UIComponent> iterator = subForm.getChildren().iterator();
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

}
