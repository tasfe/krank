package com.arcmind.jpa.course;

// HINTS
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;

/* TODO Add Entity annotation */
public class User {
	
	/* TODO Add Id Annotation. */
	/* TODO Add GeneratedValue annotation with strategy = GenerationType.AUTO */
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
