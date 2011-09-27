package org.crank.core.spring.support;

import org.crank.core.NameAware;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.*;
public class SpringSupportTest {
	@Test
	public void testNameAwareInjectorBeanPostProcessor() {
		NameAwareNameInjectorBeanPostProcessor nan = new NameAwareNameInjectorBeanPostProcessor();
		NameAware testBean = new NameAware(){
			private String name;
		
			public void setName(String name) {
				this.name = name;
			}
		
			public void init() {
			}
		
			public String getName() {
				return name;
			}
		};
		nan.postProcessBeforeInitialization(testBean, "test");
		nan.postProcessAfterInitialization(testBean, "test");
		assertEquals("test", testBean.getName());
	}
	
	@Test
	public void testSpringApplicationContextObjectRegistry() {
	//	SpringApplicationContextObjectRegistry sacor = new SpringApplicationContextObjectRegistry();
	//	sacor.convertObject(value, clazz);
	}
}
