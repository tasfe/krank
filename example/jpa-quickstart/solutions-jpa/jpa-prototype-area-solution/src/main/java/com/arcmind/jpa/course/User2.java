package com.arcmind.jpa.course;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SecondaryTable;
import javax.persistence.SecondaryTables;
import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity( name="ComplexUser" )
@Table( name="TBL_USER2" ) 
@SecondaryTables({
    @SecondaryTable(name="TBL_ADDITIONAL_USER_DATA", 
    	pkJoinColumns={
    		@PrimaryKeyJoinColumn(name="ADDITIONAL_USER_DATA_ID", 
    				referencedColumnName="ID")
    	}
    )
})        
public class User2 {
	@Id 
    @GeneratedValue( strategy = GenerationType.AUTO )		
	private Long id;
	
	@Column(table="TBL_ADDITIONAL_USER_DATA")
	private String name;
	
	@Basic(optional=false)
	private String email;
	
	@Basic(fetch=FetchType.LAZY)
	@Column(length=5000000)
	@Lob()
	private byte[] resumeWordDoc;
	
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
	public byte[] getResumeWordDoc() {
		return resumeWordDoc;
	}
	public void setResumeWordDoc(byte[] resumeWordDoc) {
		this.resumeWordDoc = resumeWordDoc;
	}

}
