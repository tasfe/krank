package org.crank.crud.jsf.support;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import org.crank.crud.controller.Row;
import org.crank.crud.controller.SelectManyByIdController;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;




@SuppressWarnings("serial")
public class JsfSelectManyByIdController<PT extends Serializable, T extends Serializable, PK extends Serializable> extends SelectManyByIdController<PT, T, PK> implements Serializable {

	private DataModel modelChoices = new ListDataModel();


    private String selectedProperty = "name";

    public JsfSelectManyByIdController(){
		
	}

	protected void prepareModelChoices(List<Row> availableTags) {
		modelChoices.setWrappedData(availableTags);
	}
	
	public DataModel getAvailableChoices() {
		if (modelChoices.getWrappedData()==null) {
			prepareModelChoices();
		}
		return modelChoices;
	}

    @SuppressWarnings("unchecked")
	public String getSelectedString() {
 
    	int hits = 0;
    	StringBuilder builder = new StringBuilder();
    	Set<T> children = super.getSelectedChildren();
    	
    	for(Object child : children){
    		hits++;
    		
    		BeanWrapper wrapper = new BeanWrapperImpl (child);
    		Object propertyValue = wrapper.getPropertyValue(selectedProperty);

    		if (propertyValue == null) {
    			 propertyValue = wrapper.getPropertyValue("id");
            }

    		builder.append(propertyValue + ", ");
    	}
    	if (hits > 0) {
             return builder.toString().substring(0,builder.length()-2);
        } else {
             return "<<empty>>";
        }
    }

    @SuppressWarnings("unchecked")
	protected List<Row> getRows() {
		List<Row> availableTags = (List<Row>) modelChoices.getWrappedData();
		return availableTags;
	}

    public void setSelectedProperty(String selectedProperty) {
        this.selectedProperty = selectedProperty;
    }
    
}
