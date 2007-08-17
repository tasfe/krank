package org.crank.crud.cache;

import org.crank.crud.GenericDaoFactory;
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
public class PreloadableCacheableGenericDaoTest extends DbUnitTestBase {

//    private EmployeeDAO preloadingEmployeeDao;
//
//    public void setPreloadingEmployeeDao( EmployeeDAO preloadingEmployeeDao ) {
//        this.preloadingEmployeeDao = preloadingEmployeeDao;
//    }

    @Override
    public String getDataSetXml() {
        return "data/Employee.xml";
    }
    
    //@Test
    public void testPreload() {
        //assert ((GenericDaoFactory)preloadingEmployeeDao).getTargetSource()getCacheConfiguration() != null;
        //Configuratio
        
    }

}
