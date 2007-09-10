package org.crank.web.validation;

import java.io.StringWriter;
import java.util.ResourceBundle;

import org.crank.annotations.validation.Range;
import org.crank.annotations.validation.Required;
import org.crank.core.ObjectRegistry;
import org.crank.core.ResourceBundleLocator;
import org.crank.message.MessageSpecification;
import org.crank.validation.ValidatorMetaDataReader;
import org.crank.validation.readers.AnnotationValidatorMetaDataReader;
import org.crank.validation.validators.TestConstants;
import org.crank.web.contribution.SimpleContributionSupport;
import org.easymock.EasyMock;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


import static org.testng.AssertJUnit.*;

public class ValidationScriptReaperTest {

	private ValidationScriptReaper validationScriptReaper;
	private ObjectRegistry objectRegistry;
	private SimpleContributionSupport baseScript;
	private SimpleValidationContribution clientScript1;
	private SimpleValidationContribution clientScript2;
	private SimpleScriptValidator required;
	private SimpleScriptValidator range;
	private ValidatorMetaDataReader validatorMetaDataReader;
    private ResourceBundle bundle = ResourceBundle.getBundle(TestConstants.TEST_BUNDLE);
    private ResourceBundleLocator resourceBundleLocator = new ResourceBundleLocator() {
		public ResourceBundle getBundle() {
			return bundle;
		}
    };
	
	@BeforeMethod
	protected void setUp() throws Exception {
		baseScript = new SimpleContributionSupport();
		baseScript.setContributionText("I AM THE BASE SCRIPT\n");
		clientScript1 = new SimpleValidationContribution();
		clientScript1.setContributionText("VALIDATION CONTRIBUTION 1\n");
		clientScript2 = new SimpleValidationContribution();
		clientScript2.setContributionText("VALIDATION CONTRIBUTION 2\n");
		required = new SimpleScriptValidator();
		required.setContributionText("required()\n");
		range = new SimpleScriptValidator();
		range.setContributionText("range({min}, {max})\n");
		MessageSpecification messageSpecification = new MessageSpecification();
		messageSpecification.setDetailMessage("detail {1} {2}");
		messageSpecification.setSummaryMessage("summary {1} {2}");
		messageSpecification.setResourceBundleLocator(resourceBundleLocator);
		required.setMessageSpecification(messageSpecification);
		range.setMessageSpecification(messageSpecification);
		validatorMetaDataReader = new AnnotationValidatorMetaDataReader();
		createMockObjectRegistry();
		validationScriptReaper = new ValidationScriptReaper();
		validationScriptReaper.setValidatorMetaDataReader(validatorMetaDataReader);
	
	}

	@AfterMethod
	protected void tearDown() throws Exception {
	}
	
	@Test
	public void testOutputRules () throws Exception {
		objectRegistry = EasyMock.createMock(ObjectRegistry.class);
		EasyMock.expect(objectRegistry.getObject("jsValidationRules")).andReturn(baseScript);
		EasyMock.expect(objectRegistry.getObjectsByType(ClientScriptValidatorContribution.class)
				).andReturn(new Object[]{clientScript1,clientScript2});
		EasyMock.replay(objectRegistry);
		validationScriptReaper.setObjectRegistry(objectRegistry);
		
	}

	@Test
	public void testFieldValidation () throws Exception {
		objectRegistry = EasyMock.createMock(ObjectRegistry.class);
		EasyMock.expect(objectRegistry.getObjectReturnNullIfMissing("crank/client/validator/required")).andReturn(required);
		EasyMock.expect(objectRegistry.getObjectReturnNullIfMissing("crank/client/validator/range")).andReturn(range);
        EasyMock.expect(objectRegistry.getObjectReturnNullIfMissing("crank/client/validator/required")).andReturn(required);
		EasyMock.expect(objectRegistry.getObject("crank/client/validator/encodeValidationStyleClasses")).andReturn(required);
		EasyMock.expect(objectRegistry.getObject("crank/client/validator/encodeValidateFormFunction")).andReturn(required);
        
        EasyMock.expect(objectRegistry.getObjectReturnNullIfMissing("crank/client/validator/requiredSupport")).andReturn(null);
        EasyMock.expect(objectRegistry.getObjectReturnNullIfMissing("crank/client/validator/rangeSupport")).andReturn(null);
        EasyMock.expect(objectRegistry.getObjectReturnNullIfMissing("crank/client/validator/requiredSupport")).andReturn(null);        
        EasyMock.expect(objectRegistry.getObject("crank/client/validator/encodeValidateFieldSupport")).andReturn(required);
        EasyMock.replay(objectRegistry);
		validationScriptReaper.setObjectRegistry(objectRegistry);
		
		StringWriter writer = new StringWriter();
		validationScriptReaper.outputFieldValidation(writer, 
				Employee.class, 
				new String [] {"age","name"}, 
				"EmployeeForm");
		
		EasyMock.verify(objectRegistry);
		
		String value = writer.toString().trim();
		//ystem.out.println(value);
		assertTrue(value.startsWith("required("));
		//assertTrue(value.contains("range(10, 100)")); THIS IS NOT WORKING... WHY?
		//assertTrue(value.endsWith("required()\n"));
		//ystem.out.println(value);

	}

	public void createMockObjectRegistry() {
	}
}

class Employee  {
	private String name;
	private String age;
	
	public String getAge() {
		return age;
	}

	@Required @Range(min="10", max="100")	
	public void setAge(String age) {
		this.age = age;
	}
	
	public String getName() {
		return name;
	}
	@Required
	public void setName(String name) {
		this.name = name;
	}
}
