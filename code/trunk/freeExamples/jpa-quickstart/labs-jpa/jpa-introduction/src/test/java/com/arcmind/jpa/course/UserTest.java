package com.arcmind.jpa.course;

//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import javax.persistence.EntityTransaction;
//import javax.persistence.Persistence;

import junit.framework.TestCase;

public class UserTest extends TestCase {

//	private EntityManager entityManager;
//	private EntityManagerFactory entityManagerFactory;

	protected void setUp() throws Exception {
		/* TODO: Use Persistence.createEntityManagerFactory to create 
		 * "security-domain" persistence unit. */ 
	}
	
	public void testCrudOperations() throws Exception{
		
        User user = new User();
        user.setName("RickHigh");        
        Long id = null;

		
		/* Overall Objective: add a new User to the database using JPA. */
		/* TODO: Create the entityManager. */
		
		/* TODO Start a transaction. */
        //HINT: EntityTransaction transaction = null; //TODO FINISH THIS LINE entityManager.getTransaction();
        //HINT: transaction.begin();

        /* TODO: Persist the user using the entityManager. */
        try {
        	// TODO Persist the user here.
        	// TODO Commit transaction here.
        	
        } catch (Exception ex) {
        	// TODO Rollback transaction here.
        }
        
        id = user.getId();
        System.out.println("ID=" + id);
        user = null;
        
        /* TODO: Close the entityManager. */
        //entityManager.close();
        
        /* Overall Objective: Read object using new entityManager. */
        /* TODO: Create entityManager with entityManagerFactory. */

        /* TODO: Start a new transaction. */

        
        /* TODO Look up the user in the database. HINT: entityManager.find(User.class, id)*/
        
        /* TODO Commit the read transaction. */
        
        /* TODO Test that the object was read from the database. Just uncomment.*/
        //assertNotNull(user);
        //assertEquals("RickHigh", user.getName());
        
        
        /* Overall Objective: Delete the user. */
        /* TODO: Start the delete transaction. */
        //EntityTransaction deleteTransaction = null;
        
        /* TODO Try to delete the user. */
        try {
        	//TODO REMOVE USER
        	//TODO commit.        	
        } catch (Exception ex) {
        	//TODO rollback.
        } finally {
        }
        /* Overall Objective: Test that the user was deleted. */
        /* TODO Start the transaction. */
        // readTransaction = null;
        /* TODO Read the user from the database. */
        // TODO Start transaction
        // user = entityManager.find(User.class, id);
        // TODO Commit transaction
        
        /* TODO Test that the user was *not* found in the database. */
        //assertNull(user);
        
	}

}
