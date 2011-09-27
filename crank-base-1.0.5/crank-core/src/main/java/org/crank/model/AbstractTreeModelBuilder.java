package org.crank.model;


import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.crank.core.CrankException;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;


/**
 * <p> This class turns a list into a tree model that can be used by GUIs </p>
 *
 * <p>
 * Takes directions on how to build the tree as a String or String array.
 * Then it uses these directions to build a tree.
 * </p>
 *
 * <p>
 * This is a base class whose subclasses can work with the two tree models from Tomahawk, and
 * possible in the future ADF and/or IceFaces models.
 * </p>
 *
 * <p>
 * The base classes just need to override the following:
 * </p>
 * <code><pre>
 *	protected abstract Object createTableModel(Object root);
 *	protected abstract void addToNode(Object parent, Object child);
 * 	protected abstract Object createNode(String name);
 *	</pre></code>
 *
 * <p> In theory, this tree builder could work with Swing. </p>
 *
 * <p> In addition, the class has several extention points (protected methods) for classes that want to
 *  process nodes a bit different than the default. </p>
 *
 * <p>
 * For example:
 * </p>
 *
 * <p>
 * Given the directions:
 * </p>
 *
 * <strong>Departments->this.name->employees.name;groups.title->employees.pets.cuteName</strong>
 *
 * <p>The above translates to building a tree like this:</p>
 *
 * <code> <pre>
 * Departments
 * 	     ->this.name
 *         ->employees.name
 *             -> employees.pets.cuteName
 *         ->groups.title
 * </pre> </code>
 *
 * <p>Thus this list:</p>
 * <code><pre>
 *
 *
 *
 *                        Department cas = new Department("cas");
 *
 *                        cas.getEmployees().add(new Employee("Rick Hightower"));
 *
 *                        cas.getEmployees().add(new Employee("David E."));
 *
 *                        cas.getGroups().add(new Group("PRESTO"));
 *
 *                        cas.getGroups().add(new Group("ArcMind"));
 *
 *                        list.add(cas);
 *
 *
 *
 *                        Department hrIt = new Department("hrIt");
 *
 *                        Employee angelica = new Employee("Angelica W.");
 *
 *                        angelica.getPets().add(new Pet("Mooney"));
 *
 *                        hrIt.getEmployees().add(angelica);
 *
 *                        hrIt.getEmployees().add(new Employee("Gordon B."));
 *
 *                        hrIt.getGroups().add(new Group("PRESTO2"));
 *
 *                        hrIt.getGroups().add(new Group("ArcMind2"));
 *
 *
 *
 *                        list.add(hrIt);
 *
 *
 * </pre></code>
 *
 * <p>Would produce a tree like this:</p>
 * <code><pre>
 * Departments
 * 	    	    -> cas
 * 	    	    	    -> employees
 * 	    	    	    	    -> Rick Hightower
 * 	    	    	    	    -> David E.
 * 	    	    	    -> groups
 * 	    	    	    	    -> PRESTO
 * 	    	    	    	    -> ArcMind
 * 	    	    -> hrIt
 * 	    	    	    -> employees
 * 	    	    	    	    -> Angelica W.
 *                                  -> Mooney
 * 	    	    	    	    -> Gordon B.
 * 	    	    	    -> groups
 * 	    	    	    	    -> PRESTO2
 * 	    	    	    	    -> ArcMind2
 * </pre></code>
 * @author Rick Hightower
 *
 */
public abstract class AbstractTreeModelBuilder<T, N> implements TreeModelBuilder<T>{

	private boolean noRoot;

	/** Directions as a single string where "->" delimits the levels */
	private String treeBuildDirections;
	/** Direction as a string array. */
	private String [] buildDirections;



	/** Create the table model. */
	protected abstract T createTreeModel(Object root);
	/** Add a child node to the parent node. */
	protected abstract void addToNode(N parent, N child);
	/** Create a new node. */
	protected abstract N createFolder(String name);
	protected abstract N createNode(String name, Object data);
	protected abstract N createRoot(String name);


