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
    String sequenceName
    boolean needsSequence

    public JavaClass() {
    	
    }
    public JavaClass(String name, String packageName) {
    	this.name = name
    	this.packageName = packageName
    }
    public boolean isNamesMatch() {
        return name == table.name;
    }
    
    String toString() {
        "JavaClass name=${name} packageName=${packageName} primitive=${primitive} properties=${properties}"
    }
    
    boolean equals(Object other) {	
    	other.name == this.name && other.packageName == this.packageName
    }
    
    int hashCode() {
    	"${name}.${packageName}".hashCode()
    }

    String getDescriptivePropertyName() {
     JavaProperty p = this.properties.find{JavaProperty jp -> jp.name=="name"}
     if (p!=null) {
        return "name"
     }
     p = this.properties.find{JavaProperty jp -> jp.name=="title"}
     if (p!=null) {
      return "title"
     }

     for (JavaProperty pp : this.properties) {
       if (pp.name.endsWith("Name")) {
           return pp.name;
       }
     }

     return "id";
    }

    List<String> getSimplePropertyNames() {
        List<String> builder = []

        this.properties.each {JavaProperty jp ->
            switch (jp.javaClass) {
                case [new JavaClass("String","java.lang"), new JavaClass("Integer","java.lang"),
                new JavaClass("Long","java.lang"), new JavaClass("Byte", "java.lang"),
                new JavaClass("Date", "java.util")]:
                builder << jp.name
            }
            if (jp.javaClass.primitive) {
                builder << jp.name
            }
        }

        return builder
    }

    List<String> getPropertyNames() {
        List<String> builder = []
        this.relationships.each{Relationship relationship ->
            switch (relationship.type) {
                case[RelationshipType.MANY_TO_ONE, RelationshipType.ONE_TO_ONE]:
                    builder << relationship.name
            }
        }

        builder.addAll(getSimplePropertyNames())
        return builder
    }


    def validateClassAsModel () {
        assert this.id
        assert this.table
        assert this.name
        assert this.packageName
        this.relationships.each {Relationship relationship -> relationship.validate()}
    }

}