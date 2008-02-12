package org.crank.security.model;

import javax.persistence.AssociationOverrides;
import javax.persistence.AssociationOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.crank.annotations.validation.Required;

@SuppressWarnings("serial")
@Entity(name="SecurityUser")
@PrimaryKeyJoinColumn(name="USER_ID")
@Table(name="SECURITY_USER")
@AssociationOverrides (value={
			@AssociationOverride(name="contactInfo",
				joinColumns={@JoinColumn(name="USER_CONTACTINFO_ID")})
		}
)
@NamedQueries( {
	@NamedQuery( name = "SecurityUser.readPopulated",
			query = "select distinct suser from SecurityUser suser " +
			"left outer join fetch suser.roles " +
			"where suser.id=?" )} )
public class User extends Subject {
	@Column (name="USER_EMAIL", length=79)
	private String email;
	

	@ManyToOne ()
	@JoinColumn(name="FK_GROUP_ID")	
	private Group parentGroup;



	public User(String name, ContactInfo contactInfo) {
		super(name, contactInfo);
	}


	public User(String name) {
		super(name);
	}

	public User() {
	}


	public Group getParentGroup() {
		return parentGroup;
	}

	public void setParentGroup(Group parent) {
		this.parentGroup = parent;
	}


	public String getEmail() {
		return email;
	}


	@Required
	public void setEmail(String email) {
		this.email = email;
	}
}
