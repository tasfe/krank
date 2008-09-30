package org.crank.crud.controller;

import java.io.Serializable;
import java.util.*;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.crank.crud.criteria.Comparison;

/**
 *  This class allows you to select an id into an object where a true JPA relationship does not exist.
 *
 */
public abstract class SelectOneByIdController
	<PT extends Serializable, T extends Serializable, PK extends Serializable> 
	extends SelectByIdController<PT, T, PK>{

    /** The item that the user clicked on in the listing. */
    private T clickedItem;

    private boolean toParent;

    public boolean isToParent() {
        return toParent;
    }

    public void setToParent(boolean toParent) {
        this.toParent = toParent;
    }

    /**
     * This method gets called when the user clicks on an item in the listing. 
     */
	public void process() {
        /* retrieve the item selected and wrap it in a bean wrapper. */
        BeanWrapper child = new BeanWrapperImpl(clickedItem);

        if (!toParent) {
            /* Set the selected id into child. */
            child.setPropertyValue(super.targetProperty, getParentId());
        } else {
            getParent().setPropertyValue(super.targetProperty, child.getPropertyValue(super.sourceProperty));
        }

        /* Unselect whatever was selected. */
        if (!toParent) {
            Set<T> unselectedChildren = getSelectedChildren();
            for (T unSelect : unselectedChildren) {
                BeanWrapper unChild = new BeanWrapperImpl(unSelect);
                unChild.setPropertyValue(super.targetProperty, null);
            }
            /* Merge the changes with the database. */
            updateChildren(unselectedChildren);
        } else {
            //
        }


        /* Quit showing the listing to the end user. */
        this.setShow(false);
	}

    protected void updateChildren(Set<T> unselectedChildren) {
        repo.merge(unselectedChildren);
        repo.merge(clickedItem);                    
    }

    public T getClickedItem() {
		return clickedItem;
	}

	public void setClickedItem(T clickedItem) {
		this.clickedItem = clickedItem;
	}
	
	public T getCurrentChild() {
		
		Iterator<T> iter = this.getSelectedChildren().iterator();
		if (iter.hasNext()) {
			return iter.next();
		} else {
			return null;
		}
		
	}

    protected Set<T> getSelectedChildren() {

        /* If we are setting the id in the child, let's do that here. */
        if (!toParent) {
            BeanWrapper parent = getParent();
            PK parentId = getParentId(parent);
            if (parentId == null) {
                return Collections.emptySet();
            }

            return new TreeSet<T>(findSelectedChildren(targetProperty, parentId));
        } else {
            /* If we are setting the id into the parent, let's find out which child is selected here. */
            /* Look up the current value in the parent. */
            Object value = getParent().getPropertyValue(targetProperty);
            if (value == null) {
                return Collections.emptySet();
            }
            /* Look up the child by the sourceProperty with the value that we just pulled from the parent. */
            return new TreeSet<T>(findSelectedChildren(sourceProperty, value));
        }
    }

    protected List<T> findSelectedChildren(String property, Object value) {
            return repo.find(Comparison.eq(property, value));
    }


    public boolean isRendered() {
		if (toParent) {
            return true;
        } else {
            return super.isRendered();
        }
    }


}