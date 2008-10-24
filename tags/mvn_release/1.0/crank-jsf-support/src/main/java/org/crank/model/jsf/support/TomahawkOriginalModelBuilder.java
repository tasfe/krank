package org.crank.model.jsf.support;



import org.apache.myfaces.custom.tree.DefaultMutableTreeNode;
import org.apache.myfaces.custom.tree.model.DefaultTreeModel;
import org.crank.model.AbstractTreeModelBuilder;


/**
 * A tree builder for the original Tomahawk tree.
 * @author Rick Hightower
 *
 */
@SuppressWarnings("unchecked")
public class TomahawkOriginalModelBuilder extends AbstractTreeModelBuilder{

	/**
	 * Create the table model based on the root object.
	 * @param root root node object.
	 */
	protected Object createTreeModel(Object root) {
		return new DefaultTreeModel((DefaultMutableTreeNode)root);
	}

	/**
	 * Add the child to the node.
	 * @param parent parent node
	 * @param child child node
	 */
	protected void addToNode(Object parent, Object child) {
		DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)parent;
		DefaultMutableTreeNode childNode = (DefaultMutableTreeNode)child;
		parentNode.insert(childNode);

	}

	/**
	 * Create a new node.
	 * @param name name of node
	 */
	protected Object createFolder(String name) {
		return new DefaultMutableTreeNode(name);
	}

	protected Object createNode(String name, Object data) {
		DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(name);
		treeNode.setUserObject(data);
		return treeNode;
	}

	protected Object createRoot(String name) {
		// TODO Auto-generated method stub
		return new DefaultMutableTreeNode(name);
	}


}
