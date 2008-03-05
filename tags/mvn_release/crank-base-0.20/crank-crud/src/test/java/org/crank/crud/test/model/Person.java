package org.crank.crud.test.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="PERSON")
@Inheritance (strategy=InheritanceType.JOINED)
public class Person implements Serializable {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )	
	private Long id;
    private String ssn;

    public Person() {
    
    }
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	
}
