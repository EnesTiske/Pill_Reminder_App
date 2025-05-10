package com.example.Pill_Reminder_App;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AddMedicineStep7Fragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_medicine_step7, container, false);

        Button btnGoHome = view.findViewById(R.id.btnGoHome);
        btnGoHome.setOnClickListener(v -> {
            requireActivity().finish(); // Aktiviteyi bitir, anasayfaya dön
        });

        return view;
    }
}