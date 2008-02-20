package org.crank.crud.test.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue( "PS_LEAD" )
public class PetStoreLead extends Lead {

}
