package com.example.Pill_Reminder_App.data.mapper;

import com.example.Pill_Reminder_App.data.model.User;
import com.example.Pill_Reminder_App.data.dto.UserDTO;
import com.example.Pill_Reminder_App.domain.service.Mapper;

public class UserMapper implements Mapper<UserDTO, User> {
    @Override
    public UserDTO toDTO(User entity) {
        return new UserDTO(
            entity.getId(),
            entity.getUserId(),
            entity.getName(),
            entity.getEmail(),
            entity.getHashedPassword(),
            entity.getUserType()
        );
    }

    @Override
    public User toEntity(UserDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setUserId(dto.getUserId());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setHashedPassword(dto.getPassword());
        user.setUserType(dto.getUserType());
        return user;
    }
} 