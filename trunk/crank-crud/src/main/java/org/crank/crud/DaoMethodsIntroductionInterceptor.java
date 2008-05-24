package org.crank.crud;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.IntroductionInterceptor;

import java.util.Set;
import java.util.HashSet;

/**
*  DAO method introduction interceptor is used to mixin dao methods to our DAO objects.
*  @version $Revision:$
*  @author Rick Hightower
*/
public class DaoMethodsIntroductionInterceptor implements IntroductionInterceptor {

    private static final String FINDER_METHOD_PREFIX = "find";
    private static final  String DELETE_METHOD_PREFIX = "delete";
    private static final  String UPDATE_METHOD_PREFIX = "update";

    private final Set<String> methodPassThroughs = new HashSet<String>();

    {
        methodPassThroughs.add("find");
        methodPassThroughs.add("delete");
        methodPassThroughs.add("update");
    }


    public Object invoke( MethodInvocation methodInvocation ) throws Throwable {
        DaoMethods genericDao = (DaoMethods) methodInvocation.getThis();


        String methodName = methodInvocation.getMethod().getName();

        /*
         * The method name must start with find but not be exactly equal to
         * find, delete or update.
         */
        if (methodPassThroughs.contains(methodName)) {
            /* Exact match so proceed. */
            return methodInvocation.proceed();
        } else if (methodName.startsWith(FINDER_METHOD_PREFIX)) {
            /* Starts with find so execute finder. */
            Object[] arguments = methodInvocation.getArguments();
            return genericDao.executeFinder( methodInvocation.getMethod(), arguments );
        } else if (methodName.startsWith(DELETE_METHOD_PREFIX)) {
            /* Starts with delete so execute delete. */
            Object[] arguments = methodInvocation.getArguments();
            return genericDao.executeDelete( methodInvocation.getMethod(), arguments );
        } else if (methodName.startsWith(UPDATE_METHOD_PREFIX)) {
            /* Starts with update so execute delete. */
            Object[] arguments = methodInvocation.getArguments();
            return genericDao.executeUpdate( methodInvocation.getMethod(), arguments );
        }
        else {
            return methodInvocation.proceed();
        }
    }

    @SuppressWarnings("unchecked")
	public boolean implementsInterface( Class intf ) {
        return intf.isInterface() && DaoMethods.class.isAssignableFrom( intf );
    }

}