package org.crank.web.validation.spring.support;


import org.crank.annotations.design.NeedsRefactoring;
import org.crank.web.validation.SimpleScriptValidator;
import org.springframework.core.io.Resource;

/** This allows us to configure the contribution as 
 *  text or as a resource (classpath resource, 
 *  file, url, web resource).
 *  
 * @author Rick Hightower
 *
 */
@NeedsRefactoring("Needs testing... ")
public class ScriptValidator extends SimpleScriptValidator {
    
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
