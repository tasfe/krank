package org.crank.security.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Table(name="CONTACT_INFO")
@Entity()
public class ContactInfo implements Serializable {
	@Id
	@GeneratedValue
	private Long id;

	@OneToMany(cascade={CascadeType.PERSIST, CascadeType.REMOVE})
	@JoinColumn(name="FK_CONTACT_INFO")
	@MapKey(name="name")
	private Map<String, PhoneNumber> phoneNumbers; 

	
	

	public void addPhoneNumber(PhoneNumber phoneNumber){
		if (phoneNumbers == null) {
			phoneNumbers = new HashMap<String, PhoneNumber>();
		}
		phoneNumbers.put(phoneNumber.getName(), phoneNumber);
	}
	
	

	
	public Map<String, PhoneNumber> getPhoneNumbers() {
		return phoneNumbers;
	}

	public void setPhoneNumbers(Map<String, PhoneNumber> phones) {
		this.phoneNumbers = phones;
	}

	
	
	
	private Address address;

	@Embedded
	@AttributeOverrides(value = {
			@AttributeOverride(name = "line1", 
					column = @Column(name = "work_Address_Line1")),
			@AttributeOverride(name = "line2", 
					column = @Column(name = "work_Address_Line2")),
			@AttributeOverride(name = "zip", 
					column = @Column(name = "work_Address_Zip")),
			@AttributeOverride(name = "state", 
					column = @Column(name = "work_Address_State")) })
	private Address workAddress;


	private String firstName;
	private String lastName;

	public ContactInfo(String phone, String firstName, String lastName,
			Address address, Address workAddress) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.workAddress = workAddress;
	}


	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public ContactInfo() {

	}

	public Address getWorkAddress() {
		return workAddress;
	}

	public void setWorkAddress(Address workAddress) {
		this.workAddress = workAddress;
	}

}
