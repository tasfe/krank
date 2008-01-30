package org.crank.crud.model.inquiry;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue( "PC_INQ" )
public class PetClinicInquiry extends Inquiry {
	private String anotherProp;
	private String bProp;

	public String getBProp() {
		return bProp;
	}

	public void setBProp(String prop) {
		bProp = prop;
	}

	public String getAnotherProp() {
		return anotherProp;
	}

	public void setAnotherProp(String anotherProp) {
		this.anotherProp = anotherProp;
	}

}
