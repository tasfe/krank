package com.arcmind.jpa.course;

import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

@Stateless
@Interceptors({
	UserServiceSecurityAdvice.class
})
public class UserServiceImpl implements UserService {

	@EJB
	UserDao userDao;

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public User create(User user) {
		processFields(user, true);
		userDao.create(user);
		return user;
	}

	public void delete(Long userId) {
		userDao.delete(userDao.read(userId));
	}

	public void delete(User user) {
		userDao.delete(user);
	}

	public User read(Long userId) {
		return userDao.read(userId);
	}

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
