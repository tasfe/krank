package com.arcmind.jpa.course;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import junit.framework.TestCase;

public class UserTest extends TestCase {

	private EntityManager entityManager;
	private EntityManagerFactory entityManagerFactory;

	protected void setUp() throws Exception {
		/* Use Persistence.createEntityManagerFactory to create 
		 * "security-domain" persistence unit. */ 
		
		entityManagerFactory = Persistence.createEntityManagerFactory("security-domain");
		createTestData();

	}
	

	protected void tearDown() throws Exception {
		deleteTestData();
		if(entityManager.isOpen()) {
			entityManager.close();
		}
	}
	
	private void deleteTestData() throws Exception {
		
		entityManager = entityManagerFactory.createEntityManager();
		/* Start a transaction. */
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
		
	}



	private void createTestData() throws Exception {
		String[] userNames = new String[]{"RickHi","BobSmith","Sergey","PaulHix","Taboraz"}; 
		entityManager = entityManagerFactory.createEntityManager();
		/* Start a transaction. */
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
        	for (String userName : userNames) {
        		User user = new User();
        		user.setName(userName);
        		entityManager.persist(user);
        	}
        	for (int index = 0; index < 100; index++) {
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
	}

	public void testQueryUsers () throws Exception {
		/* Start a transaction. */
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
        	
        	Query query =
        		entityManager.createQuery("select user from User user");
        	
        	List<User> users = query.getResultList();        	
        	for (User user : users) {
        		System.out.println(user.getName());
        	}
        	
        	assertNotNull(users);
        	assertTrue(users.size() > 0);

        	
        	transaction.commit();
        } catch (Exception ex) {
        	ex.printStackTrace();
        	transaction.rollback();
        	throw ex;
        }
	}
	
	public void testQueryUserNames () throws Exception {
		/* Start a transaction. */
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
        	
        	Query query = entityManager.createQuery("select user.name from User " +
        			" user");
        	List<String> userNames = query.getResultList();        	
        	for (String userName : userNames) {
        		System.out.println(userName);
        	}
        	transaction.commit();        	
        } catch (Exception ex) {
        	ex.printStackTrace();
        	transaction.rollback();
        	throw ex;
        }
	}
	
	public void testMoreThanOneColumn () throws Exception {
		/* Start a transaction. */
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
        	
        	Query query = 
        		entityManager.createQuery("select user, user.name " +
        				" from User user");
        	List<Object[]> usersData = query.getResultList();        	
        	for (Object[] data : usersData) {
        		User user = (User) data[0];
        		String userName = (String) data[1];
        		System.out.println(user.getId());
        		System.out.println(userName);
        	}
        	transaction.commit();        	
        } catch (Exception ex) {
        	ex.printStackTrace();
        	transaction.rollback();
        	throw ex;
        }
        	
		
	}

	
	public void testSQLQuery () throws Exception {
		/* Start a transaction. */
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
        	
        	Query query =
        		entityManager.createNativeQuery(
        				"select user.name from User user");
        	List<String> userNames = query.getResultList();        	
        	for (String userName : userNames) {
        		System.out.println(userName);
        	}
        	transaction.commit();        	
        } catch (Exception ex) {
        	ex.printStackTrace();
        	transaction.rollback();
        	throw ex;
        }
	}
		

	public void testPositionalParameters () throws Exception {
		/* Start a transaction. */
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
        	
        	Query query =
        		entityManager.createQuery(
        				"select user from User user where user.name like ?");
        	
        	query.setParameter(1, "R%");
        	
        	List<User> users = query.getResultList();
        	
        	for (User user : users) {
        		System.out.println(user.getName());
        	}
        	        	

        	transaction.commit();        	
        } catch (Exception ex) {
        	ex.printStackTrace();
        	transaction.rollback();
        	throw ex;
        }
	}

	public void testNamedParameters () throws Exception {
		/* Start a transaction. */
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
        	
        	Query query =
        		entityManager.createQuery(
        				"select user from User user where user.name like :name");
        	
        	query.setParameter("name", "R%");
        	
        	List<User> users = query.getResultList();
        	
        	for (User user : users) {
        		System.out.println(user.getName());
        	}
        	        	

        	transaction.commit();        	
        } catch (Exception ex) {
        	ex.printStackTrace();
        	transaction.rollback();
        	throw ex;
        }
	}

	public void testSingleLoad () throws Exception {
		/* Start a transaction. */
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
        	
        	Query query =
        		entityManager.createQuery(
        				"select user from User user where user.name = ?");
        	query.setParameter(1, "PaulHix");

        	User user = (User) query.getSingleResult();
        	assertNotNull(user);
        	        	

        	transaction.commit();        	
        } catch (Exception ex) {
        	ex.printStackTrace();
        	transaction.rollback();
        	throw ex;
        }
	}


	public void testSingleLoadNotFound () throws Exception {
		/* Start a transaction. */
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
        	
        	
        	try {
        		Query query =
	        		entityManager.createQuery(
	        				"select user from User user where user.name = ?");
	        	
	        	query.setParameter(1, "THIS_USER_NAME_IS_NOT_IN_THE_DB");
	        	
	        	User user2 = (User) query.getSingleResult();
	        	System.out.println(user2);
	        	fail();
        	} catch (NoResultException nre) {
        		System.out.println("This exception is expected");
        	}
        	

        	transaction.commit();        	
        } catch (Exception ex) {
        	ex.printStackTrace();
        	transaction.rollback();
        	throw ex;
        }
	}
	
	public void testTooManyFound () throws Exception {
		/* Start a transaction. */
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
        	
        	
        	try {
        		Query query =
	        		entityManager.createQuery(
	        				"select user from User user ");
	        	
	        	
	        	User user2 = (User) query.getSingleResult();
	        	System.out.println(user2);
	        	fail();

	        	
        	} catch (NonUniqueResultException nure) {
        		System.out.println("This exception is expected");
        	}
        	

        	transaction.commit();        	
        } catch (Exception ex) {
        	ex.printStackTrace();
        	transaction.rollback();
        	throw ex;
        }
	}

	public void testPagination () throws Exception {
		/* Start a transaction. */
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
        	
        	
        	try {
        		Query query =
	        		entityManager.createQuery(
	        				"select user from User user");
        		
        		query.setFirstResult(0);
        		query.setMaxResults(10);

            	List<User> users = query.getResultList();
            	
            	assertEquals(10, users.size());
            	
            	for (User user : users) {
            		System.out.println(user.getName());
            	}
        		
        	} catch (NoResultException nre) {
        		System.out.println("This exception is expected");
        	}
        	

        	transaction.commit();        	
        } catch (Exception ex) {
        	ex.printStackTrace();
        	transaction.rollback();
        	throw ex;
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
	        transaction.commit();
        } catch (Exception ex) {
        	transaction.rollback();
        } finally {
        	
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
        	deleteTransaction.rollback();
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
	
}
