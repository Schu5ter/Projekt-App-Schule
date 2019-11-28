package com.example.stockit;

public class User {

    public String username;
    public String email;
    public String userID;

    public User(){

    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getUserID() {
        return userID;
    }

    public User(String userID,String username) { // email for test purposes not added

        this.username = username;
        this.userID = userID ;
    }
}
