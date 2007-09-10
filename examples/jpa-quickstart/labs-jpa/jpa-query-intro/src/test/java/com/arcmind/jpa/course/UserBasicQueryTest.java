package com.arcmind.jpa.course;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import junit.framework.TestCase;

public class UserBasicQueryTest extends TestCase {

	private EntityManager entityManager;
	private EntityManagerFactory entityManagerFactory;

	protected void setUp() throws Exception {
		/* Use Persistence.createEntityManagerFactory to create 
		 * "security-domain" persistence unit. */ 
		
		entityManagerFactory = Persistence.createEntityManagerFactory("security-domain");
		
		// TODO: Call helper method deleteTestData() here...
		
		// TODO: Call helper method createTestData() here...

	}
	

	protected void tearDown() throws Exception {
		deleteTestData();
		if(entityManager.isOpen()) {
			entityManager.close();
		}
	}
	
	// TODO: Uncomment this method	
	private void deleteTestData() throws Exception {
		
/*		entityManager = entityManagerFactory.createEntityManager();
		// Start a transaction. 
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
        	Query query = entityManager.createQuery("delete User");
        	query.executeUpdate();
        	transaction.commit();
        	
        } catch (Exception ex) {
        	ex.printStackTrace();
        	transaction.rollback();
        	throw ex;
        }
*/		
	}



	// TODO: Uncomment this method	
	private void createTestData() throws Exception {
/*		System.out.println("CREATE CALLED");
		String[] userNames = new String[]{"RickHi","BobSmith","Sergey","PaulHix","Taboraz"}; 
		entityManager = entityManagerFactory.createEntityManager();
		// Start a transaction.
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
        	for (String userName : userNames) {
        		User user = new User();
        		user.setName(userName);
        		entityManager.persist(user);
        	}
        	for (int index = 0; index < 100; index++) {
        		if (index % 10 == 0) {
        			entityManager.flush(); //Flush data to the database
        		}
        		User user = new User();
        		user.setName("user" + index);
        		entityManager.persist(user);
        	}        	
        	transaction.commit();
        	
        } catch (Exception ex) {
        	ex.printStackTrace();
        	transaction.rollback();
        	throw ex;
        }
*/	}

	public void testQueryUsers () throws Exception {
		// TODO: Start a transaction.
		// HINT: See previous lesson / lab

        try {
        	List<User> users = new ArrayList<User>();

        	// TODO: Create the query object to retrieve users from the database, get the unit test to pass
        	// HINT: See lecture slide titled "Simple Query with List"
        	
        	assertNotNull(users);
        	assertTrue(users.size() > 0);

        	// TODO: Make sure to commit the transaction
        	//transaction.commit();        	
        } catch (Exception ex) {
        	ex.printStackTrace();
        	// TODO: Make sure to rollback the transaction in the event of an error
        	//transaction.rollback();
        	throw ex;
        }
	}
	
	public void testQueryUserNames () throws Exception {
		// TODO: Start a transaction.
		// HINT: See previous lesson / lab

        try {

        	// TODO: Using testQueryUsers as a guide, modify the query to only return the userNames instead of the entire user object.
        	// TODO BONUS: Add assertions to validate the results
        	
        	// TODO: Make sure to commit the transaction
        	//transaction.commit();        	
        } catch (Exception ex) {
        	ex.printStackTrace();
        	// TODO: Make sure to rollback the transaction in the event of an error
        	//transaction.rollback();
        	throw ex;
        }
	}
	
	public void testMoreThanOneColumn () throws Exception {
		// TODO: Start a transaction.
		// HINT: See previous lesson / lab

        try {
        	
        	// TODO: Create the query object to retrieve more than one object at a time
        	// HINT: See lecture slide titled "Retrieving more than one object at a time"

        	// TODO BONUS: Add assertions to validate the results

        	// TODO: Make sure to commit the transaction
        	//transaction.commit();        	
        } catch (Exception ex) {
        	ex.printStackTrace();
        	// TODO: Make sure to rollback the transaction in the event of an error
        	//transaction.rollback();
        	throw ex;
        }
	}

