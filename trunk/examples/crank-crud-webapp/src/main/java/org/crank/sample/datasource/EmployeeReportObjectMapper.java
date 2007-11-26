package org.crank.sample.datasource;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;


public class EmployeeReportObjectMapper implements RowMapper {

	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		EmployeeReportObject employeeReportObject = new EmployeeReportObject();
		employeeReportObject.setFirstName(rs.getString("firstName"));
		employeeReportObject.setLastName(rs.getString("lastName"));
		employeeReportObject.setId(rs.getLong("id"));
		return employeeReportObject;
	}


}
