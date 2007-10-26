package org.crank.controller;

import javax.faces.context.FacesContext;

import org.crank.crud.controller.FilterablePageable;
import org.crank.crud.criteria.Comparison;

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
        employeePaginator.clearAll();
        employeePaginator.getCriteria().clear();
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
