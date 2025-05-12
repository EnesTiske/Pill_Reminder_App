package com.example.Pill_Reminder_App.data.repository;

import com.example.Pill_Reminder_App.data.model.Medicine;
import com.example.Pill_Reminder_App.data.model.DoseTime;
import com.example.Pill_Reminder_App.data.dto.MedicineDTO;
import com.example.Pill_Reminder_App.data.dto.DoseTimeDTO;
import com.example.Pill_Reminder_App.data.model.MedicineForm;
import com.example.Pill_Reminder_App.data.model.IntakeTime;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MedicineRepository {
    private final FirebaseFirestore db;
    private static final String COLLECTION_NAME = "medicines";

    public MedicineRepository() {
        this.db = FirebaseFirestore.getInstance();
    }

    public void add(String id, Map<String, Object> data, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        db.collection(COLLECTION_NAME).document(id).set(data)
            .addOnSuccessListener(onSuccess)
            .addOnFailureListener(onFailure);
    }

    public void getAll(OnSuccessListener<List<MedicineDTO>> onSuccess, OnFailureListener onFailure) {
        db.collection(COLLECTION_NAME).get()
            .addOnSuccessListener(querySnapshot -> {
                List<MedicineDTO> medicines = new ArrayList<>();
                for (var doc : querySnapshot.getDocuments()) {
                    MedicineDTO medicine = fromMap(doc.getId(), doc.getData());
                    medicines.add(medicine);
                }
                onSuccess.onSuccess(medicines);
            })
            .addOnFailureListener(onFailure);
    }

    public void getById(String id, OnSuccessListener<MedicineDTO> onSuccess, OnFailureListener onFailure) {
        db.collection(COLLECTION_NAME).document(id).get()
            .addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    MedicineDTO medicine = fromMap(documentSnapshot.getId(), documentSnapshot.getData());
                    onSuccess.onSuccess(medicine);
                } else {
                    onFailure.onFailure(new Exception("Medicine not found"));
                }
            })
            .addOnFailureListener(onFailure);
    }

    public void update(String id, Map<String, Object> data, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        db.collection(COLLECTION_NAME).document(id).update(data)
            .addOnSuccessListener(onSuccess)
            .addOnFailureListener(onFailure);
    }

    public void delete(String id, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        db.collection(COLLECTION_NAME).document(id).delete()
            .addOnSuccessListener(onSuccess)
            .addOnFailureListener(onFailure);
    }

    public List<MedicineDTO> getMedicinesByDoctorId(String doctorId) throws Exception {
        try {
            QuerySnapshot snapshot = Tasks.await(db.collection(COLLECTION_NAME)
                .whereEqualTo("doctorId", doctorId)
                .get());

            List<MedicineDTO> medicines = new ArrayList<>();
            for (var doc : snapshot.getDocuments()) {
                try {
                    MedicineDTO medicine = fromMap(doc.getId(), doc.getData());
                    medicines.add(medicine);
                } catch (Exception e) {
                    // Hatalı ilaç verisini atla
                    continue;
                }
            }
            return medicines;
        } catch (ExecutionException | InterruptedException e) {
            throw new Exception("İlaç listesi yüklenemedi: " + e.getMessage());
        }
    }

    private MedicineDTO fromMap(String id, Map<String, Object> map) {
        List<DoseTimeDTO> doseTimes = new ArrayList<>();
        if (map.get("doseTimes") != null) {
            List<Map<String, Object>> doseList = (List<Map<String, Object>>) map.get("doseTimes");
            for (Map<String, Object> d : doseList) {
                doseTimes.add(new DoseTimeDTO(
                    (String) d.get("time"),
                    (Double) d.get("amount"),
                    (String) d.get("unit")
                ));
            }
        }

        return new MedicineDTO(
            id,
            (String) map.get("name"),
            (String) map.get("form"),
            (String) map.get("frequency"),
            (java.util.Date) map.get("startDate"),
            doseTimes,
            (String) map.get("intakeTime"),
            (String) map.get("code"),
            (String) map.get("doctorId"),
            (String) map.get("userId")
        );
    }
} 