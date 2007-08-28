package org.crank.crud.cache;

import java.util.ArrayList;
import java.util.List;

import org.crank.crud.test.DbUnitTestBase;
import org.crank.crud.test.dao.EmployeeDAO;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

public class EHCacheServiceTest extends DbUnitTestBase {
   
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
    private EHCacheService ehCacheService;

    public void setEhCacheService( EHCacheService ehCacheService ) {
        this.ehCacheService = ehCacheService;
    }

    public void setPreloadingEmployeeDao( EmployeeDAO preloadingEmployeeDao ) {
        this.preloadingEmployeeDao = preloadingEmployeeDao;
    }
    
}
