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

public class UserBasicQueryTest extends TestCase {

	private EntityManager entityManager;
	private EntityManagerFactory entityManagerFactory;

	protected void setUp() throws Exception {
		/* Use Persistence.createEntityManagerFactory to create 
		 * "security-domain" persistence unit. */ 
		
		entityManagerFactory = Persistence.createEntityManagerFactory("security-domain");
		deleteTestData();
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
		System.out.println("CREATE CALLED");
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
        	
        	Query query = entityManager.createQuery(
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
        		
        		query.setFirstResult(5);
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

	public void testNamedQueries () throws Exception {
		/* Start a transaction. */
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
        	
        	Query query =
        		entityManager.createNamedQuery("getUsers");
        	
        	List<User> users = query.getResultList();
        	
            query =
        		entityManager.createNamedQuery("loadUser");
            
            query.setParameter("userName", "PaulHix");
        	
        	User user = (User) query.getSingleResult();
        	
        	System.out.println(user.getName());
        	
        	for (User usr : users) {
        		System.out.println(usr.getName());
        	}
        	
        	transaction.commit();        	
        } catch (Exception ex) {
        	ex.printStackTrace();
        	transaction.rollback();
        	throw ex;
        }
	}
	
}
