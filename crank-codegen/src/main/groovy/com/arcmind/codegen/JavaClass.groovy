package com.arcmind.codegen

/** Represents data about a Java class and how it is mapped to a Table. */
class JavaClass {

    String name
    String packageName
    boolean primitive
    JavaProperty id
    List<JavaProperty> properties = []
    List<Relationship> relationships = []
    HashMap<String, JavaProperty> columnNameToPropertyMap = [:]
    Table table

    public boolean isNamesMatch() {
        return name == table.name;
    }
    
    String toString() {
        "JavaClass name=${name} packageName=${packageName} primitive=${primitive} properties=${properties}"
    }
    
    

}