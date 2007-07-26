package com.arcmind.jsfquickstart.controller;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.faces.application.FacesMessage;

import org.crank.jsfspring.test.CrankMockObjects;
import org.springframework.testng.AbstractDependencyInjectionSpringContextTests;

import static org.testng.AssertJUnit.*;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.arcmind.jsfquickstart.model.Calculator;
import org.easymock.EasyMock;


public class CalculatorControllerTest extends AbstractDependencyInjectionSpringContextTests {

	private CalculatorController calcController;

	private CrankMockObjects crankMockObjects;
	
	@BeforeMethod
	public void calcSetUp() throws Exception {
		//ystem.out.println("####### BEFORE TEST #######");
		crankMockObjects = new CrankMockObjects();
		crankMockObjects.setUp();
		crankMockObjects.setUpApplicationContextWithScopes(this.applicationContext);
		calcController = (CalculatorController) this.applicationContext.getBean("CalcBean");

	}

	@AfterMethod
	public void calcTearDown() throws Exception {
		//ystem.out.println("####### AFTER TEST #######");		
		crankMockObjects.tearDown();
	}

	@Override
	protected String[] getConfigLocations() {
		String filename = null;
		try {
			filename = new File(
					"./src/main/webapp/WEB-INF/applicationContext.xml")
					.getCanonicalPath();
		} catch (IOException ex) {
			throw new RuntimeException("Unable to get file", ex);
		}
		return new String[] { "file:" + filename };
	}

	@Test
	public void testMultiply() {
		

		calcController.setFirstNumber(10);
		calcController.setSecondNumber(10);
		String outcome = calcController.multiply();
		Iterator messages = crankMockObjects.getFacesContext().getMessages();
		assertEquals(false, messages.hasNext());
		assertEquals("success", outcome);
		assertEquals(10, calcController.getFirstNumber());
		assertEquals(10, calcController.getSecondNumber());
		assertEquals(100, calcController.getResult());
	}

	
	@Test()
	public void testAdd() {

		calcController.setFirstNumber(1);
		calcController.setSecondNumber(1);
		String outcome = calcController.add();
		assertEquals("success", outcome);
		assertEquals(2, calcController.getResult());
	}

	@Test()
	public void testDivideByZero() {

		calcController.setFirstNumber(1);
		calcController.setSecondNumber(0);
		String outcome = calcController.divide();
		Iterator messages = crankMockObjects.getFacesContext().getMessages();
		FacesMessage facesMessage = (FacesMessage) messages.next();
		assertEquals("Can't divide first number by second number",
				facesMessage.getSummary());
		assertEquals("/ by zero", facesMessage.getDetail());
		assertEquals(FacesMessage.SEVERITY_ERROR, facesMessage.getSeverity());
		assertEquals("mathError", outcome);
	}

	@Test()
	public void testRandomException() {
		Calculator mockCalc = EasyMock.createMock(Calculator.class);
		
		EasyMock.expect(mockCalc.divide(10, 10)).andThrow(new RuntimeException("random error"));
		EasyMock.replay(mockCalc);
		calcController.setCalculator(mockCalc);
		

		calcController.setFirstNumber(10);
		calcController.setSecondNumber(10);
		String outcome = calcController.divide();
		assertEquals("fatal", outcome);
		EasyMock.verify(mockCalc);

		Iterator messages = crankMockObjects.getFacesContext().getMessages();
		FacesMessage facesMessage = (FacesMessage) messages.next();
		assertEquals("Some sort of fatal issue see details",
				facesMessage.getSummary());
		assertEquals("random error", facesMessage.getDetail());
		assertEquals(FacesMessage.SEVERITY_FATAL, facesMessage.getSeverity());
	}

	@Test
	public void testDivide() {

		calcController.setFirstNumber(10);
		calcController.setSecondNumber(10);
		String outcome = calcController.divide();
		Iterator messages = crankMockObjects.getFacesContext().getMessages();
		assertEquals(false, messages.hasNext());
		assertEquals("success", outcome);
		assertEquals(10, calcController.getFirstNumber());
		assertEquals(10, calcController.getSecondNumber());
		assertEquals(1, calcController.getResult());
	}
	

}
