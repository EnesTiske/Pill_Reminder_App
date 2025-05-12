package com.example.Pill_Reminder_App.data.dto;

import java.util.Date;

public class UserDTO {
    private String id;
    private String name;
    private String email;
    private String password;
    private String userType; // "doctor" veya "patient"
    private Date lastActivity;

    public UserDTO() {
        this.lastActivity = new Date();
    }

    public UserDTO(String id, String name, String email, String password, String userType) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.userType = userType;
        this.lastActivity = new Date();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }

    public Date getLastActivity() { return lastActivity; }
    public void setLastActivity(Date lastActivity) { this.lastActivity = lastActivity; }
} 