package com.example.Pill_Reminder_App.domain.service;
 
public interface Mapper<DTO, Entity> {
    DTO toDTO(Entity entity);
    Entity toEntity(DTO dto);
} 