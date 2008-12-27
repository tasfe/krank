/**
 * 
 */
package com.arcmind.codegen



/**
 * @author richardhightower
 *
 */
public class Key{
	Column primaryKey = new Column()
	Column foriegnKey = new Column()
	short updateRule
	short deleteRule
	String fkName
	String pkName
	short deferrability
	boolean imported
	
    String toString() {
        "Key(primaryKey=${primaryKey} \n     foriegnKey=${foriegnKey})"
    }
}
