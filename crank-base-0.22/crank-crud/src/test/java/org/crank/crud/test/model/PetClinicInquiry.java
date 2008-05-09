package org.crank.crud.test.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue( "PC_INQ" )
public class PetClinicInquiry extends Inquiry {
	private String bb;
	private String anotherProp;

	public String getAnotherProp() {
		return anotherProp;
	}

	public void setAnotherProp(String anotherProp) {
		this.anotherProp = anotherProp;
	}

	public String getBb() {
		return bb;
	}

	public void setBb(String bb) {
		this.bb = bb;
	}


}
