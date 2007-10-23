package org.crank.validation.validators;

import org.crank.annotations.validation.DomainValidation;
import org.crank.annotations.validation.Required;

public class DomainChild {

	private DomainParent parent;
	private String name;
	private int scaleFactor;

	public void selfValidation(String value) throws Exception {
		if ((value == null) || "".equals(value)) {
			throw new Exception("Child name cannot be null or empty!");
		}
	}
	
	public DomainParent getParent() {
		return parent;
	}

	public void setParent(DomainParent parent) {
		this.parent = parent;
	}

	public int getScaleFactor() {
		return scaleFactor;
	}

	@Required
	@DomainValidation( parentProperty = "parent", method = "validateChildren" )
	public void setScaleFactor(int scaleFactor) {
		this.scaleFactor = scaleFactor;
	}

	public String getName() {
		return name;
	}

	@DomainValidation( method = "selfValidation" )
	public void setName(String name) {
		this.name = name;
	}
	

}
