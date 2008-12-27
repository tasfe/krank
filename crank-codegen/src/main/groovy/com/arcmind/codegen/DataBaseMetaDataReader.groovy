package com.arcmind.codegen
import java.sql.*

/**
* Reads database parameters.
*/
class DataBaseMetaDataReader {
    /** List of tables read from database. **/
    List <Table> tables = []
    /** Catalog for the database connection, default is null. */
    String catalog
    /** Schema for the database connection, default is null. */
    String schema
    /** Tables types that we will reverse, the default is just tables (no views) */
    String[] tableTypes = ["TABLE"]
    /** Utility class for managing database connection. */
    JdbcUtils jdbcUtils
    /* The current connection */
    private Connection connection
    boolean debug = true



    /**
    * Connect to the database and read the database meta-data for a list of tables. 
    */
    def processTables() {
        if(debug) println "DataBaseMetaDataReader: Processing Tables "
        jdbcUtils.iterate(connection.metaData.getTables (catalog, schema, null, tableTypes),
            { ResultSet resultSet ->
                String tableName = resultSet.getString ("TABLE_NAME")
                if (debug) println "DataBaseMetaDataReader: processTables() tableName=${tableName}"
                tables << new Table(name:tableName)
            }
        )
        if(debug) println "DataBaseMetaDataReader: Done Processing Tables " + tables
    }

    /**
    *  Process list of columns from the list of tables. Add the columns to the table object.
    */
    def processColumns() {
        tables.each {Table table ->
            jdbcUtils.iterate connection.metaData.getColumns(catalog, schema, table.name, null),
               { ResultSet resultSet ->
                  Column column = new Column()
                  column.name = resultSet.getString ("COLUMN_NAME")
                  column.typeName = resultSet.getString ("TYPE_NAME")
                  column.type = resultSet.getInt ("DATA_TYPE")
                  column.nullable = resultSet.getString ("IS_NULLABLE") == "YES" ? true : false
                  if (table.primaryKeys.contains(column.name)) {
                            	 column.primaryKey = true
                  }
                  table.columns << column
                             column.table = table
                }
        }
    }
    
    /**
     * Find the primary keys for each table.
     */
    def processPrimaryKeys() {
        tables.each {Table table ->
           jdbcUtils.iterate connection.metaData.getPrimaryKeys(catalog, schema, table.name), 
             { ResultSet resultSet -> table.primaryKeys << resultSet.getString ("COLUMN_NAME")}
        }
    }
    
    
    /**
     * Process import keys and export keys
     */
    def processKeys() {
        tables.each {Table table ->
            processKeys(table, connection.metaData.&getExportedKeys, table.exportedKeys, false)
            processKeys(table, connection.metaData.&getImportedKeys, table.importedKeys, true)
        }
    }

	def processKeys(Table table, getKeys, List<Key> keyList, boolean imported) {
        jdbcUtils.iterate getKeys(catalog, schema, table.name),
                { ResultSet resultSet ->
        		  Key key = new Key()
        		  key.imported = imported
        		  key.foriegnKey.name = resultSet.getString ("FKCOLUMN_NAME")
        		  key.foriegnKey.table = tables.find{it.name==resultSet.getString ("FKTABLE_NAME")}
        		  key.primaryKey.name = resultSet.getString ("PKCOLUMN_NAME")
        		  key.primaryKey.table = tables.find{it.name==resultSet.getString ("PKTABLE_NAME")} 
        		  keyList << key
                }
	}



    /** Process the database pulling out tables and columns whilst creating JavaClasses and Bean properties. */
    def processDB(){
        jdbcUtils.execute {Connection c ->
            if (debug) println "Processing the database"
            connection = c
         	processTables()
         	processPrimaryKeys()
         	processColumns()
         	processKeys()
        }
    }

}