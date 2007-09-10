package com.arcmind.jpa.course.model;



import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.OptimisticLockException;


import com.arcmind.jpa.utils.JpaTemplateWithReturn;
import com.arcmind.jpa.utils.JpaUtils;

import junit.framework.TestCase;

public class VersionAndLocksTest extends TestCase {


	public void testVersioning() throws Exception {
		Long idOfCreatedTask = null;
		Task createdTask = null;
		Task lookedUpTask = null;
		EntityManager entityManager = null;
		
		/* In first user session, create new task. */
		JpaUtils.createEntityManagerAndStartTransaction();
		entityManager = JpaUtils.getCurrentEntityManager();
		createdTask = new Task("Walk dog", 2, 1);
		entityManager.persist(createdTask);
		idOfCreatedTask = createdTask.getId();
		JpaUtils.commitTransactionAndCloseEntityManager();
		/* In second user session, updates the same task. */
		JpaUtils.createEntityManagerAndStartTransaction();
		entityManager = JpaUtils.getCurrentEntityManager(); 
		lookedUpTask = entityManager.find(Task.class, idOfCreatedTask);
		lookedUpTask.setName("Walk dog and cat");
		JpaUtils.commitTransactionAndCloseEntityManager();
		/* Change task from first user session. */
		createdTask.setName("Walk dog and pet hamster");
		/* In third user session, updates the original task */
		try {
			JpaUtils.createEntityManagerAndStartTransaction();
			entityManager = JpaUtils.getCurrentEntityManager();
			entityManager.merge(createdTask);
			JpaUtils.commitTransactionAndCloseEntityManager();
			fail();
		} catch (OptimisticLockException ole) {
			
		}
		
	}

	public Task createTask (final String name, final int estimate, final int actual) throws Exception {
		return (Task) JpaUtils.execute(new JpaTemplateWithReturn() {
			@Override
			public Object execute() throws Exception {
				
				/* In first user session, create new task. */
				EntityManager entityManager = JpaUtils.getCurrentEntityManager();
				Task createdTask = new Task(name, estimate, actual);
				entityManager.persist(createdTask);
				return createdTask;
			}
		});
	}

	public void testRecoverFromOptimisticLockException() throws Exception {
		Long idOfCreatedTask = null;
		
		Task lookedUpTask = null;
		EntityManager entityManager = null;
		
		/* In first user session, create new task. */		
		Task createdTask = createTask("Walk dog", 2, 1);
		idOfCreatedTask = createdTask.getId();
		JpaUtils.cleanup();

		/* In second user session, updates the same task. */
		JpaUtils.createEntityManagerAndStartTransaction();
		entityManager = JpaUtils.getCurrentEntityManager(); 
		lookedUpTask = entityManager.find(Task.class, idOfCreatedTask);
		lookedUpTask.setName("Walk dog and cat");
		JpaUtils.commitTransactionAndCloseEntityManager();

		/* Change task from first user session. */
		createdTask.setName("Walk dog and pet hamster");

		JpaUtils.createEntityManagerAndStartTransaction();
		/* In third user session, updates the original task */
		try {
			entityManager = JpaUtils.getCurrentEntityManager();
			entityManager.merge(createdTask);
			fail();//this example running inside of JUnit
		} catch (OptimisticLockException ole) {
			/* Catch this exception and look up version of object in database */
			EntityManager newEntityManager = 
			JpaUtils.getEntityManagerFactory().createEntityManager();
			EntityTransaction transaciton = newEntityManager.getTransaction();
			transaciton.begin();
			Task task = newEntityManager.find(Task.class, createdTask.getId());
			/* Prompt the user to manually merge or override */
			/* User picks manually override. */
			createdTask.setVersion(task.getVersion());
			newEntityManager.merge(createdTask);
			transaciton.commit();
			JpaUtils.cleanup();
		}
		JpaUtils.commitTransactionAndCloseEntityManager();
		
	}
	
	public void testReadLockMode() throws Exception {
		Long idOfCreatedTask = null;
		Task createdTask = null;
		Task lookedUpTask = null;
		EntityManager entityManager = null;
		
		/* Setup some data. */
		JpaUtils.createEntityManagerAndStartTransaction();
		entityManager = JpaUtils.getCurrentEntityManager();
		createdTask = new Task("Walk dog", 2, 1);
		entityManager.persist(createdTask);
		idOfCreatedTask = createdTask.getId();
		JpaUtils.commitTransactionAndCloseEntityManager();

		/* In second user session, updates the same task. */
		JpaUtils.createEntityManagerAndStartTransaction();
		entityManager = JpaUtils.getCurrentEntityManager(); 
		lookedUpTask = entityManager.find(Task.class, idOfCreatedTask);
		
		entityManager.lock(lookedUpTask, LockModeType.READ);
		
		lookedUpTask.setName("Walk dog and cat");
		JpaUtils.commitTransactionAndCloseEntityManager();
		
		
	}
	
