package org.crank.core.spring.support;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
//import javax.faces.application.ApplicationFactory;

public class ApplicationFactory extends javax.faces.application.ApplicationFactory {
    private Application application;
    public ApplicationFactory(javax.faces.application.ApplicationFactory appFactory) {
//        javax.faces.application.ApplicationFactory appFactory =
//            (ApplicationFactory) FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);

        Application orgApp = appFactory.getApplication();
        //System.out.println(orgApp.getClass().getName());
        application = new SpringApplication(orgApp);
    
    }
    
    @Override
    public Application getApplication() {
        return application;
    }

    @Override
    public void setApplication( Application application ) {
        this.application = application;

    }

}
