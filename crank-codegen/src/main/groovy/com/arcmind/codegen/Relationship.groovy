package com.arcmind.codegen


/** Represents data about how Java classes are related      
 * @author richardhightower */
public class Relationship{
	String name
	String singularName
	RelationshipType type
	Key key
	JavaClass owner
	JavaClass relatedClass
	boolean bidirectional=true
	boolean ignore=false
	Relationship otherSide
	
	public String toString() {
		"${name} ${type} ${relatedClass?.name} ${key}"
	}
}
