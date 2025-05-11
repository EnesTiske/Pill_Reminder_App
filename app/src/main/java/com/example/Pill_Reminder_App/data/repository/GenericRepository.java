package com.example.Pill_Reminder_App.data.repository;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class GenericRepository<T, DTO> implements IGenericRepository<DTO> {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String collectionName;
    private final Class<T> clazz;

    public GenericRepository(String collectionName, Class<T> clazz) {
        this.collectionName = collectionName;
        this.clazz = clazz;
    }

    protected abstract DTO toDTO(T entity);

    @Override
    public void add(String id, Map<String, Object> data, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        db.collection(collectionName).document(id).set(data)
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }

    @Override
    public void getAll(OnSuccessListener<List<DTO>> onSuccess, OnFailureListener onFailure) {
        db.collection(collectionName).get()
                .addOnSuccessListener(querySnapshot -> {
                    List<DTO> list = new ArrayList<>();
                    for (DocumentSnapshot doc : querySnapshot) {
                        T obj = doc.toObject(clazz);
                        if (obj != null) {
                            list.add(toDTO(obj));
                        }
                    }
                    onSuccess.onSuccess(list);
                })
                .addOnFailureListener(onFailure);
    }

    @Override
    public void getById(String id, OnSuccessListener<DTO> onSuccess, OnFailureListener onFailure) {
        db.collection(collectionName).document(id).get()
                .addOnSuccessListener(documentSnapshot -> {
                    T obj = documentSnapshot.toObject(clazz);
                    onSuccess.onSuccess(obj != null ? toDTO(obj) : null);
                })
                .addOnFailureListener(onFailure);
    }

    @Override
    public void update(String id, Map<String, Object> data, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        db.collection(collectionName).document(id).update(data)
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }

    @Override
    public void delete(String id, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        db.collection(collectionName).document(id).delete()
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }
} 