package org.crank.model.jsf.support;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.richfaces.component.TreeNode;

/**
 * @author Rick Hightower
 */
public class TreeNodeImpl implements TreeNode {
	
	private static final long serialVersionUID = -5498990493803705085L;
	private TreeNode parent;
	private String type;
	private String title;
	private Object model;
	
	private Map childrenMap = new LinkedHashMap();
	
	public Object getData() {
		return this;
	}

	public TreeNode getChild(Object identifier) {
		return (TreeNode) childrenMap.get(identifier);
	}

	public void addChild(Object identifier, TreeNode child) {
		child.setParent(this);
		childrenMap.put(identifier, child);
	}

	public void removeChild(Object identifier) {
		TreeNode treeNode = (TreeNode) childrenMap.remove(identifier);
		if (treeNode != null) {
			treeNode.setParent(null);
		}
	}


	public TreeNode getParent() {
		return parent;
	}

	public void setParent(TreeNode parent) {
		this.parent = parent;
	}

	public Iterator getChildren() {
		return childrenMap.entrySet().iterator();
	}
	
	public boolean isLeaf() {
		return childrenMap.isEmpty();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Object getModel() {
		return model;
	}

	public void setModel(Object model) {
		this.model = model;
	}

	public void setData(Object data) {
		
	}

}
