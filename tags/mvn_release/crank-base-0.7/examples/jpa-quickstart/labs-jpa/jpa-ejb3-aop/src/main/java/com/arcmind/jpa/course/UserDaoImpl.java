package com.arcmind.jpa.course;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

/**
 * Here we have a dao implementation wrappering the database code for the User
 * object.
 * 
 * It is a stateless session bean, utilizing dependency injection for setting
 * the JPA EntityManager.
 * 
 * @author Chris Mathias
 * 
 */
@Stateless
public class UserDaoImpl implements UserDao {
	@Resource EJBContext ejbContext;

	private EntityManager createEntityManager() {
		EntityManager entityManager =  
			(EntityManager) ejbContext
				.lookup("java:/EntityManagers/security-domain");
		return entityManager;
	}
	
	public void create(User user) {
		EntityManager entityManager = createEntityManager();
		entityManager.persist(user);
	}

	public void delete(User user) {
		EntityManager entityManager = createEntityManager();
		//reassociate user with session
		user = entityManager.find(User.class, user.getId());
		entityManager.remove(user);
	}

	public User read(Long userId) {
		EntityManager entityManager = createEntityManager();
		User user = entityManager.find(User.class, userId);
		return user;
	}

	public void update(User user) {
		EntityManager entityManager = createEntityManager();
		entityManager.persist(user);
	}

}
