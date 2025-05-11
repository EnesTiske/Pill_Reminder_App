package com.example.Pill_Reminder_App;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MedicinesFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medicines, container, false);
        populateMedicinesList(view);
        return view;
    }

    private void populateMedicinesList(View rootView) {
        LinearLayout layoutMedicines = rootView.findViewById(R.id.layoutMedicines);
        layoutMedicines.removeAllViews();

        // Örnek veri: ilaç adı, yemekten önce/sonra, sonraki hatırlatma
        class MedInfo {
            String name, mealInfo, nextReminder;
            MedInfo(String n, String m, String r) { name = n; mealInfo = m; nextReminder = r; }
        }
        List<MedInfo> meds = Arrays.asList(
            new MedInfo("Parol", "Yemekten önce", "15:00"),
            new MedInfo("Aferin", "Yemekten sonra", "18:00"),
            new MedInfo("Vitamin C", "Yemekten önce", "21:00")
        );

        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (MedInfo med : meds) {
            View medItem = inflater.inflate(R.layout.medicines_item, layoutMedicines, false);
            ((TextView)medItem.findViewById(R.id.tvMedName)).setText(med.name);
            ((TextView)medItem.findViewById(R.id.tvMedMealInfo)).setText(med.mealInfo);
            ((TextView)medItem.findViewById(R.id.tvMedNextReminder)).setText("Sonraki hatırlatma: " + med.nextReminder);
            layoutMedicines.addView(medItem);
        }
    }
} 