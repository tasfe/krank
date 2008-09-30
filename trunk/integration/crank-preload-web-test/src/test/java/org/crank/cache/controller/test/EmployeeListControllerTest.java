package org.crank.cache.controller.test;

import java.util.List;

import org.crank.crud.test.SpringTestNGBase;
import org.crank.crud.test.dao.EmployeeDAO;
import org.crank.crud.test.model.Employee;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.testng.annotations.Test;

public class EmployeeListControllerTest extends SpringTestNGBase {

    EmployeeDAO employeeDao;
    
    public void setEmployeeDao( EmployeeDAO employeeDao ) {
        this.employeeDao = employeeDao;
    }
    
    @SuppressWarnings("unchecked")
	@Test(groups={"broken"})
    public void testEmployeeList() throws Exception {
        EmployeeListController employeeListController = new EmployeeListController();
        employeeListController.setEmployeeDao( employeeDao );
        
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        request.setMethod("POST");

        ModelAndView modelAndView = employeeListController.handleRequest(request, response);
        
        List<Employee> employeeList = (List<Employee>) modelAndView.getModel().get( "employees" );
        
        assert employeeList.size() > 0;
        
    }
    
}
