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
 * Time: 11:19:51 AM
 * Package: org.crank.crud.model
 * Copyright Vantage Media 2008
 */

@SuppressWarnings("serial")
@Entity
public class Users implements Serializable {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private Long id;
    private Long users_id;    
    private String username;
    private String password;
    private String authority;
    //by default all users created are enabled
    private boolean enabled = true;

     public Users () {
     }

    public Users (String userName, String password, String authority) {
        this.username = userName;
        this.authority = authority;
        this.password = password;
    }

    public Users (String userName, String authority, boolean enabled) {
        this.username = userName;
        this.authority = authority;
        this.enabled = enabled;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public Long getUsers_id() {
        return users_id;
    }

    public void setUsers_id(Long users_id) {
        this.users_id = users_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
