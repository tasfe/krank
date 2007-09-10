package com.arcmind.jsfquickstart.controller;

import java.io.Serializable;

import com.arcmind.jsfquickstart.model.User;

public class UserRegistrationController implements Serializable {
    private User user = new User();
    private String userStatus="NONE YET";
    
    public UserRegistrationController () {
        System.out.println("UserRegistrationController.............. ");
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public void registerUser() {
        System.out.println(user);
        
        userStatus = "added user: " + user.toString();
    }
    

}
