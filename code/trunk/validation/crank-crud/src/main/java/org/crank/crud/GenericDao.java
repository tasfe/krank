package org.crank.crud;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.crank.crud.criteria.Criterion;
import org.crank.crud.criteria.OrderBy;
import org.crank.crud.join.Fetch;

/**
*  @param <T> DAO class
*  @param <PK> id class
*  @version $Revision:$
*  @author Rick Hightower
*/
public interface GenericDao<T, PK extends Serializable> {

    /** Persist the newInstance object into database
     * @param newInstance
     *          The new object
     */
    void create( T newInstance );

    /**
     * Retrieve an object that was previously persisted to the database using
     * the indicated id as primary key
     * @param id
     *          The Primary Key of the object to get.
     * @return Type
     */
    T read( PK id );

    /**
     * Save changes made to a persistent object.
     * @param transientObject
     *          The Object to update.
     * @return Type
     */
    T update( T transientObject );

    /**
     * Refresh a persistant object that may have changed in another thread/transaction.
     * @param transientObject
     *          The Object to refresh.
     */
    void refresh( T transientObject );

    /**
     * Write anything to db that is pending oporation and clear it.
     */
    void flushAndClear();

    /**
     * Remove an object from persistent storage in the database.
     * @param id
     *          The Primary Key of the object to delete.
     */
    void delete( PK id );

    /**
     * Remove an object from persistent storage in the database.
     * @param entity
     *          The Primary Key of the object to delete.
     */
    void delete( T entity );
    
    /**
     * Allows geting an object using a map of the field and values
     * 
     * @param propertyValues
     *            properties of VO to use as filters, values matching properties
     * @return List of requested objects.
     */
    List<T> find( Map<String, Object> propertyValues );

    /**
     * Allows geting an object using a map of the field and values
     * @param propertyNames
     *          Names of the fields on which to search.
     * @param values
     *          Values of the fields on which this is searching.
     * @return List of requested objects.
     */
    List<T> find( String[] propertyNames, Object[] values );

    /**
     * Does a query such as select yada from [yourclass] where field=value
     * orderby field.
     *
     * @param propertyNames
     *          Names of the fields on which to search.
     * @param values
     *          Values of the fields on which this is searching.
     * @param orderBy
     *          fields to order by in descending order
     * @return List of requested objects.
     */
    List<T> find( String[] propertyNames, Object[] values, String[] orderBy );

    /**
     * Does a query such as select yada from [yourclass] where field=value
     * orderby field.
     * 
     * @param propertyValues
     *            properties of VO to use as filters
     * @param orderBy
     *            fields to order by in descending order
     * @return List of requested objects.
     */
    List<T> find( Map<String, Object> propertyValues, String[] orderBy );

    /**
     * Method takes a class object and string field, and value to get a list of
     * objects. This is like a select "where something = value"
     * 
     * @param property
     *            field in hibernate object
     * @param value
     *            value to search on
     * @return list of the annotated objects
     */
    List<T> find( String property, Object value );

    /**
     * Generic method used to get all objects of a particular type. This is the
     * same as lookup up all rows in a table.
     * 
     * @return List of populated objects
     */
    List<T> find();
    
    int count();
    
    int count (Criterion... criteria);
    
    List<T> find (Criterion... criteria);
    
    List<T> find (String[] orderBy, Criterion... criteria);
    
    List<T> searchOrdered (Criterion criteria, String... orderBy);
    
    List<T> find (List<Criterion> criteria, List<String> orderBy);
    
    List<T> find (List<Criterion> criteria, String[] orderBy);
    
    List<T> find(Fetch[] fetches, String[] orderBy, Criterion... criteria);
    
    List<T> find(Fetch[] fetches, Criterion... criteria);
    
    List<T> find(Fetch... fetches);

    public List<T> find(Fetch[] fetches, String[] orderBy, int startPosition, int maxResults, Criterion... criteria);
    
    public List<T> find(Fetch[] fetches, OrderBy[] orderBy, int startPosition, int maxResults, Criterion... criteria);

	public List<T> find(String[] orderBy, int startPosition, int maxResults, Criterion... criteria);

	public List<T> find(int startPosition, int maxResults, Criterion... criteria);
	
	public List<T> find(int startPosition, int maxResults);
	
	List<T> find(OrderBy[] orderBy, int startPosition, int maxResults, Criterion... criteria);
	
	List<T> find(OrderBy[] orderBy, Criterion... criteria);

	public T readPopulated(final PK id);
}
