package com.arcmind.jpa.utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;



public class JpaUtils {
	
	private static ThreadLocal<EntityManager> entityManagerHolder = new ThreadLocal<EntityManager>();
	private static ThreadLocal<EntityTransaction> transactionHolder = new ThreadLocal<EntityTransaction>();
	private static ThreadLocal<Boolean> joinedTransactionInProgress = new ThreadLocal<Boolean>();

	private static EntityManagerFactory entityManagerFactory;
	
	
	private static EntityManagerFactory getEntityManagerFactory() {
		if (entityManagerFactory==null) {
			entityManagerFactory = Persistence
			.createEntityManagerFactory("security-domain");
		}
		return entityManagerFactory;
	}
	
	public static EntityManager getCurrentEntityManager() {
		return entityManagerHolder.get();
	}
	
	public static EntityManager getEntityManager() {
		if (entityManagerHolder.get()==null) {
			entityManagerHolder.set(getEntityManagerFactory().createEntityManager());
		}
		return entityManagerHolder.get();
	}

	public static void cleanup () {
		joinedTransactionInProgress.set(false);
		EntityManager entityManager = entityManagerHolder.get();
		EntityTransaction transaction = transactionHolder.get();

		if (entityManager != null && entityManager.isOpen()) {
			entityManager.close();
		}
		if (transaction != null && transaction.isActive()) {
			if (transaction.getRollbackOnly()) {
				transaction.rollback();
			}
		}
		entityManagerHolder.set(null);
		transactionHolder.set(null);
	}
	
	public static void execute(final JpaTemplate tt) throws Exception {
		execute( new JpaTemplateWithReturn() {
			@Override
			public Object execute() throws Exception{
				tt.execute();
				return null;
			}});
	}
	
	public static Object execute(JpaTemplateWithReturn tt) throws Exception {
		Object result = null;
		EntityManager entityManager = getOrCreateEntityManager();
		joinedTransactionInProgress.set(false);
		EntityTransaction transaction = getOrCreateTransaction(entityManager);
		
		try {
			result = tt.execute();
			/* If you did not join another transaction, try 
			 * to commit the transaction that was started. */
			if (!joinedTransactionInProgress.get()) { 
					transaction.commit();
			}
		} catch (Exception ex) {
			/* If you did not join another transaction try 
			 * to roll back the transaction that was started. */			
			if (!joinedTransactionInProgress.get()) {
				try {
					transaction.rollback();
				} catch (Exception ise) {
					ise.printStackTrace(); //log that we could not roll back.
					throw ex; //throw the original exception
				}
			}
			/* If you did join another transaction just mark 
			 * the transaction to be rolled back. */						
			if (joinedTransactionInProgress.get()) { 
				transaction.setRollbackOnly();
			}
			throw ex;
		}
		return result;
	}

	private static EntityTransaction getOrCreateTransaction(EntityManager entityManager) {
		/* See if transaction is in transaction holder. */
		EntityTransaction transaction = transactionHolder.get();
		
		/* If the transaction is *not* in the holder create and start it. */		
		if (transaction == null || !transaction.isActive()) {
			transaction = entityManager.getTransaction();
			transaction.begin();
		} else { /* If the transaction is in the holder mark as joined. */
			joinedTransactionInProgress.set(true);
		}
		return transaction;
	}

	private static EntityManager getOrCreateEntityManager() {
		EntityManager entityManager = entityManagerHolder.get();
		if (entityManager == null || !entityManager.isOpen()) {
			entityManager = getEntityManagerFactory().createEntityManager();
			entityManagerHolder.set(entityManager);
		}
		return entityManager;
	}
	
}
