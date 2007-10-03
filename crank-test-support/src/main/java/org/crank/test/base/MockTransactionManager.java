package org.crank.test.base;

import javax.transaction.*;

class MockTransactionManager implements TransactionManager {

    public void begin() throws NotSupportedException, SystemException {
    }

    public void commit() throws RollbackException, HeuristicMixedException, HeuristicRollbackException, SecurityException, IllegalStateException, SystemException {
    }

    public int getStatus() throws SystemException {
        return 0;
    }

    public Transaction getTransaction() throws SystemException {
        return null;
    }

    public void resume( Transaction arg0 ) throws InvalidTransactionException, IllegalStateException, SystemException {
    }

    public void rollback() throws IllegalStateException, SecurityException, SystemException {
    }

    public void setRollbackOnly() throws IllegalStateException, SystemException {
    }

    public void setTransactionTimeout( int arg0 ) throws SystemException {
    }

    public Transaction suspend() throws SystemException {
        return null;
    }

}
