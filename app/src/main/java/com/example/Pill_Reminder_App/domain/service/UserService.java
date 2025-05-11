package com.example.Pill_Reminder_App.domain.service;

import com.example.Pill_Reminder_App.data.dto.UserDTO;
import com.example.Pill_Reminder_App.data.model.User;
import com.example.Pill_Reminder_App.data.repository.UserRepository;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class UserService implements IService<UserDTO> {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void add(UserDTO dto, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        User user = fromDTO(dto);
        userRepository.add(user.getEmail(), toMap(user), onSuccess, onFailure);
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
        return new User(dto.getName(), dto.getEmail(), hashedPassword, dto.getUserType());
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
} 