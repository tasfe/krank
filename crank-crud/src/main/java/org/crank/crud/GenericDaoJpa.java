package org.crank.crud;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.crank.crud.criteria.Criterion;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.springframework.transaction.annotation.Transactional;

/**
*  @param <T> Dao class
*  @param <PK> id class
*  @version $Revision:$
*  @author Rick Hightower
*/
public class GenericDaoJpa<T, PK extends Serializable> extends JpaDaoSupport implements GenericDao<T, PK>, Finder<T> {
    protected Class<T> type = null;

    public GenericDaoJpa( final Class<T> aType ) {
        this.type = aType;
    }

    public GenericDaoJpa() {
    }

    @Transactional
    public void create( final T newInstance ) {
        getJpaTemplate().persist( newInstance );
    }

    @Transactional
    public void delete( final PK id ) {
        getJpaTemplate().remove( read( id ) );
    }

    public T read( PK id ) {
        return getJpaTemplate().find( type, id );
    }

    @Transactional
    public void update( final T transientObject ) {
        getJpaTemplate().merge( transientObject );
    }

    public void setType( final Class<T> aType ) {
        this.type = aType;
    }

    public List<T> find( Map<String, Object> propertyValues ) {
        return find( propertyValues, null );
    }

    @SuppressWarnings( "unchecked" )
    public List<T> find( Map<String, Object> propertyValues, String[] orderBy ) {
        String entityName = getEntityName();
        String queryString = constructQueryString( entityName, propertyValues, orderBy );
        EntityManager entityManager = getEntityManager();
        Query query = constructQuery( propertyValues, queryString, entityManager );
        return (List<T>) query.getResultList();
    }

    public List<T> find( String property, Object value ) {
        HashMap<String, Object> propertyValues = new HashMap<String, Object>();
        propertyValues.put( property, value );
        return find( propertyValues );
    }

    @SuppressWarnings( "unchecked" )
    public List<T> find() {
        String entityName = getEntityName();
        return (List<T>) getJpaTemplate().find( "SELECT instance FROM " + entityName + " instance" );
    }

    @SuppressWarnings( "unchecked" )
    public List<T> find( String[] propertyNames, Object[] values ) {
        return find( propertyNames, values, null );
    }
    
    public List<T> find (Criterion... criteria) {
    	
    	
    	return null;
    }

    @SuppressWarnings( "unchecked" )
    public List<T> find( String[] propertyNames, Object[] values, String[] orderBy ) {
        String entityName = getEntityName();
        String queryString = constructQueryString( entityName, propertyNames, values, orderBy );
        EntityManager entityManager = getEntityManager();
        Query query = entityManager.createQuery( queryString );
        constructQueryParams( propertyNames, values, query );
        return (List<T>) query.getResultList();
    }

    private EntityManager getEntityManager() {
        EntityManager entityManager = getJpaTemplate().getEntityManager();
        if (null == entityManager) {
            entityManager = getJpaTemplate().getEntityManagerFactory().createEntityManager();
        }
        return entityManager;
    }

    private Query constructQuery( Map<String, Object> propertyValues, String queryString, EntityManager entityManager ) {
    	try {
	        Query query = entityManager.createQuery( queryString );
	        for (String propName : propertyValues.keySet()) {
	            if (propertyValues.get( propName ) != null) {
	                query.setParameter( ditchDot(propName), propertyValues.get( propName ) );
	            }
	        }
	        return query;
    	} catch (Exception ex) {
    		throw new RuntimeException("Unable to create query :" + queryString, ex);
    	}
    }

    protected String getEntityName( Class<T> clazz ) {
        Entity entity = (Entity) type.getAnnotation( Entity.class );
        if (entity == null) {
            return type.getSimpleName();
        }
        String entityName = entity.name();

        if (entityName == null) {
            return type.getSimpleName();
        } else if (!( entityName.length() > 0 )) {
            return type.getSimpleName();
        } else {
            return entityName;
        }

    }

    protected String getEntityName() {
        return getEntityName( type );
    }

    String constructQueryString( String entityName, Map<String, Object> propertyValues, String[] orderBy ) {
        String instanceName = "o";
        StringBuilder query = new StringBuilder( "SELECT " + instanceName + " FROM " );
        query.append( entityName );
        query.append( " " );
        query.append( instanceName );
        if (propertyValues.size() > 0) {
            query.append( " WHERE " );
            // keep the property names in a list to make sure we get them out in
            // the right order
            Iterator<String> propItr = propertyValues.keySet().iterator();
            if (propItr.hasNext()) {
                String propName = propItr.next();
                appendValueRepresentation( query, propName, propertyValues );
                for (; propItr.hasNext();) {
                    propName = propItr.next();
                    query.append( " AND " );
                    appendValueRepresentation( query, propName, propertyValues );
                }
            }
        }
        if (null != orderBy && orderBy.length > 0) {
            query.append( " ORDER BY " );
            for (int i = 0; i < orderBy.length; i++) {
                query.append( orderBy[i] );
                if (i + 1 < orderBy.length) {
                    query.append( ", " );
                }
            }
        }
        return query.toString();
    }

    private void appendValueRepresentation( StringBuilder query, String propName, Map<String, Object> propertyValues ) {
        Object value = propertyValues.get( propName );
        if (value == null) {
            query.append( propName );
            query.append( " is null " );
        } else {
            query.append( "o." + propName );
            query.append( " = :" );
            if (propName.contains(".")) {
            	propName = ditchDot(propName);
            }
            query.append( propName );
        }
    }

	private String ditchDot(String propName) {
		propName = propName.replace('.', '_');
		return propName;
	}

    private void constructQueryParams( String[] propertyNames, Object[] values, Query query ) {
        int index = 0;
        for (String propName : propertyNames) {
            query.setParameter( ditchDot(propName), values[index] );
            index++;
        }
    }

    String constructQueryString( String entityName, String[] propertyNames, Object[] values, String[] orderBy ) {
        if (propertyNames.length != values.length) {
            throw new RuntimeException(
                    "You are not using this API correctly. The propertynames length should always match values length." );
        }
        Map<String, Object> propertyValues = new HashMap<String, Object>( propertyNames.length );
        int index = 0;
        for (String propertyName : propertyNames) {
            propertyValues.put( propertyName, values[index] );
            index++;
        }
        return constructQueryString( entityName, propertyValues, orderBy );
    }

    public Object executeFinder( final Method method, final Object[] queryArgs ) {
        final String queryName = queryNameFromMethod( method );
        return getJpaTemplate().execute( new JpaCallback() {

            public Object doInJpa( EntityManager em ) throws PersistenceException {
                Query query = em.createNamedQuery( queryName );
                int index = 1;
                for (Object arg : queryArgs) {
                    query.setParameter( index, arg );
                    index++;
                }
                if (List.class.isAssignableFrom( method.getReturnType() )) {
                    return query.getResultList();
                } else {
                    return query.getSingleResult();
                }
            }
        } );
    }

    public String queryNameFromMethod( Method finderMethod ) {
        return type.getSimpleName() + "." + finderMethod.getName();
    }

}
