package org.crank.crud;

import org.springframework.aop.support.DefaultIntroductionAdvisor;

/**
 *
 *  @version $Revision:$
 *  @author Rick Hightower
 */
public class FinderIntroductionAdvisor extends DefaultIntroductionAdvisor {

    public FinderIntroductionAdvisor() {
        super(new FinderIntroductionInterceptor());
    }


}
