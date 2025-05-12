package com.example.Pill_Reminder_App.data.dto;

public class MedAlarmDTO {
    private String id;
    private String time;
    private String medicineName;
    private String amount;
    private String date;
    private String state;
    private String userId;
    private String code;

    // Boş constructor
    public MedAlarmDTO() {}

    // Tüm alanları içeren constructor
    public MedAlarmDTO(String id, String time, String medicineName, String amount, String date, String state, String userId, String code) {
        this.id = id;
        this.time = time;
        this.medicineName = medicineName;
        this.amount = amount;
        this.date = date;
        this.state = state;
        this.userId = userId;
        this.code = code;
    }

    // Getters
    public String getId() { return id; }
    public String getTime() { return time; }
    public String getMedicineName() { return medicineName; }
    public String getAmount() { return amount; }
    public String getDate() { return date; }
    public String getState() { return state; }
    public String getUserId() { return userId; }
    public String getCode() { return code; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setTime(String time) { this.time = time; }
    public void setMedicineName(String medicineName) { this.medicineName = medicineName; }
    public void setAmount(String amount) { this.amount = amount; }
    public void setDate(String date) { this.date = date; }
    public void setState(String state) { this.state = state; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setCode(String code) { this.code = code; }
} 