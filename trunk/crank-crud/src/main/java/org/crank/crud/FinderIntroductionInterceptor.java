package org.crank.crud;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.IntroductionInterceptor;

/**
*  Finder method introduction interceptor is used to mixin finder methods to our DAO objects.
*  @version $Revision:$
*  @author Rick Hightower
*/
public class FinderIntroductionInterceptor implements IntroductionInterceptor {

    public Object invoke( MethodInvocation methodInvocation ) throws Throwable {
        Finder genericDao = (Finder) methodInvocation.getThis();
        final String finderMethodName = "find";

        String methodName = methodInvocation.getMethod().getName();

        /*
         * The method name must start with find but not be exactly equal to
         * find.
         */
        if (methodName.equals( finderMethodName )) {
            return methodInvocation.proceed();
        } else if (methodName.startsWith( finderMethodName )) {
            Object[] arguments = methodInvocation.getArguments();
            return genericDao.executeFinder( methodInvocation.getMethod(), arguments );
        } else {
            return methodInvocation.proceed();
        }
    }

    @SuppressWarnings("unchecked")
	public boolean implementsInterface( Class intf ) {
        return intf.isInterface() && Finder.class.isAssignableFrom( intf );
    }

}
