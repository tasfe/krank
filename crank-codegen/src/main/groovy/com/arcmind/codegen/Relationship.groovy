package com.arcmind.codegen


/** Represents data about how Java classes are related      
 * @author richardhightower */
public class Relationship{
	String name
	RelationshipType type
	Key key
	JavaClass relatedClass
	
	public String toString() {
		"Relationship(name=${name}, type=${type}, \n key=${key})"
	}
}
