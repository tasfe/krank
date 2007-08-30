package com.arcmind.jpa.course;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;

import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;


// TODO: Map this class to the TBL_USER table using the @Entity annotation
// TODO: Specify table mapping constraints using the @Table annotation
// HINT: See lecture slides for examples

public class User {

	// TODO: Specify this field as the identity column mapped to the USER_ID field
	private Long id;

	// TODO: Map this field to the txt_name column
	private String name;

	// TODO: Map this required field to the txt_email column 
	private String email;
	
	@Temporal(TemporalType.TIME)//TemporalType.TIMESTAMP, TemporalType.DATE, TemporalType.TIME
	private Date createDate;

	// TODO: Uncomment and map this required field as a value-based enum
	//       Uncomment the getter / setter as well (below)
	//private UserType type;
	
	// TODO: Map this field as an index-based enum
	//       Uncomment the getter / setter as well (below)
	//private EmployeeType employeeType = EmployeeType.NOT_EMPLOYEE;
	
//	public UserType getType() {
//		return type;
//	}

//	public void setType(UserType type) {
//		this.type = type;
//	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	static int sprayer = 0;

	// TODO: Decorate this method with PrePersist lifecycle support
	//       Uncomment the method as well
//	public void prepareForStorage() {
//		if (createDate==null) {
//			createDate = new Date();
//		}
//		if (email == null) {
//			email = name + "@arc-mind.com";
//		}
//		if (type==null) {
//			sprayer++;
//			if (sprayer % 2 == 0) {
//				type = UserType.SILVER; 
//			}else if (sprayer % 3 == 0) {
//				type = UserType.GOLD;
//			}else if (sprayer % 7 == 0) {
//				type = UserType.PREMIERE;				
//			}else if (sprayer % 13 == 0) {
//				type = UserType.PREMIERE_PLUS;				
//			} else {
//				type = UserType.NORMAL;
//			}
//		}
//	}

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

//	public EmployeeType getEmployeeType() {
//		return employeeType;
//	}

//	public void setEmployeeType(EmployeeType employeeType) {
//		this.employeeType = employeeType;
//	}

}
