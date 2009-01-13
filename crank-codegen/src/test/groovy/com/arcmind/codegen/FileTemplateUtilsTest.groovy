package com.arcmind.codegen

import junit.framework.TestCase


/**
 * @author richardhightower
 *
 */
public class FileTemplateUtilsTest extends TestCase{
	public void setUp () {
	}
	
	public void testTest () {
		File rootDir = new File(".")
		println rootDir.canonicalPath
		File testFile = new File(rootDir.canonicalFile, "src/test/resources/foo.txt")
		println testFile
		List<String> beforeChange = testFile.readLines()
		FileTemplateUtils utils = new FileTemplateUtils(file:testFile)
		ChangeSpec changeSpec = new ChangeSpec()
		changeSpec.replacementText="startLocationMarker\nRick is cool\n${System.currentTimeMillis()}\nstopLocationMarker\n"
		utils.changeSpecs << changeSpec
		utils.process()
		List<String> afterChange = testFile.readLines()
		int count = 0
		afterChange.eachWithIndex {String line, int index ->
			if (beforeChange[index] != afterChange[index]) {
				count ++
			}
		}
		println "COUNT ${count}"
		assert count == 1
		assert beforeChange.size() == afterChange.size()
	}

	public void testAgainstFacesConfig () {
		File rootDir = new File(".")
		println rootDir.canonicalPath
		File testFile = new File(rootDir.canonicalFile, "src/test/resources/src/main/webapp/WEB-INF/faces-config2.xml")
		println testFile
		List<String> beforeChange = testFile.readLines()
		FileTemplateUtils utils = new FileTemplateUtils(file:testFile)
		ChangeSpec changeSpec = new ChangeSpec()
		changeSpec.replacementText="""    <!-- Main Page Links (codegen) -->
		This is here... what is going on ... help me... ${System.currentTimeMillis()}
	  	<!-- End of Main Page Links (codegen) -->\n"""
	  	changeSpec.startLocationMarker="Main Page Links"
	  	changeSpec.stopLocationMarker="End of Main Page Links"
		utils.changeSpecs << changeSpec
	  	utils.process()
		List<String> afterChange = testFile.readLines()
		int count = 0
		afterChange.eachWithIndex {String line, int index ->
			if (beforeChange[index] != afterChange[index]) {
				count ++
			}
		}
		println "COUNT for faces config ${count}"
		assert count == 1
		println "beforeChange.size(${beforeChange.size()}) == afterChange.size(${afterChange.size()})"
		assert beforeChange.size() == afterChange.size()
	}
	
}
