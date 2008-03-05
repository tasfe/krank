package org.crank.crud;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Id;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.crank.crud.controller.CrudUtils;
import org.crank.crud.criteria.Between;
import org.crank.crud.criteria.Comparison;
import org.crank.crud.criteria.Criterion;
import org.crank.crud.criteria.Group;
import org.crank.crud.criteria.Operator;
import org.crank.crud.criteria.OrderBy;
import org.crank.crud.criteria.OrderDirection;
import org.crank.crud.criteria.Select;
import org.crank.crud.criteria.VerifiedBetween;
import org.crank.crud.join.EntityJoin;
import org.crank.crud.join.Join;
import org.crank.crud.join.JoinType;
import org.crank.crud.join.RelationshipFetch;
import org.crank.crud.join.RelationshipJoin;
import org.crank.crud.join.SimpleRelationshipJoin;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * This is our GenericDAO interface that supports its own Criteria API and
 * finder AOP mixins.
 * 
 * @param <T>
 *            Dao class
 * @param <PK>
 *            id class
 * @version $Revision:$
 * @author Rick Hightower
 */
public class GenericDaoJpa<T, PK extends Serializable> extends JpaDaoSupport
		implements GenericDao<T, PK>, Finder<T> {
	
	protected Class<T> type = null;

	protected boolean distinct = false;

	protected Logger logger = Logger.getLogger(GenericDaoJpa.class);
	
	private String newSelectStatement = null;
	
	private List<QueryHint<?>> queryHints;
	
	protected String idPropertyName = "id";


	public void setQueryHints(List<QueryHint<?>> queryHints) {
		this.queryHints = queryHints;
	}

	public GenericDaoJpa(final Class<T> aType) {
		this.type = aType;
	}

	public GenericDaoJpa() {
	}

	@Transactional
	public T store(T entity) {
		T persistedEntity = entity;
		try {
			try {
				/*
				 * If the entity manager does not contain this entity or
				 * the entity has a null id, then use the persist method,
				 * otherwise use the merge method.
				 */
				if (!hasId(entity)) {
					// TODO: the error reporting could be deferred (the entity has an ID
					// that is in the db but not in the entity manager)
					getJpaTemplate().persist(entity);
				} else {
					persistedEntity = (T) getJpaTemplate().merge(entity);
				}
			} catch (EntityExistsException e) {
				// if the entity exists then we call merge
				persistedEntity = (T) getJpaTemplate().merge(entity);
			}
		} catch (Exception ex) { //We want to try to merge no matter what was thrown.
			try {
				// Force merge
				persistedEntity = (T) getJpaTemplate().merge(entity);
			} catch (Exception ex2) {
				logger.warn(String.format("Unable to store object %s exception %s original exception %s", entity, ex2.getMessage(), ex.getMessage()), ex2);
				throw new RuntimeException(ex);
			}
			
		}
		return persistedEntity;
	}

	protected boolean hasId(T entity) {
		BeanWrapper bw = new BeanWrapperImpl(entity);
		Object propertyValue = bw.getPropertyValue(this.idPropertyName);
		if (propertyValue==null) {
			return false;
		}
		
		if (propertyValue instanceof Number) {
			Number number = (Number) propertyValue;
			if (number.longValue() <= 0) {
				return false;
			} else {
				return true;
			}
		}
		
		return true;
	}

	@Transactional
	public void persist(T entity) {
		getJpaTemplate().persist(entity);
	}

	@Transactional
	public T merge(T entity) {
		return getJpaTemplate().merge(entity);
	}

	@Transactional
	public <RE> RE mergeRelated(RE entity) {
		return getJpaTemplate().merge(entity);
	}
	
	@Transactional
	public Collection<T> store(Collection<T> entities) {
		Collection<T> storedResults = new ArrayList<T>(entities.size());
		for (T entity : entities) {		
			storedResults.add(store(entity));
		}
		return storedResults;
	}

	@Transactional
	public void persist(Collection<T> entities) {
		for (T entity : entities) {		
			persist(entity);
		}
	}

	@Transactional
	public Collection<T> merge(Collection<T> entities) {
		Collection<T> mergedResults = new ArrayList<T>(entities.size());
		for (T entity : entities) {		
			mergedResults.add(merge(entity));
		}
		return mergedResults;
	}
	
	@Transactional
	public <RE> Collection<RE> mergeRelated(Collection<RE> entities) {
		Collection<RE> mergedResults = new ArrayList<RE>(entities.size());
		for (RE entity : entities) {		
			mergedResults.add(mergeRelated(entity));
		}
		return mergedResults;
	}
	

	public void setIsDistinct(boolean isDistinct) {
		this.distinct = isDistinct;
	}

	public void setDistinct(boolean isDistinct) {
		this.distinct = isDistinct;
	}

	public boolean isDistinct() {
		return this.distinct;
	}

	@Transactional
	public void delete(final PK id) {
		getJpaTemplate().execute(new JpaCallback() {
			public Object doInJpa(EntityManager entityManager)
					throws PersistenceException {
				String queryString = "DELETE FROM " + getEntityName()
						+ " WHERE " + getPrimaryKeyName() + " = " + id;
				Query query = entityManager.createQuery(queryString);
				query.executeUpdate();
				return null;
			}
		});
	}
	
	@Transactional
	public void delete(final Collection<T> entities) {
		for (T entity : entities) {
			delete(entity);
		}
	}

	public T read(PK id) {
		if (type == null) {
			throw new UnsupportedOperationException(
					"The type must be set to use this method.");
		}
		return read(type, id);
	}

	@SuppressWarnings("unchecked")
	public T read(Class<?> clazz, PK id) {
		return (T) getJpaTemplate().find(clazz, id);
	}

	@SuppressWarnings("unchecked")
	public T readExclusive(Class clazz, PK id) {
		EntityManager em = getTransactionEntityManager();
		Object entity = getJpaTemplate().find(clazz, id);
		em.lock(entity, LockModeType.READ);
		return (T) entity;
	}

	public T readExclusive(PK id) {
		if (type == null) {
			throw new UnsupportedOperationException(
					"The type must be set to use this method.");
		}
		return readExclusive(type, id);
	}

	@SuppressWarnings("unchecked")
	public T refresh(final T transientObject) {
		return (T)getJpaTemplate().execute(new JpaCallback() {		
			public Object doInJpa(EntityManager em) throws PersistenceException {
				T managedEntity = null;
				if (em.contains(transientObject)) {
					managedEntity = transientObject;
				}
				else {
					managedEntity = em.merge(transientObject);
				}
				// now refresh the state of the managed object, discarding any changes that were made
				em.refresh(managedEntity);
				return managedEntity;
			}
		}); 
	}
	
	@SuppressWarnings("unchecked")
	public T refresh(final PK id) {
		if (type == null) {
			throw new UnsupportedOperationException(
					"The type must be set to use this method.");
		}
		return (T)getJpaTemplate().execute(new JpaCallback() {		
			public Object doInJpa(EntityManager em) throws PersistenceException {
				T managedEntity = em.find(type, id);
				// now refresh the state of the managed object, discarding any changes that were made
				em.refresh(managedEntity);
				return managedEntity;
			}
		}); 
	}	

	public Collection<T> refresh(final Collection<T> entities) {
		Collection<T> refreshedResults = new ArrayList<T>(entities.size());
		for (T entity : entities) {
			refreshedResults.add(refresh(entity));
		}
		return refreshedResults;
	}
	
	@SuppressWarnings("unchecked")
	protected T findManagedEntity(EntityManager em, final T entity) {
		T managedObject = null;
		if (em.contains(entity)) {
			// the entity is already managed in this context
			managedObject = entity;
		}
		else {
			// the entity is not managed in this context, presumedly detached
			// we'll find it in the context by primary key
			BeanWrapper bw = new BeanWrapperImpl(entity);
			PK key = (PK)bw.getPropertyValue(getPrimaryKeyName());
			Class<T> entityClass = (Class<T>)entity.getClass();
			managedObject = em.find(entityClass, key);
			if (managedObject == null) {
				throw new EntityNotFoundException();
			}
		}
		return managedObject;
	}
	

	@Transactional
	public void flushAndClear() {
		getJpaTemplate().execute(new JpaCallback() {
			public Object doInJpa(EntityManager entityManager)
					throws PersistenceException {
				entityManager.flush();
				entityManager.clear();
				return null;
			}
		});
	}

	
	public void clear() {
		getJpaTemplate().execute(new JpaCallback() {
			public Object doInJpa(EntityManager entityManager)
					throws PersistenceException {
				entityManager.clear();
				return null;
			}
		});
	}

	@Transactional
	public void flush() {
		getJpaTemplate().execute(new JpaCallback() {
			public Object doInJpa(EntityManager entityManager)
					throws PersistenceException {
				entityManager.flush();
				return null;
			}
		});
	}
	
	/**
	 * @deprecated use store
	 */
	@Transactional
	public void create(final T newInstance) {
		getJpaTemplate().merge(newInstance);
	}

	/**
	 * @deprecated use merge
	 */
	@Transactional
	public T update(final T transientObject) {
		return getJpaTemplate().merge(transientObject);
	}

	public void setType(final Class<T> aType) {
		this.type = aType;
	}

	public List<T> searchOrdered(Criterion criteria, String... orderBy) {
		return this.find(orderBy, criteria);
	}

	public List<T> searchOrdered(Class<T> clazz, Criterion criteria,
			String... orderBy) {
		return this.find(clazz, orderBy, criteria);
	}

	public List<T> find(List<Criterion> criteria, List<String> orderBy) {
		return find(type, criteria, orderBy);
	}

	public List<T> find(Class<T> clazz, List<Criterion> criteria,
			List<String> orderBy) {
		return find(clazz, orderBy.toArray(new String[orderBy.size()]),
				criteria.toArray(new Criterion[criteria.size()]));
	}

	public List<T> find(List<Criterion> criteria, String[] orderBy) {
		return find(type, criteria, orderBy);
	}

	public List<T> find(Class<T> clazz, List<Criterion> criteria,
			String[] orderBy) {
		return find(clazz, orderBy, criteria.toArray(new Criterion[criteria
				.size()]));
	}

	public List<T> find(Map<String, Object> propertyValues) {
		return find(propertyValues, null);
	}

	public List<T> find(Class<T> clazz, Map<String, Object> propertyValues) {
		return find(clazz, propertyValues, null);
	}

	public List<T> find(Map<String, Object> propertyValues, String[] orderBy) {
		return find(orderBy, Group.and(propertyValues));
	}

	public List<T> find(Class<T> clazz, Map<String, Object> propertyValues,
			String[] orderBy) {
		return find(clazz, orderBy, Group.and(propertyValues));
	}

	public List<T> find(String property, Object value) {
		return find(type, property, value);
	}

	public List<T> find(Class<T> clazz, String property, Object value) {
		HashMap<String, Object> propertyValues = new HashMap<String, Object>();
		propertyValues.put(property, value);
		return find(clazz, propertyValues);
	}

	public List<T> find() {
		return find(type);
	}

	@SuppressWarnings("unchecked")
	public int count() {
		final String entityName = getEntityName(type);
		return (Integer) getJpaTemplate().execute(new JpaCallback() {

			public Object doInJpa(EntityManager em) throws PersistenceException {
				Query query = em.createQuery("SELECT count(*) FROM " + entityName + " instance");
				prepareQueryHintsIfNeeded(query);
				Long count = (Long) query.getResultList().get(0);
				return count.intValue();
			}});
	}

	@SuppressWarnings("unchecked")
	public List<T> find(Class<T> clazz) {
		String entityName = getEntityName(clazz);
		String sQuery = null;
		if (newSelectStatement == null) {
			sQuery = "SELECT instance FROM " + entityName + " instance";
		} else {
			sQuery =  "SELECT " + newSelectStatement + " FROM " + entityName + " o";
		}
		final String fQuery = sQuery; 
		return (List<T>) getJpaTemplate().execute(new JpaCallback() {
			public Object doInJpa(EntityManager em) throws PersistenceException {
				Query query = em.createQuery(fQuery);
				prepareQueryHintsIfNeeded(query);
				return query.getResultList();
			}});
	}

	public List<T> find(String[] propertyNames, Object[] values) {
		return find(propertyNames, values, null);
	}

	public List<T> find(Class<T> clazz, String[] propertyNames, Object[] values) {
		return find(clazz, propertyNames, values, null);
	}

	public List<T> find(Criterion... criteria) {
		return find((String[]) null, criteria);
	}

	public int count(final Criterion... criteria) {
		final Group group = criteria != null ? Group.and(criteria) : null;

		final StringBuilder sbquery = new StringBuilder("SELECT count("
				+ (this.distinct ? "DISTINCT " : "") + "o ) " + " FROM ");
		sbquery.append(getEntityName(type));
		sbquery.append(" ");
		sbquery.append("o").append(" ").append(constuctWhereClause(group));
		return executeCountQuery(group, sbquery, criteria);
	}

	private int executeCountQuery(final Group group,
			final StringBuilder sbquery, final Criterion... criteria) {
		try {
			return (Integer) this.getJpaTemplate().execute(new JpaCallback() {
				public Object doInJpa(EntityManager em)
						throws PersistenceException {
					Query query = em.createQuery(sbquery.toString());
					if (criteria != null) {
						addGroupParams(query, group, null);
					}
					return ((Long) query.getResultList().get(0)).intValue();
				}
			});
		} catch (Exception ex) {
			throw new RuntimeException("Unable to run query : " + sbquery, ex);
		}
	}

	public int count(Join[] joins, final Criterion... criteria) {
		final Group group = criteria != null ? Group.and(criteria) : null;

		final StringBuilder sbquery = new StringBuilder("SELECT count("
				+ (this.distinct ? "DISTINCT " : "") + "o )  ");
		sbquery.append(constructFrom(joins));
		sbquery.append(constructJoins(joins));
		sbquery.append(" ");
		sbquery.append(constuctWhereClause(group));
		return executeCountQuery(group, sbquery, criteria);
		
	}
	
	private String constructFrom(Join[] joins) {
		final StringBuilder sbquery = new StringBuilder(35);
		sbquery.append(" FROM ");
		sbquery.append(getEntityName(type));
		sbquery.append(" o ");
		if (joins == null || joins.length == 0) {
			return sbquery.toString();
		}
		for (Join join : joins){
			if (join instanceof EntityJoin) {
				EntityJoin ej = (EntityJoin) join;
				sbquery.append(" , ");
				sbquery.append(ej.getName());
				sbquery.append(" ");
				sbquery.append(ej.getAlias());
			}
		}
		return sbquery.toString();
	}

	public List<T> find(Class<T> clazz, Criterion... criteria) {
		return find(clazz, (String[]) null, criteria);
	}

	public List<T> find(Class<T> clazz, String[] propertyNames,
			Object[] values, String[] orderBy) {
		if (propertyNames.length != values.length) {
			throw new RuntimeException(
					"You are not using this API correctly. The propertynames length should always match values length.");
		}
		Map<String, Object> propertyValues = new HashMap<String, Object>(
				propertyNames.length);
		int index = 0;
		for (String propertyName : propertyNames) {
			propertyValues.put(propertyName, values[index]);
			index++;
		}
		return find(clazz, propertyValues, orderBy);
	}

	public List<T> find(String[] propertyNames, Object[] values,
			String[] orderBy) {
		return find(type, propertyNames, values, orderBy);
	}

	public List<T> find(Join[] fetches, String[] orderBy,
			Criterion... criteria) {
		return doFind(this.type, orderBy, criteria, fetches);
	}

	public List<T> find(Join[] fetches, Criterion... criteria) {
		return doFind(this.type, null, criteria, fetches);
	}

	public List<T> find(Join... fetches) {
		return doFind(this.type, null, null, fetches);
	}

	public List<T> find(Join[] fetches, String[] orderBy, int startPosition,
			int maxResults, Criterion... criteria) {
		return doFind(this.type, orderBy, criteria, fetches, startPosition,
				maxResults);
	}

	public List<T> find(String[] orderBy, int startPosition, int maxResults,
			Criterion... criteria) {
		return doFind(this.type, orderBy, criteria, null, startPosition,
				maxResults);
	}

	public List<T> find(int startPosition, int maxResults,
			Criterion... criteria) {
		return doFind(this.type, (OrderBy[]) null, criteria, null,
				startPosition, maxResults);
	}

	public List<T> find(int startPosition, int maxResults) {
		return doFind(this.type, (OrderBy[]) null, null, null, startPosition,
				maxResults);
	}

	public List<T> find(Join[] fetches, OrderBy[] orderBy, int startPosition,
			int maxResults, Criterion... criteria) {
		return doFind(this.type, orderBy, criteria, fetches, startPosition,
				maxResults);
	}

	public List<T> find(OrderBy[] orderBy, int startPosition, int maxResults,
			Criterion... criteria) {
		return doFind(this.type, orderBy, criteria, null, startPosition,
				maxResults);
	}

	public List<T> find(OrderBy[] orderBy, Criterion... criteria) {
		return doFind(this.type, orderBy, criteria, null, -1, -1);
	}

	public List<T> find(Class<T> clazz, String[] orderBy, Criterion... criteria) {
		return doFind(clazz, orderBy, criteria, null);
	}

	private String constuctWhereClause(final Group group) {
		String whereClause = "";
		if (group == null || group.size() > 0) {
			whereClause = constructWhereClauseString(group, false);
		}
		return whereClause;
	}

	@SuppressWarnings("unchecked")
	private List<T> doFind(Class<T> clazz, OrderBy[] orderBy,
			final Criterion[] criteria, Join[] fetches,
			final int startPosition, final int maxResult) {
		return doFind(clazz, null, this.distinct, orderBy, criteria, fetches, startPosition,
				maxResult);
	}
	
	@SuppressWarnings("unchecked")
	private List<T> doFind(Class<T> clazz, Select[] selects, boolean distinctFlag, OrderBy[] orderBy,
			final Criterion[] criteria, Join[] joins, final int startPosition, final int maxResult) {

		final Group group = criteria != null ? Group.and(criteria)
				: new Group();

		final String sQuery = createQuery(clazz, selects, distinctFlag,
				orderBy, joins, group);
		
		

		return (List<T>)executeQueryWithJPA(criteria, startPosition, maxResult, group,
				sQuery);
	}

	@SuppressWarnings("unchecked")
	private List executeQueryWithJPA(final Criterion[] criteria,
			final int startPosition, final int maxResult, final Group group,
			final String sQuery) {
		try {
			return (List) this.getJpaTemplate().execute(new JpaCallback() {
				public Object doInJpa(EntityManager em)
						throws PersistenceException {
					Query query = em.createQuery(sQuery);
					if (criteria != null) {
						addGroupParams(query, group, null);
					}
					if (startPosition != -1 && maxResult != -1) {
						query.setFirstResult(startPosition);
						query.setMaxResults(maxResult);
					}
					prepareQueryHintsIfNeeded(query);
					return query.getResultList();
				}

			});
		} catch (Exception ex) {
			logger.debug("failed to run a query", ex);
			throw new RuntimeException("Unable to run query : " + sQuery, ex);
		}
	}

	private final void prepareQueryHintsIfNeeded(Query query) {
		if (queryHints!=null && queryHints.size()>0) {
			for (QueryHint<?> qh : queryHints) {
				query.setHint(qh.getName(), qh.getValue());
			}
		}
	}

	private String createQuery(Class<T> clazz, Select[] selects,
			boolean distinctFlag, OrderBy[] orderBy, Join[] joins,
			final Group group) {
		StringBuilder sbQuery = new StringBuilder(255);
		final String sQuery = sbQuery
				.append(	constructSelect(selects, getEntityName(clazz), "o", distinctFlag))
				.append(    constructFrom(joins) )
				.append(    constructJoins(joins)).append(constuctWhereClause(group))
				.append(	constructOrderBy(orderBy)).toString();
		logger.debug(String.format("Query %s ", sQuery));
		return sQuery;
	}

	private List<T> doFind(Class<T> clazz, String[] orderBy,
			final Criterion[] criteria, Join[] fetches,
			final int startPosition, final int maxResult) {

		if (orderBy != null) {
			List<OrderBy> list = new ArrayList<OrderBy>();
			for (String order : orderBy) {
				list.add(new OrderBy(order, OrderDirection.ASC));
			}
			return doFind(clazz, list.toArray(new OrderBy[orderBy.length]),
					criteria, fetches, startPosition, maxResult);

		} else {
			return doFind(clazz, (OrderBy[]) null, criteria, fetches,
					startPosition, maxResult);
		}

	}

	private List<T> doFind(Class<T> clazz, String[] orderBy,
			Criterion[] criteria, Join[] fetches) {
		return doFind(clazz, orderBy, criteria, fetches, -1, -1);
	}

	private String constructJoins(Join[] joins) {
		if (joins == null || joins.length == 0) {
			return "";
		}
		StringBuilder builder = new StringBuilder(255);
		for (Join join : joins) {
			if (join instanceof RelationshipJoin) {
				RelationshipJoin rj = (RelationshipJoin) join;

				
				if (rj.getJoinType() == JoinType.LEFT) {
					builder.append(" left ");
				}
				
				if (rj instanceof RelationshipFetch) {
					builder.append(" join fetch ");
				} else if (rj instanceof SimpleRelationshipJoin) {
					builder.append(" join ");
				}

				builder.append(rj.isAliasedRelationship() ? "" : "o.");
				builder.append(rj.getRelationshipProperty());
				builder.append(" ");
				builder.append(rj.getAlias().equals("") ? rj.getDefaultAlias()
						: rj.getAlias());
			}
		}

		return builder.toString();
	}

	public List<T> find(String[] orderBy, Criterion... criteria) {
		return find(type, orderBy, criteria);
	}

	private void addGroupParams(Query query, Group group, Set<String> names) {

		if (names == null) {
			names = new HashSet<String>();
		}
		for (Criterion criterion : group) {
			if (criterion instanceof Group) {
				addGroupParams(query, (Group) criterion, names);
			} else {
				Comparison comparison = (Comparison) criterion;
				
				if (comparison.isObjectIdentity()) {
					continue;
				}
				
				String name = ditchDot(comparison.getName());

				name = ensureUnique(names, name);

				if (comparison.getValue() != null) {
					final String sOperator = comparison.getOperator()
							.getOperator();
					if (!"like".equalsIgnoreCase(sOperator)) {
						if (comparison instanceof Between) {
							Between between = (Between) comparison;
							query.setParameter(name + "_1", comparison
									.getValue());
							query
									.setParameter(name + "_2", between
											.getValue2());
						} else if (comparison instanceof VerifiedBetween) {
							VerifiedBetween between = (VerifiedBetween) comparison;
							query.setParameter(name + "_1", comparison
									.getValue());
							query
									.setParameter(name + "_2", between
											.getValue2());
						} else {
							query.setParameter(name, comparison.getValue());
						}

					} else {
						Operator operator = comparison.getOperator();
						StringBuilder value = new StringBuilder(50);
						if (operator == Operator.LIKE) {
							value.append(comparison.getValue());
						} else if (operator == Operator.LIKE_CONTAINS) {
							value.append("%").append(comparison.getValue())
									.append("%");
						} else if (operator == Operator.LIKE_END) {
							value.append("%").append(comparison.getValue());
						} else if (operator == Operator.LIKE_START) {
							value.append(comparison.getValue()).append("%");
						}
						if (logger.isDebugEnabled()) {
							logger.debug("parameters");
							logger
									.debug("value name = "
											+ comparison.getName());
							logger.debug("value value = " + value);
						}
						query.setParameter(name, value.toString());
					}
				}
			}
		}
	}

	private String ensureUnique(Set<String> names, String name) {
		if (names.contains(name)) {
			int index = 0;
			String tempVar = null;
			while (true) {
				tempVar = name + "_" + index;
				if (!names.contains(tempVar)) {
					break;
				}
				index++;
			}
			name = tempVar;
		}
		names.add(name);
		return name;
	}

	protected String constructWhereClauseString(Group group, boolean parens) {
		StringBuilder builder = new StringBuilder(255);
		if (group == null || group.size() == 0) {
			return "";
		} else if (group.size() == 1) {
			Criterion criterion = group.iterator().next();
			if (criterion instanceof Group) {
				Group innerGroup = (Group) criterion;
				if (innerGroup.size() == 0) {
					return "";
				}
			}
		}
		builder.append(" WHERE ");
		constructWhereClauseString(builder, group, false, null);
		return builder.toString();
	}

	protected void constructWhereClauseString(StringBuilder builder,
			Group group, boolean parens, Set<String> names) {

		if (names == null) {
			names = new HashSet<String>();
		}

		if (parens) {
			builder.append(" ( ");
		}
		if (group.size() == 1) {
			Criterion criterion = group.iterator().next();
			if (criterion instanceof Group) {
				constructWhereClauseString(builder, (Group) criterion, true,
						names);
			} else if (criterion instanceof Comparison) {
				addComparisonToQueryString((Comparison) criterion, builder,
						names);
			}
		} else {
			int size = group.size();
			int index = 0;
			for (Criterion criterion : group) {
				index++;
				if (criterion instanceof Group) {
					constructWhereClauseString(builder, (Group) criterion,
							true, names);
				} else if (criterion instanceof Comparison) {
					addComparisonToQueryString((Comparison) criterion, builder,
							names);
				}
				if (index != size) {
					builder.append(" ");
					builder.append(group.getJunction());
					builder.append(" ");
				}
			}
		}
		if (parens) {
			builder.append(" ) ");
		}
	}

	private void addComparisonToQueryString(Comparison comparison,
			StringBuilder builder, Set<String> names) {

		String var = ":" + ditchDot(comparison.getName());
		var = ensureUnique(names, var);

		if (comparison.isObjectIdentity()) {
			builder.append(comparison.getName());
			builder.append("=");
			builder.append(comparison.getValue());
			return;
		}
		
		if (comparison.getValue() != null) {
			final String sOperator = comparison.getOperator().getOperator();

			if (!comparison.isAlias()) {
				builder.append(" o.");
			} else {
				builder.append(" ");
			}
			builder.append(comparison.getName());

			builder.append(" ");
			builder.append(sOperator);
			builder.append(" ");

			if (comparison instanceof Between
					|| comparison instanceof VerifiedBetween) {
				builder.append(var).append("_1");
				builder.append(" ");
				builder.append("and ").append(var).append("_2");
			} else if (comparison.getOperator() == Operator.IN) {
				builder.append(" (");
				builder.append(var);
				builder.append(") ");
			} else {
				builder.append(var);
			}
			builder.append(" ");
		} else {
			if (!comparison.isAlias()) {
				builder.append(" o.");
			} else {
				builder.append(" ");
			}
			builder.append(comparison.getName());
			if (comparison.getOperator() == Operator.EQ) {
				builder.append(" is null ");
			} else if (comparison.getOperator() == Operator.NE) {
				builder.append(" is not null ");
			}
		}
	}

	protected String getEntityName(Class<T> aType) {
		Entity entity = aType.getAnnotation(Entity.class);
		if (entity == null) {
			return aType.getSimpleName();
		}
		String entityName = entity.name();

		if (entityName == null) {
			return aType.getSimpleName();
		} else if (!(entityName.length() > 0)) {
			return aType.getSimpleName();
		} else {
			return entityName;
		}

	}

	protected String getEntityName() {
		if (type == null) {
			throw new UnsupportedOperationException(
					"The type must be set to use this method.");
		}
		return getEntityName(this.type);
	}

	@SuppressWarnings("unchecked")
	private String searchFieldsForPK(Class<T> aType) {
		String pkName = null;
		Field[] fields = aType.getDeclaredFields();
		for (Field field : fields) {
			Id id = field.getAnnotation(Id.class);
			if (id != null) {
				pkName = field.getName();
				break;
			}
		}
		if (pkName == null && aType.getSuperclass() != null) {
			pkName = searchFieldsForPK((Class<T>) aType.getSuperclass());
		}
		return pkName;
	}

	private String searchMethodsForPK(Class<? super T> aType) {
		String pkName = null;
		Method[] methods = aType.getDeclaredMethods();
		for (Method method : methods) {
			Id id = method.getAnnotation(Id.class);
			if (id != null) {
				pkName = method.getName().substring(4);
				pkName = method.getName().substring(3, 4).toLowerCase()
						+ pkName;
				break;
			}
		}
		if (pkName == null && aType.getSuperclass() != null) {
			pkName = searchMethodsForPK(aType.getSuperclass());
		}
		return pkName;
	}

	protected String getPrimaryKeyName(Class<T> aType) {
		String pkName = searchFieldsForPK(aType);
		if (null == pkName) {
			pkName = searchMethodsForPK(aType);
		}
		return pkName;
	}

	protected String getPrimaryKeyName() {
		if (type == null) {
			throw new UnsupportedOperationException(
					"The type must be set to use this method.");
		}
		return getPrimaryKeyName(this.type);
	}

	private String constructOrderBy(OrderBy[] orderBy) {
		StringBuilder query = new StringBuilder(100);
		if (null != orderBy && orderBy.length > 0) {
			query.append(" ORDER BY ");
			for (int index = 0; index < orderBy.length; index++) {
				if (!orderBy[index].isAlias()) {
					query.append("o." + orderBy[index].getName());
				} else {
					query.append(orderBy[index].getName());
				}
				query.append(" ");
				query.append(orderBy[index].getDirection().toString());
				if (index + 1 < orderBy.length) {
					query.append(", ");
				}
			}
		}
		return query.toString();
	}

	private String constructSelect(Select[] selects, String entityName, String instanceName, boolean distinctFlag) {
		StringBuilder query = new StringBuilder(255);
		query.append("SELECT ");

		if (newSelectStatement != null) {
			query.append(newSelectStatement);
			return query.toString();
		}			
		query.append((distinctFlag ? " DISTINCT " : " "));
		query.append(instanceName);
		
		if (selects == null || selects.length == 0) {
			return query.toString();
		} else {
			query.append(", ");
			for (Select select : selects) {
				query.append((select.isDistinct() ? " DISTINCT " : " "));
				query.append(select.getName());
				query.append(", ");
			}
			return query.substring(0, query.length()-2);			
		}		
	}

	private String ditchDot(String propName) {
		propName = propName.replace('.', '_');
		return propName;
	}

	public Object executeFinder(final Method method, final Object[] queryArgs) {
		final String queryName = queryNameFromMethod(method);
		return getJpaTemplate().execute(new JpaCallback() {

			public Object doInJpa(EntityManager em) throws PersistenceException {
				Query query = em.createNamedQuery(queryName);
				int index = 1;
				for (Object arg : queryArgs) {
					query.setParameter(index, arg);
					index++;
				}
				if (List.class.isAssignableFrom(method.getReturnType())) {
					return query.getResultList();
				} else {
					return query.getSingleResult();
				}
			}
		});
	}

	public T readPopulated(Class<?> clazz, final PK id) {
		try {
			return doReadPopulated(id);
		} catch (JpaSystemException jpaSystemException) {
			return read(id);
		}
	}

	@SuppressWarnings("unchecked")
	private T doReadPopulated(final PK id) {
		final String queryName = CrudUtils.getClassEntityName(type)
				+ ".readPopulated";
		return (T) getJpaTemplate().execute(new JpaCallback() {
			public Object doInJpa(EntityManager em) throws PersistenceException {
				Query query = em.createNamedQuery(queryName);
				query.setParameter(1, id);
				return query.getSingleResult();
			}
		});
	}

	public T readPopulated(final PK id) {
		return readPopulated(type, id);
	}

	public String queryNameFromMethod(Method finderMethod) {
		return type.getSimpleName() + "." + finderMethod.getName();
	}

	@Transactional
	public void delete(final T entity) {
		getJpaTemplate().execute(new JpaCallback() {
			public Object doInJpa(EntityManager entityManager) throws PersistenceException {
				T managedEntity = entity;
				if (!entityManager.contains(managedEntity)) {
					managedEntity = entityManager.merge(entity);
				}
				entityManager.remove(managedEntity);
				return null;
			}
		
		});
	}

	private EntityManager getTransactionEntityManager() {
		EntityManagerHolder emHolder = (EntityManagerHolder) TransactionSynchronizationManager
				.getResource(getJpaTemplate().getEntityManagerFactory());
		return emHolder.getEntityManager();
	}

	public void setNewSelectStatement(String newSelectStatement) {
		this.newSelectStatement = newSelectStatement;
	}
	
	@Transactional
	public void run(Runnable runnable) {
		runnable.run();
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> find(Select[] selects, Join[] joins,
			OrderBy[] orderBy, final int startPosition, final int maxResults,
			final Criterion... criteria) {
		final Group group = criteria != null ? Group.and(criteria)
				: new Group();

		final String sQuery = createQuery(this.type, selects, this.distinct,
				orderBy, joins, group);

		return (List<Object[]>)executeQueryWithJPA(criteria, startPosition, maxResults, group,
				sQuery);
	}

	public String getIdPropertyName() {
		return idPropertyName;
	}

	public void setIdPropertyName(String idPropertyName) {
		this.idPropertyName = idPropertyName;
	}

}
