package org.crank.crud.cache;

import org.springframework.aop.support.DefaultIntroductionAdvisor;

/**
 * Sets advising interceptor for cache awareness.
 *
 * @author Chris Mathias
 * @version $Revision$
 */
public class CachingAdvisor extends DefaultIntroductionAdvisor {
    public CachingAdvisor() {
        super(new CachingInterceptor());
    }
}
