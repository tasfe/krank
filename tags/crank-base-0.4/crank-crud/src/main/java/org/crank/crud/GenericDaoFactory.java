package org.crank.crud;

import javax.persistence.EntityManagerFactory;

import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.interceptor.TransactionInterceptor;

/**
 * This simplifies Spring configuration.
 *
 *  @version $Revision:$
 *  @author Rick Hightower
 */
public class GenericDaoFactory extends ProxyFactoryBean implements InitializingBean {

    private Class intf;

    private Class bo;

    private EntityManagerFactory entityManagerFactory;

    public void setEntityManagerFactory( EntityManagerFactory entityManagerFactory ) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public void setInterface( final Class intf ) {
        this.intf = intf;
    }

    public GenericDaoFactory( TransactionInterceptor transactionInterceptor ) {
        this.addAdvice( transactionInterceptor );
    }

    @SuppressWarnings( "unchecked" )
    public void afterPropertiesSet() throws Exception {
        if (bo == null) {
            throw new RuntimeException( "The business object property must be set." );
        }
        if (intf == null) {
            throw new RuntimeException( "The interface property must be set." );
        }

        this.setInterfaces( new Class[] { intf } );
        GenericDaoJpa dao = new GenericDaoJpa( bo );
        dao.setEntityManagerFactory( entityManagerFactory );
        this.setTarget( dao );
        this.addAdvisor( new FinderIntroductionAdvisor() );

    }

    public void setBo( Class bo ) {
        this.bo = bo;
    }
}
