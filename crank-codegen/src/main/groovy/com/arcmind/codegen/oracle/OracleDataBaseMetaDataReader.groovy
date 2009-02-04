/**
 * 
 */
package com.arcmind.codegen.oracle

import com.arcmind.codegen.DataBaseMetaDataReader
import oracle.jdbc.OracleDatabaseMetaData;


/**
 * @author Alec Kotovich
 *
 */
class OracleDataBaseMetaDataReader extends DataBaseMetaDataReader{
	
    /**
     * Connect to the database and read the database meta-data for a list of tables.
     */
    def processTables() {
    	  // Get the database meta data
        OracleDatabaseMetaData dmd = (OracleDatabaseMetaData)conn.getMetaData();
    }
}
