package org.crank.message;

import java.util.ResourceBundle;

import org.crank.core.ResourceBundleLocator;

public class ResourceBundleLocatorTest implements ResourceBundleLocator {

    public ResourceBundle getBundle() {
        return ResourceBundle.getBundle( "org/crank/message/resources" );
    }

}
