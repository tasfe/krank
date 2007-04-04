package org.crank.web.validation.spring.support; 



import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;

import org.crank.core.CrankContext;
import org.crank.core.ObjectRegistry;
import org.crank.core.spring.support.SpringApplicationContextObjectRegistry;
import org.crank.web.CrankWebContext;
import org.crank.web.validation.spring.support.SpringMVCBridgeMetaDataDrivenValidator;
import org.springframework.testng.AbstractDependencyInjectionSpringContextTests;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;




public class SpringMVCBridgeMetaDataDrivenValidatorTest extends AbstractDependencyInjectionSpringContextTests {
	private SpringMVCBridgeMetaDataDrivenValidator validator;
	private Errors errors;
	private EmployeeMock employee;
	
	@BeforeMethod
	public void setup() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("firstName", "");
		map.put("lastName", "");
		CrankWebContext crankWebContext = new CrankWebContext(map, null, null, null);
		crankWebContext.getCookieMap();
		validator = new SpringMVCBridgeMetaDataDrivenValidator();
		employee = new EmployeeMock();
		ObjectRegistry objectRegistry = CrankContext.getObjectRegistry();
		SpringApplicationContextObjectRegistry sacObjectRegistry = (SpringApplicationContextObjectRegistry) objectRegistry;
		sacObjectRegistry.setApplicationContext(this.applicationContext);
		errors = new BindException(employee, "employee");
		//CrankWebContext.clearCrankWebContext();
		
	}
	@Test()
	public void testValidate() {
		validator.validate(employee, errors);
		assertEquals(2, errors.getFieldErrors().size());
		
		List allErrors = errors.getAllErrors();
		
		for (Object oError : allErrors) {
			ObjectError error = (ObjectError) oError;
			System.out.println("The message is here " + error.getDefaultMessage());
		}
	}
	@Override
	protected String[] getConfigLocations() {	
		String filename = null;
		try {
			File srcDir = new File(
			"./src/test/resources");
			
			assert srcDir.isDirectory();
			
			File validationPackageDir = new File(srcDir, "./org/crank/web/validation/spring/support");
			
			assert validationPackageDir.isDirectory();
			
			File file = new File(validationPackageDir, "validatorContext.xml");
			
			assert file.exists();
			
			filename = file.getCanonicalPath();
			assert file !=null;
			
		} catch (IOException ex) {
			throw new RuntimeException("Unable to get file", ex);
		}
		return new String[] { "file:" + filename };
	}

}
