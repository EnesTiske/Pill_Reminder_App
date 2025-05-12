package com.example.Pill_Reminder_App;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.Pill_Reminder_App.data.dto.MedicineDTO;
import com.example.Pill_Reminder_App.data.dto.DoseTimeDTO;
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
        // Örnek veriler
        List<MedicineDTO> exampleMedicines = new ArrayList<>();
        
        MedicineDTO medicine1 = new MedicineDTO();
        medicine1.setName("Parol");
        medicine1.setForm("Tablet");
        medicine1.setFrequency("Günde 3 kez");
        medicine1.setIntakeTime("Yemeklerden sonra");
        List<DoseTimeDTO> doseTimes1 = new ArrayList<>();
        doseTimes1.add(new DoseTimeDTO("08:00", 1.0, "Sabah"));
        doseTimes1.add(new DoseTimeDTO("14:00", 1.0, "Öğle"));
        doseTimes1.add(new DoseTimeDTO("20:00", 1.0, "Akşam"));
        medicine1.setDoseTimes(doseTimes1);
        exampleMedicines.add(medicine1);

        MedicineDTO medicine2 = new MedicineDTO();
        medicine2.setName("Aferin");
        medicine2.setForm("Şurup");
        medicine2.setFrequency("Günde 2 kez");
        medicine2.setIntakeTime("Yemeklerden önce");
        List<DoseTimeDTO> doseTimes2 = new ArrayList<>();
        doseTimes2.add(new DoseTimeDTO("09:00", 1.0, "Sabah"));
        doseTimes2.add(new DoseTimeDTO("21:00", 1.0, "Akşam"));
        medicine2.setDoseTimes(doseTimes2);
        exampleMedicines.add(medicine2);

        MedicineDTO medicine3 = new MedicineDTO();
        medicine3.setName("Vitamin C");
        medicine3.setForm("Kapsül");
        medicine3.setFrequency("Günde 1 kez");
        medicine3.setIntakeTime("Sabah");
        List<DoseTimeDTO> doseTimes3 = new ArrayList<>();
        doseTimes3.add(new DoseTimeDTO("08:00", 1.0, "Sabah"));
        medicine3.setDoseTimes(doseTimes3);
        exampleMedicines.add(medicine3);

        MedicineDTO medicine4 = new MedicineDTO();
        medicine4.setName("Parol");
        medicine4.setForm("Tablet");
        medicine4.setFrequency("Günde 3 kez");
        medicine4.setIntakeTime("Yemeklerden sonra");
        List<DoseTimeDTO> doseTimes4 = new ArrayList<>();
        doseTimes4.add(new DoseTimeDTO("08:00", 1.0, "Sabah"));
        medicine4.setDoseTimes(doseTimes4);
        exampleMedicines.add(medicine4);

        MedicineDTO medicine5 = new MedicineDTO();
        medicine5.setName("Parol");
        medicine5.setForm("Tablet");
        medicine5.setFrequency("Günde 3 kez");
        medicine5.setIntakeTime("Yemeklerden sonra");
        List<DoseTimeDTO> doseTimes5 = new ArrayList<>();
        doseTimes5.add(new DoseTimeDTO("08:00", 1.0, "Sabah"));
        medicine5.setDoseTimes(doseTimes5);
        exampleMedicines.add(medicine5);

        MedicineDTO medicine6 = new MedicineDTO();
        medicine6.setName("Parol");
        medicine6.setForm("Tablet");
        medicine6.setFrequency("Günde 3 kez");
        medicine6.setIntakeTime("Yemeklerden sonra");
        List<DoseTimeDTO> doseTimes6 = new ArrayList<>();
        doseTimes6.add(new DoseTimeDTO("08:00", 1.0, "Sabah"));
        medicine6.setDoseTimes(doseTimes6);
        exampleMedicines.add(medicine6);

        displayMedicines(exampleMedicines);
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