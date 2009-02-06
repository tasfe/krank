/**
 * 
 */
package org.codegen.util

import org.codegen.util.JdbcUtils
import java.sql.*
import org.codegen.util.JdbcUtils

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
}
