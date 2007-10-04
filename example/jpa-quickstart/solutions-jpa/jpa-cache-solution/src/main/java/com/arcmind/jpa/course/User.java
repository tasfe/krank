package com.arcmind.jpa.course;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.QueryHint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CacheModeType;

@Entity
@NamedQueries( {
		@NamedQuery(name = "getUsers", query = "SELECT u FROM User u ORDER BY u.name ASC", 
					hints={@QueryHint(name="org.hibernate.CacheMode", value="NORMAL")}),
		
})
@org.hibernate.annotations.NamedQueries({
	@org.hibernate.annotations.NamedQuery(
			name = "loadUser", 
			query = "SELECT u FROM User u WHERE u.name=:userName", 
			cacheable=true,
			cacheRegion="User.getUsers",
			cacheMode=CacheModeType.NORMAL
	) 	
})

@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String name;

	@OneToMany(mappedBy = "user", cascade=CascadeType.ALL)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private Set<Phone> phones;

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

	public Set<Phone> getPhones() {
		return phones;
	}

	public void setPhones(Set<Phone> phones) {
		this.phones = phones;
	}
	
	public void addPhone(Phone phone) {
		if (phones == null) {
			phones = new HashSet<Phone>();
		}
		phone.setUser(this);
		phones.add(phone);
	}

}

