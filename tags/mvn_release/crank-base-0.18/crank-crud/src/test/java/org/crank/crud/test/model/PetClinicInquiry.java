package org.crank.crud.test.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue( "PC_INQ" )
public class PetClinicInquiry extends Inquiry {
	private String bb;

	public String getBb() {
		return bb;
	}

	public void setBb(String bb) {
		this.bb = bb;
	}


}
