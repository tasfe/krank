package com.arcmind.codegen
class JPACodeGeneratorTest extends GroovyTestCase {
    JavaProperty propertyMatch
    JavaProperty propertyNoMatch
    JavaClass bean
    JPACodeGenerator codeGenerator

    public void setUp() {
        bean = new JavaClass()
        codeGenerator = new JPACodeGenerator()
        bean.properties = []
        propertyMatch = new JavaProperty()
        propertyMatch.javaClass = new JavaClass(name:"String", packageName:"org.lang")
        propertyMatch.name = "Foo"
        propertyMatch.column = new Column(name:"Foo")
        bean.properties << propertyMatch

        propertyNoMatch = new JavaProperty()
        propertyNoMatch.javaClass = new JavaClass(name:"String", packageName:"org.lang")
        propertyNoMatch.name = "FooBar"
        propertyNoMatch.column = new Column(name:"FOO_BAR")
        bean.properties << propertyNoMatch

    }

    public void testIsColumnImportNeeded() {
        //assert codeGenerator.needsColumnImport(bean) == true
        //put only the property that has matching names
        //bean.properties = [propertyMatch,]
        //assert !codeGenerator.needsColumnImport (bean)
        //bean.properties[0].column.nullable = false
        //assert codeGenerator.needsColumnImport (bean)
    }

}