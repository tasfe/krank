package org.crank.model.jsf.support;

import java.io.Serializable;

import org.apache.myfaces.custom.tree2.TreeNodeBase;
import org.apache.myfaces.custom.tree2.TreeModelBase;
import org.crank.model.AbstractTreeModelBuilder;


/**
 * A Tree builder for the new Tomahawk tree 2.
 * @author Rick Hightower
 *
 */
@SuppressWarnings("unchecked")
public class TomahawkTree2ModelBuilder extends AbstractTreeModelBuilder{


	public static class ExtendedTreeNodeBase extends TreeNodeBase {
		private Serializable data;
		public ExtendedTreeNodeBase(String type, String name, Object data, boolean flag) {
			super(type, name, flag);
			this.data = (Serializable) data;
		}
		public Serializable getData() {
			return data;
		}
		public void setData(Serializable data) {
			this.data = data;
		}

	}

	/**
	 * Create the table model based on the root object.
	 * @param root root node object.
	 */
	protected Object createTreeModel(Object root) {
		return new TreeModelBase((TreeNodeBase)root);
	}

	/**
	 * Add the child to the node.
	 * @param parent parent node
	 * @param child child node
	 */
	protected void addToNode(Object parent, Object child) {
		TreeNodeBase parentNode = (TreeNodeBase)parent;
		TreeNodeBase childNode = (TreeNodeBase)child;
		parentNode.getChildren().add(childNode);
	}

	/**
	 * Create a new node.
	 */
	protected Object createFolder(String name) {
		return new TreeNodeBase("folder",name,false);
	}

	protected Object createNode(String name, Object data) {
		return new ExtendedTreeNodeBase("node", name, data, false);
	}

	protected Object createRoot(String name) {
		// TODO Auto-generated method stub
		return new TreeNodeBase("root",name,false);
	}

}
