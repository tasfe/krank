package org.codegen.model

import org.codegen.model.JavaClass
import org.codegen.model.Column

/** Represents data about a Java property and how it is mapped to a database column. */
class JavaProperty {
    JavaClass javaClass
    JavaClass parentClass
    String name
    Column column
    Integer size
    
    public boolean isNamesMatch() {
        return name == column.name;
    }
    public Integer getSize() {
    	size==null ? column.size : size
    }
    String toString() {
        "${name} ${javaClass.name} column=${column?.name}"
    }
}