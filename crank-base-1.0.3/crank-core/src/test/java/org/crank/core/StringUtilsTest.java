package org.crank.core;
import static org.testng.AssertJUnit.*;
import org.testng.annotations.Test;
public class StringUtilsTest {
	@Test
	public void testCapitalize() {
		assertEquals("Testing 123", StringUtils.capitalize("Testing 123"));
		assertEquals("Testing 123", StringUtils.capitalize("testing 123"));
		assertEquals("123 testing", StringUtils.capitalize("123 testing"));
		assertEquals("", StringUtils.capitalize(""));
	}
	@Test
	public void testUncapitalize() {
		assertEquals("testing 123", StringUtils.unCapitalize("testing 123"));
		assertEquals("testing 123", StringUtils.unCapitalize("Testing 123"));
		assertEquals("123 testing", StringUtils.unCapitalize("123 testing"));
		assertEquals("", StringUtils.capitalize(""));
	}
	
	@Test
	public void testOthers() {
		assertTrue(StringUtils.contains("testing 123", "123"));
		assertEquals("testing 456", StringUtils.replace("testing 123", "123", "456"));
		java.util.List<String> splits = StringUtils.split("one,two,three");
		assertEquals("one", splits.get(0));
		assertEquals("two", splits.get(1));
		assertEquals("three", splits.get(2));
		splits = StringUtils.splitProperty("one.two.three");
		assertEquals("one", splits.get(0));
		assertEquals("two.three", splits.get(1));
	}
	
	
}
