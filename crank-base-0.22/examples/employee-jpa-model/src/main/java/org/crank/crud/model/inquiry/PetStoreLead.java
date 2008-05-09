package org.crank.crud.model.inquiry;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue( "PS_LEAD" )
public class PetStoreLead extends Lead {

}
