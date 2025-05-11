package com.example.Pill_Reminder_App.domain.service;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import java.util.List;

public interface IService<DTO> {
    void add(DTO dto, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure);
    void getAll(OnSuccessListener<List<DTO>> onSuccess, OnFailureListener onFailure);
    void getById(String id, OnSuccessListener<DTO> onSuccess, OnFailureListener onFailure);
    void update(String id, DTO dto, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure);
    void delete(String id, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure);
} 