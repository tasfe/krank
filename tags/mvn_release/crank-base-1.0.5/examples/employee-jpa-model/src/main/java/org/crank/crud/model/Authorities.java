package org.crank.crud.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: kregester
 * Date: Sep 26, 2008
 * Time: 11:22:03 AM
 * Package: org.crank.crud.model
 * Copyright Vantage Media 2008
 */
@SuppressWarnings("serial")
@Entity
public class Authorities implements Serializable {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )    
    private Long id;
    private String username;
    private String authority;

    public Authorities() {
    }

    public Authorities(String username, String authority) {
        this.username = username;
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
