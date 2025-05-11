package com.example.Pill_Reminder_App.data.repository;

import com.example.Pill_Reminder_App.data.model.IntakeTime;
import com.example.Pill_Reminder_App.data.model.Medicine;
import com.example.Pill_Reminder_App.data.model.DoseTime;
import com.example.Pill_Reminder_App.data.dto.MedicineDTO;
import com.example.Pill_Reminder_App.data.dto.DoseTimeDTO;
import com.example.Pill_Reminder_App.data.model.MedicineForm;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MedicineRepository {
    private final FirebaseFirestore db;
    private static final String COLLECTION_NAME = "medicines";

    public MedicineRepository() {
        this.db = FirebaseFirestore.getInstance();
    }

    public void add(String id, Map<String, Object> medicine, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        db.collection(COLLECTION_NAME)
            .document(id)
            .set(medicine)
            .addOnSuccessListener(onSuccess)
            .addOnFailureListener(onFailure);
    }

    public void getAll(OnSuccessListener<List<MedicineDTO>> onSuccess, OnFailureListener onFailure) {
        db.collection(COLLECTION_NAME)
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                List<MedicineDTO> medicines = new ArrayList<>();
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    Medicine medicine = document.toObject(Medicine.class);
                    medicine.setCode(document.getId());
                    medicines.add(toDTO(medicine));
                }
                onSuccess.onSuccess(medicines);
            })
            .addOnFailureListener(onFailure);
    }

    public void getById(String id, OnSuccessListener<MedicineDTO> onSuccess, OnFailureListener onFailure) {
        db.collection(COLLECTION_NAME)
            .document(id)
            .get()
            .addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    Medicine medicine = documentSnapshot.toObject(Medicine.class);
                    if (medicine != null) {
                        medicine.setCode(documentSnapshot.getId());
                        onSuccess.onSuccess(toDTO(medicine));
                    }
                }
            })
            .addOnFailureListener(onFailure);
    }

    public void update(String id, Map<String, Object> medicine, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        db.collection(COLLECTION_NAME)
            .document(id)
            .update(medicine)
            .addOnSuccessListener(onSuccess)
            .addOnFailureListener(onFailure);
    }

    public void delete(String id, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        db.collection(COLLECTION_NAME)
            .document(id)
            .delete()
            .addOnSuccessListener(onSuccess)
            .addOnFailureListener(onFailure);
    }

    public MedicineDTO toDTO(Medicine entity) {
        List<DoseTimeDTO> doseTimeDTOs = new ArrayList<>();
        if (entity.getDoseTimes() != null) {
            for (DoseTime d : entity.getDoseTimes()) {
                doseTimeDTOs.add(new DoseTimeDTO(d.getTime(), d.getAmount(), d.getUnit()));
            }
        }
        return new MedicineDTO(
            entity.getName(),
            entity.getForm() != null ? entity.getForm().name() : null,
            entity.getFrequency(),
            entity.getStartDate(),
            doseTimeDTOs,
            entity.getIntakeTime() != null ? entity.getIntakeTime().name() : null,
            entity.getCode(),
            entity.getDoctorId(),
            entity.getUserId()
        );
    }

    public Medicine fromDTO(MedicineDTO dto) {
        List<DoseTime> doseTimes = new ArrayList<>();
        if (dto.getDoseTimes() != null) {
            for (DoseTimeDTO d : dto.getDoseTimes()) {
                doseTimes.add(new DoseTime(d.getTime(), d.getAmount(), d.getUnit()));
            }
        }
        return new Medicine(
            dto.getName(),
            dto.getForm() != null ? MedicineForm.valueOf(dto.getForm()) : null,
            dto.getFrequency(),
            dto.getStartDate(),
            doseTimes,
            dto.getIntakeTime() != null ? IntakeTime.valueOf(dto.getIntakeTime()) : null,
            dto.getCode(),
            dto.getDoctorId(),
            dto.getUserId()
        );
    }

    public Map<String, Object> toMap(Medicine medicine) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", medicine.getName());
        map.put("form", medicine.getForm() != null ? medicine.getForm().name() : null);
        map.put("frequency", medicine.getFrequency());
        map.put("startDate", medicine.getStartDate());
        map.put("doseTimes", medicine.getDoseTimes());
        map.put("intakeTime", medicine.getIntakeTime() != null ? medicine.getIntakeTime().name() : null);
        map.put("doctorId", medicine.getDoctorId());
        map.put("userId", medicine.getUserId());
        return map;
    }
} 