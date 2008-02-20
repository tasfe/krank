package com.arcmind.jsfquickstart.controller;


import org.crank.annotations.ErrorHandler;
import org.crank.annotations.validation.LongRange;
import org.crank.annotations.validation.Range;
import org.crank.annotations.validation.Required;
//import org.crank.annotations.validation.Required;
//import org.crank.annotations.ErrorHandlers;
import org.crank.metadata.Severity;

import com.arcmind.jsfquickstart.model.Calculator;
import com.arcmind.jsfquickstart.model.CalculatorImpl;


/**
 * Calculator Controller
 *
 * @author $author$
 * @version $Revision$
 */
@ErrorHandler(defaultHandler=true, 
		messageSummary="Some sort of fatal issue see details",
		severity=Severity.FATAL, outcome="fatal")

public class CalculatorController {
    //~ Instance fields --------------------------------------------------------

    /**
     * Represent the model object.
     */
    private Calculator calculator = new CalculatorImpl();

    /** First number used in operation. */
    private int firstNumber = 0;

    /** Result of operation on first number and second number. */
    private int result = 0;

    /** Second number used in operation. */
    private int secondNumber = 0;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new CalculatorController object.
     */
    public CalculatorController() {
        super();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Calculator, this class represent the model.
     *
     * @param aCalculator The calculator to set.
     */
    public void setCalculator(Calculator aCalculator) {
        this.calculator = aCalculator;
    }

    /**
     * First Number property
     *
     * @param aFirstNumber first number
     */
    @Range (max="100", min="1")
    @Required
    public void setFirstNumber(int aFirstNumber) {
        this.firstNumber = aFirstNumber;
    }

    /**
     * First number property
     *
     * @return First number.
     */
    public int getFirstNumber() {
        return firstNumber;
    }

    /**
     * Result of the operation on the first two numbers.
     *
     * @return Second Number.
     */
    public int getResult() {
        return result;
    }

    /**
     * Second number property
     *
     * @param aSecondNumber Second number.
     */
    @LongRange (max=100L, min=1L, 
    		detailMessage="Must be within {1} and {2}",
    		summaryMessage="The field {0} must be within {1} and {2}.")
    @Required (detailMessage="This field is required", 
    		   summaryMessage="Second number is required") 
    public void setSecondNumber(int aSecondNumber) {
        this.secondNumber = aSecondNumber;
    }

    /**
     * Get second number.
     *
     * @return Second number.
     */
    public int getSecondNumber() {
        return secondNumber;
    }

    /**
     * Adds the first number and second number together.
     *
     * @return next logical outcome.
     */
    public String add() {
        
        result = calculator.add(firstNumber, secondNumber);

        return "success";
    }

    /**
     * Multiplies the first number and second number together.
     *
     * @return next logical outcome.
     */
    public String multiply() {

        result = calculator.multiply(firstNumber, secondNumber);
    	
        return "success";
    }

//    @ErrorHandlers (value={
//    		@ErrorHandler(defaultHandler=true, 
//    		     messageSummary="Some sort of fatal issue see details",
//    		     severity=Severity.FATAL, outcome="fatal"),
//    		@ErrorHandler(id="secondNumber",
//    				messageSummary="Problem dividing first number by second number",
//    				exceptionClass=ArithmeticException.class, outcome="mathError")
//    	    }
//    )
	@ErrorHandler(id="calcForm:secondNumber",
				messageSummary="Can't divide first number by second number",
				exceptionClass=ArithmeticException.class, 
				outcome="mathError")
    public String divide() {
		result = calculator.divide(firstNumber, secondNumber);
		return "success";
	}

    public String subtract() {
        result = calculator.subtract(firstNumber, secondNumber);
        return "success";
    }

}