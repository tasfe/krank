package org.codegen.model

import org.codegen.model.RelationshipType
import org.codegen.model.Key
import org.codegen.model.JavaClass

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


    def validate() {
        assert name : "name is not null"
        assert type : "type is not null"
        assert owner: "owner is not null"
        assert relatedClass : "relatedClass is not null"
        if (bidirectional) {
            assert otherSide : "other side is not null"
        }
    }
	
	public String toString() {
		"${name} ${type} ${relatedClass?.name} ${key}"
	}
}
