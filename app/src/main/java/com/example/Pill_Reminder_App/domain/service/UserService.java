package com.example.Pill_Reminder_App.domain.service;

import com.example.Pill_Reminder_App.data.dto.UserDTO;
import com.example.Pill_Reminder_App.data.model.User;
import com.example.Pill_Reminder_App.data.repository.UserRepository;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.ArrayList;

public class UserService implements IService<UserDTO> {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void add(UserDTO dto, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        User user = fromDTO(dto);
        userRepository.add(user.getId(), toMap(user), onSuccess, onFailure);
    }

    @Override
    public void getAll(OnSuccessListener<List<UserDTO>> onSuccess, OnFailureListener onFailure) {
        userRepository.getAll(onSuccess, onFailure);
    }

    @Override
    public void getById(String id, OnSuccessListener<UserDTO> onSuccess, OnFailureListener onFailure) {
        userRepository.getById(id, onSuccess, onFailure);
    }

    @Override
    public void update(String id, UserDTO dto, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        User user = fromDTO(dto);
        userRepository.update(id, toMap(user), onSuccess, onFailure);
    }

    @Override
    public void delete(String id, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        userRepository.delete(id, onSuccess, onFailure);
    }

    // DTO -> Entity
    private User fromDTO(UserDTO dto) {
        String hashedPassword = hashPassword(dto.getPassword());
        return new User(dto.getId(), dto.getName(), dto.getEmail(), hashedPassword, dto.getUserType());
    }

    private String hashPassword(String password) {
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Entity -> Map (Firestore için)
    private Map<String, Object> toMap(User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", user.getName());
        map.put("email", user.getEmail());
        map.put("hashedPassword", user.getHashedPassword());
        map.put("userType", user.getUserType());
        return map;
    }

    public void getUser(String userId, Consumer<UserDTO> onSuccess, Consumer<Exception> onError) {
        try {
            userRepository.getById(userId,
                user -> {
                    if (user != null) {
                        onSuccess.accept(user);
                    } else {
                        onError.accept(new Exception("Kullanıcı bulunamadı"));
                    }
                },
                e -> onError.accept(new Exception("Kullanıcı bilgileri yüklenemedi: " + e.getMessage()))
            );
        } catch (Exception e) {
            onError.accept(e);
        }
    }

    public void getPatients(String doctorId, Consumer<List<UserDTO>> onSuccess, Consumer<Exception> onError) {
        try {
            userRepository.getAll(
                users -> {
                    if (users != null) {
                        List<UserDTO> patients = users.stream()
                            .filter(u -> "patient".equals(u.getUserType()))
                            .collect(Collectors.toList());
                        onSuccess.accept(patients);
                    } else {
                        onSuccess.accept(new ArrayList<>());
                    }
                },
                e -> onError.accept(new Exception("Hasta listesi yüklenemedi: " + e.getMessage()))
            );
        } catch (Exception e) {
            onError.accept(e);
        }
    }
} 