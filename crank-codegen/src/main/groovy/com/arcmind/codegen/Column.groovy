package com.arcmind.codegen

/** Represents data about a column in a table */
class Column {
	String name
	int type
	String typeName
	boolean nullable=true
	Table table
	boolean primaryKey
	int size = -1
	
	public String toString() {
		//"Column( name=${name} type=${type} typeName=${typeName} nullable=${nullable} table.name=${table.name})"
		"Name: ${name}, typeName: ${typeName}, table.name=${table?.name}"
	}
	
}