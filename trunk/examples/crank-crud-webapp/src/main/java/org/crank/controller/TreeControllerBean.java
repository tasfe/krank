package org.crank.controller;

import javax.faces.context.FacesContext;

import org.crank.crud.controller.datasource.DaoFilteringDataSource;
import org.crank.crud.jsf.support.JsfCrudAdapter;
import org.crank.crud.model.Department;
import org.crank.model.TreeModelBuilder;



public class TreeControllerBean {
	private DaoFilteringDataSource<Department, Long> dataSource;
	
	private TreeModelBuilder treeBuilder;
	private Object treeModel;
	private Object componentState;
	private JsfCrudAdapter employeeCrud;
	private JsfCrudAdapter deptCrud;

	public void setTreeBuilder(TreeModelBuilder treeBuilder) {
		this.treeBuilder = treeBuilder;
	}

	
	public void buildTree () {
		treeModel = treeBuilder.createTreeModelFromList(dataSource.list());
	}

	public Object getTreeModel() {
		if (treeModel == null) {
			buildTree();
		}
		return treeModel;
	}


	public void setDataSource(DaoFilteringDataSource<Department, Long> dataSource) {
		this.dataSource = dataSource;
	}


	public Object getComponentState() {
		return componentState;
	}


	public void setComponentState(Object componentState) {
		this.componentState = componentState;
	}
	
	public String selectEmployee() {
		String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
		if (id!=null) {
			employeeCrud.getController().read();
		}
		return "EMPLOYEE_FORM";
	}

	public String selectDepartment() {
		String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
		if (id!=null) {
			deptCrud.getController().read();
		}
		return "DEPARTMENT_FORM";
	}

	public void setEmployeeCrud(JsfCrudAdapter employeeCrud) {
		this.employeeCrud = employeeCrud;
		
	}
	public void setDeptCrud(JsfCrudAdapter deptCrud) {
		this.deptCrud = deptCrud;
		
	}

}