	/**
	 * Creates a table model from a list.
	 *  @param list A list of objects that will be base objects in the tree.
	 *  @return A new Table Model
	 * */
	public T createTreeModelFromList(List<?> list) {
		if (list == null) {
			throw new CrankException("the list argument cannot be null");			
		}
		
		/* As we build the tree let's walk through an example of parsing a build string like this:
		  "Departments->this.name->employees.name;groups.name"
		*/
		String [] levels = getDirections();
		if (levels == null || levels.length == 0) {
			throw new CrankException("the tree build directions could not be interpreted");
		}

		N root = null;
		/* Create the root Node. */
		int level = 0; //index=0
		String mainObjectPropertyLabel = getFirstLevelLabel(levels);
		if (!noRoot) {
			root = createFolder(levels[level]);
			level ++; //walk down the tree. index=1

			/* Create the BaseObjects */
			createBaseObjects(list, levels, level, root);

		} else if (list.size()==1) {
			Object objRoot = list.get(0);

			root = createNodeBasedOnObjectProperty(mainObjectPropertyLabel, objRoot);
			level++; level++;
			processNextLevels(objRoot, objRoot, levels, level, root, null);
		} else {

			root = createNodeBasedOnObjectProperty(mainObjectPropertyLabel, list.get(0));
			list = list.subList(1, list.size());
			level ++; //walk down the tree. index=1

			/* Create the BaseObjects */
			createBaseObjects(list, levels, level, root);

		}

		return createTreeModel(root);
	}
	private String getFirstLevelLabel(String[] levels) {
		if (levels.length < 2) {
			throw new CrankException("expected at least two levels in getFirstLevelLabel");
		}
		String currentBuildDirections = levels[1]; //currentBuildDirections like "this.name"
		String[] level2 = currentBuildDirections.split("[.]"); //Splits into something like "this", "name"
		return level2[1]; //grabs the label, i.e., "name".
	}

	/**
	 * This creates a list of base objects.
	 *
	 * @param list The list we are converting to a tree.
	 * @param levels The level building direction, each element is buildign directions for a new level.
	 * @param level The current level that we are on.
	 * @param root The root object that this will be added to.
	 */
	protected void createBaseObjects(List<?> list, String[] levels, int level, N root) {

		String mainObjectPropertyLabel = getFirstLevelLabel(levels);
		level ++; //walk down the tree. index = 2


		/* Iterate through the list. For each object recursively walk the object properties, using
		 * the build directions as a guide for building the tree.
		 */
		Iterator<?> iterator = list.iterator();
		while(iterator.hasNext()) {
			Object object = iterator.next();

			/* Extract the property to display and create the Node with this extracted property. */
			N mainObjectNode = createNodeBasedOnObjectProperty(mainObjectPropertyLabel, object);

			/* Add this new baseObjectNode to the root */
			addToNode(root, mainObjectNode);

			/* Now recursively walk the tree for each level. */
			if (levels.length >= level +1) {
					processNextLevels(object, object, levels, level, mainObjectNode, null);
			}

		}
	}



	/**
	 * This is an recursive method that walks the levels and produces subtrees.
	 * @param baseObject The very base object. This is the root object or an object off the root (a base object).
	 * @param object This is the current object, i.e., an employee in a department.
	 * @param levels This is the array of level direction where each element is a direction for the level.
	 * @param level This is the current level number.
	 * @param parentNode This is the parent node of the node we are building.
	 * @param branch This is the branch name. The branch is used to identify which directions belong to this node.
	 */
	private void processNextLevels(Object baseObject, Object object, String[] levels, int level,
			                       N parentNode, String branch) {

		/* Get the current node build directions. */
		String sLevel = levels[level]; //"employees.name;groups.name"

		/* A node may have multiple directions for adding children.
		 * For example, we may have directions to add children that are employees and children that are groups.
		 * The direction employees.name;groups.name means add groups and employees. */
		String [] processNodes = sLevel.split("[;]"); //"employees.name;groups.name" becomes ["employee.name", "groups.name"]

		/*
		 * If there are more than one child to add, create a container node for each child.
		 * Thus, if we have just employees.name then add the employees direction to the parent node.
		 * However if we have employees.name;group.name then add a folder called employees add that
		 * to the parent node, and then add the individual employees (e.g., Rick Hightower) to the employees node.
		 */
		boolean createContainer = processNodes.length > 1;

		/* Process the children nodes by adding each group of children to this parentNode.*/
		for (int  index = 0;  index < processNodes.length; index++) {


			/* Get the directions for this child node. */
			String processNodeInstructions = processNodes[index]; //employees.name

			/* Should we recurse? Used for rollups. The "*" means we don't know how many levels deep
			 * the tree goes. */
			boolean recursive=false;
			if (processNodeInstructions.endsWith("*")) {
				recursive=true;
				processNodeInstructions = processNodeInstructions.substring(0,processNodeInstructions.length()-1);
			}
			String[] nodeParams = processNodeInstructions.split("[.]"); //employees.name becomes ["employees", "name"]

			/* Get the name of the list property and the name of the label for the items in the list. */
			String listProperty = nodeParams[nodeParams.length-2];  //"employees"
			String labelProperty = nodeParams[nodeParams.length-1]; //"name"

			/* Get the branch id for this child. Only use directions that correspond to the current branch*/
			String newBranch = getBranchId(nodeParams);

			/* If there is not a match it means that these direction correspond to another child at this node
			 * so just continue.
			 * The branch id for our example would be employees. If the current branch passed does not
			 * begin with "employees" then we don't process it.
			 */
			if (branch!=null) {
				if (!newBranch.startsWith(branch)) {
					continue;
				}
			}

			N realParentNode = createParentNodeIfNeeded(parentNode, createContainer, listProperty);
			Iterator<?> iterator = getIteratorOfChildObjectsFromParentObject(object, listProperty);

			/* Process the children node. */
			while(iterator.hasNext()) {
				Object childObject = iterator.next();
				N childNode = createNodeBasedOnObjectProperty(labelProperty, childObject);
				boolean handled = false; //has the children nodes been handled?
				if (level + 2 <= levels.length) {
					level++;
					processNextLevels(baseObject, childObject, levels, level, childNode, newBranch);
					handled = true;
				}
				/* If the child nodes have not already been handled check to see if this is a recursive node.
				 * If it is a recursive node, recurse.*/
				if (!handled && recursive) {
					/* Notice this level is not incremented because we stay at this level's instructions
					 * until the children have no more children.
					 */
					processNextLevels(baseObject, childObject, levels, level, childNode, newBranch);
				}

				addToNode(realParentNode, childNode);
			}
		}
	}

