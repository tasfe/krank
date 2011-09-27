package org.crank.crud.model.inquiry;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


@Entity
@DiscriminatorValue( "PC_LEAD" )
public class PetClinicLead extends Lead{

}
