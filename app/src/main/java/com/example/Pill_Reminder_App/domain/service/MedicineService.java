package com.example.Pill_Reminder_App.domain.service;

import com.example.Pill_Reminder_App.data.dto.MedicineDTO;
import com.example.Pill_Reminder_App.data.dto.DoseTimeDTO;
import com.example.Pill_Reminder_App.data.model.Medicine;
import com.example.Pill_Reminder_App.data.model.DoseTime;
import com.example.Pill_Reminder_App.data.model.MedicineForm;
import com.example.Pill_Reminder_App.data.model.IntakeTime;
import com.example.Pill_Reminder_App.data.repository.MedicineRepository;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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

    public void generateUniqueCode(OnSuccessListener<String> onSuccess, OnFailureListener onFailure) {
        generateUniqueCodeInternal(onSuccess, onFailure);
    }

    private void generateUniqueCodeInternal(OnSuccessListener<String> onSuccess, OnFailureListener onFailure) {
        String code = java.util.UUID.randomUUID().toString().substring(0, 8);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("medicines").whereEqualTo("code", code).get()
            .addOnSuccessListener((QuerySnapshot snapshot) -> {
                if (snapshot.isEmpty()) {
                    onSuccess.onSuccess(code);
                } else {
                    // Çakışma varsa tekrar dene
                    generateUniqueCodeInternal(onSuccess, onFailure);
                }
            })
            .addOnFailureListener(onFailure);
    }

    // DTO -> Entity
    private Medicine fromDTO(MedicineDTO dto) {
        List<DoseTime> doseTimes = new ArrayList<>();
        if (dto.getDoseTimes() != null) {
            for (DoseTimeDTO d : dto.getDoseTimes()) {
                doseTimes.add(new DoseTime(d.getTime(), d.getAmount(), d.getUnit()));
            }
        }
        MedicineForm form = dto.getForm() != null ? MedicineForm.valueOf(dto.getForm()) : null;
        IntakeTime intakeTime = dto.getIntakeTime() != null ? IntakeTime.valueOf(dto.getIntakeTime()) : null;
        return new Medicine(
            dto.getName(),
            form,
            dto.getFrequency(),
            dto.getStartDate(),
            doseTimes,
            intakeTime,
            dto.getCode(),
            dto.getDoctorId(),
            dto.getUserId()
        );
    }

    // Entity -> Map (Firestore için)
    private Map<String, Object> toMap(Medicine medicine) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", medicine.getName());
        map.put("doctorId", medicine.getDoctorId());
        map.put("form", medicine.getForm() != null ? medicine.getForm().name() : null);
        map.put("frequency", medicine.getFrequency());
        map.put("startDate", medicine.getStartDate());
        map.put("intakeTime", medicine.getIntakeTime() != null ? medicine.getIntakeTime().name() : null);
        map.put("code", medicine.getCode());
        map.put("userId", medicine.getUserId());
        if (medicine.getDoseTimes() != null) {
            List<Map<String, Object>> doseList = new ArrayList<>();
            for (DoseTime d : medicine.getDoseTimes()) {
                Map<String, Object> dMap = new HashMap<>();
                dMap.put("time", d.getTime());
                dMap.put("amount", d.getAmount());
                dMap.put("unit", d.getUnit());
                doseList.add(dMap);
            }
            map.put("doseTimes", doseList);
        }
        return map;
    }

    public void getMedicines(String doctorId, Consumer<List<MedicineDTO>> onSuccess, Consumer<Exception> onError) {
        try {
            medicineRepository.getAll(
                medicines -> {
                    if (medicines != null) {
                        List<MedicineDTO> doctorMedicines = medicines.stream()
                            .filter(m -> doctorId.equals(m.getDoctorId()))
                            .collect(Collectors.toList());
                        onSuccess.accept(doctorMedicines);
                    } else {
                        onSuccess.accept(new ArrayList<>());
                    }
                },
                e -> onError.accept(new Exception("İlaç listesi yüklenemedi: " + e.getMessage()))
            );
        } catch (Exception e) {
            onError.accept(e);
        }
    }
} 