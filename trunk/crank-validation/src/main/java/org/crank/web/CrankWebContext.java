package org.crank.web;

import java.util.Map;

@SuppressWarnings("unchecked")
public class CrankWebContext {
	private Map sessionScope;
	private Map requestScope;
	private Map requestParameters;
	private Map cookieMap;
	private static ThreadLocal<CrankWebContext> threadLocal = new ThreadLocal<CrankWebContext>();
	
	public CrankWebContext(final Map aRequestParameters, final Map aRequestScope,
			               final Map aSessionScope, final Map aCookieMap) {
		this.sessionScope = aSessionScope;
		this.requestParameters = aRequestParameters;
		this.requestScope = aRequestScope;
		this.cookieMap = aCookieMap;
		threadLocal.set(this);
	}
	
	public static void clearCrankWebContext() {
		threadLocal.set(null);
	}
	
	public static CrankWebContext getInstance() {
		return threadLocal.get();
	}
	
	public Map getCookieMap() {
		return cookieMap;
	}
	public Map getRequestParameters() {
		return requestParameters;
	}
	public Map getRequestScope() {
		return requestScope;
	}
	public Map getSessionScope() {
		return sessionScope;
	}
}