	/**
	 * <p>
	 * The getDirections method will take a build string like this:
	 * </p>
	 *
	 * <code><pre>
	 * 		"Departments->this.name->employees.name;groups.name"
	 * </code></pre>
	 *
	 * And turn it into a string array like this:
	 * <code><pre>
	 *       ["Departments", "this.name", "employees.name;groups.name"]
	 * </code></pre>
	 *
	 *  Each element in the array represents another level.
	 *
	 *  If the treeBuildDirections is not set then we assume that the user set the buildDirections array,
	 *  and we return that instead.
	 */
	protected String[] getDirections() {
		if (treeBuildDirections!=null) {
			return treeBuildDirections.split("->");
		} else {
			return buildDirections;
		}
	}

	/** Creates a node object using a property from a object.
	 *
	 * @param propertyLabel property of label
	 * @param object object that we are extracting the label from.
	 * @return new node
	 */
	protected N createNodeBasedOnObjectProperty(String propertyLabel, Object object) {
		BeanWrapper wrapper = new BeanWrapperImpl(object);

		if (!propertyLabel.contains(",")) {
			String nodeLabel = wrapper.getPropertyValue(propertyLabel).toString();
			return createNode(nodeLabel, object);
		} else {
			StringBuilder builder = new StringBuilder(255);
			String[] labelProperties = propertyLabel.split("[,]");
			for (String label : labelProperties) {
				Object nodeObj = wrapper.getPropertyValue(label);
				if (nodeObj!=null) {
					String nodeLabel = nodeObj.toString();
					builder.append(nodeLabel + " ");
				}				
			}
			return createNode(builder.toString(), object);			
		}
	}

	/**
	 * Creates a new parentNode if the parent has more than one child.
	 *
	 * @param parentNode current parentNode which may be the parentNode or may be the parent of the parentNode.
	 * @param createContainer true if parent has more than one child.
	 * @param listProperty The list property used to look up the label in the resource bundle.
	 * @return parentNode
	 */
	private N createParentNodeIfNeeded(N parentNode, boolean createContainer, String listProperty) {
		N realParentNode = null;

		if (createContainer==true) {
			//TODO look up the label in the resource label ${listProperty}Node.
			realParentNode = createFolder(listProperty);
			addToNode(parentNode,realParentNode);
		} else {
			realParentNode = parentNode;
		}
		return realParentNode;
	}
	/**
	 * Gets a list of objects from a parent object.
	 * @param parentObject parent object
	 * @param listProperty The list property (can be a Set or any java.util.Collection)
	 * @return iterator
	 */
	@SuppressWarnings("unchecked")
	protected Iterator<?> getIteratorOfChildObjectsFromParentObject(Object parentObject, String listProperty) {
		BeanWrapper wrapper = new BeanWrapperImpl(parentObject);
		Object listTypeThing = wrapper.getPropertyValue(listProperty);
		Collection<?> collection = null;
		if (listTypeThing instanceof Map) {
			collection = ((Map)listTypeThing).values();
		} else {
			collection = (Collection) listTypeThing;
		}

		return collection.iterator();
	}

	/**
	 * Calculates the current branch id and passes the branch id.
	 * This is so we add children to the correct parent.
	 * @param nodeParams the list of node params
	 * @return the new branch id based on the list of nodeparams.
	 */
	private String getBranchId(String[] nodeParams) {
		StringBuffer newBranchBuffer = new StringBuffer(255);
		for (int bindex=0; bindex < nodeParams.length -1; bindex++) {
			newBranchBuffer.append(nodeParams[bindex]);
			newBranchBuffer.append(".");
		}

		String newBranch = newBranchBuffer.toString().substring(0, newBranchBuffer.length()-1);
		return newBranch;
	}


	/**
	 * Property method.
	 */
	public void setBuildDirections(String[] buildDirections) {
		this.buildDirections = buildDirections;
	}

	/**
	 * Property method.
	 */
	public void setTreeBuildDirections(String treeBuildDirections) {
		this.treeBuildDirections = treeBuildDirections;
	}
	public boolean isNoRoot() {
		return noRoot;
	}
	public void setNoRoot(boolean noRoot) {
		this.noRoot = noRoot;
	}


}
