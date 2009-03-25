package org.crank.tags;

import java.util.*;                            
/**
 * Created by IntelliJ IDEA.
 * User: reggiedigital
 * Date: Nov 9, 2008
 * Time: 7:40:22 PM
 */
public final class SpringSecurityTagUtils {

   	/**
	 * Stops creation of a new SpringSecurityTagUtils objects.
	 */
    private SpringSecurityTagUtils() { }

    public static boolean ifAnyGranted(final String grantedRoles) {
        final Collection granted = SpringSecurityUtils.getPrincipalAuthorities();

        boolean isGranted = false;

        //test if user has any of the roles assigned
        if ((null != grantedRoles) && !"".equals(grantedRoles)) {
            Set parsedAuthoritiesString = SpringSecurityUtils.parseAuthoritiesString(grantedRoles);
            Set grantedCopy = SpringSecurityUtils.retainAll(granted, parsedAuthoritiesString);
            if (!grantedCopy.isEmpty()) {
                isGranted = true;
            }
        }

        return isGranted;
    }

    private static ClassLoader getClassLoader() {
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		if (classLoader == null) {
			return SpringSecurityTagUtils.class.getClassLoader();
		}
		return classLoader;
	}

        
}
