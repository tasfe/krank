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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Inheritance( strategy = InheritanceType.JOINED )
@DiscriminatorColumn( name = "LEAD_TYPE_ID", discriminatorType = DiscriminatorType.STRING )
@Table( name = "BASE_LEAD" )
public class Lead implements Serializable{
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )	
	private Long id;
    
	private String name;
	
    @ManyToOne
    @JoinColumn( name = "INQUIRY_ID" )
	private Inquiry inquiry;
	
	/* ------------------------------ */
	
	public Inquiry getInquiry() {
		return inquiry;
	}
	public void setInquiry(Inquiry inquiry) {
		this.inquiry = inquiry;
	}
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
