package org.crank.crud.model.inquiry;

import java.io.Serializable;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Inheritance( strategy = InheritanceType.JOINED )
@DiscriminatorColumn( name = "INQUIRY_TYPE_ID", discriminatorType = DiscriminatorType.STRING )
@Table( name = "BASE_INQUIRY" )
public class Inquiry implements Serializable {
	
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
	private Long id;
    
	private String name;
	
	/* ------------------------------ */
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
