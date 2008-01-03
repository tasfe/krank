package org.crank.core.spring.support;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;


/**
 * Swaps out the regular JSF Application object with
 * the Spring enhanced version of the application object.
 *
 * @author Rick Hightower
 */
public class ChangeApplicationContextServlet extends HttpServlet {
    /**
      */
    private static final long serialVersionUID = 1L;

    /**
    * This actually does the swapping.
    *
    * @throws ServletException Problems.
    */
    public void init() throws ServletException {
        super.init();

        /* Get the application factory, and swap the Application that is in it
         * with the one Spring enhanced Application.
         */
        ApplicationFactory appFactory =
            (ApplicationFactory) FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);

        Application application = appFactory.getApplication();

        /* Nest the original in the enhanced version.*/
        SpringApplication springApplication = new SpringApplication(application);

        /* Stuff the enhanced version back into the factory. */
        appFactory.setApplication(springApplication);
    }
}
