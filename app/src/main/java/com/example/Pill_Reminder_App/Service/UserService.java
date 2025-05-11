package com.example.Pill_Reminder_App.Service;

import com.example.Pill_Reminder_App.DTOs.UserDTO;
import com.example.Pill_Reminder_App.Models.User;
import com.example.Pill_Reminder_App.Repository.UserRepository;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import java.util.List;
import java.util.Map;

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
        return new User(dto.getName(), dto.getEmail(), dto.getPassword(), dto.getUserType());
    }

    // Entity -> Map (Firestore için)
    private Map<String, Object> toMap(User user) {
        return new java.util.HashMap<String, Object>() {{
            put("name", user.getName());
            put("email", user.getEmail());
            put("hashedPassword", user.getHashedPassword());
            put("userType", user.getUserType());
        }};
    }
} 