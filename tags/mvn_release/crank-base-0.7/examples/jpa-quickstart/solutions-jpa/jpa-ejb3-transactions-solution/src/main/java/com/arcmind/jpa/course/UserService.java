package com.arcmind.jpa.course;

import javax.ejb.Local;

@Local
public interface UserService {
	User create(User user);
	User read(Long userId);
	User update(User user);
	void delete(User user);
	void delete(Long userId);
}
