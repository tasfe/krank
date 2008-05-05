package org.crank.validation;

import org.crank.annotations.validation.Required;

public class AddressMock {
	private String line1;
	private String line2;
	private String zip;
	public String getLine1() {
		return line1;
	}
    @Required (summaryMessage="line 1 is required", detailMessage="required")
	public void setLine1(String line1) {
		this.line1 = line1;
	}
	public String getLine2() {
		return line2;
	}
	@Required
	public void setLine2(String line2) {
		this.line2 = line2;
	}
	public String getZip() {
		return zip;
	}
	@Required
	public void setZip(String zip) {
		this.zip = zip;
	}
	

}
