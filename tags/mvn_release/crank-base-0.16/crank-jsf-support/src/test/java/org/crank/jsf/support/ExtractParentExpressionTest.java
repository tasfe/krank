package org.crank.jsf.support;

import org.testng.annotations.Test;

import junit.framework.TestCase;

public class ExtractParentExpressionTest extends TestCase {

	@Test
	public void testSimple() {
		String expression = ExtractParentExpression.extractParentExpression("${foo.bar}");
		assertEquals("${foo}", expression);
	}

	@Test
	public void testDeep() {
		String expression = ExtractParentExpression.extractParentExpression("${foo.doo.bar}");
		assertEquals("${foo.doo}", expression);
	}

	@Test
	public void testSimpleIndexed() {
		String expression = ExtractParentExpression.extractParentExpression("${foo[bar]}");
		assertEquals("${foo}", expression);
	}

	@Test
	public void testDeepIndexed() {
		String expression = ExtractParentExpression.extractParentExpression("${foo.doo[bar]}");
		assertEquals("${foo.doo}", expression);
	}
	@Test
	public void testDeepIndexed2() {
		String expression = ExtractParentExpression.extractParentExpression("${foo.doo['bar']}");
		assertEquals("${foo.doo}", expression);
	}
	
}
