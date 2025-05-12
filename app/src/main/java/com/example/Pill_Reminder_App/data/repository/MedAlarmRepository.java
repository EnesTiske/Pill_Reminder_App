package com.example.Pill_Reminder_App.data.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.Pill_Reminder_App.data.dto.MedAlarmDTO;

import java.util.ArrayList;
import java.util.List;

public class MedAlarmRepository {
    private List<MedAlarmDTO> medAlarms;

    public MedAlarmRepository(Context context) {
        this.medAlarms = new ArrayList<>();
    }


    public void insert(MedAlarmDTO medAlarm) {

        ContentValues values = new ContentValues();
        values.put("time", medAlarm.getTime());
        values.put("medicine_name", medAlarm.getMedicineName());
        values.put("amount", medAlarm.getAmount());
        values.put("date", medAlarm.getDate());
        values.put("state", medAlarm.getState());
        values.put("user_id", medAlarm.getUserId());

        medAlarms.add(medAlarm);
    }
} 