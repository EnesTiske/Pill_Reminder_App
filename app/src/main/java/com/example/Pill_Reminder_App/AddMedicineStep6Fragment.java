package com.example.Pill_Reminder_App;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AddMedicineStep6Fragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_medicine_step6, container, false);

        TextView optionBefore = view.findViewById(R.id.optionBefore);
        TextView optionDuring = view.findViewById(R.id.optionDuring);
        TextView optionAfter = view.findViewById(R.id.optionAfter);
        TextView optionDoesntMatter = view.findViewById(R.id.optionDoesntMatter);

        View.OnClickListener listener = v -> {
            String mealTime = "";
            if (v == optionBefore) {
                mealTime = "Yemekten önce";
            } else if (v == optionDuring) {
                mealTime = "Yemek sırasında";
            } else if (v == optionAfter) {
                mealTime = "Yemekten sonra";
            } else if (v == optionDoesntMatter) {
                mealTime = "Farketmez";
            }

            if (getActivity() instanceof AddMedicineActivity) {
                ((AddMedicineActivity) getActivity()).setMedicineMealTime(mealTime);
            }
        };

        optionBefore.setOnClickListener(listener);
        optionDuring.setOnClickListener(listener);
        optionAfter.setOnClickListener(listener);
        optionDoesntMatter.setOnClickListener(listener);

        return view;
    }
}