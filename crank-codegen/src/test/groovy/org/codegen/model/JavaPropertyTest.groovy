package org.codegen.model

import org.codegen.model.JavaProperty
import org.codegen.model.JavaClass
import org.codegen.model.Column

class JavaPropertyTest extends GroovyTestCase {
    JavaProperty propertyMatch
    JavaProperty propertyNoMatch

    public void setUp() {
        propertyMatch = new JavaProperty()
        propertyMatch.javaClass = new JavaClass(name:"String", packageName:"org.lang")
        propertyMatch.name = "Foo"
        propertyMatch.column = new Column(name:"Foo")

        propertyNoMatch = new JavaProperty()
        propertyNoMatch.javaClass = new JavaClass(name:"String", packageName:"org.lang")
        propertyNoMatch.name = "FooBar"
        propertyNoMatch.column = new Column(name:"FOO_BAR")

    }
    public void testPropertyName() {
         assert propertyMatch.namesMatch
         assert !propertyNoMatch.namesMatch
    }

}