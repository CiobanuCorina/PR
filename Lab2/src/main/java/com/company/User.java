package com.company;

public class User {
    private String emailAddress;
    private char[] password;

    public User(String emailAddress, char[] password) {
        this.emailAddress = emailAddress;
        this.password = password;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public char[] getPassword() {
        return password;
    }
}
