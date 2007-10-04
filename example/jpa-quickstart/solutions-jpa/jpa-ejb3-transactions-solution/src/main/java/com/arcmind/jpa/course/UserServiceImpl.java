package com.arcmind.jpa.course;

import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Stateless
public class UserServiceImpl implements UserService {

	@EJB
	private UserDaoCMT userDao;

	@TransactionAttribute(value=TransactionAttributeType.REQUIRES_NEW)
	public User create(User user) {
		processFields(user, true);
		userDao.create(user);
		return user;
	}

	@TransactionAttribute(value=TransactionAttributeType.REQUIRED)
	public void delete(Long userId) {
		userDao.delete(userDao.read(userId));
	}

	@TransactionAttribute(value=TransactionAttributeType.REQUIRED)
	public void delete(User user) {
		userDao.delete(user);
	}
	
	@TransactionAttribute(value=TransactionAttributeType.SUPPORTS)
	public User read(Long userId) {
		return userDao.read(userId);
	}

	@TransactionAttribute(value=TransactionAttributeType.REQUIRED)
	public User update(User user) {
		processFields(user, false);
		userDao.update(user);
		return user;
	}

	private void processFields(User user, boolean isCreate) {
		user.setUpdatedBy("Joe LoggedIn");// would normally be a handle to
											// logged in user.
		user.setUpdatedDate(new Date());
		if (isCreate) {
			user.setCreatedBy("Joe LoggedIn");
			user.setCreatedDate(new Date());
		}
	}

}
