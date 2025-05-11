package com.example.Pill_Reminder_App.data.model;

import java.util.Date;
import java.util.List;

public class Medicine {
    private String name;
    private MedicineForm form;
    private String frequency;
    private Date startDate;
    private List<DoseTime> doseTimes;
    private IntakeTime intakeTime;
    private String code;
    private String doctorId; // nullable

    public Medicine() {}

    public Medicine(String name, MedicineForm form, String frequency, Date startDate, List<DoseTime> doseTimes, IntakeTime intakeTime, String code, String doctorId) {
        this.name = name;
        this.form = form;
        this.frequency = frequency;
        this.startDate = startDate;
        this.doseTimes = doseTimes;
        this.intakeTime = intakeTime;
        this.code = code;
        this.doctorId = doctorId;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public MedicineForm getForm() { return form; }
    public void setForm(MedicineForm form) { this.form = form; }

    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public List<DoseTime> getDoseTimes() { return doseTimes; }
    public void setDoseTimes(List<DoseTime> doseTimes) { this.doseTimes = doseTimes; }

    public IntakeTime getIntakeTime() { return intakeTime; }
    public void setIntakeTime(IntakeTime intakeTime) { this.intakeTime = intakeTime; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }
} 