	public void testSQLQuery () throws Exception {
		// TODO: Start a transaction.
		// HINT: See previous lesson / lab

        try {
        	
        	// TODO: Create the query object to test native SQL queries
        	// HINT: See lecture slide titled "Working with SQL queries"

        	// TODO: Make sure to commit the transaction
        	//transaction.commit();        	
        } catch (Exception ex) {
        	ex.printStackTrace();
        	// TODO: Make sure to rollback the transaction in the event of an error
        	//transaction.rollback();
        	throw ex;
        }
	}
		
	public void testPositionalParameters () throws Exception {
		// TODO: Start a transaction.
		// HINT: See previous lesson / lab

        try {
        	
        	// TODO: Query the users by user.name using positional parameter
        	// HINT: See slide titled "Simple Query with Positional Parameter"

        	// TODO: Make sure to commit the transaction
        	//transaction.commit();        	
        } catch (Exception ex) {
        	ex.printStackTrace();
        	// TODO: Make sure to rollback the transaction in the event of an error
        	//transaction.rollback();
        	throw ex;
        }
	}

	public void testNamedParameters () throws Exception {
		// TODO: Start a transaction.
		// HINT: See previous lesson / lab

        try {
        	
        	// TODO: Query the users by user.name using named parameter
        	// HINT: See slide titled "Simple Query with Named Parameter"

        	// TODO: Make sure to commit the transaction
        	//transaction.commit();        	
        } catch (Exception ex) {
        	ex.printStackTrace();
        	// TODO: Make sure to rollback the transaction in the event of an error
        	//transaction.rollback();
        	throw ex;
        }
	}


	public void testSingleLoad () throws Exception {
		// TODO: Start a transaction.
		// HINT: See previous lesson / lab

        try {
        	
        	// TODO: Query the users for a single user
        	// HINT: See slide titled "Working with getSingleResult()"
        	        	
        	// TODO: Make sure to commit the transaction
        	//transaction.commit();        	
        } catch (Exception ex) {
        	ex.printStackTrace();
        	// TODO: Make sure to rollback the transaction in the event of an error
        	//transaction.rollback();
        	throw ex;
        }
	}


	public void testSingleLoadNotFound () throws Exception {
		// TODO: Start a transaction.
		// HINT: See previous lesson / lab

        try {
        	
        	// TODO: Query the users for a single user not found
        	// HINT: See slide titled "Working with getSingleResult() too little"
        	
        	// TODO: Make sure to commit the transaction
        	//transaction.commit();        	
        } catch (Exception ex) {
        	ex.printStackTrace();
        	// TODO: Make sure to rollback the transaction in the event of an error
        	//transaction.rollback();
        	throw ex;
        }
	}
	
	public void testTooManyFound () throws Exception {
		// TODO: Start a transaction.
		// HINT: See previous lesson / lab

        try {
        	
        	// TODO: Query the users and test too many results found
        	// HINT: See slide titled "Working with getSingleResult() too much"
        	
        	// TODO: Make sure to commit the transaction
        	//transaction.commit();        	
        } catch (Exception ex) {
        	ex.printStackTrace();
        	// TODO: Make sure to rollback the transaction in the event of an error
        	//transaction.rollback();
        	throw ex;
        }
	}

	public void testPagination () throws Exception {
		// TODO: Start a transaction.
		// HINT: See previous lesson / lab

        try {
        	
        	try {

            	// TODO: Query the users by user.name using positional parameter
            	// HINT: See slide titled "Working with getSingleResult() too much"
        		
        	} catch (NoResultException nre) {
        		System.out.println("This exception is expected");
        	}
        	
        	// TODO: Make sure to commit the transaction
        	//transaction.commit();        	
        } catch (Exception ex) {
        	ex.printStackTrace();
        	// TODO: Make sure to rollback the transaction in the event of an error
        	//transaction.rollback();
        	throw ex;
        }
	}

	public void testNamedQueries () throws Exception {
		/* Start a transaction. Ah-HA!  Thought you would have to create it again, didn't you... practice makes perfect. */
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
        	
        	// TODO: Query users using named queries
        	// HINT: See slide titled "Using Named Queries in Annotations"
        	
        	transaction.commit();        	
        } catch (Exception ex) {
        	ex.printStackTrace();
        	transaction.rollback();
        	throw ex;
        }
	}
	
}
