package org.crank.web.validation.spring.support;

import org.crank.web.validation.ClientScriptValidatorContribution;
import org.crank.web.validation.SimpleJSLibraryContribution;
import org.springframework.core.io.Resource;

public class JavaScriptLibraryContribution extends SimpleJSLibraryContribution implements
        ClientScriptValidatorContribution {

    private Resource resource;

    protected Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    @Override
    protected String getContributionText() {
        if (super.getContributionText() == null ) {
            return ResourceUtils.convertResourceToString(resource);
        } else {
            return super.getContributionText();
        }
    }    
}
