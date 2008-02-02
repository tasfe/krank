package org.crank.test.base;

import org.springframework.orm.jpa.EntityManagerFactoryAccessor;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.dao.DataAccessResourceFailureException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

public class OpenEntityManagerInTest extends EntityManagerFactoryAccessor {

    public void loadEntityManager() {
        if (!TransactionSynchronizationManager.hasResource(getEntityManagerFactory())) {
            logger.debug("Opening JPA EntityManager in OpenEntityManagerInTest");
            try {
                EntityManager em = createEntityManager();
                TransactionSynchronizationManager.bindResource(
                        getEntityManagerFactory(), new EntityManagerHolder(em));
            }
            catch (PersistenceException ex) {
                throw new DataAccessResourceFailureException("Could not create JPA EntityManager", ex);
            }
        }
    }

    public void unloadEntityManager() {
        EntityManagerHolder emHolder = (EntityManagerHolder)
        TransactionSynchronizationManager.unbindResource(getEntityManagerFactory());
        logger.debug("Closing JPA EntityManager in OpenEntityManagerInTest");
        emHolder.getEntityManager().close();
    }

}
