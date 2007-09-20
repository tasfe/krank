package com.arcmind.jpa.course;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class User {
	@Id 
    @GeneratedValue( strategy = GenerationType.AUTO )		
	private Long id;
	    
	private String name;

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
	@Column( name = "created_by", length = 45 )
    private String createdBy;
	
    @Temporal( value = TemporalType.TIMESTAMP )
    @Column( name = "created_date" )
    private Date createdDate;
    
    @Column( name = "updated_by", length = 45 )
    private String updatedBy;
	
    @Temporal( value = TemporalType.TIMESTAMP )
    @Column( name = "updated_date" )
    private Date updatedDate;
    
    public String getCreatedBy() {
		return createdBy;
	}
	
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	public Date getCreatedDate() {
		return createdDate;
	}
	
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
}
