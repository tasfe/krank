package com.arcmind.jpa.course;

import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.TransactionManager;

public class UserServiceTest extends EmbeddedEJB3JBossBaseTestCase {

	UserService userService;

	protected void setUp() throws Exception {

		super.setUp();
		
		//Pull userdao from entity manager.  This will inject user dao cmt (and internal to that the entity manager)		
		InitialContext ctx = getInitialContext();
		
		userService = (UserService) ctx.lookup("UserServiceImpl/local");
		
		createTestData();
	}

	protected void tearDown() throws Exception {
		deleteTestData();
		super.tearDown();
	}

	public void testCrudOperations() throws Exception {

		User user = new User();
		Long id = null;
		user.setName("RickHigh");

		/* Overall Objective: add a new User to the database using JPA with EJB. */
		/* Use the EJB3-configured UserServiceImpl. */

		/* Persist the user using the userService. */
		userService.create(user);
		
		id = user.getId();
		assert user.getCreatedDate() != null;
		
		user = null;

		/* Look up the user in the database. */
		user = userService.read(id);
		user.setName("RicardoTorreAlto");

		/* Test that the object was read from the database. */
		assertNotNull(user);
		assertEquals("RicardoTorreAlto", user.getName());

		/* Overall Objective: Delete the user. */
		/* Try to delete the user. */
		userService.delete(user);

		/* Overall Objective: Test that the user was deleted. */
		user = userService.read(id);

		/* Test that the user was not found in the database. */
		assertNull(user);

	}

	private void deleteTestData() throws Exception {

		// Obtain entity manager
		EntityManager entityManager = (EntityManager) getInitialContext().lookup("java:/EntityManagers/security-domain");

		// Obtain JBoss transaction
		TransactionManager transactionManager = (TransactionManager) getInitialContext().lookup("java:/TransactionManager"); 

		/* Start a transaction. */
		transactionManager.begin();

		try {
			Query query = entityManager.createQuery("delete User");
			query.executeUpdate();
			transactionManager.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			transactionManager.rollback();
			throw ex;
		}

	}

	private void createTestData() throws Exception {
		String[] userNames = new String[] { "RickHi", "BobSmith", "Sergey",
				"PaulHix", "Taboraz" };
		// Obtain entity manager
		EntityManager entityManager = (EntityManager) getInitialContext().lookup("java:/EntityManagers/security-domain");

		// Obtain JBoss transaction
		TransactionManager transactionManager = (TransactionManager) getInitialContext().lookup("java:/TransactionManager"); 

		transactionManager.begin();
		
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

			transactionManager.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			transactionManager.rollback();
			throw ex;
		}
	}

}
