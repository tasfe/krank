package org.crank.validation;

import java.util.Map;
import java.util.Stack;

/** This is the validator context. 
 *  It holds thread local state that is important to validation.
 *  It is an interface into underlying component and/or MVC/Model 2 architectures
 *  to abstract and simplify access to the context of things we need to perform validation.
 *  @author Rick Hightower
 * */
public class ValidationContext {


    private Stack<String> bindingPath = new Stack<String>();
    
    
    /** Holds the parent object of the field. A parent object is an
     * object that contains fields.*/
    private Object parentObject;
    
    /** Context for validaiton rule variables. */
    private Map<String, Object> params;
    
    /** Holds the data(context) for the current thread. */
    private static ThreadLocal<ValidationContext> holder = new ThreadLocal<ValidationContext>();
    
    /** Provides access to the ValidationContext.
     * @return xx
     * */
    public static ValidationContext getCurrentInstance() {
        return holder.get();
    }
    
    /**Allows the subclass to register an instance of itself 
     * as the context. The subclass will either be JSF, Struts 2 (WebWork) or
     * Spring MVC aware.
     * 
     * @param context xx
     */
    protected void register(ValidationContext context) {
        holder.set(context);
    }

    /** Get the parent object. Allows the FieldValidators to access
     * the parent object. 
     * @return xx
     */
    public Object getParentObject() {
        return parentObject;
    }
    
    /** Allows our integration piece for JSF or Spring MVC to set the 
     * parent object. The parent object is the form bean in Spring MVC speak.
     * @param parentObject  xx
     */
    public void setParentObject(Object parentObject) {
        this.parentObject = parentObject;
    }

    /**
     * Proivde a list of parameters that we can access from field validators.
     * @return xx
     */
    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
    
    /**
     * Gets the proposed property value.
     * This is the value before it gets applied to the actual bean.
     * @param propertyName  xx
     * @return   xx
     */
    public Object getProposedPropertyValue(String propertyName) {
        return null;
    }

    private String calculateBindingPath() {
        StringBuilder builder = new StringBuilder(255);
        int index = 0;
        for (String component : bindingPath) {
            index++;
            builder.append(component);
            if (index!=bindingPath.size()) {
                builder.append('.');
            }
        }
        return builder.toString();
    }

    public void pop () {
        bindingPath.pop();
    }

    public void pushProperty (final String component) {
        bindingPath.push(component);
    }
    public void pushObject (final Object object) {
        String simpleName = object.getClass().getSimpleName();
        simpleName = simpleName.substring(0,1).toLowerCase() + simpleName.substring(1,simpleName.length());
        bindingPath.push(simpleName);
    }

    public static String getBindingPath() {
        if (getCurrentInstance() != null) {
            return getCurrentInstance().calculateBindingPath();
        }
        return "";
    }

    public static ValidationContext create() {
        holder.set(new ValidationContext());
        return get();
    }

    public static ValidationContext get () {
        return holder.get();
    }

    public static void destroy() {
        holder.set(null);
    }


    
}
