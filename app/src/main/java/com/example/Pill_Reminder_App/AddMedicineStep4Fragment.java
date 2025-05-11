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
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_medicine_step4, container, false);

        DatePicker datePicker = view.findViewById(R.id.datePicker);
        Button btnNext = view.findViewById(R.id.btnNext);

        // Minimum tarihi bugün olarak ayarla
        Calendar calendar = Calendar.getInstance();
        datePicker.setMinDate(calendar.getTimeInMillis());

        btnNext.setOnClickListener(v -> {
            int year = datePicker.getYear();
            int month = datePicker.getMonth();
            int day = datePicker.getDayOfMonth();
            
            calendar.set(year, month, day);
            Date startDate = calendar.getTime();
            
            if (getActivity() instanceof AddMedicineActivity) {
                ((AddMedicineActivity) getActivity()).setMedicineStartDate(startDate);
            }
        });

        return view;
    }
}
