package com.example.Pill_Reminder_App;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.textfield.TextInputEditText;

public class AddMedicineStep1Fragment extends Fragment {
    private TextInputEditText medicineNameEditText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_medicine_step1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        medicineNameEditText = view.findViewById(R.id.medicineNameEditText);
        
        // İlaç adı değiştiğinde ana aktiviteye bildir
        medicineNameEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String medicineName = medicineNameEditText.getText() != null ? 
                    medicineNameEditText.getText().toString() : "";
                if (getActivity() instanceof AddMedicineActivity) {
                    ((AddMedicineActivity) getActivity()).setMedicineName(medicineName);
                }
            }
        });
    }

    public String getMedicineName() {
        return medicineNameEditText.getText() != null ? medicineNameEditText.getText().toString() : "";
    }

    public boolean isStepValid() {
        String medicineName = medicineNameEditText.getText().toString().trim();
        if (medicineName.isEmpty()) {
            return false;
        }

        // Verileri Activity'ye kaydet
        if (getActivity() instanceof AddMedicineActivity) {
            ((AddMedicineActivity) getActivity()).setMedicineName(medicineName);
        }

        return true;
    }
} 