package com.arcmind.codegen



/** Represents data about keys in a table (imported and exported)
 * @author richardhightower 
 */
public class Key{
	Column primaryKey = new Column()
	Column foriegnKey = new Column()
	boolean imported
	
	String toString() {
		"Key(primaryKey=${primaryKey}  foriegnKey=${foriegnKey})"
	}
}
