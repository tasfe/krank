package org.crank.crud.dao;

import java.util.List;

import org.crank.crud.GenericDao;
import org.crank.crud.model.Tag;

public interface TagDAO extends GenericDao<Tag, Long> {
	List<Tag> findTagsForEmployee (Long employeeId);
}
