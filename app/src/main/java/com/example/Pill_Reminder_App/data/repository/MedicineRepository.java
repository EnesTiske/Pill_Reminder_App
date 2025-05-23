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
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.Date;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;

public class MedicineRepository {
    private final FirebaseFirestore db;
    private final CollectionReference medicinesCollection;
    private final DatabaseReference medicinesRef;
    private static final String COLLECTION_NAME = "medicines";

    public MedicineRepository() {
        this.db = FirebaseFirestore.getInstance();
        medicinesCollection = db.collection(COLLECTION_NAME);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        medicinesRef = database.getReference("medicines");
    }

    public void add(Medicine medicine, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        Map<String, Object> medicineMap = toMap(medicine);
        medicineMap.put("createdAt", new Date());

        db.collection(COLLECTION_NAME)
            .add(medicineMap)
            .addOnSuccessListener(documentReference -> onSuccess.onSuccess(null))
            .addOnFailureListener(onFailure);
    }

    public void update(String id, Medicine medicine, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        Map<String, Object> medicineMap = toMap(medicine);
        // createdAt alanını güncelleme, çünkü bu alan sadece oluşturulduğunda set edilmeli

        db.collection(COLLECTION_NAME)
            .document(id)
            .update(medicineMap)
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

    public void getById(String id, OnSuccessListener<Medicine> onSuccess, OnFailureListener onFailure) {
        db.collection(COLLECTION_NAME)
            .document(id)
            .get()
            .addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    Map<String, Object> data = documentSnapshot.getData();
                    if (data != null) {
                        Medicine medicine = fromMapToEntity(documentSnapshot.getId(), data);
                        onSuccess.onSuccess(medicine);
                    } else {
                        onFailure.onFailure(new Exception("Medicine data is null"));
                    }
                } else {
                    onFailure.onFailure(new Exception("Medicine not found"));
                }
            })
            .addOnFailureListener(onFailure);
    }

    public void getAll(OnSuccessListener<List<MedicineDTO>> onSuccess, OnFailureListener onFailure) {
        db.collection(COLLECTION_NAME)
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                List<MedicineDTO> medicines = new ArrayList<>();
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    Map<String, Object> data = document.getData();
                    MedicineDTO medicine = fromMap(document.getId(), data);
                    medicines.add(medicine);
                }
                onSuccess.onSuccess(medicines);
            })
            .addOnFailureListener(onFailure);
    }

    public void getByUserId(String userId, OnSuccessListener<List<MedicineDTO>> onSuccess, OnFailureListener onFailure) {
        db.collection(COLLECTION_NAME)
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                List<MedicineDTO> medicines = new ArrayList<>();
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    Map<String, Object> data = document.getData();
                    MedicineDTO medicine = fromMap(document.getId(), data);
                    medicines.add(medicine);
                }
                onSuccess.onSuccess(medicines);
            })
            .addOnFailureListener(onFailure);
    }

    public List<MedicineDTO> getMedicinesByDoctorId(String doctorId) throws Exception {
        try {
            QuerySnapshot snapshot = Tasks.await(db.collection(COLLECTION_NAME)
                .whereEqualTo("doctorId", doctorId)
                .get());

            List<MedicineDTO> medicines = new ArrayList<>();
            for (DocumentSnapshot doc : snapshot.getDocuments()) {
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

    public void getByCode(String code, OnSuccessListener<MedicineDTO> onSuccess, OnFailureListener onFailure) {
        db.collection(COLLECTION_NAME)
            .whereEqualTo("code", code)
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                if (!queryDocumentSnapshots.isEmpty()) {
                    DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                    MedicineDTO medicine = fromMap(document.getId(), document.getData());
                    onSuccess.onSuccess(medicine);
                } else {
                    onFailure.onFailure(new Exception("Bu kod ile eşleşen ilaç bulunamadı"));
                }
            })
            .addOnFailureListener(onFailure);
    }

    public void getByUserEmail(String userEmail, OnSuccessListener<List<MedicineDTO>> onSuccess, OnFailureListener onFailure) {
        db.collection(COLLECTION_NAME)
                .whereEqualTo("userEmail", userEmail)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<MedicineDTO> medicines = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Map<String, Object> data = document.getData();
                        MedicineDTO medicine = fromMap(document.getId(), data);
                        medicines.add(medicine);
                    }
                    onSuccess.onSuccess(medicines);
                })
                .addOnFailureListener(onFailure);
    }

    private MedicineDTO fromMap(String id, Map<String, Object> map) {
        List<DoseTimeDTO> doseTimes = new ArrayList<>();
        if (map.get("doseTimes") != null) {
            List<Map<String, Object>> doseList = (List<Map<String, Object>>) map.get("doseTimes");
            for (Map<String, Object> d : doseList) {
                Object amountObj = d.get("amount");
                double amount;
                if (amountObj instanceof Long) {
                    amount = ((Long) amountObj).doubleValue();
                } else if (amountObj instanceof Double) {
                    amount = (Double) amountObj;
                } else {
                    amount = 0.0;
                }
                
                doseTimes.add(new DoseTimeDTO(
                    (String) d.get("time"),
                    amount,
                    (String) d.get("unit")
                ));
            }
        }

        Object startDateObj = map.get("startDate");
        Date startDate = null;
        if (startDateObj instanceof com.google.firebase.Timestamp) {
            startDate = ((com.google.firebase.Timestamp) startDateObj).toDate();
        } else if (startDateObj instanceof Date) {
            startDate = (Date) startDateObj;
        }

        Object createdAtObj = map.get("createdAt");
        Date createdAt = null;
        if (createdAtObj instanceof com.google.firebase.Timestamp) {
            createdAt = ((com.google.firebase.Timestamp) createdAtObj).toDate();
        } else if (createdAtObj instanceof Date) {
            createdAt = (Date) createdAtObj;
        }

        MedicineDTO medicine = new MedicineDTO(
            id,
            (String) map.get("name"),
            (String) map.get("form"),
            (String) map.get("frequency"),
            startDate,
            doseTimes,
            (String) map.get("intakeTime"),
            (String) map.get("code"),
            (String) map.get("doctorId"),
            (String) map.get("userEmail")
        );
        medicine.setCreatedAt(createdAt);
        return medicine;
    }

    private Medicine fromMapToEntity(String id, Map<String, Object> map) {
        List<DoseTime> doseTimes = new ArrayList<>();
        if (map.get("doseTimes") != null) {
            List<Map<String, Object>> doseList = (List<Map<String, Object>>) map.get("doseTimes");
            for (Map<String, Object> d : doseList) {
                Object amountObj = d.get("amount");
                double amount;
                if (amountObj instanceof Long) {
                    amount = ((Long) amountObj).doubleValue();
                } else if (amountObj instanceof Double) {
                    amount = (Double) amountObj;
                } else {
                    amount = 0.0;
                }
                
                doseTimes.add(new DoseTime(
                    (String) d.get("time"),
                    amount,
                    (String) d.get("unit")
                ));
            }
        }

        Object startDateObj = map.get("startDate");
        Date startDate = null;
        if (startDateObj instanceof com.google.firebase.Timestamp) {
            startDate = ((com.google.firebase.Timestamp) startDateObj).toDate();
        } else if (startDateObj instanceof Date) {
            startDate = (Date) startDateObj;
        }

        Object createdAtObj = map.get("createdAt");
        Date createdAt = null;
        if (createdAtObj instanceof com.google.firebase.Timestamp) {
            createdAt = ((com.google.firebase.Timestamp) createdAtObj).toDate();
        } else if (createdAtObj instanceof Date) {
            createdAt = (Date) createdAtObj;
        }

        Medicine medicine = new Medicine(
            id,
            (String) map.get("name"),
            map.get("form") != null ? MedicineForm.valueOf((String) map.get("form")) : null,
            (String) map.get("frequency"),
            startDate,
            doseTimes,
            map.get("intakeTime") != null ? IntakeTime.valueOf((String) map.get("intakeTime")) : null,
            (String) map.get("code"),
            (String) map.get("doctorId"),
            (String) map.get("userEmail")
        );
        medicine.setCreatedAt(createdAt);
        return medicine;
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
        map.put("userEmail", medicine.getUserEmail());
        map.put("code", medicine.getCode());
        map.put("createdAt", medicine.getCreatedAt());
        return map;
    }

    public interface OnMedicinesLoadedListener {
        void onMedicinesLoaded(List<Medicine> medicines);
        void onError(String error);
    }

    public void getMedicinesBeforeDate(Date date, OnMedicinesLoadedListener listener) {
        medicinesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Medicine> medicines = new ArrayList<>();
                
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Medicine medicine = snapshot.getValue(Medicine.class);
                    if (medicine != null && medicine.getStartDate().before(date)) {
                        medicines.add(medicine);
                    }
                }
                
                listener.onMedicinesLoaded(medicines);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onError(databaseError.getMessage());
            }
        });
    }

    public Task<QuerySnapshot> getMedicines(String userId) {
        return medicinesCollection
                .whereEqualTo("userId", userId)
                .get();
    }

    public Task<Void> addMedicine(MedicineDTO medicine) {
        return medicinesCollection.document().set(medicine);
    }

    public Task<Void> updateMedicine(String medicineId, MedicineDTO medicine) {
        return medicinesCollection.document(medicineId).set(medicine);
    }

    public Task<Void> deleteMedicine(String medicineId) {
        return medicinesCollection.document(medicineId).delete();
    }
}