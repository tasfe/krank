/**
 * 
 */
package com.arcmind.codegen



/**
 * Represents JDBC settings
 * 
 * @author richardhightower
 */
class JDBCSettings {
	String url
	String userName
	String password
	String driver
	
    String toString() {
        "JDBCSettings: url=${url}, userName=${userName}, password=${password}, driver=${driver}"
    }
	
}
