package com.example.Pill_Reminder_App.ui.patient;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Pill_Reminder_App.R;
import com.example.Pill_Reminder_App.utils.UserSessionManager;

public class PatientHomeActivity extends AppCompatActivity {
    private UserSessionManager sessionManager;
    private TextView tvPatientName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_home);

        try {
            // Session manager'ı başlat
            sessionManager = new UserSessionManager(this);

            // Session kontrolü
            if (!sessionManager.isLoggedIn()) {
                Toast.makeText(this, "Lütfen giriş yapın", Toast.LENGTH_SHORT).show();
                startActivity(new android.content.Intent(this, com.example.Pill_Reminder_App.ui.auth.LoginActivity.class));
                finish();
                return;
            }

            if (!sessionManager.isPatient()) {
                Toast.makeText(this, "Bu sayfaya sadece hastalar erişebilir", Toast.LENGTH_SHORT).show();
                startActivity(new android.content.Intent(this, com.example.Pill_Reminder_App.ui.doctor.DoctorHomeActivity.class));
                finish();
                return;
            }

            // UI bileşenlerini başlat
            initializeViews();

            // Verileri yükle
            loadPatientData();
        } catch (Exception e) {
            Toast.makeText(this, "Bir hata oluştu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initializeViews() {
        try {
            tvPatientName = findViewById(R.id.tvPatientName);
        } catch (Exception e) {
            Toast.makeText(this, "Arayüz başlatılamadı: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadPatientData() {
        try {
            String patientName = sessionManager.getUserName();
            if (patientName != null && !patientName.isEmpty()) {
                tvPatientName.setText(patientName);
            } else {
                tvPatientName.setText("Hasta Bilgisi");
                Toast.makeText(this, "Henüz hasta bilgisi girilmemiş", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            tvPatientName.setText("Hasta Bilgisi");
            Toast.makeText(this, "Hasta bilgileri yüklenemedi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
} 