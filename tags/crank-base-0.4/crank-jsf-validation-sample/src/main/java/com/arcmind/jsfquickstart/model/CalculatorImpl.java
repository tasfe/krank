package com.arcmind.jsfquickstart.model;

/**
 * Calculator
 *
 * @author Rick Hightower
 * @version 0.1
 */
public class CalculatorImpl implements Calculator {
    //~ Methods ----------------------------------------------------------------

	public CalculatorImpl () {
		//ystem.out.println("CREATE CALCULATOR");
	}
    /* (non-Javadoc)
	 * @see com.arcmind.jsfquickstart.model.Calculator#add(int, int)
	 */
    public int add(int a, int b) {
        return a + b;
    }

    /* (non-Javadoc)
	 * @see com.arcmind.jsfquickstart.model.Calculator#multiply(int, int)
	 */
    public int multiply(int a, int b) {
        return a * b;
    }
	public int divide(int i, int j) {
		return i/j;
	}
	public int subtract(int i, int j) {
		return i-j;
	}
    
}