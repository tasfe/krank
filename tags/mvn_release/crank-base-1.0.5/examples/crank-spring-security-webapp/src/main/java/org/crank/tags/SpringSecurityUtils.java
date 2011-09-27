package org.crank.tags;

import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: reggiedigital
 * Date: Nov 9, 2008
 * Time: 8:04:02 PM
 */
public class SpringSecurityUtils {
    public static Collection getPrincipalAuthorities() {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();

        if (null == currentUser) {
            return Collections.EMPTY_LIST;
        }

        if ((null == currentUser.getAuthorities()) || (currentUser.getAuthorities().length < 1)) {
            return Collections.EMPTY_LIST;
        }

        Collection granted = Arrays.asList(currentUser.getAuthorities());

        return granted;
    }

    public static Set parseAuthoritiesString(String authorizationsString) {
        final Set requiredAuthorities = new HashSet();
        final String[] authorities = StringUtils.commaDelimitedListToStringArray(authorizationsString);

        for (int i = 0; i < authorities.length; i++) {
            String authority = authorities[i];

            // Remove the role's whitespace characters without depending on JDK 1.4+
            // Includes space, tab, new line, carriage return and form feed.
            String role = authority.trim(); // trim, don't use spaces, as per SEC-378
            role = StringUtils.deleteAny(role, "\t\n\r\f");

            requiredAuthorities.add(new GrantedAuthorityImpl(role));
        }

        return requiredAuthorities;
    }

    /**
     * Find the common authorities between the current authentication's {@link org.springframework.security.GrantedAuthority} and the ones
     * that have been specified in the tag's ifAny, ifNot or ifAllGranted attributes.<p>We need to manually
     * iterate over both collections, because the granted authorities might not implement {@link
     * Object#equals(Object)} and {@link Object#hashCode()} in the same way as {@link GrantedAuthorityImpl}, thereby
     * invalidating {@link Collection#retainAll(java.util.Collection)} results.</p>
     * <p>
     * <strong>CAVEAT</strong>:  This method <strong>will not</strong> work if the granted authorities
     * returns a <code>null</code> string as the return value of {@link
     * org.springframework.security.GrantedAuthority#getAuthority()}.
     * </p>
     * <p>Reported by rawdave, on Fri Feb 04, 2005 2:11 pm in the Spring Security forum.</p>
     *
     * @param granted  The authorities granted by the authentication. May be any implementation of {@link
     *                 org.springframework.security.GrantedAuthority} that does <strong>not</strong> return <code>null</code> from {@link
     *                 org.springframework.security.GrantedAuthority#getAuthority()}.
     * @param required A {@link Set} of {@link GrantedAuthorityImpl}s that have been built using ifAny, ifAll or
     *                 ifNotGranted.
     * @return A set containing only the common authorities between <var>granted</var> and <var>required</var>.
     */
    public static Set retainAll(final Collection granted, final Set required) {
        Set grantedRoles = authoritiesToRoles(granted);
        Set requiredRoles = authoritiesToRoles(required);
        grantedRoles.retainAll(requiredRoles);

        return rolesToAuthorities(grantedRoles, granted);
    }

    public static Set removeAll(final Collection granted, final Set required) {
        Set grantedRoles = authoritiesToRoles(granted);
        Set requiredRoles = authoritiesToRoles(required);
        grantedRoles.removeAll(requiredRoles);

        return rolesToAuthorities(grantedRoles, granted);
    }

    public static Set authoritiesToRoles(Collection c) {
        Set target = new HashSet();

        for (Iterator iterator = c.iterator(); iterator.hasNext();) {
            GrantedAuthority authority = (GrantedAuthority) iterator.next();

            if (null == authority.getAuthority()) {
                throw new IllegalArgumentException(
                        "Cannot process GrantedAuthority objects which return null from getAuthority() - attempting to process "
                                + authority.toString());
            }

            target.add(authority.getAuthority());
        }

        return target;
    }

    public static Set rolesToAuthorities(Set grantedRoles, Collection granted) {
        Set target = new HashSet();

        for (Iterator iterator = grantedRoles.iterator(); iterator.hasNext();) {
            String role = (String) iterator.next();

            for (Iterator grantedIterator = granted.iterator(); grantedIterator.hasNext();) {
                GrantedAuthority authority = (GrantedAuthority) grantedIterator.next();

                if (authority.getAuthority().equals(role)) {
                    target.add(authority);

                    break;
                }
            }
        }

        return target;
    }
}
