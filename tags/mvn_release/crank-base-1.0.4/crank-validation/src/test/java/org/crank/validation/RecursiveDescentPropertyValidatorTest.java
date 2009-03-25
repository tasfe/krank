package org.crank.validation;



import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


import org.crank.core.CrankContext;
import org.crank.core.ObjectRegistry;
import org.crank.core.spring.support.SpringApplicationContextObjectRegistry;
import org.crank.web.CrankWebContext;
import org.springframework.testng.AbstractDependencyInjectionSpringContextTests;




public class RecursiveDescentPropertyValidatorTest extends AbstractDependencyInjectionSpringContextTests {
	private RecursiveDescentPropertyValidator validator;

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
	}
	@Test()
	public void testValidate() {
        employee.setFirstName("");
        List<RecursiveDescentPropertyValidator.MessageHolder> list = validator.validateObject(employee);
        for (RecursiveDescentPropertyValidator.MessageHolder holder : list) {
            System.out.printf("holder %s \n", holder.propertyPath);
            ValidatorMessageHolder holder2 = holder.holder;
            if (holder2 instanceof ValidatorMessage) {
            	ValidatorMessage message = (ValidatorMessage) holder2;
            	System.out.printf("%s %s", message.getDetail(), message.getSummary());
            }

        }



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
