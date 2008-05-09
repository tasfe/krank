package org.crank.crud.jsf.support;

import java.io.IOException;
import java.net.URL;

import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;

import com.sun.facelets.FaceletContext;
import com.sun.facelets.FaceletException;
import com.sun.facelets.tag.TagConfig;
import com.sun.facelets.tag.TagHandler;

public class WebIncludeHandler extends TagHandler {
	public WebIncludeHandler(TagConfig config) {
		super(config);
	}

	public void apply(FaceletContext faceletContext, UIComponent uiComponent) throws IOException, FacesException,
			FaceletException, ELException {
		String resourceName = (String)super.getRequiredAttribute("resource").getObject(faceletContext);
		ExternalContext externalContext = faceletContext.getFacesContext().getExternalContext();
		URL resourceURL = externalContext.getResource(resourceName);
		if (resourceURL != null) {
			faceletContext.includeFacelet(uiComponent, resourceURL);
		}
		else {
			
		}
	}

}
