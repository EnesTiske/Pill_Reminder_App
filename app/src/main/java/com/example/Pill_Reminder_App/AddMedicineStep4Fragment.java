package com.example.Pill_Reminder_App;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.Calendar;
import java.util.Date;

public class AddMedicineStep4Fragment extends Fragment {
    private Date selectedDate;
    private DatePicker datePicker;
    private Button btnNext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_medicine_step4, container, false);
        
        datePicker = view.findViewById(R.id.datePicker);
        btnNext = view.findViewById(R.id.btnNext);

        // Minimum tarihi bugün olarak ayarla
        Calendar calendar = Calendar.getInstance();
        datePicker.setMinDate(calendar.getTimeInMillis());

        // Varsayılan olarak bugünün tarihini seç
        selectedDate = calendar.getTime();
        if (getActivity() instanceof AddMedicineActivity) {
            ((AddMedicineActivity) getActivity()).setMedicineStartDate(selectedDate);
        }

        // Tarih değiştiğinde
        datePicker.setOnDateChangedListener((view1, year, month, day) -> {
            calendar.set(year, month, day);
            selectedDate = calendar.getTime();
            if (getActivity() instanceof AddMedicineActivity) {
                ((AddMedicineActivity) getActivity()).setMedicineStartDate(selectedDate);
            }
        });

        return view;
    }

    public boolean isStepValid() {
        // Tarih seçilmiş mi kontrol et
        if (selectedDate == null) {
            return false;
        }

        // Tarihi Activity'ye kaydet
        if (getActivity() instanceof AddMedicineActivity) {
            ((AddMedicineActivity) getActivity()).setMedicineStartDate(selectedDate);
        }

        return true;
    }
}
