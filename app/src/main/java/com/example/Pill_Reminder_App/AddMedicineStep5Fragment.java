package com.example.Pill_Reminder_App;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.Pill_Reminder_App.data.dto.DoseTimeDTO;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddMedicineStep5Fragment extends Fragment {
    private LinearLayout timesContainer;
    private List<DoseTimeDTO> timeDoseList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_medicine_step5, container, false);
        timesContainer = view.findViewById(R.id.timesContainer);
        Button btnAddTime = view.findViewById(R.id.btnAddTime);

        // Varsayılan 3 satır ekle
        addTimeDoseRow("08:00", 1);
        addTimeDoseRow("15:30", 1);
        addTimeDoseRow("23:00", 1);

        btnAddTime.setOnClickListener(v -> addTimeDoseRow("08:00", 1));

        return view;
    }

    private void addTimeDoseRow(String time, int dose) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View row = inflater.inflate(R.layout.item_time_dose, timesContainer, false);
        TextView tvTime = row.findViewById(R.id.tvTime);
        TextView tvDose = row.findViewById(R.id.tvDose);
        ImageView ivArrow = row.findViewById(R.id.ivArrow);

        tvTime.setText(time);
        tvDose.setText(dose + " Hap");

        tvTime.setOnClickListener(v -> {
            // Saat seçici aç
            Calendar now = Calendar.getInstance();
            TimePickerDialog dialog = new TimePickerDialog(getContext(), (view, hourOfDay, minute) -> {
                String newTime = String.format("%02d:%02d", hourOfDay, minute);
                tvTime.setText(newTime);
                updateTimeDoseList();
            }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true);
            dialog.show();
        });

        tvDose.setOnClickListener(v -> {
            // Adet seçici aç
            final String[] items = new String[10];
            for (int i = 0; i < 10; i++) items[i] = (i + 1) + " Hap";
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Adet Seç");
            builder.setItems(items, (dialog, which) -> {
                tvDose.setText(items[which]);
                updateTimeDoseList();
            });
            builder.show();
        });

        timesContainer.addView(row);
        updateTimeDoseList();
    }

    private void updateTimeDoseList() {
        timeDoseList.clear();
        for (int i = 0; i < timesContainer.getChildCount(); i++) {
            View row = timesContainer.getChildAt(i);
            TextView tvTime = row.findViewById(R.id.tvTime);
            TextView tvDose = row.findViewById(R.id.tvDose);
            
            String time = tvTime.getText().toString();
            String doseText = tvDose.getText().toString();
            int dose = Integer.parseInt(doseText.split(" ")[0]);
            
            timeDoseList.add(new DoseTimeDTO(time, dose, "tablet"));
        }
        
        if (getActivity() instanceof AddMedicineActivity) {
            ((AddMedicineActivity) getActivity()).setMedicineTimeDoses(timeDoseList);
        }
    }

    public boolean isStepValid() {
        if (timeDoseList.isEmpty()) {
            return false;
        }

        if (getActivity() instanceof AddMedicineActivity) {
            ((AddMedicineActivity) getActivity()).setMedicineTimeDoses(timeDoseList);
        }

        return true;
    }
} 