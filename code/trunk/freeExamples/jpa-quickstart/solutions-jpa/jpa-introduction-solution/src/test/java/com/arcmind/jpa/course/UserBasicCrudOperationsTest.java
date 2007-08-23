package com.arcmind.jpa.course;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import junit.framework.TestCase;

public class UserBasicCrudOperationsTest extends TestCase {

	private EntityManager entityManager;
	private EntityManagerFactory entityManagerFactory;

	protected void setUp() throws Exception {
		/* Use Persistence.createEntityManagerFactory to create 
		 * "security-domain" persistence unit. */ 
		
		entityManagerFactory = Persistence.createEntityManagerFactory("security-domain");

	}
	
	protected void tearDown() throws Exception {
		if(entityManager.isOpen()) {
			entityManager.close();
		}
	}
	
	public void testCrudOperations() throws Exception{
		
        User user = new User();
        Long id = null;
        user.setName("RickHigh");        
		
		/* Overall Objective: add a new User to the database using JPA. */
		/* Create the entityManager. */
		entityManager = entityManagerFactory.createEntityManager();
		/* Start a transaction. */
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        /* Persist the user using the entityManager. */
        try {
	        entityManager.persist(user);
        	//user = entityManager.merge(user);
        	transaction.commit();

        } catch (Exception ex) {
        	try {
        		transaction.rollback();
        	} catch (Exception ex2) {
        		
        	}
        	throw ex;
        }
        id = user.getId();
        user = null;
        
        /* Close the entityManager. */
        entityManager.close();
        
        /* Overall Objective: Read object using new entityManager. */
        entityManager = entityManagerFactory.createEntityManager();
        /* Start a new transaction. */
        EntityTransaction readTransaction = entityManager.getTransaction();
        readTransaction.begin();
        /* Look up the user in the database. */
        user = entityManager.find(User.class, id);
        user.setName("RicardoTorreAlto");
        
        /* Stop the read transaction. */
        readTransaction.commit();
        
        /* Test that the object was read from the database. */
        assertNotNull(user);
        assertEquals("RicardoTorreAlto", user.getName());
        
        
        /* Overall Objective: Delete the user. */
        /* Start the delete transaction. */
        EntityTransaction deleteTransaction = entityManager.getTransaction();
        deleteTransaction.begin();
        /* Try to delete the user. */
        try {
        	entityManager.remove(user);
        	deleteTransaction.commit();        	
        } catch (Exception ex) {
        	try {
        		transaction.rollback();
        	} catch (Exception ex2) {
        		
        	}
        	throw ex;
        }
        /* Overall Objective: Test that the user was deleted. */
        /* Start the transaction. */
        readTransaction = entityManager.getTransaction();
        /* Read the user from the database. */
        readTransaction.begin();
        user = entityManager.find(User.class, id);
        readTransaction.commit();
        /* Test that the user was not found in the database. */
        assertNull(user);
        
	}

	public void testJustForLessonSlides() throws Exception{

		/* FIRST REQUEST. */
		/* Simulation User fills out web application, 
		 * action handler copies request parameters 
		 * to new User object */
        User user = new User();
        Long id = null;
        user.setName("RickHigh2");        
		
        /* New EntityManager (in DAO layer) */
		entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        
        /* Call merge or persist. Note merge works with new objects. */
        user = entityManager.merge(user);
        
        transaction.commit();//Commit 
        //Store user in HttpSession
        /* Close the entityManager. End web request.*/
        entityManager.close();

        /* SECOND REQUEST. */
        /* Simulation user fills out 
         * user name in web form. Action handler
         * copies information into user object. */
        user.setName("PERSIST");

        /* In Dao Layer. Open entityManger again. */
		entityManager = entityManagerFactory.createEntityManager();
        transaction = entityManager.getTransaction();
        transaction.begin();

        /* Call merge, persist method will not work. */
        user = entityManager.merge(user);
        
        transaction.commit();
        /* Close the entityManager. */
        entityManager.close();

        /* Overall Objective: Read object using new entityManager. */
        entityManager = entityManagerFactory.createEntityManager();
        /* Start a new transaction. */
        EntityTransaction readTransaction = entityManager.getTransaction();
        readTransaction.begin();
        /* Look up the user in the database. */
        user = entityManager.find(User.class, id);
        
        /* Stop the read transaction. */
        readTransaction.commit();
        
        /* Test that the object was read from the database. */
        assertNotNull(user);
        assertEquals("PERSIST", user.getName());
        
        entityManager.close();
        
        
	}

}
