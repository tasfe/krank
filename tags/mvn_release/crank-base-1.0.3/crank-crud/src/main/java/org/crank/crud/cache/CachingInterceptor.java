package org.crank.crud.cache;

import org.springframework.aop.IntroductionInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * Interceptor for cache awareness.
 *
 * @author Chris Mathias
 * @version $Revision$
 */
public class CachingInterceptor implements IntroductionInterceptor {

    public Object invoke( MethodInvocation methodInvocation ) throws Throwable {

        PreloadableGenericDao genericDao = (PreloadableGenericDao) methodInvocation.getThis();


        //TODO: Wire with caching service

        //check for existing cache.

        //update properties

        //create cache if necessary

        if (genericDao==null) {
        	
        }

        return methodInvocation.proceed();
    }

    @SuppressWarnings("unchecked")
	public boolean implementsInterface( Class intf ) {
        return intf.isInterface() && PreloadableGenericDao.class.isAssignableFrom( intf );
    }
    
}
