package org.crank.core.spring.support;


import java.math.BigDecimal;
import java.math.BigInteger;

import org.crank.core.ObjectNotFound;
import org.crank.core.ObjectRegistry;
import org.crank.web.ServletContextUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class SpringApplicationContextObjectRegistry implements ObjectRegistry {
	private ApplicationContext applicationContext;
	
	public void setApplicationContext(ApplicationContext applcationContext) {
		this.applicationContext = applcationContext;
	}
	
	public Object getObject(String name) {
		initIfNeeded();
		try {
			return applicationContext.getBean(name);
		} catch (BeansException be) {
			throw new ObjectNotFound(name, be);
		}
	}

	private void initIfNeeded() {
		if (applicationContext==null) {
			applicationContext = WebApplicationContextUtils.getWebApplicationContext(ServletContextUtils.context());
		}
	}

	public Object getObject(String name, Class<?> clazz) {
		initIfNeeded();		
		return applicationContext.getBean(name, clazz);
	}

	public void resolveCollaborators(Object object) {
		initIfNeeded();
		ConfigurableApplicationContext configApplicationContext = (ConfigurableApplicationContext) applicationContext;
		configApplicationContext.getBeanFactory().autowireBeanProperties(object, 
				AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false);
	}

	public Object[] getObjectsByType(Class<?> clazz) {
		initIfNeeded();
		return applicationContext.getBeansOfType(clazz).values().toArray();
	}

    public Object getObjectReturnNullIfMissing(String name) {
        initIfNeeded();
        try {
            return applicationContext.getBean(name);
        } catch (BeansException be) {
            return null;
        }
    }

    public Object convertObject(Object value, Class<?> clazz) {
        
        if (clazz==String.class && value.getClass() == String.class){
            return value;
        } else if (clazz==String.class && value.getClass() == Integer.class) {
            return Integer.valueOf((String)value );
        } else if (clazz==String.class && value.getClass() == Long.class) {
            return Long.valueOf((String)value );
        } else if (clazz==String.class && value.getClass() == Short.class) {
            return Short.valueOf((String)value );
        } else if (clazz==String.class && value.getClass() == Byte.class) {
            return Byte.valueOf((String)value );
        } else if (clazz==String.class && value.getClass() == Double.class) {
            return Double.valueOf((String)value );
        } else if (clazz==String.class && value.getClass() == Float.class) {
            return Float.valueOf((String)value );
        } else if (clazz==String.class && value.getClass() == BigDecimal.class) {
            return new BigDecimal((String)value);
        } else if (clazz==String.class && value.getClass() == BigInteger.class) {
            return new BigInteger((String)value);
        } else {
            //TODO look up converter in Spring context. Spring uses property editors
            return null;
        }
        
        
    }

}
