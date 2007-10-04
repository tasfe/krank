package com.arcmind.jpa.course;

import javax.ejb.Stateless;

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
//@Stateless
public class UserDaoImpl {//TODO: implements UserDao {
	
	//TODO: - set this var up for injection - EJBContext ejbContext;

	//TODO: create method that returns this - 	EntityManager entityManager = (EntityManager) ejbContext.lookup("java:/EntityManagers/security-domain");
	
	//TODO: implement methods from interface using entity manager obtained from the method created above.

}
