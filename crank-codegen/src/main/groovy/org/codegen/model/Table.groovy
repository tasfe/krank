package org.codegen.model

import org.codegen.model.Column
import org.codegen.model.Key

/** Represents data about a table in a database */
class Table {
	String name    
	List <Column> columns = []
	List <String> primaryKeys = []
	List <Key> exportedKeys = []
	List <Key> importedKeys = []
	public String toString() {
		"Table (name=${name}, columns=${columns}, primaryKeys=${primaryKeys} )"
	}
}