package org.codegen.generator

import junit.framework.TestCase
import org.codegen.util.StringCategory
import org.codegen.generator.SpringJavaConfigCodeGen
import org.codegen.model.Relationship
import org.codegen.model.JavaProperty
import org.codegen.model.JavaClass
import org.codegen.model.RelationshipType

/**
 * @author richardhightower
 *
 */
public class SpringJavaConfigCodeGenTest extends TestCase{
	List<JavaClass> classes = new ArrayList<JavaClass>();
	
	public void setUp () {
		classes.add(new JavaClass(name:"Foo", packageName:"com.foo"))
		classes.add(new JavaClass(name:"Bar", packageName:"com.foo"))
		classes.add(new JavaClass(name:"Baz", packageName:"com.foo"))
		
		classes[0].relationships << new Relationship(type:RelationshipType.MANY_TO_MANY, name:"bars", owner:classes[0], relatedClass:classes[1])
		classes[0].relationships << new Relationship(type:RelationshipType.ONE_TO_MANY, name:"baz", owner:classes[0], relatedClass:classes[2])
		classes[0].properties << new JavaProperty(javaClass:new JavaClass("String", "java.lang"), name:"firstName")
	}
	
	public void testTest () {
		File rootDir = new File(".")
		println rootDir.canonicalPath
		File testFile = new File(rootDir.canonicalFile, "src/test/resources/src/main/java/org/yomama/EmployeeTaskApplicationContext.java")
		println testFile
		List<String> beforeChange = testFile.readLines()
		SpringJavaConfigCodeGen codeGen = new SpringJavaConfigCodeGen(file:testFile, classes:classes)
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
		println "COUNT for spring java config ${count}"

	}

}
