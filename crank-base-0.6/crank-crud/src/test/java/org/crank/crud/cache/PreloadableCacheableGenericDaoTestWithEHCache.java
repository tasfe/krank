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
        configs.add( "org/crank/crud/cache/spring/applicationContext.xml" );
        return configs;
    }

    @Override
    public String getDataSetXml() {
        return "data/Employee.xml";
    }

    private EmployeeDAO preloadingEmployeeDao;

    public void setPreloadingEmployeeDao( EmployeeDAO preloadingEmployeeDao ) {
        this.preloadingEmployeeDao = preloadingEmployeeDao;
    }
    
    @Test
    public void testPreload() {
        //Validation of this in
        Employee employee = preloadingEmployeeDao.read( 1l );
        employee.setNumberOfPromotions( 5 );
        preloadingEmployeeDao.update( employee );
        
        assert preloadingEmployeeDao.read( 1l ) != null;
        
    }

}

