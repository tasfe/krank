package org.crank.model;

import java.util.List;

/** Builds a tree model for a tree component from a list. */
public interface TreeModelBuilder {

	/**
	 * Build a tree based on a java.util.List
	 * @param list
	 * @return the new tree model object.
	 */
	public Object createTreeModelFromList(List list);

}
