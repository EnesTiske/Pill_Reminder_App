package com.example.Pill_Reminder_App.data.repository;

import com.example.Pill_Reminder_App.data.model.User;
import com.example.Pill_Reminder_App.data.dto.UserDTO;

public class UserRepository extends GenericRepository<User, UserDTO> {
    public UserRepository() {
        super("users", User.class);
    }

    @Override
    protected UserDTO toDTO(User entity) {
        return new UserDTO(entity.getName(), entity.getEmail(), entity.getHashedPassword(), entity.getUserType());
    }
} 