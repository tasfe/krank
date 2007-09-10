package org.crank.sample;


import org.crank.crud.controller.FilterablePageable;
import org.crank.crud.controller.Pageable;
import org.crank.crud.jsf.support.JsfCrudAdapter;
import org.crank.crud.model.Employee;
import org.crank.crud.GenericDao;


/**
 * @author Rick Hightower
 * 
 */
public class DataTableScrollerBean {
    private FilterablePageable employeeDataPaginator;
    
    
    @SuppressWarnings("unchecked")
    public void populate() {
    	boolean activeToggle = true;
        Employee employee = null;
        for (int index = 0; index < 100; index ++) {
            employee = new Employee();
            employee.setFirstName( "FOO" + index );
            employee.setLastName( "BAR" + index );
            employee.setActive((activeToggle ^= true));
            this.employeeDao.create( employee );
        }
        employeeDataPaginator.reset();
    }
    
    public void setEmployeeDataPaginator( FilterablePageable employeeDataPaginator ) {
        this.employeeDataPaginator = employeeDataPaginator;
    }

    public Pageable getEmployeeDataPaginator() {
        return employeeDataPaginator;
    }

    private GenericDao employeeDao;
    public void setEmployeeDAO( GenericDao employeeDao ) {
        this.employeeDao = employeeDao;
    }


}
