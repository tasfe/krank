/**
 *
 */
package org.codegen.gui
import javax.swing.event.TreeModelEvent
import javax.swing.event.TreeModelListener
import javax.swing.tree.TreeModel
import javax.swing.tree.TreePath
import org.codegen.model.Relationship
import org.codegen.gui.ListHolder
import org.codegen.model.JavaProperty
import org.codegen.model.JavaClass

class JavaClassHolder {
	JavaClass javaClass
	ListHolder properties
	ListHolder relationships
	List<ListHolder> lists

	void setJavaClass (JavaClass javaClass) {
		this.javaClass = javaClass
		properties = new ListHolder(name: "Properties", list: javaClass.properties)
		relationships = new ListHolder(name: "Relationships", list: javaClass.relationships)
		lists = [properties, relationships]
	}
	public String toString() {
		javaClass.name
	}
}



/**
 * @author richardhightower
 *
 */
public class JavaClassTreeModel implements TreeModel{
	List<JavaClassHolder> javaClassHolders = []

    private Vector<TreeModelListener> treeModelListeners =
    new Vector<TreeModelListener>();

    /**
     * The only event raised by this model is TreeStructureChanged with the
     * root as path, i.e. the whole tree has changed.
     */
    protected void setClasses(List<JavaClass> classes) {
    	javaClassHolders.clear()
    	for (JavaClass javaClass : classes) {
    		JavaClassHolder holder = new JavaClassHolder(javaClass:javaClass)
    		javaClassHolders << holder
    	}
        TreeModelEvent event = new TreeModelEvent(this, [this] as Object[]);
        for (TreeModelListener tml : treeModelListeners) {
            tml.treeStructureChanged(event)
        }
    }

    public void nodeChanged(Object nodeUpdated) {
        TreeModelEvent event = new TreeModelEvent(this, [nodeUpdated] as Object[]);
        for (TreeModelListener tml : treeModelListeners) {
            tml.treeNodesChanged(event)
        }
    }

    /**
     * Adds a listener for the TreeModelEvent posted after the tree changes.
     */
    public void addTreeModelListener(TreeModelListener l) {
        treeModelListeners.addElement(l);
    }

    /**
     * Returns the child of parent at index index in the parent's child array.
     */
    public Object getChild(Object parent, int index) {
    	if (parent instanceof JavaClassTreeModel) {
    		return javaClassHolders[index]
    	} else if (parent instanceof JavaClassHolder) {
    		JavaClassHolder javaClass = (JavaClassHolder)parent;
    		return javaClass.lists[index]
    	} else if (parent instanceof ListHolder) {
    		ListHolder listHolder = (ListHolder) parent
    		return listHolder.list[index]
    	}
    }

    /**
     * Returns the number of children of parent.
     */
    public int getChildCount(Object parent) {
    	if (parent instanceof JavaClassTreeModel) {
    		return javaClassHolders==null ? 0 : javaClassHolders.size()
    	} else if (parent instanceof JavaClassHolder) {
    		JavaClassHolder javaClass = (JavaClassHolder) parent;
    		return javaClass.lists.size()
    	} else if (parent instanceof ListHolder) {
    		ListHolder listHolder = (ListHolder) parent
    		return listHolder.list == null ? 0 : listHolder.list.size()
    	}
    }

    /**
     * Returns the index of child in parent.
     */
    public int getIndexOfChild(Object parent, Object child) {
    	if (parent instanceof JavaClassTreeModel) {
    		return javaClassHolders.indexOf(child)
    	} else if (parent instanceof JavaClassHolder) {
    		JavaClassHolder javaClass = (JavaClassHolder) parent;
    		return javaClass.lists.indexOf(child)
    	}  else if (parent instanceof ListHolder) {
    		ListHolder listHolder = (ListHolder) parent
    		return listHolder.list.indexOf(child)
    	}
    }

    /**
     * Returns the root of the tree.
     */
    public Object getRoot() {
        return this;
    }

    /**
     * Returns true if node is a leaf.
     */
    public boolean isLeaf(Object node) {
        node instanceof JavaProperty || node instanceof Relationship
    }

    /**
     * Removes a listener previously added with addTreeModelListener().
     */
    public void removeTreeModelListener(TreeModelListener l) {
        treeModelListeners.removeElement(l);
    }

    /**
     * Messaged when the user has altered the value for the item
     * identified by path to newValue.  Not used by this model.
     */
    public void valueForPathChanged(TreePath path, Object newValue) {
        System.out.println("*** valueForPathChanged : "
            + path + " --> " + newValue);
    }


    public String toString() {
    	return "classes";
    }
}
