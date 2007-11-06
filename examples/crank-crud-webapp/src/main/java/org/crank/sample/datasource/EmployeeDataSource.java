package org.crank.sample.datasource;

import java.util.ArrayList;
import java.util.List;

import org.crank.crud.controller.datasource.FilteringPagingDataSource;
import org.crank.crud.criteria.Comparison;
import org.crank.crud.criteria.Criterion;
import org.crank.crud.criteria.Group;
import org.crank.crud.criteria.Operator;
import org.crank.crud.criteria.OrderBy;
import org.crank.crud.join.Fetch;
import org.springframework.jdbc.core.JdbcTemplate;

public class EmployeeDataSource implements FilteringPagingDataSource {
	
	private Group group = new Group();
	private OrderBy[] orderBys = new OrderBy []{};
	private JdbcTemplate jdbcTemplate;
	
	private static String SELECT_COUNT = "select count(*) from Employee ";
	private static String SELECT_ROW = "select id, firstName, lastName, 5 from Employee ";
	
	
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

	public int getCount() {
		
		int count = jdbcTemplate.queryForInt(SELECT_COUNT + constructWhereClause(), extractValues());
		System.out.println("Count = " + count);
		return count;
	}

	public List list(int startItem, int numItems) {
		String query;
		query = SELECT_ROW + constructWhereClause() + constructOrderByClause() + 
		" LIMIT " + (startItem + numItems) + " OFFSET " + startItem;
		System.out.println("EmployeeDataSource.list(start, num): query = " + query);
		return jdbcTemplate.query(
				query, 
				extractValues(), new EmployeeReportObjectMapper());
	}

	public List list() {
		System.out.println("EmployeeDataSource.list(): query = " + SELECT_ROW + constructWhereClause() + " " + extractValues());
		return jdbcTemplate.query(
				SELECT_ROW + constructWhereClause() + constructOrderByClause(), 
				extractValues(), new EmployeeReportObjectMapper());
	}


	public Group group() {
		return group;
	}

	public OrderBy[] orderBy() {
		return this.orderBys;
	}

	public void setFetches(Fetch[] fetches) {
	}

	public Fetch[] fetches() {
		return null;
	}

	public void setOrderBy(OrderBy[] orderBy) {
		this.orderBys = orderBy;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	protected String constructWhereClause() {
		StringBuilder builder = new StringBuilder();
		if (group.size() > 0) {
			builder.append(" WHERE ");
			for (Criterion criterion : group) {
				if (criterion instanceof Comparison) {
					Comparison comparison = (Comparison) criterion;
					if (comparison.getName().equals("taskCount")) {
						continue;
					}
					builder.append(comparison.getName());
					builder.append(" " + comparison.getOperator().getOperator());
					builder.append(" ? ");
				}
			}
			
			return builder.toString();
		} else {
			return "";
		}
	}

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
	
}
