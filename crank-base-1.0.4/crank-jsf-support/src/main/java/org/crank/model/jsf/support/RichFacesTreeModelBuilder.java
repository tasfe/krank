package org.crank.model.jsf.support;




import org.crank.model.AbstractTreeModelBuilder;



/**
 * A tree builder for Rich Faces.
 * @author Rick Hightower
 *
 */
@SuppressWarnings("unchecked")
public class RichFacesTreeModelBuilder extends AbstractTreeModelBuilder{
	private String idProperty = "id";

	public String getIdProperty() {
		return idProperty;
	}

	public void setIdProperty(String idProperty) {
		this.idProperty = idProperty;
	}

	/**
	 * Create the table model based on the root object.
	 * @param root root node object.
	 */
	protected Object createTreeModel(Object root) {
		return root;
	}

	/**
	 * Add the child to the node.
	 * @param parent parent node
	 * @param child child node
	 */
	protected void addToNode(Object parent, Object child) {
		//TreeNodeImpl parentNode = (TreeNodeImpl)parent;
		//TreeNodeImpl childNode = (TreeNodeImpl)child;
		//Object identifier = null;
		
//		if (childNode.getModel()!=null) {
//			identifier = new BeanWrapperImpl(childNode.getModel()).getPropertyValue(idProperty);
//		} else {
//			identifier = childNode.getTitle();
//		}
//		parentNode.addChild(identifier, childNode);

	}

	/**
	 * Create a new node.
	 * @param name name of node
	 */
	protected Object createFolder(String name) {
		TreeNodeImpl treeNode = new TreeNodeImpl();
//		treeNode.setTitle(name);
//		treeNode.setType("Folder");
//		treeNode.setModel(null);
		return treeNode;
	}

	protected Object createNode(String name, Object data) {
		TreeNodeImpl treeNode = new TreeNodeImpl();
//		treeNode.setTitle(name);
//		treeNode.setType(data.getClass().getSimpleName());
//		treeNode.setModel(data);
		return treeNode;
	}

	protected Object createRoot(String name) {
		return createFolder(name);
	}


}
