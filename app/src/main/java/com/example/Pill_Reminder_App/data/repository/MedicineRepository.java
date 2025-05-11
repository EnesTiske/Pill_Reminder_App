package com.example.Pill_Reminder_App.data.repository;

import com.example.Pill_Reminder_App.data.model.Medicine;
import com.example.Pill_Reminder_App.data.model.DoseTime;
import com.example.Pill_Reminder_App.data.dto.MedicineDTO;
import com.example.Pill_Reminder_App.data.dto.DoseTimeDTO;
import java.util.ArrayList;
import java.util.List;

public class MedicineRepository extends GenericRepository<Medicine, MedicineDTO> {
    public MedicineRepository() {
        super("medicines", Medicine.class);
    }

    @Override
    protected MedicineDTO toDTO(Medicine entity) {
        List<DoseTimeDTO> doseTimeDTOs = new ArrayList<>();
        if (entity.getDoseTimes() != null) {
            for (DoseTime d : entity.getDoseTimes()) {
                doseTimeDTOs.add(new DoseTimeDTO(d.getTime(), d.getAmount(), d.getUnit()));
            }
        }
        return new MedicineDTO(
            entity.getName(),
            entity.getForm() != null ? entity.getForm().name() : null,
            entity.getFrequency(),
            entity.getStartDate(),
            doseTimeDTOs,
            entity.getIntakeTime() != null ? entity.getIntakeTime().name() : null,
            // entity.getIntakeTime() != null ? entity.getIntakeTime().name() : null,
            entity.getCode(),
            entity.getDoctorId(),
            entity.getUserId()

        );
    }
} 