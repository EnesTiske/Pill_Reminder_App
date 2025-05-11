package com.example.Pill_Reminder_App.data.mapper;

import com.example.Pill_Reminder_App.data.dto.UserDTO;
import com.example.Pill_Reminder_App.data.model.User;
import com.example.Pill_Reminder_App.domain.service.Mapper;

public class UserMapper implements Mapper<UserDTO, User> {
    @Override
    public UserDTO toDTO(User entity) {
        return new UserDTO(entity.getName(), entity.getEmail(), entity.getHashedPassword(), entity.getUserType());
    }

    @Override
    public User toEntity(UserDTO dto) {
        return new User(dto.getName(), dto.getEmail(), dto.getPassword(), dto.getUserType());
    }
} 