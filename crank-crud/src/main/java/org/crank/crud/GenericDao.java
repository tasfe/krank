package org.crank.crud;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
*  @param <T> DAO class
*  @param <PK> id class
*  @version $Revision:$
*  @author Rick Hightower
*/
public interface GenericDao<T, PK extends Serializable> {

    /** Persist the newInstance object into database */
    void create( T newInstance );

    /**
     * Retrieve an object that was previously persisted to the database using
     * the indicated id as primary key
     */
    T read( PK id );

    /** Save changes made to a persistent object. */
    void update( T transientObject );

    /** Remove an object from persistent storage in the database */
    void delete( PK id );

    /**
     * Allows geting an object using a map of the field and values
     * 
     * @param propertyValues
     *            properties of VO to use as filters, values matching properties
     */
    List<T> find( Map<String, Object> propertyValues );

    /**
     * Allows geting an object using a map of the field and values
     * 
     */
    List<T> find( String[] propertyNames, Object[] values );

    /**
     * Does a query such as select yada from [yourclass] where field=value
     * orderby field.
     * 
     * @param orderBy
     *            fields to order by in descending order
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

}
