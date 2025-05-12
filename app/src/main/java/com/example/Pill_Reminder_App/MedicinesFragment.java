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
import com.example.Pill_Reminder_App.utils.UserSessionManager;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class MedicinesFragment extends Fragment {
    private MedicineRepository medicineRepository;
    private LinearLayout layoutMedicines;
    private UserSessionManager sessionManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        medicineRepository = new MedicineRepository();
        sessionManager = new UserSessionManager(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medicines, container, false);
        layoutMedicines = view.findViewById(R.id.layoutMedicines);
        loadMedicines();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadMedicines(); // Fragment her görünür olduğunda ilaçları yeniden yükle
    }

    private void loadMedicines() {
        String userEmail = sessionManager.getUserEmail();
        if (userEmail == null || userEmail.isEmpty()) {
            Toast.makeText(requireContext(), "Kullanıcı bilgisi bulunamadı", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kullanıcının ilaçlarını getir
        medicineRepository.getByUserEmail(userEmail,
            new OnSuccessListener<List<MedicineDTO>>() {
                @Override
                public void onSuccess(List<MedicineDTO> medicines) {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            if (medicines != null) {
                                displayMedicines(medicines);
                            } else {
                                showEmptyList();
                            }
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
                            showEmptyList();
                        });
                    }
                }
            }
        );
    }

    private void showEmptyList() {
        layoutMedicines.removeAllViews();
        TextView noMedicinesText = new TextView(requireContext());
        noMedicinesText.setText("Henüz ilaç eklenmemiş");
        noMedicinesText.setTextSize(16);
        noMedicinesText.setPadding(32, 32, 32, 32);
        layoutMedicines.addView(noMedicinesText);
    }

    private void displayMedicines(List<MedicineDTO> medicines) {
        layoutMedicines.removeAllViews();
        
        if (medicines.isEmpty()) {
            showEmptyList();
            return;
        }

        for (MedicineDTO medicine : medicines) {
            if (medicine != null && medicine.getName() != null) {
                View medItem = LayoutInflater.from(getContext()).inflate(R.layout.medicines_item, layoutMedicines, false);
                
                TextView tvMedName = medItem.findViewById(R.id.tvMedName);
                TextView tvMedMealInfo = medItem.findViewById(R.id.tvMedMealInfo);
                TextView tvMedNextReminder = medItem.findViewById(R.id.tvMedNextReminder);
                
                tvMedName.setText(medicine.getName());
                
                // Alım zamanı bilgisini göster
                String intakeTime = medicine.getIntakeTime() != null ? medicine.getIntakeTime() : "Belirtilmemiş";
                tvMedMealInfo.setText("Alım Zamanı: " + intakeTime);
                
                // Sonraki hatırlatma zamanını göster
                String nextReminder = calculateNextReminder(medicine);
                tvMedNextReminder.setText("Sonraki Hatırlatma: " + nextReminder);
                
                layoutMedicines.addView(medItem);
            }
        }
    }

    private String calculateNextReminder(MedicineDTO medicine) {
        if (medicine.getDoseTimes() != null && !medicine.getDoseTimes().isEmpty()) {
            return medicine.getDoseTimes().get(0).getTime();
        }
        return "Belirlenmedi";
    }
} 