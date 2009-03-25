package org.crank.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import org.crank.crud.controller.CrudOperations;
import org.crank.crud.controller.Row;
import org.crank.crud.dao.TagDAO;
import org.crank.crud.model.Employee;
import org.crank.crud.model.Tag;


@SuppressWarnings("serial")
public class TagController implements Serializable {
	private TagDAO tagRepo;
	private CrudOperations<Employee> parentCrudController;
	private DataModel modelTags = new ListDataModel();

	public void setTagRepo(TagDAO tagRepo) {
		this.tagRepo = tagRepo;
	}
	
	protected Set<Tag> getSelectedTags() {
		Employee employee = getEmployee();
		List<Tag> tagsForEmployee = tagRepo.findTagsForEmployee(employee.getId());
		return new TreeSet<Tag>(tagsForEmployee);
	}

	private Employee getEmployee() {
		Employee employee = parentCrudController.getEntity();
		return employee;
	}
	
	public DataModel getAvailableTags() {
		List<Row> availableTags = new ArrayList<Row>();
		Set<Tag> selectedTags = getSelectedTags();
		List<Tag> allTags = tagRepo.find();
		for (Tag availableTag : allTags) {
			Row row = new Row();
			row.setObject(availableTag);
			if (selectedTags.contains(availableTag)) {
				row.setSelected(true);
			}
			availableTags.add(row);
		}
		modelTags.setWrappedData(availableTags);
		return modelTags;
	}
	
	@SuppressWarnings("unchecked")
	public void process() {
		List<Row> availableTags = (List<Row>) modelTags.getWrappedData();
		List<Tag> tagsToProcess = new ArrayList<Tag>();
		for (Row row : availableTags) {
			Tag tag = (Tag) row.getObject();
			tagsToProcess.add(tag);
			if (row.isSelected()) {
				tag.setEmployeeId(getEmployee().getId());
			} else {
				Long employeeId = tag.getEmployeeId();
				if (employeeId!= null && employeeId.equals(tag.getEmployeeId())) {
					tag.setEmployeeId(null);
				}
			}
		}
		this.tagRepo.merge(tagsToProcess);
	}

	public void setParentCrudController(
			CrudOperations<Employee> crudOperations) {
		this.parentCrudController = crudOperations;
	}

	public CrudOperations<Employee> getParentCrudController() {
		return parentCrudController;
	}
	
}
