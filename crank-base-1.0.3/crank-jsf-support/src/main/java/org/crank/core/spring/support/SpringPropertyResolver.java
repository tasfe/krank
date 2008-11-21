package org.crank.core.spring.support;

import org.springframework.context.ApplicationContext;

import javax.faces.el.PropertyResolver;


/**
 *
 * @author $author$
 * @version $Revision$
  */
@SuppressWarnings("deprecation")
public class SpringPropertyResolver extends PropertyResolver {
    // ------------------------------------------------------ Instance Variables

    /**
     * <p>The original <code>VariableResolver</code> passed to our constructor.</p>
     */
    private PropertyResolver original = null;

    /**
     * <p>Construct a new {@link SpringPropertyResolver} instance.</p>
     *
     *
     * @param aOriginal Original resolver to delegate to.
     */
    public SpringPropertyResolver(final PropertyResolver aOriginal) {
        this.original = aOriginal;
    }

    // ------------------------------------------------ PropertyResolver Methods

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
        if (base instanceof ApplicationContext) {
            ApplicationContext context = (ApplicationContext) base;

            return context.getBean(property.toString());
        } else {
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
        if (base instanceof ApplicationContext) {
            return; //read only for now
        } else {
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
        if (base instanceof ApplicationContext) {
            return true;
        } else {
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
    @SuppressWarnings("unchecked")
	public Class getType(final Object base, final Object property) {
        if (base instanceof ApplicationContext) {
            Object value;
            ApplicationContext context = (ApplicationContext) base;
            value = context.getBean(property.toString());

            return (value == null) ? null : value.getClass();
        } else {
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
        if (base instanceof ApplicationContext) {
            return getValue(base, "" + index);
        } else {
            return original.getValue(base, index);
        }
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
        if (base instanceof ApplicationContext) {
            //AppContext read only
            return;
        } else {
            original.setValue(base, index, value);
        }
    }

    /**
     * <p>Convert an index into a corresponding string, and delegate.</p>
     *
     * @param base Base object from which to check a property
     * @param index Index to be checked
     * @return true always
     */
    public boolean isReadOnly(final Object base, final int index) {
        if (base instanceof ApplicationContext) {
            return true;
        } else {
            return original.isReadOnly(base, index);
        }
    }

    /**
     * <p>Convert an index into a corresponding string, and delegate.</p>
     *
     * @param base Base object from which to return a property type
     * @param index Index whose type is to be returned
     * @return type
     */
    @SuppressWarnings("unchecked")
	public Class getType(final Object base, final int index) {
        if (base instanceof ApplicationContext) {
            return getType(base, "" + index);
        } else {
            return original.getType(base, index);
        }
    }
}