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
public class GroupAuthorities implements Serializable {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private Long id;
    private Long group_id;    
    private String authority;

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGroup_id() {
        return group_id;
    }

    public void setGroup_id(Long group_id) {
        this.group_id = group_id;
    }
}
