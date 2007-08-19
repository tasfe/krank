package com.arcmind.jpa.course;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

@Entity(name="User")
@Table(//catalog="security", schema="adminaccount", 
		name="TBL_USER", 
		uniqueConstraints={
			@UniqueConstraint(columnNames={"txt_name",
					                      "txt_email"}
			)
		}
        )
public class User {
	@Id 
    @GeneratedValue( strategy = GenerationType.AUTO )
    @Column (name="USER_ID", nullable=true)
	private Long id;
	
	@Column(name="txt_name", nullable=false,
			length=10, unique=true, updatable=false, 
			insertable=true)
	private String name;
	
	@Column(name="txt_email", length=80)
	@Basic(optional=false)
	private String email;
	
	@Temporal(TemporalType.TIME)//TemporalType.TIMESTAMP, TemporalType.DATE, TemporalType.TIME
	private Date createDate;
	
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@PrePersist
	public void prepareForStorage() {
		if (createDate==null) {
			createDate = new Date();
		}
		if (email == null) {
			email = name + "@arc-mind.com";
		}
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

}
