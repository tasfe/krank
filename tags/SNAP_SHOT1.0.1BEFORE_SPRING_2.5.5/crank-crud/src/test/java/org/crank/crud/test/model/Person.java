package org.crank.crud.test.model;

import java.io.Serializable;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
@Table(name="PERSON")
@Inheritance (strategy=InheritanceType.JOINED)
public class Person implements Serializable {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )	
	private Long id;

    @Column(length=100)
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
