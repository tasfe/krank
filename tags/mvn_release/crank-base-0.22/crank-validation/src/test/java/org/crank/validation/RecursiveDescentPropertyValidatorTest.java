package org.crank.validation;



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
import org.springframework.testng.AbstractDependencyInjectionSpringContextTests;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;




public class RecursiveDescentPropertyValidatorTest extends AbstractDependencyInjectionSpringContextTests {
	private RecursiveDescentPropertyValidator validator;
	private Errors errors;
	private EmployeeMock employee;
	
	@BeforeMethod
	public void setup() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("firstName", "");
		map.put("lastName", "");
		map.put("adrress.line1", "");
		map.put("department.name", "");
		map.put("department.address.line1", "");

		CrankWebContext crankWebContext = new CrankWebContext(map, null, null, null);
		crankWebContext.getCookieMap();
		validator = new RecursiveDescentPropertyValidator() {
            protected boolean shouldFieldBeValidated() {
                return true;
            }
        };
		employee = new EmployeeMock();
		employee.setAddress(new AddressMock());
		employee.setDepartment(new DepartmentMock());
		employee.getDepartment().setAddress(new AddressMock());
		employee.getDepartment().getAddress().setLine1("foo bar");
		ObjectRegistry objectRegistry = CrankContext.getObjectRegistry();
		SpringApplicationContextObjectRegistry sacObjectRegistry = (SpringApplicationContextObjectRegistry) objectRegistry;
		sacObjectRegistry.setApplicationContext(this.applicationContext);
		errors = new BindException(employee, "employee");
		//CrankWebContext.clearCrankWebContext();
		
	}
	@Test()
	public void testValidate() {
        employee.setFirstName("BOB");
        validator.validateObject(employee);
		
        //assertEquals(3, errors.getFieldErrors().size());

    }
	@Override
	protected String[] getConfigLocations() {	
		String filename = null;
		try {
			File srcDir = new File(
			"./src/test/resources");
			
			assert srcDir.isDirectory();
			
			File validationPackageDir = new File(srcDir, "./org/crank/validation/validators");
			
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
