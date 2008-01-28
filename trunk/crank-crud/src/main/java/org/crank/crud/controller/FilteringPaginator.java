package org.crank.crud.controller;

import org.crank.core.MapUtils;
import org.crank.crud.controller.datasource.FilteringPagingDataSource;
import org.crank.crud.controller.datasource.PagingDataSource;
import org.crank.crud.criteria.Comparison;
import org.crank.crud.criteria.Criterion;
import org.crank.crud.criteria.Group;
import org.crank.crud.criteria.OrderBy;
import org.crank.crud.criteria.Select;
import org.crank.crud.join.Join;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.*;

/**
 * This class is the main "backing bean" for paginated, filterable, sortable
 * listings. This class has the following:
 * 
 * <ul>
 * <li>Map of <code>filterableProperties</code>, which hold users sorts and
 * filters (comparisons)</li>
 * <li><code>criteria</code> set by programmers</li>
 * <li><code>orderBy</code> set by programmers</li>
 * </ul>
 * 
 * The main difference between <code>filterableProperties</code> and
 * <code>criteria</code> & <code>orderBy</code> is that
 * <code>filterableProperties</code> are set by users of the application while
 * <code>criteria</code> & <code>orderBy</code> are setup programmatically.
 * 
 * """True genius and creativity is always despised by the mundanes of the
 * world. When people tell their friends, "That (Rick) is such an (jerk)." Their
 * friends, reply: "I know. What a (jerk)." When I tell my friends that my
 * enemies are jerks they reply, "Who?"""" --Zed Shaw (with slight edits). RISA.
 * It is a joke so...
 * 
 * @author Rick Hightower
 * 
 */
