package org.crank.validation;

import java.util.Map;

/** This is the validator context. 
 *  It holds thread local state that is important to validation.
 *  It is an interface into underlying component and/or MVC/Model 2 architectures
 *  to abstract and simplify access to the context of things we need to perform validation.
 *  @author Rick Hightower
 * */
public abstract class ValidationContext {
    
    /** Holds the parent object of the field. A parent object is an
     * object that contains fields.*/
    private Object parentObject;
    
    /** Context for validaiton rule variables. */
    private Map<String, Object> params;
    
    /** Holds the data(context) for the current thread. */
    private static ThreadLocal holder = new ThreadLocal();
    
    /** Provides access to the ValidationContext. */
    public static ValidationContext getCurrentInstance() {
        return (ValidationContext) holder.get();
    }
    
    /**Allows the subclass to register an instance of itself 
     * as the context. The subclass will either be JSF, Struts 2 (WebWork) or
     * Spring MVC aware.
     * 
     * @param context
     */
    protected void register(ValidationContext context) {
        holder.set((ValidationContext)context);
    }

    /** Get the parent object. Allows the FieldValidators to access
     * the parent object. 
     * @return
     */
    public Object getParentObject() {
        return parentObject;
    }
    
    /** Allows our integration piece for JSF or Spring MVC to set the 
     * parent object. The parent object is the form bean in Spring MVC speak.
     * @param parentObject
     */
    public void setParentObject(Object parentObject) {
        this.parentObject = parentObject;
    }

    /**
     * Proivde a list of parameters that we can access from field validators.
     * @return
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
     * @param propertyName
     * @return
     */
    public abstract Object getProposedPropertyValue(String propertyName);
}
