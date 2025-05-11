package com.example.Pill_Reminder_App.Repository;

import com.example.Pill_Reminder_App.DTOs.MedicineDTO;
import com.example.Pill_Reminder_App.Models.Medicine;

public class MedicineRepository extends GenericRepository<Medicine, MedicineDTO> {
    public MedicineRepository() {
        super("medicines", Medicine.class);
    }

    @Override
    protected MedicineDTO toDTO(Medicine entity) {
        return new MedicineDTO(
                entity.getName(),
                entity.getDoctorId()
        );
    }
}
