package com.example.Pill_Reminder_App.ui.doctor;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Pill_Reminder_App.AddMedicineActivity;
import com.example.Pill_Reminder_App.R;
import android.widget.Button;
import android.content.Intent;

public class DoctorHomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_home);
        Button btnAddMedicine = findViewById(R.id.btnAddMedicine);
        btnAddMedicine.setOnClickListener(v -> {
            // Burada yeni bir ilaç oluşturma ekranına gidebilir veya doğrudan DTO oluşturup kaydedebilirsin.
            // Örnek: AddMedicineActivity'ye doktorId, unique code ve userId ile DTO oluşturma ve kaydetme akışı başlatılabilir.
            startActivity(new Intent(this, AddMedicineActivity.class));
        });
    }
} 