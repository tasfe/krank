package org.crank.web.validation;

import org.crank.web.contribution.TemplateContribution;

/**
 * Classes that implement this class represent validation 
 * contributions that are templates.
 * 
 * @author Rick Hightower
 *
 */
public interface ValidatorTemplateContribution extends TemplateContribution{
	
	public void placeValidatorContext(ValidatorContext validatorContext);

}
