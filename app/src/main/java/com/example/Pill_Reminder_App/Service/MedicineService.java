package com.example.Pill_Reminder_App.Service;

import com.example.Pill_Reminder_App.DTOs.MedicineDTO;
import com.example.Pill_Reminder_App.Models.Medicine;
import com.example.Pill_Reminder_App.Repository.MedicineRepository;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import java.util.List;
import java.util.Map;

public class MedicineService implements IService<MedicineDTO> {
    private final MedicineRepository medicineRepository;

    public MedicineService(MedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    @Override
    public void add(MedicineDTO dto, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        Medicine medicine = fromDTO(dto);
        medicineRepository.add(medicine.getCode(), toMap(medicine), onSuccess, onFailure);
    }

    @Override
    public void getAll(OnSuccessListener<List<MedicineDTO>> onSuccess, OnFailureListener onFailure) {
        medicineRepository.getAll(onSuccess, onFailure);
    }

    @Override
    public void getById(String id, OnSuccessListener<MedicineDTO> onSuccess, OnFailureListener onFailure) {
        medicineRepository.getById(id, onSuccess, onFailure);
    }

    @Override
    public void update(String id, MedicineDTO dto, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        Medicine medicine = fromDTO(dto);
        medicineRepository.update(id, toMap(medicine), onSuccess, onFailure);
    }

    @Override
    public void delete(String id, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        medicineRepository.delete(id, onSuccess, onFailure);
    }

    // DTO -> Entity
    private Medicine fromDTO(MedicineDTO dto) {
        // Burada MedicineDTO'dan Medicine'a dönüşümünü yapmalısın
        // Örnek: return new Medicine(dto.getName(), ...);
        return null; // TODO: Gerçek dönüşümü uygula
    }

    // Entity -> Map (Firestore için)
    private Map<String, Object> toMap(Medicine medicine) {
        // Burada Medicine'den Map'e dönüşümünü yapmalısın
        return null; // TODO: Gerçek dönüşümü uygula
    }
} 