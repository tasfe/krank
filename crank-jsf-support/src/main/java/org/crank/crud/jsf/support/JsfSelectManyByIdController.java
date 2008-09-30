package org.crank.crud.jsf.support;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import org.crank.crud.controller.Row;
import org.crank.crud.controller.SelectManyByIdController;




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
        DataModel model = getAvailableChoices();
        StringBuilder builder = new StringBuilder();
        List<Row> availableTags = (List<Row>) model.getWrappedData();
        int hits = 0;
        for (Row row : availableTags) {
            if (!row.isSelected()){
                 continue;
            }
            hits++;
            String label;
            label = (String) row.get(selectedProperty);
            if (label == null) {
               label = (String) row.get("id");
            }

            if (label == null) {
                label = "UNABLE TO RETRIEVE PROPERTY....";
            }

            builder.append(label + ", ");
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
