package org.crank.crud;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.crank.crud.criteria.Between;
import org.crank.crud.criteria.Comparison;
import org.crank.crud.criteria.Criterion;
import org.crank.crud.criteria.Group;
import org.crank.crud.criteria.Operator;
import org.crank.crud.criteria.VerifiedBetween;
import org.crank.crud.join.Fetch;
import org.crank.crud.join.Join;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.springframework.transaction.annotation.Transactional;

/**
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

	public GenericDaoJpa(final Class<T> aType) {
		this.type = aType;
	}

	public GenericDaoJpa() {
	}

	@Transactional
	public void create(final T newInstance) {
		getJpaTemplate().persist(newInstance);
	}

	@Transactional
	public void delete(final PK id) {
		getJpaTemplate().remove(read(id));
	}

	public T read(PK id) {
		if (type == null) {
			throw new UnsupportedOperationException(
					"The type must be set to use this method.");
		}
		return read(type, id);
	}

	public T read(Class clazz, PK id) {
		return (T) getJpaTemplate().find(clazz, id);
	}

	@Transactional
	public void update(final T transientObject) {
		getJpaTemplate().merge(transientObject);
	}

	public void setType(final Class<T> aType) {
		this.type = aType;
	}

	public List<T> searchOrdered(Criterion criteria, String... orderBy) {
		return this.find(orderBy, criteria);
	}

	public List<T> searchOrdered(Class clazz, Criterion criteria,
			String... orderBy) {
		return this.find(clazz, orderBy, criteria);
	}

	public List<T> find(List<Criterion> criteria, List<String> orderBy) {
		return find(type, criteria, orderBy);
	}

	public List<T> find(Class clazz, List<Criterion> criteria,
			List<String> orderBy) {
		return find(clazz, (String[]) orderBy
				.toArray(new String[orderBy.size()]), (Criterion[]) criteria
				.toArray(new Criterion[criteria.size()]));
	}

	public List<T> find(Map<String, Object> propertyValues) {
		return find(propertyValues, null);
	}

	public List<T> find(Class clazz, Map<String, Object> propertyValues) {
		return find(clazz, propertyValues, null);
	}

	@SuppressWarnings("unchecked")
	public List<T> find(Map<String, Object> propertyValues, String[] orderBy) {
		return find(orderBy, Group.and(propertyValues));
	}

	@SuppressWarnings("unchecked")
	public List<T> find(Class clazz, Map<String, Object> propertyValues,
			String[] orderBy) {
		return find(clazz, orderBy, Group.and(propertyValues));
	}

	public List<T> find(String property, Object value) {
		return find(type, property, value);
	}

	public List<T> find(Class clazz, String property, Object value) {
		HashMap<String, Object> propertyValues = new HashMap<String, Object>();
		propertyValues.put(property, value);
		return find(clazz, propertyValues);
	}

	@SuppressWarnings("unchecked")
	public List<T> find() {

		return find(type);
	}

	@SuppressWarnings("unchecked")
	public List<T> find(Class clazz) {
		String entityName = getEntityName(clazz);
		return (List<T>) getJpaTemplate().find(
				"SELECT instance FROM " + entityName + " instance");
	}

	@SuppressWarnings("unchecked")
	public List<T> find(String[] propertyNames, Object[] values) {
		return find(propertyNames, values, null);
	}

	@SuppressWarnings("unchecked")
	public List<T> find(Class clazz, String[] propertyNames, Object[] values) {
		return find(clazz, propertyNames, values, null);
	}

	@SuppressWarnings("unchecked")
	public List<T> find(Criterion... criteria) {
		return find((String[]) null, criteria);
	}

	@SuppressWarnings("unchecked")
	public List<T> find(Class clazz, Criterion... criteria) {
		return find(clazz, (String[]) null, criteria);
	}

	@SuppressWarnings("unchecked")
	public List<T> find(Class clazz, String[] propertyNames, Object[] values,
			String[] orderBy) {
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

	@SuppressWarnings("unchecked")
	public List<T> find(String[] propertyNames, Object[] values,
			String[] orderBy) {
		return find(type, propertyNames, values, orderBy);
	}

	@SuppressWarnings("unchecked")
	public List<T> find(Class clazz, String[] orderBy, Criterion... criteria) {
		return doFind(clazz, orderBy, criteria, null);
	}
	@SuppressWarnings("unchecked")
	private List<T> doFind(Class clazz, String[] orderBy, Criterion[] criteria, Fetch[] fetches) {
		StringBuilder sbQuery = new StringBuilder(255);
		String select = createSelect(getEntityName(clazz), "o");
		final Group group = Group.and(criteria);
		String whereClause = "";
		if (group.size() > 0) {
			whereClause = constructWhereClauseString(group, false);
		}
		final String sQuery = sbQuery.append(select).append(processJoins(fetches)).append(whereClause)
				.append(constructOrderBy(orderBy)).toString();
		try {
			return (List<T>) this.getJpaTemplate().execute(new JpaCallback() {
				public Object doInJpa(EntityManager em)
						throws PersistenceException {
					Query query = em.createQuery(sQuery);
					addGroupParams(query, group);
					return query.getResultList();
				}
			});
		} catch (Exception ex) {
			throw new RuntimeException("Unable to run query : " + sQuery, ex);
		}
	}

	private String processJoins(Fetch[] fetches) {
		if (fetches==null || fetches.length == 0) {
			return "";
		}
		StringBuilder builder = new StringBuilder(255);
		for (Fetch fetch : fetches) {
			if (fetch.getJoin() == Join.LEFT) {
				builder.append(" left ");
			}
			builder.append(" join fetch ")
			.append("o.").append(fetch.getRelationshipProperty());
		}
		return builder.toString();
	}

	@SuppressWarnings("unchecked")
	public List<T> find(String[] orderBy, Criterion... criteria) {
		return find(type, orderBy, criteria);
	}

	private void addGroupParams(Query query, Group group) {

		for (Criterion criterion : group) {
			if (criterion instanceof Group) {
				addGroupParams(query, (Group) criterion);
			} else {
				Comparison comparison = (Comparison) criterion;
                if (comparison.getValue() != null) {
    				final String sOperator = comparison.getOperator().getOperator();
    				if (!"like".equals(sOperator)) {
    					if (comparison instanceof Between || comparison instanceof VerifiedBetween) {
    						Between between = (Between) comparison;
    						query.setParameter(
    								ditchDot(comparison.getName()) + "1",
    								comparison.getValue());
    						query.setParameter(
    								ditchDot(comparison.getName()) + "2", between
    										.getValue2());
    					} else {
    						query.setParameter(ditchDot(comparison.getName()),
    								comparison.getValue());
    					}
    
    				} else {
    					Operator operator = comparison.getOperator();
    					StringBuilder value = new StringBuilder(50);
    					if (operator == Operator.LIKE) {
    						value.append(comparison.getValue());
    					} else if (operator == Operator.LIKE_CONTAINS) {
    						value.append("%").append(comparison.getValue()).append(
    								"%");
    					} else if (operator == Operator.LIKE_END) {
    						value.append("%").append(comparison.getValue());
    					} else if (operator == Operator.LIKE_START) {
    						value.append(comparison.getValue()).append("%");
    					}
    					query.setParameter(ditchDot(comparison.getName()), value
    							.toString());
    				}
                }
			}
		}
	}

	protected String constructWhereClauseString(Group group, boolean parens) {
		StringBuilder builder = new StringBuilder(255);
		if (group.size() == 0) {
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
		constructWhereClauseString(builder, group, false);
		return builder.toString();
	}

	protected void constructWhereClauseString(StringBuilder builder,
			Group group, boolean parens) {
		if (parens) {
			builder.append(" ( ");
		}
		if (group.size() == 1) {
			Criterion criterion = group.iterator().next();
			if (criterion instanceof Group) {
				constructWhereClauseString(builder, (Group) criterion, true);
			} else if (criterion instanceof Comparison) {
				addComparisonToQueryString((Comparison) criterion, builder);
			}
		} else {
			int size = group.size();
			int index = 0;
			for (Criterion criterion : group) {
				index++;
				if (criterion instanceof Group) {
					constructWhereClauseString(builder, (Group) criterion, true);
				} else if (criterion instanceof Comparison) {
					addComparisonToQueryString((Comparison) criterion, builder);
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
			StringBuilder builder) {

		String var = ":" +ditchDot(comparison.getName());
        if( comparison.getValue() != null ) {
            final String sOperator = comparison.getOperator().getOperator();
    
            builder.append(" o.");
            builder.append(comparison.getName());
            builder.append(" ");
            builder.append(sOperator);
            builder.append(" ");
    
            if (comparison instanceof Between || comparison instanceof VerifiedBetween) {
                builder.append(var + "1");
                builder.append(" ");
                builder.append("and " + var + "2");
            } else if (comparison.getOperator() == Operator.IN) {
                builder.append(" (");
                builder.append(var);
                builder.append(") ");
            } else {
                builder.append(var);
            }
            builder.append(" ");                        
        } else {
            builder.append(" o.");
            builder.append( comparison.getName() );
            if (comparison.getOperator() == Operator.EQ) {
                builder.append( " is null " );
            } else if (comparison.getOperator() == Operator.NE) {
                builder.append( " is not null " );
            }
        }
	}

	protected String getEntityName(Class<T> aType) {
		Entity entity = (Entity) aType.getAnnotation(Entity.class);
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

	private String constructOrderBy(String[] orderBy) {
		StringBuilder query = new StringBuilder(100);
		if (null != orderBy && orderBy.length > 0) {
			query.append(" ORDER BY ");
			for (int i = 0; i < orderBy.length; i++) {
				query.append(orderBy[i]);
				if (i + 1 < orderBy.length) {
					query.append(", ");
				}
			}
		}
		return query.toString();
	}

	private String createSelect(String entityName, String instanceName) {
		StringBuilder query = new StringBuilder("SELECT " + instanceName
				+ " FROM ");
		query.append(entityName);
		query.append(" ");
		query.append(instanceName + " ");
		return query.toString();
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

	public T readPopulated(Class clazz, final PK id) {
		try {
			return doReadPopulated(id);
		} catch (JpaSystemException jpaSystemException) {
			return read(id);
		}
	}

	private T doReadPopulated(final PK id) {
		final String queryName = type.getSimpleName() + ".readPopulated";
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

	public List<T> find(Fetch[] fetches, String[] orderBy, Criterion... criteria) {
		return doFind(this.type, orderBy, criteria, fetches);
	}

}
