package org.crank.crud.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.crank.annotations.validation.Length;
import org.crank.annotations.validation.ProperNoun;
import org.crank.annotations.validation.Required;
//import org.crank.jsf.annotations.ToolTipFromNameSpace;

@SuppressWarnings("serial")
@MappedSuperclass
public class Person implements Serializable {
	//@ToolTipFromNameSpace
    @Column( nullable = false, length = 32 )
    private String firstName;
    private String lastName;
    @Column( length = 150 )
    private String description;

    public String getFirstName() {
        return firstName;
    }

    @Required
    @ProperNoun
    public void setFirstName( String firstName ) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Required
    @ProperNoun
    @Length( min = 2, max = 35 )
    public void setLastName( String lastName ) {
        this.lastName = lastName;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription( String description ) {
        this.description = description;
    }
    
}
