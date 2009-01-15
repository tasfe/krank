package com.arcmind.codegen

import junit.framework.TestCase


/**
 * @author richardhightower
 *
 */
public class XHTMLCodeGenTest extends TestCase{
	List<JavaClass> classes = new ArrayList<JavaClass>();
	
	public void setUp () {
		classes.add(new JavaClass(name:"Foo", packageName:"com.foo"))
		classes.add(new JavaClass(name:"Bar", packageName:"com.foo"))
		classes.add(new JavaClass(name:"Baz", packageName:"com.foo"))
		
		classes[0].relationships << new Relationship(type:RelationshipType.MANY_TO_ONE, name:"bar")
		classes[0].relationships << new Relationship(type:RelationshipType.ONE_TO_MANY, name:"baz")
		classes[0].properties << new JavaProperty(javaClass:new JavaClass("String", "java.lang"), name:"firstName")
	}
	
	public void testTest () {
		XHTMLCodeGenerator codeGen = new XHTMLCodeGenerator(classes:classes, rootDir:new File("./target/src/main/webapp"))
		use(StringCategory) { 
			codeGen.process()
		}
	}

}
