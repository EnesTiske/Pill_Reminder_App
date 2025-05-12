package com.example.Pill_Reminder_App.data.repository;

import com.example.Pill_Reminder_App.data.model.User;
import com.example.Pill_Reminder_App.data.dto.UserDTO;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class UserRepository extends GenericRepository<User, UserDTO> {
    private final FirebaseFirestore db;
    private static final String COLLECTION_NAME = "users";

    public UserRepository() {
        super("users", User.class);
        this.db = FirebaseFirestore.getInstance();
    }

    @Override
    protected UserDTO toDTO(User entity) {
        return new UserDTO(
            entity.getId(),
            entity.getName(),
            entity.getEmail(),
            entity.getHashedPassword(),
            entity.getUserType()
        );
    }

    public void add(String id, Map<String, Object> data, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        db.collection(COLLECTION_NAME).document(id).set(data)
            .addOnSuccessListener(onSuccess)
            .addOnFailureListener(onFailure);
    }

    public void getAll(OnSuccessListener<List<UserDTO>> onSuccess, OnFailureListener onFailure) {
        db.collection(COLLECTION_NAME).get()
            .addOnSuccessListener(querySnapshot -> {
                List<UserDTO> users = new ArrayList<>();
                for (var doc : querySnapshot.getDocuments()) {
                    UserDTO user = fromMap(doc.getId(), doc.getData());
                    users.add(user);
                }
                onSuccess.onSuccess(users);
            })
            .addOnFailureListener(onFailure);
    }

    public void getById(String id, OnSuccessListener<UserDTO> onSuccess, OnFailureListener onFailure) {
        db.collection(COLLECTION_NAME).document(id).get()
            .addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    UserDTO user = fromMap(documentSnapshot.getId(), documentSnapshot.getData());
                    onSuccess.onSuccess(user);
                } else {
                    onFailure.onFailure(new Exception("User not found"));
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

    public UserDTO getUserById(String userId) throws Exception {
        try {
            var doc = Tasks.await(db.collection(COLLECTION_NAME).document(userId).get());
            if (doc.exists()) {
                return fromMap(doc.getId(), doc.getData());
            }
            throw new Exception("User not found");
        } catch (ExecutionException | InterruptedException e) {
            throw new Exception("Failed to get user: " + e.getMessage());
        }
    }

    public List<UserDTO> getPatientsByDoctorId(String doctorId) throws Exception {
        try {
            QuerySnapshot snapshot = Tasks.await(db.collection(COLLECTION_NAME)
                .whereEqualTo("userType", "patient")
                .get());

            List<UserDTO> patients = new ArrayList<>();
            for (var doc : snapshot.getDocuments()) {
                UserDTO user = fromMap(doc.getId(), doc.getData());
                if (user.getUserType().equals("patient")) {
                    patients.add(user);
                }
            }
            return patients;
        } catch (ExecutionException | InterruptedException e) {
            throw new Exception("Hasta listesi yüklenemedi: " + e.getMessage());
        }
    }

    private UserDTO fromMap(String id, Map<String, Object> map) {
        return new UserDTO(
            id,
            (String) map.get("userId"),
            (String) map.get("name"),
            (String) map.get("email"),
            (String) map.get("hashedPassword"),
            (String) map.get("userType")
        );
    }
} 