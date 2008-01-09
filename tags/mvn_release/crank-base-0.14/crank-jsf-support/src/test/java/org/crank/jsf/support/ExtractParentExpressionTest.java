package org.crank.jsf.support;

import junit.framework.TestCase;

public class ExtractParentExpressionTest extends TestCase {

	public void testSimple() {
		String expression = ExtractParentExpression.extractParentExpression("${foo.bar}");
		assertEquals("${foo}", expression);
	}

	public void testDeep() {
		String expression = ExtractParentExpression.extractParentExpression("${foo.doo.bar}");
		assertEquals("${foo.doo}", expression);
	}

	public void testSimpleIndexed() {
		String expression = ExtractParentExpression.extractParentExpression("${foo[bar]}");
		assertEquals("${foo}", expression);
	}

	public void testDeepIndexed() {
		String expression = ExtractParentExpression.extractParentExpression("${foo.doo[bar]}");
		assertEquals("${foo.doo}", expression);
	}
	public void testDeepIndexed2() {
		String expression = ExtractParentExpression.extractParentExpression("${foo.doo['bar']}");
		assertEquals("${foo.doo}", expression);
	}
	
}
