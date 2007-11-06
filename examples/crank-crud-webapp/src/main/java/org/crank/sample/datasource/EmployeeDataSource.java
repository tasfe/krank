package org.crank.sample.datasource;

import java.util.ArrayList;
import java.util.List;

import org.crank.crud.controller.datasource.FilteringPagingDataSource;
import org.crank.crud.criteria.Comparison;
import org.crank.crud.criteria.Criterion;
import org.crank.crud.criteria.Group;
import org.crank.crud.criteria.OrderBy;
import org.crank.crud.join.Fetch;
import org.springframework.jdbc.core.JdbcTemplate;

public class EmployeeDataSource implements FilteringPagingDataSource {
	
	private Group group = new Group();
	private OrderBy[] orderBys = new OrderBy []{};
	private JdbcTemplate jdbcTemplate;
	
	private static String SELECT_COUNT = "select count(*) from Employee ";
	private static String SELECT_ROW = "select id, firstName, lastName, 5 from Employee ";
	
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
					builder.append(comparison.getOperator().getOperator());
					builder.append(" ? ");
				}
			}
			
			return builder.toString();
		} else {
			return "";
		}
	}
	
	protected Object[] extractValues() {
		List <Object> values = new ArrayList<Object>();
		for (Criterion criterion : group) {
			if (criterion instanceof Comparison) {
				Comparison comparison = (Comparison) criterion;
				values.add(comparison.getValue());
			}
		}
		return values.toArray(new Object[values.size()]);
	}

	public int getCount() {
		
		return jdbcTemplate.queryForInt(SELECT_COUNT + constructWhereClause());
	}

	public List list(int startItem, int numItems) {
		return jdbcTemplate.query(
				SELECT_ROW + constructWhereClause() + " LIMIT " + (startItem + numItems) + " OFFSET " + startItem, 
				extractValues(), new EmployeeReportObjectMapper());
	}

	public List list() {
		return jdbcTemplate.query(
				SELECT_ROW + constructWhereClause(), 
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

}
