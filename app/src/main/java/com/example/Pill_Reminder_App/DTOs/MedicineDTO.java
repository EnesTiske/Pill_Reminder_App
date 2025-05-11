package com.example.Pill_Reminder_App.DTOs;

public class MedicineDTO {
    private String name;
    private String doctorId;

    public MedicineDTO() {}

    public MedicineDTO(String name, String doctorId) {
        this.name = name;
        this.doctorId = doctorId;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }
} 