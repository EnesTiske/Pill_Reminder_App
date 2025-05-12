package com.example.Pill_Reminder_App.data.model;

public enum MedicineForm {
    HAP("Hap"),
    ENJEKSIYON_COZELTI("Enjeksiyon Çözelti"),
    DAMLA("Damla"),
    INHALER("İnhaler"),
    TOZ("Toz"),
    DIGER("Diğer");

    private final String displayName;

    MedicineForm(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
} 