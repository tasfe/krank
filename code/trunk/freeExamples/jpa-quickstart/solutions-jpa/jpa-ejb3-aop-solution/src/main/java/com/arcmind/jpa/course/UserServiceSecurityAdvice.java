package com.arcmind.jpa.course;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;


public class UserServiceSecurityAdvice {
	@Resource EJBContext ctx;
	
	@AroundInvoke
	public Object validateCredentials(InvocationContext invocationContext) throws Exception {
		System.out.println("Validating user authorization for method:" + invocationContext.getMethod().getName());
		if (isUserAuthorized()) {
			return invocationContext.proceed(); 
		} else {
			throw new Exception("User not valid for method!!");
		}
	}
	
	private boolean isUserAuthorized() {
		//Do some work to access the logged in user from the context.
		//User loggedInUser = (User) ctx.lookup("java:comp/env/ejb/loggedInUser");
		return true;
	}
}
