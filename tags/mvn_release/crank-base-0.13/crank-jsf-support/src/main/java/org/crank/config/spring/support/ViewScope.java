package org.crank.config.spring.support;

import java.util.HashMap;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

public class ViewScope implements Scope {

	public final String VIEW_SCOPE_KEY = "CRANK_VIEW_SCOPE";
	
	public Object get(String name, ObjectFactory objectFactory) {
		
		if (FacesContext.getCurrentInstance().getViewRoot() != null) {
			Map<String, Object> viewScope = extractViewScope();
			
			if (viewScope.get(name) == null) {
				Object object = objectFactory.getObject();
				viewScope.put(name, object);
				return object;
			} else {
				return viewScope.get(name);
			}
		} else {
			return null;
		}
		
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> extractViewScope() {
		Map<String, Object> attributes = FacesContext.getCurrentInstance().getViewRoot().getAttributes();
		
		Map<String, Object> viewScope = null;
		
		if (attributes.get(VIEW_SCOPE_KEY)==null) {
			viewScope = new HashMap<String, Object>();
			attributes.put(VIEW_SCOPE_KEY, viewScope);
		} else {
			viewScope = (Map<String, Object>) attributes.get(VIEW_SCOPE_KEY);
		}
		return viewScope;
	}

	public String getConversationId() {
		return null;
	}

	public void registerDestructionCallback(String name, Runnable callback) {
	}

	public Object remove(String name) {
		if (FacesContext.getCurrentInstance().getViewRoot() != null) {
			Map<String, Object> viewScope = extractViewScope();
			return viewScope.remove(name);
		} else {
			return null;
		}
	}

}
