package com.example.Pill_Reminder_App.domain.service;

import android.app.AlarmManager;
import android.content.Context;

import com.example.Pill_Reminder_App.data.dto.MedAlarmDTO;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;


public class MedAlarmService implements IService<MedAlarmDTO> {
    private Context context;
    private AlarmManager alarmManager;

    public MedAlarmService(Context context) {
        this.context = context;
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    @Override
    public void add(MedAlarmDTO dto, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'add'");
    }

    @Override
    public void getAll(OnSuccessListener<List<MedAlarmDTO>> onSuccess, OnFailureListener onFailure) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAll'");
    }

    @Override
    public void getById(String id, OnSuccessListener<MedAlarmDTO> onSuccess, OnFailureListener onFailure) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getById'");
    }   

    @Override
    public void update(String id, MedAlarmDTO dto, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void delete(String id, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {

    }


} 