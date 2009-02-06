/**
 * 
 */
package org.codegen.model



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
	
	public boolean equals(Object obj) {
		if((obj == null) || (obj.getClass() != this.getClass()))
			return false;
		// object must be JDBCSettings at this point
		JDBCSettings test = (JDBCSettings)obj;
		return test.toString().equals(this.toString());
	}
	
	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + (null == url ? 0 : url.hashCode());
		hash = 31 * hash + (null == userName ? 0 : userName.hashCode());
		hash = 31 * hash + (null == password ? 0 : password.hashCode());
		hash = 31 * hash + (null == driver ? 0 : driver.hashCode());
		return hash;
	}	
	
}
