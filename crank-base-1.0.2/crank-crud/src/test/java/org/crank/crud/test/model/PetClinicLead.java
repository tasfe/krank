package org.crank.crud.test.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


@Entity
@DiscriminatorValue( "PC_LEAD" )
public class PetClinicLead extends Lead{

}
