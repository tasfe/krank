package org.crank.controller;

import org.crank.crud.controller.datasource.DaoFilteringDataSource;
import org.crank.crud.model.Department;
import org.crank.model.TreeModelBuilder;

public class TreeControllerBean {
	private DaoFilteringDataSource<Department, Long> dataSource;
	
	private TreeModelBuilder treeBuilder;
	private Object treeModel;

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

}
