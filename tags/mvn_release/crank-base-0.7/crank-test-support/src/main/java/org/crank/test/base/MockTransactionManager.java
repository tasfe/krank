package org.crank.test.base;

import javax.transaction.*;

class MockTransactionManager implements TransactionManager {

    public void begin() throws NotSupportedException, SystemException {
        // TODO Auto-generated method stub

    }

    public void commit() throws RollbackException, HeuristicMixedException, HeuristicRollbackException, SecurityException, IllegalStateException, SystemException {
        // TODO Auto-generated method stub

    }

    public int getStatus() throws SystemException {
        // TODO Auto-generated method stub
        return 0;
    }

    public Transaction getTransaction() throws SystemException {
        // TODO Auto-generated method stub
        return null;
    }

    public void resume( Transaction arg0 ) throws InvalidTransactionException, IllegalStateException, SystemException {
        // TODO Auto-generated method stub

    }

    public void rollback() throws IllegalStateException, SecurityException, SystemException {
        // TODO Auto-generated method stub

    }

    public void setRollbackOnly() throws IllegalStateException, SystemException {
        // TODO Auto-generated method stub

    }

    public void setTransactionTimeout( int arg0 ) throws SystemException {
        // TODO Auto-generated method stub

    }

    public Transaction suspend() throws SystemException {
        // TODO Auto-generated method stub
        return null;
    }

}
