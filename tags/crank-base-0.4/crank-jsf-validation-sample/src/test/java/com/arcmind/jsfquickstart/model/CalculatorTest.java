package com.arcmind.jsfquickstart.model;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;

public class CalculatorTest {

	private Calculator calculator;
	
	@BeforeMethod protected void setUp() throws Exception {
		calculator = new CalculatorImpl();
	}

	@AfterMethod protected void tearDown() throws Exception {
		calculator = null;
	}

	@Test() public void testAdd() {
		 int results = calculator.add(1, 1);
		 assertEquals(2, results);
		 
	}

	@Test() public void testMultiply() {
		 int results = calculator.multiply(1, 1);
		 assertEquals(1, results);
	}

	@Test() public void testDivide() {
		 int results = calculator.divide(1, 1);
		 assertEquals(1, results);
	}
	@Test() public void testSubtract() {
		 int results = calculator.subtract(1, 1);
		 assertEquals(0, results);
	}

	@DataProvider(name = "divideData")
	public Object[][] generateDivideData() {
	 return new Integer[][] {
	   { 1, 1 },
	   { 10, 200},
	   { 200, 10}
	 };
	}	
	@Test(dataProvider="divideData") 
	public void testDivide(int num1, int num2) {
		 calculator.divide(num1, num2);
	}


	@Test(expectedExceptions={ArithmeticException.class}) 	
	public void testDivideByZero() {
		 calculator.divide(10, 0);
	}

}
