package com.example.Pill_Reminder_App.Repository;

import com.example.Pill_Reminder_App.DTOs.UserDTO;
import com.example.Pill_Reminder_App.Models.User;

public class UserRepository extends GenericRepository<User, UserDTO> {
    public UserRepository() {
        super("users", User.class);
    }

    @Override
    protected UserDTO toDTO(User entity) {
        return new UserDTO(
                entity.getName(),
                entity.getEmail(),
                entity.getHashedPassword(),
                entity.getUserType()
        );
    }
}
