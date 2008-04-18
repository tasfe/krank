package org.crank.jsf.support;

public class ExtractParentExpression {
	
	public static String extractParentExpression(final String expression) {
		int indexOfDot = expression.lastIndexOf('.');
		int indexOfBracket = expression.lastIndexOf('[');
		if (indexOfDot > indexOfBracket) {
			return expression.substring(0, indexOfDot) + "}";
		} else {
			return expression.substring(0, indexOfBracket) + "}";
		}
	}

}
