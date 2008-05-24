package org.crank.crud;

import org.springframework.aop.support.DefaultIntroductionAdvisor;

/**
 *  Introduction Advisor for finder method mixins.
 *  @version $Revision:$
 *  @author Rick Hightower
 */
public class DaoMethodsIntroductionAdvisor extends DefaultIntroductionAdvisor {

    public DaoMethodsIntroductionAdvisor() {
        super(new DaoMethodsIntroductionInterceptor());
    }
}

