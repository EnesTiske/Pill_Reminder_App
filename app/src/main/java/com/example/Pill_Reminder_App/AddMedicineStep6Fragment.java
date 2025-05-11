package com.example.Pill_Reminder_App;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.Pill_Reminder_App.data.model.IntakeTime;

public class AddMedicineStep6Fragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_medicine_step6, container, false);
        Spinner spinner = view.findViewById(R.id.spinnerMealTime);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
            android.R.layout.simple_spinner_item,
            java.util.Arrays.stream(IntakeTime.values()).map(Enum::name).toArray(String[]::new));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                String selectedMealTime = IntakeTime.values()[position].name();
                if (getActivity() instanceof AddMedicineActivity) {
                    ((AddMedicineActivity) getActivity()).setMedicineMealTime(selectedMealTime);
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
        return view;
    }
}