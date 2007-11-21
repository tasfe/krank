package org.crank.test.base;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import java.util.List;
import java.util.Map;

/**
*
*  @version $Revision:$
*  @author Rick Hightower
*/
public class SpringTestingUtility {

    public static ConfigurableApplicationContext getContext( List<String> configs,
            Map<String, ConfigurableApplicationContext> contexts ) {
        return SpringTestingUtility.getContext( null, configs, contexts, false, "" );
    }

    public static ConfigurableApplicationContext getContext( Object contextualizedObject, List<String> configs,
            Map<String, ConfigurableApplicationContext> contexts, boolean isolateConfigs, String moduleName ) {
        if (!isolateConfigs) {
            if (moduleName == null || moduleName.length() == 0) {
                configs.add( "applicationContext.xml" );
            } else {
                configs.add( moduleName + "ApplicationContext.xml" );
            }
        }
        String contextKey = SpringTestingUtility.getContextName( configs );
        String[] configLocations = new String[configs.size()];
        configs.toArray( configLocations );

        ConfigurableApplicationContext applicationContext = contexts.get( contextKey );

        if (applicationContext == null) {
            applicationContext = new ClassPathXmlApplicationContext( configLocations );
            contexts.put( contextKey, applicationContext );
        }

        if (contextualizedObject != null) {
            applicationContext.getBeanFactory().autowireBeanProperties( contextualizedObject,
                    AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false );
        }
        return applicationContext;
    }

    /**
     * Converts a String array to a comma deliminated string. For example, this:
     *
     * String[] values = {"string", "one", "two"};
     *
     * will become:
     *
     * "string, one, two"
     *
     * used to create the coma delimited list of config locations to load by
     * spring
     *
     * @param configLocations
     * @return configLocations converted to a single string either by appending
     *         string array elements or invoking toString on configLocations.
     */
    private static String getContextName( Object configLocations ) {
        if (configLocations instanceof String[]) {
            StringBuilder sb = new StringBuilder();
            int i = 0;
            for (String path : (String[]) configLocations) {
                if (i++ > 0) {
                    sb.append( "," );
                }
                sb.append( path );
            }
            return sb.toString();
        } else {
            return configLocations.toString();
        }
    }

}
