package org.crank.jsfspring.test;

import org.apache.shale.test.ShaleMockObjects;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.context.ServletConfigAware;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.RequestScope;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.SessionScope;
import org.springframework.web.context.support.ServletContextAwareProcessor;

public class CrankMockObjects extends ShaleMockObjects {

	@Override
	public void setUp() throws Exception {
		super.setUp();
		/* Simulate the Spring environment. */
		LocaleContextHolder.setLocale(request.getLocale());
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

	}

	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		/* Tear down the Spring web environment. */
		ServletRequestAttributes requestAttributes =
			(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		requestAttributes.requestCompleted();
		RequestContextHolder.setRequestAttributes(null);
		LocaleContextHolder.setLocale(null);
		
	}

	public void setUpApplicationContextWithScopes(ConfigurableApplicationContext applicationContext) {
		assert applicationContext!=null;
		ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
		beanFactory.registerScope("request", new RequestScope());
		beanFactory.registerScope("session", new SessionScope(false));
		beanFactory.registerScope("globalSession", new SessionScope(true));

		beanFactory.addBeanPostProcessor(new ServletContextAwareProcessor(getServletContext(), 
				getConfig()));
		beanFactory.ignoreDependencyInterface(ServletContextAware.class);
		beanFactory.ignoreDependencyInterface(ServletConfigAware.class);
		
	}

}
