package com.arcmind.jpa.course;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries ({
    @NamedQuery (
        name="getUsers", 
        query="SELECT u FROM User u ORDER BY u.name ASC"
    ),
    @NamedQuery (
        name="loadUser", 
        query="SELECT u FROM User u WHERE u.name=:userName"
    )
})
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

}
