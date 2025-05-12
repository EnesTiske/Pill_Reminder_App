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
import java.util.Date;

public class MedicineService implements IService<MedicineDTO> {
    private final MedicineRepository medicineRepository;

    public MedicineService(MedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    @Override
    public void add(MedicineDTO dto, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        Medicine medicine = fromDTO(dto);
        medicineRepository.add(medicine, onSuccess, onFailure);
    }

    @Override
    public void getAll(OnSuccessListener<List<MedicineDTO>> onSuccess, OnFailureListener onFailure) {
        medicineRepository.getAll(onSuccess, onFailure);
    }

    @Override
    public void getById(String id, OnSuccessListener<MedicineDTO> onSuccess, OnFailureListener onFailure) {
        medicineRepository.getById(id, 
            medicine -> onSuccess.onSuccess(toDTO(medicine)),
            onFailure
        );
    }

    @Override
    public void update(String id, MedicineDTO dto, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        Medicine medicine = fromDTO(dto);
        medicineRepository.update(id, medicine, onSuccess, onFailure);
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
        IntakeTime intakeTime = null;
        if (dto.getIntakeTime() != null) {
            switch (dto.getIntakeTime()) {
                case "Yemekten önce":
                case "YEMEK_ONCE":
                    intakeTime = IntakeTime.YEMEK_ONCE;
                    break;
                case "Yemek sırasında":
                case "YEMEK_SIRASINRDA":
                    intakeTime = IntakeTime.YEMEK_SIRASINRDA;
                    break;
                case "Yemekten sonra":
                case "YEMEK_SONRA":
                    intakeTime = IntakeTime.YEMEK_SONRA;
                    break;
                case "Farketmez":
                case "FARKETMEZ":
                    intakeTime = IntakeTime.FARKETMEZ;
                    break;
            }
        }
        Medicine medicine = new Medicine(
            dto.getId(),
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
        medicine.setCreatedAt(dto.getCreatedAt());
        return medicine;
    }

    // Entity -> DTO
    private MedicineDTO toDTO(Medicine medicine) {
        List<DoseTimeDTO> doseTimeDTOs = new ArrayList<>();
        if (medicine.getDoseTimes() != null) {
            for (DoseTime d : medicine.getDoseTimes()) {
                doseTimeDTOs.add(new DoseTimeDTO(d.getTime(), d.getAmount(), d.getUnit()));
            }
        }
        MedicineDTO dto = new MedicineDTO(
            medicine.getId(),
            medicine.getName(),
            medicine.getForm() != null ? medicine.getForm().name() : null,
            medicine.getFrequency(),
            medicine.getStartDate(),
            doseTimeDTOs,
            medicine.getIntakeTime() != null ? medicine.getIntakeTime().name() : null,
            medicine.getCode(),
            medicine.getDoctorId(),
            medicine.getUserId()
        );
        dto.setCreatedAt(medicine.getCreatedAt());
        return dto;
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

    public void getRecentMedicines(String doctorId, int days, Consumer<List<MedicineDTO>> onSuccess, Consumer<Exception> onError) {
        try {
            medicineRepository.getAll(
                medicines -> {
                    if (medicines != null) {
                        long cutoffTime = System.currentTimeMillis() - (days * 24 * 60 * 60 * 1000L);
                        List<MedicineDTO> recentMedicines = medicines.stream()
                            .filter(m -> doctorId.equals(m.getDoctorId()))
                            .filter(m -> m.getCreatedAt() != null && m.getCreatedAt().getTime() > cutoffTime)
                            .collect(Collectors.toList());
                        onSuccess.accept(recentMedicines);
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