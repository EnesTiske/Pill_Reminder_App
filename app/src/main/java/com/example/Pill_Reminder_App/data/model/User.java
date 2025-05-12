package com.example.Pill_Reminder_App.data.model;

public class User {
    private String id;
    private String userId;
    private String name;
    private String email;
    private String hashedPassword;
    private String userType; // "doctor" veya "patient"

    public User() {}

    public User(String id, String userId, String name, String email, String hashedPassword, String userType) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.userType = userType;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getHashedPassword() { return hashedPassword; }
    public void setHashedPassword(String hashedPassword) { this.hashedPassword = hashedPassword; }

    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }
} 