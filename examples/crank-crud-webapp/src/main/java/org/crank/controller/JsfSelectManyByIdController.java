package org.crank.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import org.crank.crud.controller.Row;




@SuppressWarnings("serial")
public class JsfSelectManyByIdController<PT extends Serializable, T extends Serializable, PK extends Serializable> extends SelectManyByIdController2<PT, T, PK> implements Serializable {

	private DataModel modelChoices = new ListDataModel();

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
	protected List<Row> getRows() {
		List<Row> availableTags = (List<Row>) modelChoices.getWrappedData();
		return availableTags;
	}
	
}
