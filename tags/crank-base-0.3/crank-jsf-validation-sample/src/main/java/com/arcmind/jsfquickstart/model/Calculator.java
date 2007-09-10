package com.arcmind.jsfquickstart.model;

public interface Calculator {

	/**
	 * add numbers.
	 *
	 * @param a first number
	 * @param b second number
	 *
	 * @return result
	 */
	public abstract int add(int a, int b);

	/**
	 * multiply numbers.
	 *
	 * @param a first number
	 * @param b second number
	 *
	 * @return result
	 */
	public abstract int multiply(int a, int b);

	public abstract int divide(int i, int j);

	public abstract int subtract(int i, int j);

}