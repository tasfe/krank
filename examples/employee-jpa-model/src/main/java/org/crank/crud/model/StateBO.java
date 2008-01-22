package org.crank.crud.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@SuppressWarnings("serial")
@Entity
public class StateBO implements Serializable {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )    
    private Long id;
    
    private String name;
    private String abbreviation;
    public String getName() {
        return name;
    }
    public void setName( String name ) {
        this.name = name;
    }
    public String getAbbreviation() {
        return abbreviation;
    }
    public void setAbbreviation( String abbreviation ) {
        this.abbreviation = abbreviation;
    }
    public Long getId() {
        return id;
    }
    public void setId( Long id ) {
        this.id = id;
    }
}