@SuppressWarnings("serial")
public class FilteringPaginator extends Paginator implements
		FilterablePageable, Serializable {

	/** User filters. A filter is a combination of a Comparison and an OrderBy. */
	private Map<String, FilterableProperty> filterableProperties = null;
	/**
	 * Programmaticly setup Criteria. This allows developer to pre-filter a
	 * listing, for example only showing employees in a certain department. This
	 * criteria can not be cleared by the end user.
	 */
	private List<Criterion> criteria;
	/**
	 * Programmaticly setup order by clause. This allows developer to sort a
	 * listing, for example sort by creation date. This orderBy is cleared as
	 * soon as the end user selects any sort.
	 */
	private List<OrderBy> orderBy;

	/** The type of object that we are creating a listing for. */
	@SuppressWarnings("unchecked")
	private Class type;

	/**
	 * Allows us to add joins: Relationship joins, entity joins and fetch joins
	 * to the listing. Note adding a fetch join is of dubious value for a
	 * listing, but not prevented.
	 */
	private List<Join> joins = new ArrayList<Join>();

	/** The user friendly name of the object we are creating this listing for. */
	private String name;

	/** The orderBy sequence number. Used to order orderBy clauses. */
	private int sequence;

	/**
	 * Allows automatic joining to avoid n+1, this is a powerful feature that is
	 * under utilized. It probably needs to be revisited with our improved
	 * understanding of JPA.
	 * 
	 */
	private boolean autoJoin = false;

	/** Create a FilteringPaginator. */
	public FilteringPaginator() {
	}

	/** Create a FilteringPaginator. */
	@SuppressWarnings("unchecked")
	public FilteringPaginator(FilteringPagingDataSource dataSource, Class type) {
		super((PagingDataSource) dataSource);
		this.type = type;
		createFilterProperties();
	}

	/** Gets the name of the listing. */
	public String getName() {
		return (name != null ? name : CrudUtils.getClassEntityName(type))
				+ "Paginator";
	}

	public void setName(String name) {
		this.name = name;
	}

	@Deprecated
	public List<Join> getFetches() {
		return joins;
	}

	@Deprecated
	public void setFetches(List<Join> fetches) {
		this.joins = fetches;
	}

	/**
	 * Allows us to add joins: Relationship joins, entity joins and fetch joins
	 * to the listing. Note adding a fetch join is of dubious value for a
	 * listing, but not prevented.
	 */
	public List<Join> getJoins() {
		return joins;
	}

	/**
	 * Allows us to add joins: Relationship joins, entity joins and fetch joins
	 * to the listing. Note adding a fetch join is of dubious value for a
	 * listing, but not prevented.
	 */
	public void setJoins(List<Join> joins) {
		this.joins = joins;
	}

	/** Helper method to cast dataSource into the one we need. */
	private FilteringPagingDataSource filterablePaginatableDataSource() {
		return (FilteringPagingDataSource) this.dataSource;
	}

	/**
	 * This method automatically creates a list of FilterableProperty using
	 * reflection.
	 */
	private void createFilterProperties() {
		filterableProperties = new HashMap<String, FilterableProperty>();
		createFilterProperties(type, null, new PropertyScanner());
	}

	/**
	 * This class implements the toggle listener. We use this to register toggle
	 * listeners with FitlerableProperties so that when the user triggers a
	 * filter this class knows about it.
	 * 
	 * This implements the Observer/Observable pattern.
	 * 
	 * @author Rick Hightower
	 * 
	 */
	private class FPToggleListener implements ToggleListener, Serializable {
		private String property;

		public String getProperty() {
			return property;
		}

		public void setProperty(String property) {
			this.property = property;
		}

		public FPToggleListener(String property) {
			this.property = property;
		}

		public FPToggleListener() {
		}

		/** This method is the callback listener and gets notified of filteableProperties that change. */
		public void toggle(ToggleEvent event) {
			if (event.getSource() instanceof OrderBy) {
				OrderBy orderBy = (OrderBy) event.getSource();
				orderBy.setSequence(sequence);
				sequence++;
			}
			filter();
		}
	}

	
	/**
	 * This method creates the filter properties list using reflection.
	 * 
	 * This method is recursive in that it calls setupFilters which can call createFilterProperties.
	 * 
	 *   REFACTOR: Seems spds and pds both get processed the same way so not sure why we need 
	 *   the delination of two lists. Please refactor.
	 *   
	 * @author Rick Hightower
	 * @param theType
	 * @param propertyName
	 * @param ps
	 */
	@SuppressWarnings("unchecked")
	private void createFilterProperties(final Class theType,
			final String propertyName, PropertyScanner ps) {
		
		/* Get the beaninfo from the type object. */
		BeanInfo beanInfo = null;
		try {
			beanInfo = Introspector.getBeanInfo(theType);
		} catch (IntrospectionException ie) {
			throw new RuntimeException(ie);
		}
		/* Get the properties for the type. */
		PropertyDescriptor[] propertyDescriptors = beanInfo
				.getPropertyDescriptors();
		List<PropertyDescriptor> pds = new ArrayList<PropertyDescriptor>();
		List<PropertyDescriptor> spds = new ArrayList<PropertyDescriptor>();

		/* Iterate through the properties in this type. */
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			
			/* If autoJoin is enabled, then left fetch join the property. 
			 * 
			 * */
			if (autoJoin
					&& CrudUtils.isEntity(propertyDescriptor.getPropertyType())) {
				joins.add(Join.leftJoinFetch(propertyDescriptor.getName()));
			}
			
			
			if (theType == propertyDescriptor.getPropertyType()) {
				spds.add(propertyDescriptor);
			} else {
				pds.add(propertyDescriptor);
			}
		}
		setupFilters(theType, propertyName, ps, pds);
		setupFilters(theType, propertyName, ps, spds);
	}

	/**
	 * 
	 * @param theType
	 * @param propertyName
	 * @param ps
	 * @param pds
	 */
	@SuppressWarnings("unchecked")
	private void setupFilters(final Class theType, final String propertyName,
			PropertyScanner ps, List<PropertyDescriptor> pds) {
		
		String key;
		
		/* Iterate through the properties. */
		for (PropertyDescriptor propertyDescriptor : pds) {
			
			String property = null;
			
			/* Build the property path name. ex. o.employee.department.name */
			if (propertyName != null) {
				property = propertyName + "." + propertyDescriptor.getName();
			} else {
				property = propertyDescriptor.getName();
			}

			/* Create the new filterableProperty. */
			FilterableProperty filterableProperty = new FilterableProperty(
					property, propertyDescriptor.getPropertyType(), theType);

			/* Add it to our list of filterableProperties. */ 
			filterableProperties.put(property, filterableProperty);
			
			/* Register our toggle listener to be notified if the end user activates this property. */
			filterableProperty
					.addToggleListener(new FPToggleListener(property));
			
			/* Build a key to uniquely identify this property so we don't add it twice to the
			 * filterableProperties.
			 */
			/* Get the parent class name. */
			String parentClassName = theType.getName();
			String childClassName = propertyDescriptor.getPropertyType()
					.getName();
			
			key = parentClassName + "." + childClassName + "."
					+ propertyDescriptor.getName();

			/*
			 * See if this property is an entity or a embeddable object. 
			 */
			if (CrudUtils.isEntity(propertyDescriptor.getPropertyType())
					|| CrudUtils.isEmbeddable(propertyDescriptor
							.getPropertyType())) {
				/* Ask the property scanner if we can recurse on this property or if 
				 * we have recursed on it too many time already. This is to help us
				 * get rid of recursive loops which are as fun a barrel of monkey's 
				 * infected with ebola virus.
				 */
				if (ps.canIAddThisToTheFilterableProperties(key)) {

					createFilterProperties(
							propertyDescriptor.getPropertyType(), property, ps);

				}
			}

		}
	}

	private static int pscount;

	/** This class helps getting rid of the recursive loops, e.g., 
	 * employee.manager where manager is an employee 
	 * who has employees who have managers who have employees 
	 * who have manager who have employees.*/ 
	class PropertyScanner implements Serializable {

		int number = 0;

		public PropertyScanner() {
			pscount++;
			this.number = pscount;
		}

		private Map<String, Integer> visitorSet = new HashMap<String, Integer>();

		private boolean canIAddThisToTheFilterableProperties(String key) {
			/* Check to see if we visited this unique key already. */
			Integer visits = visitorSet.get(key);
			
			/* Nope we have not been here before. */
			if (visits == null) {
				visitorSet.put(key, 0);
				return true;
				
				/* We have been here before and now lets see if we have the proper depth.
				 * For example we might allow employee.manager.employees.manager.employees.name but no further.
				 * The default property depth is 1 b/c this recurive loop only is a problem for 1 to 1 relationships.
				 * This code could use some refactoring.
				 */
			} else if (visits < propertyDepth) {
				int newVisits = visits.intValue() + 1;
				visitorSet.put(key, new Integer(newVisits));
				return true;
			} else {
				return false;
			}
		}
	}

	public int getPropertyDepth() {
		return propertyDepth;
	}

	public void setPropertyDepth(int propertyDepth) {
		this.propertyDepth = propertyDepth;
	}

	private int propertyDepth = 1;


	/** The filter method fires a before filter event, prepares user filters (comparisons), prepares orderBys,
	 * prepares programatic criteria, then sets up the joins for the datasource, calls reset, 
	 * lastly fires an after filter event.
	 */
	public void filter() {
		fireBeforeFilter(filterablePaginatableDataSource().group());

		List<OrderBy> orderBys = prepareUserFiltersAndExtractOrderBysForFilter();

		prepareOrderByClauseProgramaticOrUserForFilter(orderBys);

		prepareProgramaticCriteriaForFilter();

		filterablePaginatableDataSource().setJoins(
				this.joins.toArray(new Join[this.joins.size()]));

		fireAfterFilter(filterablePaginatableDataSource().group());
		
		/* Reset the page count and such for pagination. */
		reset();
	}

	/** Builds the programmatic criteria list. */
	private void prepareProgramaticCriteriaForFilter() {
		/* Build the Criteria list. */
		if (criteria != null && criteria.size() > 0) {
			for (Criterion criterion : criteria) {
				filterablePaginatableDataSource().group().add(criterion);
			}
		}
	}

	/** Builds the order by clause for default <code>order by</code> or end user <code>order by</code>. */
	private void prepareOrderByClauseProgramaticOrUserForFilter(
			List<OrderBy> orderBys) {
		/* Sort the orderBys. */
		Collections.sort(orderBys, new Comparator<OrderBy>() {
			public int compare(OrderBy ob1, OrderBy ob2) {
				return ob1.getSequence().compareTo(ob2.getSequence());
			}
		});

		/*
		 * Re-sequence the orderBys so the number show up correctly to the end
		 * user.
		 */
		int sequence = 0;
		for (OrderBy order : orderBys) {
			order.setSequence(sequence);
			sequence++;
		}

		/*
		 * If there were OrderBys passed by the end user, use them, if not, use
		 * the default orderBys.
		 */
		if (orderBys.size() > 0) {
			/* Set the orderBy list. */
			filterablePaginatableDataSource().setOrderBy(
					orderBys.toArray(new OrderBy[orderBys.size()]));
		} else {
			/* Use default sorts */
			if (orderBy != null && this.orderBy.size() > 0) {
				filterablePaginatableDataSource().setOrderBy(
						this.orderBy.toArray(new OrderBy[this.orderBy.size()]));
			}
		}
	}

	/** Prepare UserFilters and extract OrderBys for filtering. */
	@SuppressWarnings("unchecked")
	private List<OrderBy> prepareUserFiltersAndExtractOrderBysForFilter() {
		/* Clear the comparison group b/c we are about to recreate it */
		filterablePaginatableDataSource().group().clear();

		/* OrderBy collection list. */
		List<OrderBy> orderBys = new ArrayList<OrderBy>();

		/* Iterator through the filters. */
		Collection<FilterableProperty> values = filterableProperties.values();
		for (FilterableProperty fp : values) {
			/* Add the comparison to the group. */
			if (fp.getComparison().isEnabled()
					&& fp.getComparison().getValue() != null) {

				/*
				 * If it is an Enumerator, let's convert it here. We let JSF
				 * manage the other types, but since there is no easy way to
				 * tell JSF about the Enumerator, we just convert it here since
				 * comparison.getValue() is an Object.
				 */
				if (fp.isEnum()) {
					Enum theEnumValue = Enum.valueOf(fp.getType(), (String) fp
							.getComparison().getValue());
					Comparison eqc = Comparison.eq(
							fp.getComparison().getName(), theEnumValue);
					filterablePaginatableDataSource().group().add(eqc);
				} else {
					filterablePaginatableDataSource().group().add(
							fp.getComparison());
				}
			}

			/* Add the order by clause to the list. */
			if (fp.getOrderBy().isEnabled()) {
				orderBys.add(fp.getOrderBy());
			}

		}
		return orderBys;
	}

	/** Clear all of the filterable properties. */
	public void clearAll() {
		filterableProperties.clear();
		sequence = 0;
		createFilterProperties();
		filter();
	}

	/** See if anyone is sorting any column. */
	public boolean isSorting() {
		Collection<FilterableProperty> values = filterableProperties.values();
		for (FilterableProperty fp : values) {
			if (fp.getOrderBy().isEnabled()) {
				return true;
			}
		}
		return false;
	}

	/** See if anyone is filtering any column. */
	public boolean isFiltering() {
		Collection<FilterableProperty> values = filterableProperties.values();
		for (FilterableProperty fp : values) {
			if (fp.getComparison().isEnabled()) {
				return true;
			}
		}
		return false;
	}

	/** Disable all of the sorts.  */
	public void disableSorts() {
		sequence = 0;
		Collection<FilterableProperty> values = filterableProperties.values();
		for (FilterableProperty fp : values) {
			fp.getOrderBy().setEnabled(false);
		}
		filter();
	}

	/** Disable all of the filters.  */
	public void disableFilters() {
		Collection<FilterableProperty> values = filterableProperties.values();
		for (FilterableProperty fp : values) {
			fp.getComparison().setEnabled(false);
		}
		filter();
	}

	/* Get the filterable properties. */
	public Map<String, FilterableProperty> getFilterableProperties() {
		return filterableProperties;
	}

	
	@SuppressWarnings("unchecked")
	public Class getType() {
		return type;
	}

	@SuppressWarnings("unchecked")
	public void setType(Class type) {
		this.type = type;
	}

	public List<OrderBy> getOrderBy() {
		if (orderBy == null) {
			orderBy = new ArrayList<OrderBy>();
		}
		return orderBy;
	}

	public void setOrderBy(List<OrderBy> orderBy) {
		this.orderBy = orderBy;
	}

	public void addCriterion(Criterion criterion) {
		List<Criterion> criteriaList = getCriteria();
		criteriaList.add(criterion);
	}

	public void addOrderBy(OrderBy orderBy) {
		getOrderBy().add(orderBy);
	}

	public List<Criterion> getCriteria() {
		if (criteria == null) {
			criteria = new ArrayList<Criterion>();
		}
		return criteria;
	}

	private List<FilteringListener> listeners = new ArrayList<FilteringListener>();

	public void addFilteringListener(FilteringListener listener) {
		listeners.add(listener);
	}

	public void removeFilteringListener(FilteringListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Fire and event to the listeners.
	 * 
	 */
	protected void fireBeforeFilter(Group group) {
		FilteringEvent fe = new FilteringEvent(this, group);
		for (FilteringListener fl : listeners) {
			fl.beforeFilter(fe);
		}
	}

	/**
	 * Fire and event to the listeners.
	 * 
	 */
	protected void fireAfterFilter(Group group) {
		FilteringEvent fe = new FilteringEvent(this, group);
		for (FilteringListener fl : listeners) {
			fl.afterFilter(fe);
		}
	}

	public boolean isAutoJoin() {
		return autoJoin;
	}

	public void setAutoJoin(boolean autoJoin) {
		this.autoJoin = autoJoin;
	}

	private List<Select> selects;
	
	public void addSelect(Select select) {
		if (selects == null) {
			selects = new ArrayList<Select>();
		}
		selects.add(select);
	}

	@SuppressWarnings("unchecked")
	public void addFilterableEntityJoin(Class entityClass, String entityName, String alias, String properties[]) {
		addSelect(Select.select(alias, false));
		joins.add(Join.entityJoin(entityName, alias));


		BeanInfo beanInfo = null;
		try {
			beanInfo = Introspector.getBeanInfo(entityClass);
		} catch (IntrospectionException ie) {
			throw new RuntimeException(ie);
		}
		
		Map<String, PropertyDescriptor> props = MapUtils.convertArrayToMap("name",  beanInfo
				.getPropertyDescriptors());
		
		for (String property : properties) {
			
			String propertyName = alias + "." + property;
			

			/* Create the new filterableProperty. */
			FilterableProperty filterableProperty = new FilterableProperty(
					propertyName, props.get(property).getPropertyType(), entityClass, false);

			/* Register our toggle listener to be notified if the end user activates this property. */
			filterableProperty
					.addToggleListener(new FPToggleListener(property));
			
			
			/* Add it to our list of filterableProperties. */ 
			filterableProperties.put(propertyName, filterableProperty);
			
		}
		
		this.addCriterion(Comparison.objectEq("o", "alias"));
		
	}
}
