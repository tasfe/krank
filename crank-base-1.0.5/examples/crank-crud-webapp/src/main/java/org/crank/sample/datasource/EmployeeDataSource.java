package org.crank.sample.datasource;

import java.util.ArrayList;
import java.util.List;

import org.crank.crud.controller.datasource.FilteringPagingDataSource;
import org.crank.crud.criteria.Comparison;
import org.crank.crud.criteria.Criterion;
import org.crank.crud.criteria.Group;
import org.crank.crud.criteria.Operator;
import org.crank.crud.criteria.OrderBy;
import org.crank.crud.criteria.Select;
import org.crank.crud.join.Join;
import org.springframework.jdbc.core.JdbcTemplate;

@SuppressWarnings("unchecked")
public class EmployeeDataSource implements FilteringPagingDataSource {
	
	/** The group holds conditions used by the listing to filter results. */
	private Group group = new Group();
	/** The orderBys holds a list of how the user wants the listings sorted. */
	private OrderBy[] orderBys = new OrderBy []{};
	/** Used to execute queries. */
	private JdbcTemplate jdbcTemplate;
	
	/** Used for calculating count of employees. */
	private static final String SELECT_COUNT = "select count(*) from Employee ";
	/** Used to calculate employee list. */
	private static final String SELECT_ROW = "select id, firstName, lastName, 5 from Employee ";
	
	
    /** Get the count of employees based on the current generated where clause.
     * The where clause is generated based on the values. */
	public int getCount() {
		return  jdbcTemplate.queryForInt(SELECT_COUNT + constructWhereClause(), extractValues());
	}

	/**
	 * Get the employees for the current page of data. 
	 * @param startItem where to start retrieving records.
	 * @param how many records to retrieve.
	 */
	public List list(int startItem, int numItems) {
		String query = SELECT_ROW + constructWhereClause() + constructOrderByClause() + 
		" LIMIT " + (startItem + numItems) + " OFFSET " + startItem;
		return jdbcTemplate.query(
				query, 
				extractValues(), new EmployeeReportObjectMapper());
	}

	/**
	 * Get all employees.
	 */
	public List list() {
		return jdbcTemplate.query(
				SELECT_ROW + constructWhereClause() + constructOrderByClause(), 
				extractValues(), new EmployeeReportObjectMapper());
	}


	/** Group gets manipualted by the paginator. */
	public Group group() {
		return group;
	}

	/** Order by gets used by the paginator class. */
	public OrderBy[] orderBy() {
		return this.orderBys;
	}

	/** Not used. */
	public void setFetches(Join[] fetches) {
	}

	/** Not used. */
	public Join[] fetches() {
		return null;
	}

	
	public void setOrderBy(OrderBy[] orderBy) {
		this.orderBys = orderBy;
	}

	/** Used for dependency injection of jdbcTemplate. */
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/** Construct the where clause from the group of Comparison objects. 
	 * @return the where clause based on the group of comparisons
	 */
	protected String constructWhereClause() {
		StringBuilder builder = new StringBuilder();
		/* If the group has something in it build a where clause. */
		if (group.size() > 0) {
			builder.append(" WHERE ");
			/* If the current iteration is a comparison, use it. */
			for (Criterion criterion : group) {
				if (criterion instanceof Comparison) {
					Comparison comparison = (Comparison) criterion;
					if (comparison.getName().equals("taskCount")) {
						continue;
					}
					builder.append(comparison.getName());
					builder.append(" " + comparison.getOperator().getOperator());
					builder.append(" ? ");
					builder.append("AND ");
				}
			}
			return builder.toString().substring(0,builder.length()-4);
		} else {
			return "";
		}
	}

	/**
	 * Construct the order by clause.  
	 * @return
	 */
	protected String constructOrderByClause() {
		StringBuilder builder = new StringBuilder();
		if (this.orderBys.length > 0) {
			builder.append(" ORDER BY ");
			for (OrderBy orderBy : orderBys) {
				builder.append(" " + orderBy.getName() + " " + orderBy.getDirection() + ",");
			}
			
			return builder.toString().substring(0,builder.length()-1);
		} else {
			return "";
		}
	}

	/**
	 * Extract the values for the prepared statement calls.
	 * @return extracted values.
	 */
	protected Object[] extractValues() {
		List <Object> values = new ArrayList<Object>();
		for (Criterion criterion : group) {
			if (criterion instanceof Comparison) {
				Comparison comparison = (Comparison) criterion;
				if (comparison.getOperator() == Operator.LIKE || comparison.getOperator() == Operator.LIKE_START) {
					String sValue = (String) comparison.getValue();
					values.add(sValue + "%");
				} else {
					values.add(comparison.getValue());
				}
			}
		}
		return values.toArray(new Object[values.size()]);
	}

	public Join[] joins() {
		
		return fetches();
	}

	public void setJoins(Join[] fetches) {
		setFetches(fetches);
	}

	public Select[] selects() {
		return null;
	}

	public void setSelects(Select[] arg0) {
	}
	
}
