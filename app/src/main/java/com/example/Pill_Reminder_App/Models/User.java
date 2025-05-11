package com.example.Pill_Reminder_App.Models;

public class User {
    private String name;
    private String email;
    private String hashedPassword;
    private String userType; // "doctor" veya "patient"

    public User() {}

    public User(String name, String email, String hashedPassword, String userType) {
        this.name = name;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.userType = userType;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getHashedPassword() { return hashedPassword; }
    public void setHashedPassword(String hashedPassword) { this.hashedPassword = hashedPassword; }

    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }
} 