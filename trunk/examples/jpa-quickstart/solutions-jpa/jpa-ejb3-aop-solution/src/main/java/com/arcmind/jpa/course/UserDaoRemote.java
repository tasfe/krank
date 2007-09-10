package com.arcmind.jpa.course;

import javax.ejb.Local;

@Local
public interface UserDaoRemote {
	void create(User user);
	User read(Long userId);
	void update(User user);
	void delete(User user);
}

