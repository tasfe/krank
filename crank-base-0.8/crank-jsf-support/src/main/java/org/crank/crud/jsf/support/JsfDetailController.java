package org.crank.crud.jsf.support;

import java.util.ArrayList;
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
    private DataModel model = new ListDataModel();

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
        if (parent.getEntity()==null) {
            model.setWrappedData( new ArrayList() );
        } else {
            Object object = this.relationshipManager.retrieveChildCollectionFromParentObject(parent.getEntity(), true);
            if (object instanceof List) {
                model.setWrappedData( object );
            } else if (object instanceof Set) {
                model.setWrappedData( new ArrayList((Set)object) );
            } else if (object instanceof Map)  {
                Map map = (Map) object;
                model.setWrappedData( new ArrayList(map.values()) );
            }
        }
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

}
