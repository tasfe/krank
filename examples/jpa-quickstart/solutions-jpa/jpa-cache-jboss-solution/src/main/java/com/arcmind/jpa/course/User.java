package com.arcmind.jpa.course;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.QueryHint;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CacheModeType;

@Entity
@NamedQueries( {
		@NamedQuery(name = "getUsers", query = "SELECT u FROM User u ORDER BY u.name ASC", 
					hints={@QueryHint(name="org.hibernate.CacheMode", value="PUT")})
		
})
@org.hibernate.annotations.NamedQueries({
	@org.hibernate.annotations.NamedQuery(
			name = "loadUser", 
			query = "SELECT u FROM User u WHERE u.name=:userName", 
			cacheable=true,
			cacheRegion="User.getUsers",
			cacheMode=CacheModeType.GET
	) 	
})

@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String name;

	@OneToMany(mappedBy = "user", fetch=FetchType.EAGER)
	@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL, region="userPhones")
	private Set<Phone> phones;
	
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

