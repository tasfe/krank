package org.crank.web.validation.jsf.support;

import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import org.crank.annotations.design.NeedsRefactoring;

/**
 * @author Rick Hightower heavily based on code 
 * from Cagatay Civici writen with Apache license.
 */
public class JSFComponentTreeUtils {

    /** Find a component in a component subtree.. 
     * Here we are navigating down the tree. */
	@SuppressWarnings("unchecked")
	public static UIComponent findComponent(UIComponent root, String id) {
        
        /* Hey maybe we are lucky and the component is the root. */
		if (id.equals(root.getId()))
			return root;

        /* Recursively iterate through the component tree and find
         * this id. Peek a boo... I see you.*/
		UIComponent child = null;
		UIComponent component = null;
		Iterator children = root.getFacetsAndChildren();
        
        /* Iterate through the component tree. */
		while (children.hasNext() && (component == null)) {
            
            /* Evaluate this child. */
			child = (UIComponent) children.next();
            
            /* Hey child are you the component I am looking for? */
			if (id.equals(child.getId())) {
				component = child;
				break;
			}
            /* If not, does the component I looking for exist
             * in your component tree?
             */
			component = findComponent(child, id);
			if (component != null) {
				break;
			}
		}
		return component;
	}

    /* This method starts at the root and finds the component. */
	public static UIComponent findComponentInView(String id) {
		UIComponent component = null;
		FacesContext context = FacesContext.getCurrentInstance();
		if (context != null) {
			UIComponent root = context.getViewRoot();
			component = findComponent(root, id);
		}
		return component;
	}

    /** Find a childs component form if it can be found. 
     *  Here we are navigating up the tree.
     * */
	public static UIForm findForm(UIComponent child) {
		UIComponent component = child;
		while (!((component=component.getParent())instanceof UIForm)){
			if (component instanceof UIViewRoot) break;
		}
		return (UIForm)component;
	}

    /** 
     * Find an input component given a property name it is bound to.
     * 
     * @param container
     * @param propertyName
     * @return
     */
    @SuppressWarnings("unchecked")
	@NeedsRefactoring("See long comment at the end of the method.")
    public static UIInput findInput(UIComponent container, String propertyName) {
        
        /* Let's try this the easy way first. */
        UIInput input = (UIInput)container.findComponent(propertyName);
        
        if (input == null) {
            /* If that did not work, let's get a little rougher. */
            input = (UIInput) findComponent(container, propertyName);
        }
      
        /* Look for the property as part of the value expression. */
        if (input==null) {
            List<UIComponent> children = container.getChildren();
            for (Iterator iter = children.iterator(); iter.hasNext();) {
                UIComponent comp = (UIComponent) iter.next();
                if (!(comp instanceof UIInput)) {
                    return findInput(comp, propertyName);
                } else {
                    UIInput potentialInput = (UIInput) comp;
                    String expression = potentialInput.getValueExpression("value").getExpressionString();
                    if (expression.endsWith(propertyName+"}")
                            || 
                        expression.endsWith(propertyName+"']}")        
                    ){
                        input = potentialInput;
                    } 
                }
            }
        }

        /* The same thing but less strict. */
        if (input==null) {
            List<UIComponent> children = container.getChildren();
            for (Iterator iter = children.iterator(); iter.hasNext();) {
                UIComponent comp = (UIComponent) iter.next();
                if (!(comp instanceof UIInput)) {
                    return findInput(comp, propertyName);
                } else {
                    UIInput potentialInput = (UIInput) comp;
                    String expression = potentialInput.getValueExpression("value").getExpressionString();
                    if (expression.contains(propertyName)){
                        input = potentialInput;
                    } 
                }
            }
        }
        /* The only other thing left is to look for brackets ([]) that don't have
         * quotes (not ['...'] OR ["..."]) and then evaluate the object in 
         * the brackets and see if it is
         * a string. If it is a string, then see if it is equal to our
         * property value. We may have to do this to make this work in all
         * situations. The above should make this work Presto and 99% of
         * other JSF applications. The last bit I described should cover the remained
         * 1%. This is a lot of effort for the remaing 1% so I defer for now.
         */
        return input;
    }
    
}
