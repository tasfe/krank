package org.crank.crud.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;

@SuppressWarnings("serial")
@Entity
public class Specialty implements Serializable {
    
    @Id
    @GeneratedValue( strategy=GenerationType.AUTO )
    private Long id;
    
    private String name;
    
    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

}
