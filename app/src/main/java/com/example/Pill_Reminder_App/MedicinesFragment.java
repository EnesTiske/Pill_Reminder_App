package com.example.Pill_Reminder_App;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.Pill_Reminder_App.data.dto.MedicineDTO;
import com.example.Pill_Reminder_App.data.repository.MedicineRepository;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class MedicinesFragment extends Fragment {
    private MedicineRepository medicineRepository;
    private LinearLayout layoutMedicines;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        medicineRepository = new MedicineRepository();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medicines, container, false);
        layoutMedicines = view.findViewById(R.id.layoutMedicines);
        loadMedicines();
        return view;
    }

    private void loadMedicines() {
        medicineRepository.getAll(
            new OnSuccessListener<List<MedicineDTO>>() {
                @Override
                public void onSuccess(List<MedicineDTO> medicines) {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            displayMedicines(medicines);
                        });
                    }
                }
            },
            new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "İlaçlar yüklenirken hata oluştu: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        });
                    }
                }
            }
        );
    }

    private void displayMedicines(List<MedicineDTO> medicines) {
        layoutMedicines.removeAllViews();
        
        for (MedicineDTO dto : medicines) {
            View medItem = LayoutInflater.from(getContext()).inflate(R.layout.medicines_item, layoutMedicines, false);
            
            ((TextView)medItem.findViewById(R.id.tvMedName)).setText(dto.getName());
            ((TextView)medItem.findViewById(R.id.tvMedMealInfo)).setText(dto.getIntakeTime());
            
            // Sonraki hatırlatma zamanını hesapla
            String nextReminder = calculateNextReminder(dto);
            ((TextView)medItem.findViewById(R.id.tvMedNextReminder)).setText("Sonraki hatırlatma: " + nextReminder);

            //
            //TODO sonraki hatırlatma yanlış şuan hesaplama kısmından çekeceksin veriyi
            //
            
            layoutMedicines.addView(medItem);
        }
    }

    private String calculateNextReminder(MedicineDTO dto) {
        // Burada sonraki hatırlatma zamanını hesaplama mantığı eklenebilir
        // Şimdilik ilk doz zamanını döndürüyoruz
        if (dto.getDoseTimes() != null && !dto.getDoseTimes().isEmpty()) {
            return dto.getDoseTimes().get(0).getTime();
        }
        return "Belirlenmedi";
    }
} 