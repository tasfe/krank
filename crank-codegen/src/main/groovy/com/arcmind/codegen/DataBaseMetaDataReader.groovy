package com.arcmind.codegen
import java.sql.*

/**
 * Used to read database metadata and create a hierarchy of Table, Column and Key model objects.
 * The database metadata is read from connection.metaData.getTables, connection.metaData.getColumns,
 * connection.metaData.getPrimaryKeys, connection.metaData.getExportedKeys and
 * connection.metaData.getImportedKeys see Java API docs for JDBC Connection for more detail.
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
    boolean debug
    boolean trace



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
        	if (debug) println "reading table ${table.name} for columns"
            jdbcUtils.iterate connection.metaData.getColumns(catalog, schema, table.name, null),
            { ResultSet resultSet ->
                Column column = new Column()
                column.name = resultSet.getString ("COLUMN_NAME")
                if (debug) println "COLUMN NAME = ${column.name} ==================================="
                column.typeName = resultSet.getString ("TYPE_NAME")
                column.type = resultSet.getInt ("DATA_TYPE")
                column.nullable = resultSet.getString ("IS_NULLABLE") == "YES" ? true : false
                if (column.type in [Types.VARCHAR, Types.CHAR]) {
                	column.size = resultSet.getInt("COLUMN_SIZE")
                }
                if (table.primaryKeys.contains(column.name)) {
                    column.primaryKey = true
                }
                table.columns << column
                column.table = table
            }
        	if (debug) "\n"
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
		if (debug) println "processing keys for table ${table.name} imported=${imported}"
        boolean flag = false
        jdbcUtils.iterate getKeys(catalog, schema, table.name),
        { ResultSet resultSet ->
            flag = true
            processKey(keyList, debug, tables, resultSet, imported)
        }

        try {
            if (flag == false) {
                if (debug) "There were no results from the process keys method"
                jdbcUtils.iterate getKeys(catalog, schema, table.name.toLowerCase()),
                { ResultSet resultSet ->
                    flag = true
                    processKey(keyList, debug, tables, resultSet, imported)
                }
            }

            if (flag == false) {
                if (debug) "There were no results from the process keys method"
                jdbcUtils.iterate getKeys(catalog, schema, table.name.toUpperCase()),
                { ResultSet resultSet ->
                    flag = true
                    processKey(keyList, debug, tables, resultSet, imported)
                }
            }
        } catch (Exception ex) {
            //do nothing... this is not a fatal error.
             if (debug) println "Unable to process keys ${ex.message}"  
        }

		if (debug) println "\n"
	}

    private List processKey(List<Key> keyList, boolean debug, List<Table> tables, ResultSet resultSet, boolean imported) {
        Key key = new Key()
        key.imported = imported
        String fkName = resultSet.getString("FKCOLUMN_NAME")
        String fkTableName = resultSet.getString("FKTABLE_NAME")
        String pkName = resultSet.getString("PKCOLUMN_NAME")
        String pkTableName = resultSet.getString("PKTABLE_NAME")

        key.foriegnKey.name = fkName
        key.primaryKey.name = pkName

        key.foriegnKey.table = tables.find {it.name == fkTableName}
        key.primaryKey.table = tables.find {it.name == pkTableName}

        /* The following seven lines of code compensate for a MySQL JDBC error running on Windows. */
        if (key.foriegnKey.table == null) {
            key.foriegnKey.table = tables.find {it.name == fkTableName.toUpperCase()}
        }

        if (key.primaryKey.table == null) {
            key.primaryKey.table = tables.find {it.name == pkTableName.toUpperCase()}
        }


        if (key?.foriegnKey?.table == null || key?.primaryKey?.table == null ||
                key.primaryKey.name == null || key.foriegnKey.name == null) {
            println "WARNING: THE KEY IS NOT WELL FORMED. IT MIGHT BE THAT THE DATABASE DOES NOT SUPPORT FORIEGN KEYS---------"
            println "fkName=${fkName} fkTableName=${fkTableName} pkName=${pkName} pkTableName=${pkTableName}"
            println "Available table names = ${tables.collect {it.name}}"
            println "Setting well formed to false for key"
            key.wellFormed = false
        }
        if (debug) println "processKeys(), key found: ${key}"
        keyList << key
    }



    /** Process the database pulling out tables and columns whilst creating 
     * JavaClasses and Bean properties. */
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