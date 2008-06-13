package org.crank.crud.controller;

import org.crank.crud.criteria.Criterion;
import org.crank.crud.criteria.OrderBy;
import org.crank.crud.criteria.Select;
import org.crank.crud.join.Join;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Defines an interface for a filtering, paginating controller.
 * </p>
 * 
 * <p>
 * This is not an extension point per se. If you want to plug into Crank pagination, filtering and sorting,
 * it is very likely that you want to extend the FilteringDataSource not this. 
 * @see {@link org.crank.crud.controller.datasource.FilteringDataSource}
 * </p>
 * 
 * @author Rick Hightower
 * 
 */
public interface FilterablePageable extends Pageable {
	
	/**
	 * <p>
	 * Map of FilterableProperty. A FilterableProperty allows an end user to 
	 * filter and sort a column in a listing.
	 * </p>
	 * 
	 * <p>
	 * The key is the property name of the filter, i.e., department.name.
	 * </p>
	 * 
	 * @return map of FilterableProperty 
	 */
	Map<String, FilterableProperty> getFilterableProperties();

	/**
	 * Apply the filters. This sets the filters from the user and the programatic filters into
	 * the dataSource. This fires two filter events (pre and post).
	 */
	void filter();

	/**
	 * This clears all of the end user defined filters and sorts.
	 * Clear all of the filterable properties. FilterableProperties are end user defined.
	 */
	void clearAll();

	/**
	 * Are we sorting?
	 * @return is the user sorting anything?
	 */
	boolean isSorting();

	/**
	 * Are we filtering?
	 * @return is the user filtering anything?
	 */
	boolean isFiltering();

	/** 
	 * Disable the user's sorts.
	 */
	void disableSorts();

	/** 
	 * Disable the user's filters.
	 */
	void disableFilters();
	
	/**
	 * 
	 * @return the type that this listing is for, i.e., Employee, Department, PetClinicLead.
	 */
	Class<?> getType();

	/**
	 * Add a programmatic criterion.
	 * @param criterion The Criterion
	 */
	void addCriterion(Criterion criterion);

	/**
	 * Get the list of programmatic criteria that we are using to prefilter the listing. 
	 * @return criteria
	 */
	List<Criterion> getCriteria();

	/**
	 * Register listeners to hear filter events.
	 * @param listener listener to add
	 */
	void addFilteringListener(FilteringListener listener);

	/**
	 * Remove listeners to stop listening to filter events.
	 * @param listener listener to add
	 */
	void removeFilteringListener(FilteringListener listener);

	/**
	 * Add a programmatic sort.
	 * @param orderBy the sort you are adding
	 */
	void addOrderBy(OrderBy orderBy);

	/** List of joins we are using, can be fetch, relationship join or fetch join. 
	 * 
	 * @return list of joins
	 */
	List<Join> getJoins();

	/** List of joins we are using, can be fetch, relationship join or fetch join. 
	 * 
	 * @return list of joins
	 */
	void setJoins(List<Join> joins);

	/** Configure a select, which allows us to join to anything.
	 * 
	 * @param select
	 */
	void addSelect(Select select);

	List<Select> getSelects();

	/**
	 * 
	 * <p>
	 * The addFilterableEntityJoin method allows us to join subclasses.
	 * </p>
	 * 
	 * <p>
	 * To call addFilterableEntityJoin we pass the class we are joining to, the
	 * name of the entity, the name of the alias, an array of property names,
	 * and an optional join that will be added to the where clause.
	 * </p>
	 * 
	 * <p>
	 * See http://code.google.com/p/krank/w/list and search for this method name
	 * to see how to use this method. It is in the crank-crud webapp example.
	 * Other methods which they could be like this method.
	 * </p>
	 * 
	 * <code><pre>
	 * paginator.addFilterableEntityJoin(PetClinicInquiry.class, //Class we are joining 
	 * 		&quot;PetClinicInquiry&quot;, //Entity name         
	 * 		&quot;inquiry&quot;, //Alias   
	 * 		new String[] { &quot;anotherProp&quot; }, //Array of property names we want to join to. 
	 * 		&quot;o.inquiry&quot;); //How to join to the PetClinicLead
	 * </pre></code>
	 * 
	 * @author Rick Hightower
	 * @param entityClass
	 *            the class we are joining to
	 * @param alias
	 *            the name of the alias
	 * @param properties
	 *            array of property names
	 * @param joinBy
	 *            (optional if null ignored) How to join to the PetClinicLead
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	void addFilterableEntityJoin(Class entityClass, String entityName,
			String alias, String properties[], String joinBy);

	@Deprecated
	List<Join> getFetches();

	@Deprecated
	void setFetches(List<Join> fetches);

    List<String> getPropertyNames();
}
