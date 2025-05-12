package com.example.Pill_Reminder_App.ui.doctor;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.Pill_Reminder_App.R;
import com.example.Pill_Reminder_App.data.repository.MedicineRepository;
import com.example.Pill_Reminder_App.data.repository.UserRepository;
import com.example.Pill_Reminder_App.domain.service.MedicineService;
import com.example.Pill_Reminder_App.domain.service.UserService;
import com.example.Pill_Reminder_App.utils.UserSessionManager;

public class DoctorStatisticsActivity extends AppCompatActivity {
    private UserSessionManager sessionManager;
    private MedicineService medicineService;
    private UserService userService;
    private TextView tvTotalMedicines;
    private TextView tvTotalPatients;
    private TextView tvActiveMedicines;
    private TextView tvActivePatients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_statistics);

        // Session manager'ı başlat
        sessionManager = new UserSessionManager(this);

        // Service'leri başlat
        medicineService = new MedicineService(new MedicineRepository());
        userService = new UserService(new UserRepository());

        // UI bileşenlerini başlat
        initializeViews();
        
        // Verileri yükle
        loadStatistics();
    }

    private void initializeViews() {
        tvTotalMedicines = findViewById(R.id.tvTotalMedicines);
        tvTotalPatients = findViewById(R.id.tvTotalPatients);
        tvActiveMedicines = findViewById(R.id.tvActiveMedicines);
        tvActivePatients = findViewById(R.id.tvActivePatients);
    }

    private void loadStatistics() {
        String doctorId = sessionManager.getUserId();

        // Toplam ilaç sayısını yükle
        medicineService.getMedicines(doctorId,
            medicines -> {
                tvTotalMedicines.setText("Toplam İlaç: " + medicines.size());
                // Aktif ilaçları say (son 30 gün içinde oluşturulanlar)
                long activeCount = medicines.stream()
                    .filter(m -> (System.currentTimeMillis() - m.getCreatedAt().getTime()) < (30 * 24 * 60 * 60 * 1000))
                    .count();
                tvActiveMedicines.setText("Aktif İlaç: " + activeCount);
            },
            e -> Toast.makeText(this, "İlaç istatistikleri yüklenemedi: " + e.getMessage(), Toast.LENGTH_SHORT).show()
        );

        // Toplam hasta sayısını yükle
        userService.getPatients(doctorId,
            patients -> {
                tvTotalPatients.setText("Toplam Hasta: " + patients.size());
                // Aktif hastaları say (son 30 gün içinde ilaç kullananlar)
                long activeCount = patients.stream()
                    .filter(p -> (System.currentTimeMillis() - p.getLastActivity().getTime()) < (30 * 24 * 60 * 60 * 1000))
                    .count();
                tvActivePatients.setText("Aktif Hasta: " + activeCount);
            },
            e -> Toast.makeText(this, "Hasta istatistikleri yüklenemedi: " + e.getMessage(), Toast.LENGTH_SHORT).show()
        );
    }
} 