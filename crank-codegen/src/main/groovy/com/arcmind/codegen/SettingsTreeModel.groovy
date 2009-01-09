/**
 *
 */
package com.arcmind.codegen
import javax.swing.event.TreeModelEvent
import javax.swing.event.TreeModelListener
import javax.swing.tree.TreeModel
import javax.swing.tree.TreePath


class SettingsHolder {
	JDBCSettings settings

	void setJDBCSettings (JDBCSettings jdbcSettings) {
		this.settings = jdbcSettings
	}
	public String toString() {
		assert settings != null 
		settings.toString()
	}
}



/**
 * @author richardhightower
 *
 */
public class SettingsTreeModel implements TreeModel{
	List<SettingsHolder> settingsHolders = []

    private Vector<TreeModelListener> treeModelListeners =
    new Vector<TreeModelListener>();

    /**
     * The only event raised by this model is TreeStructureChanged with the
     * root as path, i.e. the whole tree has changed.
     */
    protected void setSettings(List<JDBCSettings> settings) {
    	settingsHolders.clear()
    	for (JDBCSettings jdbcSettings : settings) {
    		SettingsHolder holder = new SettingsHolder(settings:jdbcSettings)
    		settingsHolders << holder
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
    	assert settingsHolders
    	settingsHolders[index]
//    	if (parent instanceof JavaClassTreeModel) {
//    		return javaClassHolders[index]
//    	} else if (parent instanceof JavaClassHolder) {
//    		JavaClassHolder javaClass = (JavaClassHolder)parent;
//    		return javaClass.lists[index]
//    	} else if (parent instanceof ListHolder) {
//    		ListHolder listHolder = (ListHolder) parent
//    		return listHolder.list[index]
//    	}
    }

    /**
     * Returns the number of children of parent.
     */
    public int getChildCount(Object parent) {
    	if (parent instanceof SettingsTreeModel) {
    		return settingsHolders==null ? 0 : settingsHolders.size()
    	}
    				
//    	} else if (parent instanceof JavaClassHolder) {
//    		JavaClassHolder javaClass = (JavaClassHolder) parent;
//    		return javaClass.lists.size()
//    	} else if (parent instanceof ListHolder) {
//    		ListHolder listHolder = (ListHolder) parent
//    		return listHolder.list == null ? 0 : listHolder.list.size()
//    	}
    }

    /**
     * Returns the index of child in parent.
     */
    public int getIndexOfChild(Object parent, Object child) {
    	settingsHolders.indexOf(child)
//    	if (parent instanceof JavaClassTreeModel) {
//    		return javaClassHolders.indexOf(child)
//    	} else if (parent instanceof JavaClassHolder) {
//    		JavaClassHolder javaClass = (JavaClassHolder) parent;
//    		return javaClass.lists.indexOf(child)
//    	}  else if (parent instanceof ListHolder) {
//    		ListHolder listHolder = (ListHolder) parent
//    		return listHolder.list.indexOf(child)
//    	}
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
        !(node instanceof SettingsTreeModel)
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
    	return "jdbc settings";
    }
}
