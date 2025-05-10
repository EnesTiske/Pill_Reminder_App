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

public class AddMedicineStep2Fragment extends Fragment {
    private TextView[] options;
    private int selectedIndex = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_medicine_step2, container, false);

        options = new TextView[]{
                view.findViewById(R.id.optionHap),
                view.findViewById(R.id.optionEnjeksiyon),
                view.findViewById(R.id.optionCozelti),
                view.findViewById(R.id.optionDamla),
                view.findViewById(R.id.optionInhaler),
                view.findViewById(R.id.optionToz),
                view.findViewById(R.id.optionDiger)
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
        // Seçilen değeri burada kullanabilirsin
    }
} 