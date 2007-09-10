package org.crank.controller;

import java.util.Iterator;
import java.util.Map;

import org.crank.jsfspring.test.CrankMockObjects;
import org.crank.model.jsf.support.TreeNodeImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


import junit.framework.TestCase;

public class TreeControllerBeanTest extends TestCase {
	private TreeControllerBean controllerBean;
	private CrankMockObjects crankMockObjects;
	
	
	public void setUp() throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

		crankMockObjects = new CrankMockObjects();
		crankMockObjects.setUp();
		crankMockObjects.setUpApplicationContextWithScopes((ConfigurableApplicationContext) context);

		controllerBean = (TreeControllerBean) context.getBean("treeControllerBean");

	}

	public void tearDown() throws Exception {
		crankMockObjects.tearDown();
	}
	
	public void testTreeModel() throws Exception {
		Object treeModel = controllerBean.getTreeModel();
		TreeNodeImpl node = (TreeNodeImpl) treeModel;
		processNode(node);
	}

	private void processNode(TreeNodeImpl node) {
		Iterator children = node.getChildren();
		while (children.hasNext()) {
			
			Map.Entry<Object, Object> entry =  (Map.Entry<Object, Object>) children.next();
			TreeNodeImpl childNode = (TreeNodeImpl) entry.getValue();
			System.out.println("key " + entry.getKey());
			System.out.println("title " + childNode.getTitle());
			System.out.println("type #" + childNode.getType() + "#");
			processNode(childNode);
		}		
	}

}
