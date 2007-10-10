package org.crank.security.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Entity;
import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
//import javax.persistence.OrderBy; See NOTE
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;


@NamedQuery(name = "in3.loadGroup", 
		query = "select group from In3Group "
	+ " group where group.name = :name")	
@Table(name="IN3_GROUP")
@Entity(name="In3Group")
@PrimaryKeyJoinColumn(name="GROUP_ID")
@AssociationOverrides (value={
			@AssociationOverride(name="contactInfo",
				joinColumns={@JoinColumn(name="GROUP_CONTACTINFO_ID")})
		}
)
public class Group extends Subject{


	//NOTE @OrderBy("name ASC") We add to remove this b/c the name is in the Subject table not the user table.
	@OneToMany(mappedBy="parentGroup",
		cascade={CascadeType.PERSIST, CascadeType.REMOVE})
	private Set<User> users;

	
	
	
	public void addUser(User user) {
		user.setParentGroup(this);
		getUsers().add(user);		
	}
	
	public void removeUser(User user) {
		user.setParentGroup(null);
		users.remove(user);
	}

	public Set<User> getUsers() {
		if (users == null) {
			users = new HashSet<User>();
		}
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}


	public Group(String name, ContactInfo info) {
		super(name);
	}

	public Group(String name) {
		super(name);
	}

	public Group() {

	}

	
}
