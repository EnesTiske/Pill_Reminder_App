package com.example.Pill_Reminder_App.Repository;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Map;
import java.util.List;

public interface IGenericRepository<DTO> {
    void add(String id, Map<String, Object> data, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure);
    void getAll(OnSuccessListener<List<DTO>> onSuccess, OnFailureListener onFailure);
    void getById(String id, OnSuccessListener<DTO> onSuccess, OnFailureListener onFailure);
    void update(String id, Map<String, Object> data, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure);
    void delete(String id, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure);
} 