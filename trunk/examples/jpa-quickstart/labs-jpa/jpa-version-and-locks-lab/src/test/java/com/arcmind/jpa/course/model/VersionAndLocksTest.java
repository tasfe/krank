package com.arcmind.jpa.course.model;



import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
//import javax.persistence.FlushModeType;
//import javax.persistence.LockModeType;
import javax.persistence.OptimisticLockException;


import com.arcmind.jpa.utils.JpaTemplateWithReturn;
import com.arcmind.jpa.utils.JpaUtils;

import junit.framework.TestCase;

public class VersionAndLocksTest extends TestCase {
	
	/* Make sure after each method is complete that you run the method 
	 * in debug mode (single step) and you watch the log. */

	/* TODO Add a version field to Task class (com.arcmind.jpa.course.model.Task). */
	/* TODO Finish this method using the lesson slides as a guide. */
	public void testVersioning() throws Exception {
		Long idOfCreatedTask = null;
		Task createdTask = null;
		Task lookedUpTask = null;
		EntityManager entityManager = null;
		
		/* In first user session, create new task. */
		JpaUtils.createEntityManagerAndStartTransaction();
		entityManager = JpaUtils.getCurrentEntityManager();
		/* TODO Create a new Task and presist it. */
		JpaUtils.commitTransactionAndCloseEntityManager();
		/* -------------------------------------------- */
		
		/* In second user session, updates the same task. */
		JpaUtils.createEntityManagerAndStartTransaction();
		entityManager = JpaUtils.getCurrentEntityManager();
		//TODO simulate second user session.
		//TODO use idOfCreatedTask to load Task
		//TODO modify loaded Task
		JpaUtils.commitTransactionAndCloseEntityManager();
		/* -------------------------------------------- */
		
		
		/*TODO  Change task from first user session. */
		//HINT: createdTask.setName("Walk dog and pet hamster");
		
		/* In third user session, updates the original task */
		try {
			/** HINT HINT HINT */
			/* 
			JpaUtils.createEntityManagerAndStartTransaction();
			entityManager = JpaUtils.getCurrentEntityManager();
			entityManager.merge(createdTask);
			JpaUtils.commitTransactionAndCloseEntityManager();
			fail(); */
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

	/* TODO write recovery code */
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
			//TODO uncomment this line fail();
			//HINT fail();
		} catch (OptimisticLockException ole) {
			/* Catch this exception and look up version of object in database */
			EntityManager newEntityManager = 
			JpaUtils.getEntityManagerFactory().createEntityManager();
			EntityTransaction transaciton = newEntityManager.getTransaction();
			transaciton.begin();
			
			//HINT Task task = newEntityManager.find(Task.class, createdTask.getId());
			/* Prompt the user to manually merge or override */
			/* User picks manually override. */
			//HINT createdTask.setVersion(task.getVersion());
			//TODO merge the original entity back in.
			transaciton.commit();
			JpaUtils.cleanup();
		}
		JpaUtils.commitTransactionAndCloseEntityManager();
		
	}
	
	/* TODO finish this method. */
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
		
		/* TODO observe the log and see what happens when you lock this lookedUpTask object */
		/* TODO Lock lookedUpTask with LockModeType.READ. */
		//
		
		lookedUpTask.setName("Walk dog and cat");
		JpaUtils.commitTransactionAndCloseEntityManager();
		
		
	}
	
	/* TODO finish this method. */
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
		
		/* TODO observe the log and see what happens when you lock this lookedUpTask object */
		/* TODO Lock lookedUpTask with LockModeType.WRITE. */
		//
		
		lookedUpTask.setName("Walk dog and cat");
		JpaUtils.commitTransactionAndCloseEntityManager();
		
		
	}

	/* TODO finish this method. */	
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
		//TODO set the flush mode of the entityManager to auto.

		
		lookedUpTask = entityManager.find(Task.class, idOfCreatedTask);
		lookedUpTask.setName("Walk dog and cat");
		
		//TODO execute a query to select all tasks.
		//TODO Ensure this query causes a flush by putting a break point and watching the log.
		//TODO You are not done until you see the query causing a flush.
		
		
		//if we did not have that query, it would write lookedUpTask here
		JpaUtils.commitTransactionAndCloseEntityManager();//commit here
		
		
	}
	
	/* TODO finish this method. */	
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
		
		//TODO set the entityManager flush mode to commit

		
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
		//TODO Set entityManager to flush type commit

		lookedUpTask = entityManager.find(Task.class, idOfCreatedTask);
		lookedUpTask.setName("Walk dog and cat");
		
		//TODO Set query to flush mode auto
		entityManager.createQuery("select task from Task task")
			.getResultList();
		
		
		//if we did not have that query, it would write lookedUpTask here
		JpaUtils.commitTransactionAndCloseEntityManager();//commit here
		
		
		
		
	}
	
	protected void setUp() throws Exception {

	}


	protected void tearDown() throws Exception {

		JpaUtils.cleanup();


	}


}
