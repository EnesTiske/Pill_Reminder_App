package com.example.Pill_Reminder_App;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.app.Dialog;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import java.util.Calendar;
import android.widget.CheckBox;

public class AddMedicineStep3Fragment extends Fragment {
    private TextView[] options;
    private int selectedIndex = -1;
    private String selectedFrequency = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_medicine_step3, container, false);

        options = new TextView[]{
                view.findViewById(R.id.optionEveryDay),
                view.findViewById(R.id.optionCertainDays),
                view.findViewById(R.id.optionEveryXDay),
                view.findViewById(R.id.optionEveryXWeek),
                view.findViewById(R.id.optionEveryXMonth),
        };

        for (int i = 0; i < options.length; i++) {
            final int index = i;
            options[i].setOnClickListener(v -> selectOption(index));
        }

        return view;
    }

    private void selectOption(int index) {
        for (int i = 0; i < options.length; i++) {
            if (i == index) {
                options[i].setBackgroundColor(Color.parseColor("#2196F3"));
                options[i].setTextColor(Color.WHITE);
            } else {
                options[i].setBackgroundColor(Color.TRANSPARENT);
                options[i].setTextColor(Color.BLACK);
            }
        }
        selectedIndex = index;
        
        if (index == 1) {
            showCertainDaysDialog();
        } else {
            showFrequencyDialog();
        }
    }

    private void showFrequencyDialog() {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_frequency_picker);
        dialog.setCancelable(false);

        NumberPicker numberPicker = dialog.findViewById(R.id.numberPicker);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnContinue = dialog.findViewById(R.id.btnContinue);

        // NumberPicker ayarları
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(10);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setValue(1);

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        btnContinue.setOnClickListener(v -> {
            int value = numberPicker.getValue();
            String frequency = "";
            
            switch (selectedIndex) {
                case 0: // Her gün
                    frequency = "Her gün";
                    break;
                case 2: // X günde bir
                    frequency = value + " günde bir";
                    break;
                case 3: // X haftada bir
                    frequency = value + " haftada bir";
                    break;
                case 4: // X ayda bir
                    frequency = value + " ayda bir";
                    break;
            }
            
            selectedFrequency = frequency;
            if (getActivity() instanceof AddMedicineActivity) {
                ((AddMedicineActivity) getActivity()).setMedicineFrequency(frequency);
            }
            
            dialog.dismiss();
        });

        dialog.show();
    }

    private void showCertainDaysDialog() {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_certain_days_picker);
        dialog.setCancelable(false);

        TextView[] dayViews = new TextView[]{
                dialog.findViewById(R.id.dayMon),
                dialog.findViewById(R.id.dayTue),
                dialog.findViewById(R.id.dayWed),
                dialog.findViewById(R.id.dayThu),
                dialog.findViewById(R.id.dayFri),
                dialog.findViewById(R.id.daySat),
                dialog.findViewById(R.id.daySun)
        };
        final boolean[] selectedDays = new boolean[7];

        for (int i = 0; i < dayViews.length; i++) {
            final int index = i;
            dayViews[i].setSelected(false);
            dayViews[i].setOnClickListener(v -> {
                boolean isSelected = !v.isSelected();
                v.setSelected(isSelected);
                selectedDays[index] = isSelected;
            });
        }

        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnContinue = dialog.findViewById(R.id.btnContinue);

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        btnContinue.setOnClickListener(v -> {
            StringBuilder frequency = new StringBuilder("Belirli günler: ");
            String[] dayNames = {"Pazartesi", "Salı", "Çarşamba", "Perşembe", "Cuma", "Cumartesi", "Pazar"};
            
            for (int i = 0; i < selectedDays.length; i++) {
                if (selectedDays[i]) {
                    if (frequency.length() > 14) { // "Belirli günler: " uzunluğu
                        frequency.append(", ");
                    }
                    frequency.append(dayNames[i]);
                }
            }
            
            selectedFrequency = frequency.toString();
            if (getActivity() instanceof AddMedicineActivity) {
                ((AddMedicineActivity) getActivity()).setMedicineFrequency(selectedFrequency);
            }
            
            dialog.dismiss();
        });

        dialog.show();
    }
} 