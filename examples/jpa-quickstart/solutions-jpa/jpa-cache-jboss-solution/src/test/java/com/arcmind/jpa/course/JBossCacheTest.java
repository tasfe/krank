package com.arcmind.jpa.course;


public class JBossCacheTest { // extends EmbeddedEJB3JBossBaseTestCase {

	
	/**
	 * http://objectwave.javadevelopersjournal.com/owhibjboss.htm
	 * 
	 */
	
	
	//@Override
	protected void setUp() throws Exception {
		//super.setUp();
//		CacheFactory factory = DefaultCacheFactory.getInstance();
//        Cache cache = factory.createCache("cache-configuration.xml");
	}

	public void testCrudOperations() throws Exception {

		/* Overall Objective: add a new User to the database using JPA. */
		/* Create the entityManager. */
		/*
		 * Use Persistence.createEntityManagerFactory to create
		 * "security-domain" persistence unit.
		 */

//		EntityManager entityManager = (EntityManager) getInitialContext()
//				.lookup("java:/EntityManagers/security-domain");
//		TransactionManager transactionManager = (TransactionManager) getInitialContext()
//				.lookup("java:/TransactionManager");/* Start a transaction. */
//
//		transactionManager.begin();
//
//		/* Persist the user using the entityManager. */
//		for (int index = 0; index < 25; index++) {
//			User user = new User();
//			user.setName("FOO" + index);
//			entityManager.persist(user);
//		}
//
//		transactionManager.commit();
//
//		transactionManager.begin();
//
//		Query query = entityManager.createNamedQuery("getUsers");
//		query.setMaxResults(10);
//		List<User> users = query.getResultList();
//
//		assert users.size() > 0;
//
//		transactionManager.commit();
//
//		// Review the cache output in the console view.
//
//		transactionManager.begin();
//
//		User user = users.get(0);
//		user = entityManager.find(User.class, user.getId());
//		user.setName("Hong Kong Fooey");
//		entityManager.persist(user);
//
//		transactionManager.commit();
//
//		// Review the cache output in the console view.
//
//		transactionManager.begin();
//
//		User updatedUser = entityManager.find(User.class, user.getId());
//		System.out.println("Reloaded updated user:" + updatedUser.getId());
//
//		Phone phone = new Phone();
//		phone.setPhoneNumber("(123) 456-7891");
//		entityManager.persist(phone);
//		updatedUser.addPhone(phone);
//
//		phone = new Phone();
//		phone.setPhoneNumber("(456) 789-1011");
//		entityManager.persist(phone);
//		updatedUser.addPhone(phone);
//
//		entityManager.persist(updatedUser);
//
//		transactionManager.commit();

	}

}
