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
            dialog.dismiss();

            requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, new AddMedicineStep4Fragment())
                .addToBackStack(null)
                .commit();
        });

        dialog.show();
    }
} 