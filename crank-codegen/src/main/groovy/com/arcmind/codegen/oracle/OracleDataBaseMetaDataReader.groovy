/**
 * 
 */
package com.arcmind.codegen.oracle

import com.arcmind.codegen.DataBaseMetaDataReader
import com.arcmind.codegen.Table

//Let's not import this so we don't depend on a dependency that does not exist in the public repos
//import oracle.jdbc.OracleDatabaseMetaData;
import java.sql.ResultSet


/**
 * @author Alec Kotovich
 *
 */
class OracleDataBaseMetaDataReader extends DataBaseMetaDataReader {
	
	OracleDataBaseMetaDataReader(DataBaseMetaDataReader parent) {
		this.tables = new ArrayList(parent.tables)
		this.catalog = parent.catalog
		//this.schema = parent.schema
		this.schema = 'HR' //todo
		List lst = new ArrayList()
		parent.tableTypes.each {
			lst.add(it)
		}
		this.tableTypes = lst.toArray(new String[0])
		this.jdbcUtils = parent.jdbcUtils
		this.debug = parent.debug
		this.trace = parent.trace
		
		//Share the same connection
		this.connection = parent.connection		
	}
	
    /**
     * Connect to the database and read the database meta-data for a list of tables.
     */
    def processTables() {
    	  // Get the database meta data
    	  //OracleDatabaseMetaData dmd = (OracleDatabaseMetaData)this.connection.getMetaData();
    	  
    	  // Get schemas
    	  //ResultSet rs = dmd.getSchemas()
          ResultSet rs1 = null;
    	  
//          while (rs.next()) {
//        	  if (schema.equals(rs.getString(1))) {
//		    	  //Get tables
//		          rs1 = dmd.getTables(null,rs.getString(1),"%",tableTypes)
//
//			          jdbcUtils.iterate(rs1, { ResultSet resultSet ->
//			                String tableName = resultSet.getString (3)
//			                if (debug) println "DataBaseMetaDataReader: processTables() tableName=${tableName}"
//			                tables << new Table(name:tableName)
//			            })
//        	  }
//          }                                                                
    }
    
    // For debugging
    public void listWholeOracleDb() {
  	  // Get the database meta data
//  	  OracleDatabaseMetaData dmd = (OracleDatabaseMetaData)this.connection.getMetaData();
  	  
  	  // Get schemas
//  	  ResultSet rs = dmd.getSchemas();
//        ResultSet rs1 = null;
//
//        println("The following Schemas are available in the database:");
//        rs = dmd.getSchemas();
//        rs1 = null;
//        while(rs.next()) {
//          println(rs.getString(1));
//          println(
//              "The following tables are available in the "+rs.getString(1)+" schema:");
//          rs1 = dmd.getTables(null,rs.getString(1),"%",null);
//          while(rs1.next()) {
//            System.out.println("   "+ rs1.getString(3)+" : "+rs1.getString(4));
//          }
//        }
    }

}
