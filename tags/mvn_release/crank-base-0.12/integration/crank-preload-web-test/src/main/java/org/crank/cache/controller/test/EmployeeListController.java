package org.crank.cache.controller.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.crank.crud.test.dao.EmployeeDAO;
import org.crank.crud.test.model.Employee;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

public class EmployeeListController extends AbstractController {

    EmployeeDAO employeeDao;
    
    public void setEmployeeDao( EmployeeDAO employeeDao ) {
        this.employeeDao = employeeDao;
    }
    
    @Override
    protected ModelAndView handleRequestInternal( HttpServletRequest request, HttpServletResponse response ) throws Exception {
        List<Employee> employees = employeeDao.find();
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("employees", employees);
        return new ModelAndView("listEmployees", context);
    }

}
