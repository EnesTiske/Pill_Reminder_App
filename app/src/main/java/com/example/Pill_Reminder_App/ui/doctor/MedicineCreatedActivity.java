package com.example.Pill_Reminder_App.ui.doctor;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.Pill_Reminder_App.R;

public class MedicineCreatedActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_created);
        String code = getIntent().getStringExtra("medicineCode");
        TextView tvCode = findViewById(R.id.tvMedicineCode);
        tvCode.setText("İlaç Kodu: " + code);
    }
} 