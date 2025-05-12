package com.example.Pill_Reminder_App.data.dto;

public class DoseTimeDTO {
    private String time;
    private double amount;
    private String unit;

    public DoseTimeDTO() {}

    public DoseTimeDTO(String time, double amount, String unit) {
        this.time = time;
        this.amount = amount;
        this.unit = unit;
    }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
} 