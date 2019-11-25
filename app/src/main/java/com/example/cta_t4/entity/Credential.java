package com.example.cta_t4.entity;

import java.util.Date;

public class Credential {

    private String username;
    private String passwordHash;
    private Date signUpDate;
    private User userId;

    public Credential(String username, String passwordHash, Date signUpDate,User userId) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.signUpDate = signUpDate;
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Date getSignUpDate() {
        return signUpDate;
    }

    public void setSignUpDate(Date signUpDate) {
        this.signUpDate = signUpDate;
    }

    public User getUser() {
        return userId;
    }

    public void setUser(User userId) {
        this.userId = userId;
    }
}
