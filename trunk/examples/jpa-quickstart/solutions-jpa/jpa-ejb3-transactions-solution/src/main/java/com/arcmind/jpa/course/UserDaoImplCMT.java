package com.arcmind.jpa.course;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Here we have a dao implementation wrappering
 * the database code for the User object.
 * 
 * It is a stateless session bean, utilizing dependency injection
 * for setting the JPA EntityManager.
 * 
 * CMT in this case stands for "Container Managed Transactions"
 * 
 * In this case our entity manager will have it's transactions managed by the
 * container.  The entity manager is injected by virtue of the @PersistenceContext annotation.
 * 
 * @author Chris Mathias
 *
 */
@Stateless public class UserDaoImplCMT implements UserDaoCMT {

	@PersistenceContext(unitName="security-domain")
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public void create(User user) {
		entityManager.persist(user);
	}

	public void delete(User user) {
		//reassociate user with session
		user = entityManager.find(User.class, user.getId());
		entityManager.remove(user);
	}

	public User read(Long userId) {
		return entityManager.find(User.class, userId);
	}

	public void update(User user) {
		entityManager.persist(user);
	}

}
