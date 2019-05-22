package com.example.corbi.dartevent;

public class User {
    private String userEmail;
    private String userID;

    public User(String userEmail, String userID) {
        this.userEmail = userEmail;
        this.userID = userID;
    }

    public User() {

    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
