package com.arcmind.codegen

import junit.framework.TestCase


/**
 * @author richardhightower
 *
 */
public class FacesConfigCodeGenTest extends TestCase{
	List<JavaClass> classes = new ArrayList<JavaClass>();
	
	public void setUp () {
		classes.add(new JavaClass(name:"Foo", packageName:"com.foo"))
		classes.add(new JavaClass(name:"Bar", packageName:"com.foo"))
		classes.add(new JavaClass(name:"Baz", packageName:"com.foo"))
	}
	
	public void testTest () {
		File rootDir = new File(".")
		println rootDir.canonicalPath
		File testFile = new File(rootDir.canonicalFile, "src/test/resources/src/main/webapp/WEB-INF/faces-config.xml")
		println testFile
		List<String> beforeChange = testFile.readLines()
		FacesConfigCodeGen codeGen = new FacesConfigCodeGen(file:testFile, classes:classes)
		use(StringCategory) { 
			codeGen.process()
		}
		
		List<String> afterChange = testFile.readLines()
		int count = 0
		afterChange.eachWithIndex {String line, int index ->
			if (beforeChange[index] != afterChange[index]) {
				count ++
			}
		}
		println "COUNT for faces config ${count}"

	}

}
