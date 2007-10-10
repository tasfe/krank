package org.crank.controller;

import org.crank.crud.controller.FilterablePageable;
import org.crank.crud.criteria.Comparison;
import org.crank.crud.jsf.support.JsfCrudAdapter;

import javax.faces.context.FacesContext;

/**
 * Created by IntelliJ IDEA.
 * User: Rick
 * Date: Sep 27, 2007
 * Time: 11:58:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class SelectEmployeeListingController {
    private FilterablePageable employeePaginator;

    public SelectEmployeeListingController(FilterablePageable employeePaginator) {
        this.employeePaginator = employeePaginator;
    }

    public String showListingForDepartment () {
        employeePaginator.disableFilters();
        employeePaginator.disableSorts();
        employeePaginator.clearAll();
        String sId = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        employeePaginator.addCriterion(Comparison.eq("department.id",Long.valueOf(sId)));
        employeePaginator.filter();
        return "EMPLOYEES";
    }

    public String showListing() {
        employeePaginator.getCriteria().clear();
        employeePaginator.filter();
        return "EMPLOYEES";
    }
}
