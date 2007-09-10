package org.crank.crud.test;


import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.springframework.context.ConfigurableApplicationContext;
import org.testng.annotations.BeforeClass;



/**
 * Base class for working with spring/testng tests.
 * 
 * @author Chris Mathias, cmathias@arcmind.com
 * @version $Revision: 1906 $
 */
public abstract class SpringTestNGBase {
    
    static {
        URL log4j = Thread.currentThread().getContextClassLoader().getResource(
                "log4j.xml");
        DOMConfigurator.configure(log4j);
    }
    
    private static final Map<String, ConfigurableApplicationContext> contexts =
            new HashMap<String, ConfigurableApplicationContext>();
    protected ConfigurableApplicationContext applicationContext;
    protected Logger logger = Logger.getLogger( getClass() );

    @BeforeClass( groups = { "class-init" } )
    public void setUpSpring() {
        /** May not be necessary but since we are dealing with 3+ different platforms.... **/
        applicationContext = SpringTestingUtility.getContext( this, getConfigLocations(), contexts );
        logger.debug( "initialization complete" );
    }

    @SuppressWarnings( "unchecked" )
    public <T> T getBean( Class<T> type, String beanName ) {
        return (T) applicationContext.getBean( beanName );
    }

    public List<String> getConfigLocations() {
        // changed from abstract to template method
        return new ArrayList<String>();
    }
}