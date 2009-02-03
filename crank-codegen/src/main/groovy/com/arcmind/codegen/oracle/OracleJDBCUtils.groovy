/**
 * 
 */
package com.arcmind.codegen.oracle

import com.arcmind.codegen.JdbcUtils
import java.sql.*;


/**
 * @author Alec Kotovich
 *
 */
class OracleJDBCUtils extends JdbcUtils {
	
	OracleJDBCUtils(JdbcUtils parent, locale) {
		this.url = parent.url
		this.driver = parent.driver
		this.userName = parent.userName
		this.password = parent.password
		this.debug = parent.debug
	    this.trace = parent.trace

	    // Avoid here ORA-12705:
		// Cannot access NLS data files or invalid environment specified
		//Locale.default = Locale.US 
		Locale.setDefault(new Locale(locale))
	}
	
	def execute(Closure callme) {
		
		// Make connection
		Class.forName(driver)
		try {
			connection = DriverManager.getConnection (url, userName, password)
			callme(connection)
		} finally {
			connection?.close()
			connection=null
		}	
	}	
	
}
