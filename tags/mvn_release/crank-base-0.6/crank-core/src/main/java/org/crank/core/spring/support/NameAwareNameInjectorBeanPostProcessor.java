package org.crank.core.spring.support;


import org.crank.annotations.design.DependsOnSpring;
import org.crank.core.NameAware;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 *
 * <p>
 * <small>
 * Injects Spring names as names.
 * </small>
 * </p>
 * @author Rick Hightower
 */
@DependsOnSpring
public class NameAwareNameInjectorBeanPostProcessor implements BeanPostProcessor {

    public Object postProcessBeforeInitialization(Object object, String name) throws BeansException {
        // no op
        return object;
    }

    public Object postProcessAfterInitialization(Object object, String name) throws BeansException {
        // no op
        if (object instanceof NameAware) {
            NameAware nameAware = (NameAware) object;
            nameAware.setName(name);
            nameAware.init();
        }
        return object;
    }

}
