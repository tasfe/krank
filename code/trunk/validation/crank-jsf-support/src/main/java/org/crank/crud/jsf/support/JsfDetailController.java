package org.crank.crud.jsf.support;

import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;

import org.crank.crud.controller.CrudOperations;
import org.crank.crud.controller.CrudOutcome;
import org.crank.crud.controller.DetailController;

public class JsfDetailController extends DetailController{
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
        return subForm;
    }

    public void setSubForm( UIPanel subForm ) {
        this.subForm = subForm;
    }

    /**
     * Blank out the component fields so users don't see old values.
     *
     */
    private void blankOutInputComponentFields() {
        Iterator<UIComponent> iterator = subForm.getChildren().iterator();
        while (iterator.hasNext()) {
            Object comp = iterator.next();
            if (comp instanceof UIInput) {
                /* Reset the submittedValue for this component. */
                ((UIInput) comp).setSubmittedValue(null);
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
