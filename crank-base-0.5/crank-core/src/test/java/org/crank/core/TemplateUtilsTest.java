package org.crank.core;

import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;
import java.util.HashMap;
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

}
