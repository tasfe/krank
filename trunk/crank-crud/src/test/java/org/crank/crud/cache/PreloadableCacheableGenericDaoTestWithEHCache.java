package org.crank.crud.cache;

import java.util.ArrayList;
import java.util.List;

import org.crank.crud.test.DbUnitTestBase;
import org.crank.crud.test.dao.EmployeeDAO;
import org.crank.crud.test.model.Employee;
import org.testng.annotations.Test;

/**
 * Created by IntelliJ IDEA.
 * User: chris_java
 * Date: Aug 16, 2007
 * Time: 3:23:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class PreloadableCacheableGenericDaoTestWithEHCache extends DbUnitTestBase {

    @Override
    protected boolean isSetupJndiContext() {
        return true;
    }
    
    @Override
    protected boolean isIsolateConfigs() {
        return true;
    }

    @Override
    public List<String> getConfigLocations() {
        List<String> configs = new ArrayList<String>();
        configs.add( "spring/applicationContext.xml" );
        return configs;
    }

    @Override
    public String getDataSetXml() {
        return "data/Employee.xml";
    }
    
    @Test(groups="broken")
    public void testPreloadWithCount() {
        Employee employee = preloadingEmployeeDao.read( 1l );
        employee.setNumberOfPromotions( 5 );
        preloadingEmployeeDao.merge( employee );
        assert preloadingEmployeeDao.read( 1l ) != null;
    }
    
    @Test(groups="broken")
    public void testPreloadWithHQL() {
        //Validation of this in
        Employee employee = preloadingWithHQLEmployeeDao.read( 1l );
        assert employee != null;
        assert employee.getDepartment() != null;
    }
    
    private EmployeeDAO preloadingEmployeeDao;
    private EmployeeDAO preloadingWithHQLEmployeeDao;

    public void setPreloadingEmployeeDao( EmployeeDAO preloadingEmployeeDao ) {
        this.preloadingEmployeeDao = preloadingEmployeeDao;
    }

    public void setPreloadingWithHQLEmployeeDao( EmployeeDAO preloadingWithHQLEmployeeDao ) {
        this.preloadingWithHQLEmployeeDao = preloadingWithHQLEmployeeDao;
    }

}

