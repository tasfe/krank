package org.crank.controller;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.crank.crud.jsf.support.JsfDetailController;
import org.crank.crud.model.Employee;

public class SayHelloController {

	@SuppressWarnings("unchecked")
	public JsfDetailController employeesController;

	@SuppressWarnings("unchecked")
	public JsfDetailController getEmployeesController() {
		return employeesController;
	}

	@SuppressWarnings("unchecked")
	public void setEmployeesController(JsfDetailController employeesController) {
		this.employeesController = employeesController;
	}
	
	public void sayHello() {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Hello Task " + 
				FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id")
		));
	}
	
	@SuppressWarnings("unchecked")
	public void hello() {
		List<Employee> list = employeesController.getSelectedEntities();
		for (Employee emp : list) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Hello " + emp.getFirstName()));
		}
	}
}
