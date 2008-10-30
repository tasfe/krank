package org.crank.core.spring.support;

import java.util.Map;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;


import javax.faces.el.PropertyResolver;


/**
 *
 * @author $author$
 * @version $Revision$
  */
@SuppressWarnings("deprecation")
public class DotPropertyResolver extends PropertyResolver {
    // ------------------------------------------------------ Instance Variables

    /**
     * <p>The original <code>VariableResolver</code> passed to our constructor.</p>
     */
    private PropertyResolver original = null;

    /**
     * <p>Construct a new {@link DotPropertyResolver} instance.</p>
     *
     *
     * @param aOriginal Original resolver to delegate to.
     */
    public DotPropertyResolver(final PropertyResolver aOriginal) {
        this.original = aOriginal;
    }

    // ------------------------------------------------ PropertyResolver Methods
    
    boolean doesPropertyHaveDots(Object objProperty) {
        if (!(objProperty instanceof String)) {
            return false;
        }
        String propertyName = (String) objProperty;
        return (propertyName.contains( "." ));
    }

    /**
     * <p>Look up and return the named object corresponding to the
     * specified property name from this Context.</p>
     *
     * @param base Base object from which to return a property
     * @param property Property to be returned
     * @return the value in the app context.
     *  object in this context
     */
    public Object getValue(final Object base, final Object property) {
        if (base == null) {
            return original.getValue(base, property);
        }
        try {
            if (doesPropertyHaveDots(property) && !(base instanceof Map)) {
                BeanWrapper wrapper = new BeanWrapperImpl (base);
                Object propertyValue = wrapper.getPropertyValue( (String) property);
                if (propertyValue == null) {
                    return original.getValue(base, property);
                }
                return propertyValue;
            } else {
                return original.getValue(base, property);
            }
        } catch (Exception ex) {
            return original.getValue(base, property);            
        }
    }

    /**
     *
     * @param base Base object in which to store a property
     * @param property Property to be stored
     * @param value Value to be stored
     *
     *  object in this context
     */
    public void setValue(final Object base, final Object property,
        final Object value) {
        if (base == null) {
            original.setValue(base, property, value);
        }
        
        try {
            if (doesPropertyHaveDots(property) && !(base instanceof Map)) {
                BeanWrapper wrapper = new BeanWrapperImpl (base);
                wrapper.setPropertyValue( (String) property, value);
            } else {
                original.setValue(base, property, value);
            }
        } catch (Exception ex) {
            original.setValue(base, property, value);           
        }
    }

    /**
     * <p>Arbitrarily return false because we cannot determine if a
     * Context is read only or not.</p>
     *
     * @param base Base object from which to return read only state
     * @param property Property to be checked
     * @return always true if ApplicationContext
     *  object in this context
     */
    public boolean isReadOnly(final Object base, final Object property) {
        if (base == null) {
            return original.isReadOnly(base, property);
        }
        try {
            if (doesPropertyHaveDots(property) && !(base instanceof Map)) {
                return true;
            } else {
                return original.isReadOnly(base, property);
            }
        } catch (Exception ex) {
            return original.isReadOnly(base, property);
        }
    }

    /**
     * <p>Look up and return the type of the named object corresponding to the
     * specified property name from this Context.</p>
     *
     * @param base Base object from which to return a property type
     * @param property Property whose type is to be returned
     * @return type
     */
    public Class<?> getType(final Object base, final Object property) {
        if (base == null) {
            return original.getType(base, property);
        }

        try {
            if (doesPropertyHaveDots(property) && !(base instanceof Map)) {
                BeanWrapper wrapper = new BeanWrapperImpl (base);
                return wrapper.getPropertyType( (String) property);
            } else {
                return original.getType(base, property);
            }
        } catch (Exception ex) {
            return original.getType(base, property);
            
        }
    }

    /**
     * <p>Convert an index into a corresponding string, and delegate.</p>
     *
     * @param base Base object from which to return a property
     * @param index Index to be returned
     *
     * @return object in this context
     */
    public Object getValue(final Object base, final int index) {
         return original.getValue(base, index);
    }

    /**
     * <p>Convert an index into a corresponding string, and delegate.</p>
     *
     * @param base Base object into which to store a property
     * @param index Index to be stored
     * @param value Value to be stored
     *
     *  object in this context
     */
    public void setValue(final Object base, final int index, final Object value) {
         original.setValue(base, index, value);
    }

    /**
     * <p>Convert an index into a corresponding string, and delegate.</p>
     *
     * @param base Base object from which to check a property
     * @param index Index to be checked
     * @return true always
     */
    public boolean isReadOnly(final Object base, final int index) {
        return original.isReadOnly(base, index);
    }

    /**
     * <p>Convert an index into a corresponding string, and delegate.</p>
     *
     * @param base Base object from which to return a property type
     * @param index Index whose type is to be returned
     * @return type
     */
    public Class<?> getType(final Object base, final int index) {
        return original.getType(base, index);
    }
}