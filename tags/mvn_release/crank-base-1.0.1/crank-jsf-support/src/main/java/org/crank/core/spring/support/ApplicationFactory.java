package org.crank.core.spring.support;


import javax.faces.application.Application;

public class ApplicationFactory extends javax.faces.application.ApplicationFactory {
    private Application application;
    public ApplicationFactory(javax.faces.application.ApplicationFactory appFactory) {
        Application orgApp = appFactory.getApplication();
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
