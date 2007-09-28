package org.crank.controller;

import org.crank.crud.GenericDao;
import org.crank.crud.criteria.Comparison;
import org.crank.crud.jsf.support.JsfCrudAdapter;
import org.crank.crud.model.Department;

import javax.faces.context.FacesContext;

/**
 * Created by IntelliJ IDEA.
 * User: Rick
 * Date: Sep 27, 2007
 * Time: 11:58:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class SelectEmployeeListingController {
    private JsfCrudAdapter employeeCrud;
    private GenericDao<Department, Long> departmentDao;

    public SelectEmployeeListingController(GenericDao<Department, Long> departmentDao, JsfCrudAdapter employeeCrud) {
        this.employeeCrud = employeeCrud;
        this.departmentDao = departmentDao;
    }

    public String process () {
        String sId = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        Department department = departmentDao.read(Long.valueOf(sId));
        employeeCrud.getPaginator().addCriterion(Comparison.eq("department",department));
        employeeCrud.getPaginator().filter();
        return "EMPLOYEES";
    }

    public String showListing() {
        employeeCrud.getPaginator().getCriteria().clear();
        employeeCrud.getPaginator().filter();        
        return "EMPLOYEES";
    }
}
