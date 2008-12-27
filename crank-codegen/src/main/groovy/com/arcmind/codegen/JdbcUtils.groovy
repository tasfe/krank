package com.arcmind.codegen
import java.sql.*;
/** Utility class for working with low-level JDBC connections and ResultSets. */
class JdbcUtils {
	String url
	String driver
	String userName
	String password
	Connection connection
	boolean debug
	
	def executeScript(String sql) {
		execute {Connection con ->
			Statement statement = con.createStatement();
			try {
				statement.execute(sql);
			} finally {
				statement?.close();
			}
		}
	}
	
	def execute(Closure callme) {
		Class.forName(driver)
		try {
			connection = DriverManager.getConnection (url,userName,password)
			callme(connection)
		} finally {
			connection?.close()
			connection=null
		}
	}
	
	def iterate(ResultSet resultSet, Closure callme) {
		try {
			while (resultSet.next()) {
				callme(resultSet)
			}
		} finally {
			resultSet?.close()
		}
		
	}
}
