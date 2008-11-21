package org.crank.crud.jsf.support;

import java.io.IOException;

import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;

import com.sun.facelets.FaceletContext;
import com.sun.facelets.FaceletException;
import com.sun.facelets.tag.TagAttribute;
import com.sun.facelets.tag.TagConfig;
import com.sun.facelets.tag.TagHandler;

public class IfDefHandler extends TagHandler {
	private static final String ATTR_NAME_TARGET_VAR = "tovar";
	private static final String ATTR_NAME_TEST_CONDITION = "test";
	private static final String ATTR_NAME_NOT_DEFINED = "notdef";
	private TagConfig config;
	public IfDefHandler(TagConfig config) {
		super(config);
		this.config = config;
	}

	public void apply(FaceletContext faceletContext, UIComponent uiComponent) throws IOException, FacesException,
			FaceletException, ELException {
		TagAttribute testNameAttribute = getRequiredAttribute(ATTR_NAME_TEST_CONDITION);
		String testName = (String)testNameAttribute.getObject(faceletContext);
		Object testValue = faceletContext.getAttribute(testName);
		
		if (testValue != null) {			
			setTargetValue(faceletContext, testValue);		
			config.getNextHandler().apply(faceletContext, uiComponent);
		}
		else {
			TagAttribute defaultExpression = getAttribute(ATTR_NAME_NOT_DEFINED);
			if (defaultExpression != null) {
				testValue = defaultExpression.getObject(faceletContext);
				setTargetValue(faceletContext, testValue);		
			}
		}
	}

	private void setTargetValue(FaceletContext faceletContext, Object targetValue) {
		TagAttribute tovar = getAttribute(ATTR_NAME_TARGET_VAR);
		if (tovar != null) {
			String varName = (String)tovar.getObject(faceletContext);
			faceletContext.setAttribute(varName, targetValue);
		}
	}

}
