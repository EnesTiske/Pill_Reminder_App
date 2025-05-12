package com.example.Pill_Reminder_App.data.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.Pill_Reminder_App.data.dto.MedAlarmDTO;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MedAlarmRepository {
    private final FirebaseFirestore db;
    private static final String COLLECTION_NAME = "medAlarm";

    public MedAlarmRepository() {
        this.db = FirebaseFirestore.getInstance();
    }

    public void add(MedAlarmDTO medAlarm, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        Map<String, Object> data = toMap(medAlarm);
        db.collection(COLLECTION_NAME)
                .add(data)
                .addOnSuccessListener(documentReference -> onSuccess.onSuccess(null))
                .addOnFailureListener(onFailure);
    }

    public void getAll(OnSuccessListener<List<MedAlarmDTO>> onSuccess, OnFailureListener onFailure) {
        db.collection(COLLECTION_NAME)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<MedAlarmDTO> alarms = new ArrayList<>();
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        MedAlarmDTO alarm = fromMap(doc.getId(), doc.getData());
                        alarms.add(alarm);
                    }
                    onSuccess.onSuccess(alarms);
                })
                .addOnFailureListener(onFailure);
    }

    public void updateState(String id, String state, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        db.collection(COLLECTION_NAME)
                .document(id)
                .update("state", state)
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }

    // Map'e çevirme fonksiyonu
    private Map<String, Object> toMap(MedAlarmDTO alarm) {
        Map<String, Object> map = new HashMap<>();
        map.put("time", alarm.getTime());
        map.put("medicineName", alarm.getMedicineName());
        map.put("amount", alarm.getAmount());
        map.put("date", alarm.getDate());
        map.put("state", alarm.getState());
        map.put("userId", alarm.getUserId());
        map.put("code", alarm.getCode());
        return map;
    }

    // Map'ten DTO'ya çevirme fonksiyonu
    private MedAlarmDTO fromMap(String id, Map<String, Object> map) {
        return new MedAlarmDTO(
                id,
                (String) map.get("time"),
                (String) map.get("medicineName"),
                (String) map.get("amount"),
                (String) map.get("date"),
                (String) map.get("state"),
                (String) map.get("userId"),
                (String) map.get("code")
        );
    }
} 