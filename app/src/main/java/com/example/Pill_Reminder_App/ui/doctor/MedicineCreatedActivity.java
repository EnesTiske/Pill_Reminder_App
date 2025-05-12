package com.example.Pill_Reminder_App.ui.doctor;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.Pill_Reminder_App.R;
import com.example.Pill_Reminder_App.utils.UserSessionManager;

public class MedicineCreatedActivity extends AppCompatActivity {
    private UserSessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Session manager'ı başlat
        sessionManager = new UserSessionManager(this);

        // Kullanıcı tipi kontrolü
        if (!sessionManager.isDoctor()) {
            Toast.makeText(this, "Bu sayfaya sadece doktorlar erişebilir", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, com.example.Pill_Reminder_App.ui.patient.PatientHomeActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_medicine_created);
        String code = getIntent().getStringExtra("medicineCode");
        TextView tvCode = findViewById(R.id.tvMedicineCode);
        tvCode.setText("İlaç Kodu: " + code);
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            // Kullanıcı tipine göre yönlendirme yap
            Intent intent;
            if (sessionManager.isDoctor()) {
                intent = new Intent(this, DoctorHomeActivity.class);
            } else {
                intent = new Intent(this, com.example.Pill_Reminder_App.ui.patient.PatientHomeActivity.class);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
} 