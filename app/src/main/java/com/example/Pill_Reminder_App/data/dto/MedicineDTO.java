package com.example.Pill_Reminder_App.data.dto;

import java.util.Date;
import java.util.List;

public class MedicineDTO {
    private String name;
    private String doctorId;
    private String form;
    private String frequency;
    private Date startDate;
    private List<DoseTimeDTO> doseTimes;
    private String intakeTime;
    private String code;
    private String userId;

    public MedicineDTO() {}


    public MedicineDTO(String name, String form, String frequency, Date startDate, List<DoseTimeDTO> doseTimes, String intakeTime, String code, String doctorId, String userId) {
        this.name = name;
        this.form = form;
        this.frequency = frequency;
        this.startDate = startDate;
        this.doseTimes = doseTimes;
        this.intakeTime = intakeTime;
        this.code = code;
        this.doctorId = doctorId;
        this.userId = userId;
    }

    public MedicineDTO(String name, String form, String frequency, Date startDate, List<DoseTimeDTO> doseTimes, String intakeTime, String userId) {
        this.name = name;
        this.form = form;
        this.frequency = frequency;
        this.startDate = startDate;
        this.doseTimes = doseTimes;
        this.intakeTime = intakeTime;
        this.userId = userId;

    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }

    public String getForm() { return form; }
    public void setForm(String form) { this.form = form; }

    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public List<DoseTimeDTO> getDoseTimes() { return doseTimes; }
    public void setDoseTimes(List<DoseTimeDTO> doseTimes) { this.doseTimes = doseTimes; }

    public String getIntakeTime() { return intakeTime; }
    public void setIntakeTime(String intakeTime) { this.intakeTime = intakeTime; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
} 