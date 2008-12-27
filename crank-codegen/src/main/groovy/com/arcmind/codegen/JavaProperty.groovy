package com.arcmind.codegen
class JavaProperty {
    JavaClass javaClass
    JavaClass parentClass
    String name
    Column column
    public boolean isNamesMatch() {
        return name == column.name;
    }
    String toString() {
        "JavaProperty(name=${name} javaClass.name=${javaClass.name})"
    }
}