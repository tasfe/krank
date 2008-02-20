package org.crank.crud.test.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue( "PS_INQ" )
public class PetStoreInquiry extends Inquiry {
	private String aProp;

	public String getAProp() {
		return aProp;
	}

	public void setAProp(String prop) {
		aProp = prop;
	}
}
