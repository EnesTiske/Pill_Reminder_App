package com.example.Pill_Reminder_App.data.model;
 
public enum IntakeTime {
    // YEMEK_ONCE,
    // YEMEK_SIRASINRDA,
    // YEMEK_SONRA,
    // FARKETMEZ

    YEMEK_ONCE("Yemekten önce"),
    YEMEK_SONRA("Yemekten sonra"),
    YEMEK_SIRASINRDA("Yemekle birlikte"),
    FARKETMEZ("Herhangi bir zamanda");

    private final String displayName;

    IntakeTime(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
} 