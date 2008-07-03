package org.crank.crud;

import org.springframework.aop.support.DefaultIntroductionAdvisor;

/**
 *  Introduction Advisor for finder method mixins.
 *  @version $Revision:$
 *  @author Rick Hightower
 */
@Deprecated
public class FinderIntroductionAdvisor extends DefaultIntroductionAdvisor {

    public FinderIntroductionAdvisor() {
        super(new FinderIntroductionInterceptor());
    }


}
