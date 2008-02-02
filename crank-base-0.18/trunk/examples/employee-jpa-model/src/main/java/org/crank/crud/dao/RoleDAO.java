package org.crank.crud.dao;

import java.util.List;

import org.crank.crud.GenericDao;
import org.crank.crud.model.Role;

public interface RoleDAO extends GenericDao<Role, Long> {
	List<Role> findInRoleIds (List ids);
}
