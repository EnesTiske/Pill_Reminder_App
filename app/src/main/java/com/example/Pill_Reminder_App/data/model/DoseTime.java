package com.example.Pill_Reminder_App.data.model;

public class DoseTime {
    private String time; // "08:00" gibi
    private double amount; // 1, 2, ...
    private String unit; // "Hap", "ml" gibi

    public DoseTime() {}

    public DoseTime(String time, double amount, String unit) {
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