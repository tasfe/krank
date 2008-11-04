package org.crank.crud.jsf.support;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import org.crank.crud.controller.Row;
import org.crank.crud.controller.SelectOneByIdController;

@SuppressWarnings("serial")
public class JsfSelectOneByIdController<PT extends Serializable, T extends Serializable, PK extends Serializable> extends SelectOneByIdController<PT, T, PK> implements Serializable {

	private DataModel modelChoices = new ListDataModel();

	public JsfSelectOneByIdController(){
		
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
	protected List<Row> getRows() {
		List<Row> availableTags = (List<Row>) modelChoices.getWrappedData();
		return availableTags;
	}
	
}