	public void testWriteLockMode() throws Exception {
		Long idOfCreatedTask = null;
		Task createdTask = null;
		Task lookedUpTask = null;
		EntityManager entityManager = null;
		
		/* Setup some data. */
		JpaUtils.createEntityManagerAndStartTransaction();
		entityManager = JpaUtils.getCurrentEntityManager();
		createdTask = new Task("Walk dog", 2, 1);
		entityManager.persist(createdTask);
		idOfCreatedTask = createdTask.getId();
		JpaUtils.commitTransactionAndCloseEntityManager();

		/* In second user session, updates the same task. */
		JpaUtils.createEntityManagerAndStartTransaction();
		entityManager = JpaUtils.getCurrentEntityManager(); 
		lookedUpTask = entityManager.find(Task.class, idOfCreatedTask);
		
		entityManager.lock(lookedUpTask, LockModeType.WRITE);
		
		lookedUpTask.setName("Walk dog and cat");
		JpaUtils.commitTransactionAndCloseEntityManager();
		
		
	}

	public void testFlushModeAuto() throws Exception {
		Long idOfCreatedTask = null;
		Task createdTask = null;
		Task lookedUpTask = null;
		EntityManager entityManager = null;
		
		/* Setup some data. */
		JpaUtils.createEntityManagerAndStartTransaction();
		entityManager = JpaUtils.getCurrentEntityManager();
		createdTask = new Task("Walk dog", 2, 1);
		entityManager.persist(createdTask);
		idOfCreatedTask = createdTask.getId();
		JpaUtils.commitTransactionAndCloseEntityManager();

		/* In second user session, updates the same task. */
		JpaUtils.createEntityManagerAndStartTransaction();
		entityManager = JpaUtils.getCurrentEntityManager(); 
		entityManager.setFlushMode(FlushModeType.AUTO); //default

		lookedUpTask = entityManager.find(Task.class, idOfCreatedTask);
		lookedUpTask.setName("Walk dog and cat");
		
		//writes lookedUpTask here to avoid dirty reads
		entityManager.createQuery("select task from Task task").getResultList();
		
		
		//if we did not have that query, it would write lookedUpTask here
		JpaUtils.commitTransactionAndCloseEntityManager();//commit here
		
		
	}
	
	public void testFlushModeCommit() throws Exception {
		Long idOfCreatedTask = null;
		Task createdTask = null;
		Task lookedUpTask = null;
		EntityManager entityManager = null;
		
		/* Setup some data. */
		JpaUtils.createEntityManagerAndStartTransaction();
		entityManager = JpaUtils.getCurrentEntityManager();
		createdTask = new Task("Walk dog", 2, 1);
		entityManager.persist(createdTask);
		idOfCreatedTask = createdTask.getId();
		JpaUtils.commitTransactionAndCloseEntityManager();

		/* In second user session, updates the same task. */
		JpaUtils.createEntityManagerAndStartTransaction();
		entityManager = JpaUtils.getCurrentEntityManager(); 
		entityManager.setFlushMode(FlushModeType.COMMIT); //changed

		lookedUpTask = entityManager.find(Task.class, idOfCreatedTask);
		lookedUpTask.setName("Walk dog and cat");//update object here
		
		//No writes here
		entityManager.createQuery("select task from Task task").getResultList();
		
		
		//Writes lookedUpTask here
		JpaUtils.commitTransactionAndCloseEntityManager();//commit here
		
		
	}

	public void testFlushModeAutoForQuery() throws Exception {
		Long idOfCreatedTask = null;
		Task createdTask = null;
		Task lookedUpTask = null;
		EntityManager entityManager = null;
		
		/* Setup some data. */
		JpaUtils.createEntityManagerAndStartTransaction();
		entityManager = JpaUtils.getCurrentEntityManager();
		createdTask = new Task("Walk dog", 2, 1);
		entityManager.persist(createdTask);
		idOfCreatedTask = createdTask.getId();
		JpaUtils.commitTransactionAndCloseEntityManager();

		/* In second user session, updates the same task. */
		JpaUtils.createEntityManagerAndStartTransaction();
		entityManager = JpaUtils.getCurrentEntityManager(); 
		entityManager.setFlushMode(FlushModeType.COMMIT); //changed

		lookedUpTask = entityManager.find(Task.class, idOfCreatedTask);
		lookedUpTask.setName("Walk dog and cat");
		
		//writes lookedUpTask here to avoid dirty reads
		entityManager.createQuery("select task from Task task")
			.setFlushMode(FlushModeType.AUTO).getResultList();
		
		
		//if we did not have that query, it would write lookedUpTask here
		JpaUtils.commitTransactionAndCloseEntityManager();//commit here
		
		
		
		
	}
	
	protected void setUp() throws Exception {

	}


	protected void tearDown() throws Exception {

		JpaUtils.cleanup();


	}


}
