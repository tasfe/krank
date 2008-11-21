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
public class Groups implements Serializable {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private Long id;
    private String group_name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }
}
