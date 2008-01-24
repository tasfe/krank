package org.crank.core;

import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TemplateUtilsTest {
	
	private static final String testTemplate = 
"		<SCRIPT language='javascript'> " +
"	function validate() { " +
"		var vs = new Object(); " +
"		vs.popup = false; " +
"		vs.summary = ''; " +
"		var errorCount = 0; " +
"		var wasFieldValid = false; " +
"	" +
"		clearValidationSummary(vs); " +
"		${validators} " +
"		showPopupIfNecessary(vs); " +
"		return errorCount==0; " +
"	} " +
"	</SCRIPT> ";
		

	@SuppressWarnings("unchecked")
    @Test
	public void test() {
		Map map = new HashMap ();
		map.put("test", new Integer(1));
		map.put("test no space", new Integer(1));
		map.put("apple", "steve");
		map.put("validators", "YO");
		String string = TemplateUtils.newReplaceAll("${test} is ${apple}", map);
		assertEquals("1 is steve", string);
		
		string = TemplateUtils.newReplaceAll(testTemplate, map);
		assertTrue(string.contains("YO"));
		
		
		string = TemplateUtils.newReplaceAll("crap = ${crapkey}", map);
		
		assertEquals("crap = error ${crapkey} not found!!!! ", string);
		
		string = TemplateUtils.newReplaceAll("crap = ${crap\nkey}", map);
		
		assertEquals("crap = ${crap\nkey}", string);
		string = TemplateUtils.newReplaceAll("crap = ${crap key}", map);
		
		assertEquals("crap = ${crap key}", string);

	}
	
	@Test
	public void testReplaceAll() {
		Map<String, String> dictionary = new HashMap<String, String>();
		dictionary.put("one", "1");
		dictionary.put("two", "2");
		dictionary.put("three", "3");
		dictionary.put("four", "4");
		dictionary.put("five", "5");
		
		String result = TemplateUtils.replaceAll("${one}${two}${three}${four}${five}", dictionary);
		assertEquals("12345", result);
		
		result = TemplateUtils.replaceAll("onetwothreefourfive^one^^two^^three^^four^^five^onetwothreefourfive", dictionary, "^", "^");		
		assertEquals("onetwothreefourfive12345onetwothreefourfive", result);

		result = TemplateUtils.replaceAll("onetwothreefourfive^one^^two^^three^^four^^five", dictionary, "^", "^");		
		assertEquals("onetwothreefourfive1234^five", result);
	
		result = TemplateUtils.replaceAll("onetwothreefourfive", dictionary);
		assertEquals("onetwothreefourfive", result);
		
		result = TemplateUtils.replaceAll("one}two}three}four}five}", dictionary);
		assertEquals("one}two}three}four}five}", result);
	}

	
	@Test
	public void testReplaceAllFiles() throws IOException {
		Map<String, String> dictionary = new HashMap<String, String>();
		dictionary.put("one", "1");
		dictionary.put("two", "2");
		dictionary.put("three", "3");
		dictionary.put("four", "4");
		dictionary.put("five", "5");
		
		File inputFile = writeToTmpFile("${one}${two}${three}${four}${five}");
		File outputFile = createTmpFile();
		TemplateUtils.replaceAll(
				inputFile,
				outputFile,
				dictionary);
		assertEquals("12345", readFromFile(outputFile).trim());
		
		inputFile = writeToTmpFile("${one}${two}${three}${four}${five}");
		outputFile = createTmpFile();
		TemplateUtils.replaceAll(
				inputFile.getAbsolutePath(),
				outputFile.getAbsolutePath(),
				dictionary);
		assertEquals("12345", readFromFile(outputFile).trim());

	}
	
	@Test
	public void testLoadTemplate() throws IOException {
		try {
			File tmpFile = createTmpFile();
			tmpFile.delete();
			TemplateUtils.loadTemplate(tmpFile.getAbsolutePath());
		}
		catch (FileNotFoundException fnfe) {
			// expected
		}
		
		{
			String multiLineContent = "line one\nline two\nline three\n\n\n";
		    File template = writeToTmpFile(multiLineContent);
		    List<String> templateLines = TemplateUtils.loadTemplate(template.getAbsolutePath());
		    assertEquals(5, templateLines.size());
		    assertEquals("line one", templateLines.get(0));
		    assertEquals("line two", templateLines.get(1));
		    assertEquals("line three", templateLines.get(2));
		    assertEquals("", templateLines.get(3));
		    assertEquals("", templateLines.get(4));
		}
		{
			String multiLineContent = "line one\nline two\nline three";
		    File template = writeToTmpFile(multiLineContent);
		    List<String> templateLines = TemplateUtils.loadTemplate(template.getAbsolutePath());
		    assertEquals(3, templateLines.size());
		    assertEquals("line one", templateLines.get(0));
		    assertEquals("line two", templateLines.get(1));
		    assertEquals("line three", templateLines.get(2));
		}

	}
	
	
	private File writeToTmpFile(String content) throws IOException {
		File tmpFile = File.createTempFile("templateUtilsTest", ".txt");
		FileWriter fw = new FileWriter(tmpFile);
		try {
			fw.write(content);
		}
		finally {
			fw.close();
		}
		tmpFile.deleteOnExit();
		return tmpFile;
	}
	
	private File createTmpFile() throws IOException {
		File rv = File.createTempFile("templateUtilsTest", ".txt");
		rv.deleteOnExit();
		return rv;
	}
	
	private String readFromFile(File f) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(f));
		try {
			StringBuilder sb = new StringBuilder();
			char[] buf = new char[1024];
			int br = 0;
			while ((br = reader.read(buf)) != -1) {
				sb.append(buf, 0, br);
			}
			return sb.toString();
		}
		finally {
			reader.close();
		}
	}	
	
}
