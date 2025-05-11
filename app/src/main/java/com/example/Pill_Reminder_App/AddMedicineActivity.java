package com.example.Pill_Reminder_App;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.Pill_Reminder_App.data.dto.MedicineDTO;
import com.example.Pill_Reminder_App.domain.service.MedicineService;
import com.example.Pill_Reminder_App.data.repository.MedicineRepository;
import com.example.Pill_Reminder_App.data.dto.DoseTimeDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddMedicineActivity extends AppCompatActivity {
    private int currentStep = 1;
    private int stepAmount = 7;
    private Button btnNext;
    private ImageButton btnBack;
    private MedicineService medicineService;
    private MedicineDTO medicineDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);

        // Service ve DTO'yu başlat
        MedicineRepository repository = new MedicineRepository();
        medicineService = new MedicineService(repository);
        medicineDTO = new MedicineDTO();
        medicineDTO.setDoseTimes(new ArrayList<>());

        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);

        btnNext.setOnClickListener(v -> nextStep());
        btnBack.setOnClickListener(v -> previousStep());

        // İlk adımı göster
        showStep(currentStep);
    }

    private void showStep(int step) {
        Fragment fragment;
        String titleText = "";
        switch (step) {
            case 1:
                fragment = new AddMedicineStep1Fragment();
                titleText = "Hangi ilacı eklemek istiyorsunuz?";
                break;
            case 2:
                fragment = new AddMedicineStep2Fragment();
                titleText = "İlacın şekli nedir?";
                break;
            case 3:
                fragment = new AddMedicineStep3Fragment();
                titleText = "Hangi sıklıkta kullanılır?";
                break;
            case 4:
                fragment = new AddMedicineStep4Fragment();
                titleText = "Başlangıç tarihi seçin";
                break;
            case 5:
                fragment = new AddMedicineStep5Fragment();
                titleText = "Doz ve saat bilgilerini kontrol edin";
                break;
            case 6:
                fragment = new AddMedicineStep6Fragment();
                titleText = "İlacınızı ne zaman alacaksınız?";
                break;
            case 7:
                fragment = new AddMedicineStep7Fragment();
                titleText = "İlaç başarıyla eklendi!";
                saveMedicine();
                break;
            default:
                return;
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();

        // Başlık metnini güncelle
        TextView stepTitle = findViewById(R.id.stepTitle);
        if (stepTitle != null) {
            stepTitle.setText(titleText);
        }

        // Adım dairelerini güncelle
        LinearLayout stepIndicators = findViewById(R.id.stepIndicators);
        if (stepIndicators != null) {
            for (int i = 0; i < stepIndicators.getChildCount(); i++) {
                View dot = stepIndicators.getChildAt(i);
                if (i < step) {
                    dot.setBackgroundResource(R.drawable.circle_filled);
                } else {
                    dot.setBackgroundResource(R.drawable.circle_empty);
                }
            }
        }

        // Butonların görünürlüğünü güncelle
        updateButtonVisibility();
    }

    private void updateButtonVisibility() {
        btnNext.setText(currentStep == stepAmount ? "Tamamla" : "İleri");
    }

    public void nextStep() {
        if (currentStep < stepAmount) {
            currentStep++;
            showStep(currentStep);
        } else {
            finish();
        }
    }

    public void previousStep() {
        if (currentStep > 1) {
            currentStep--;
            showStep(currentStep);
        }
    }

    private void saveMedicine() {
        medicineService.add(
                medicineDTO,
                unused -> {
                    // ✅ Başarılı işlem
                    Toast.makeText(this, "İlaç başarıyla kaydedildi!", Toast.LENGTH_SHORT).show();
                    finish(); // veya başka bir sayfaya geç
                },
                e -> {
                    // ❌ Hatalı işlem
                    Toast.makeText(this, "Hata oluştu: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
        );
    }


    // Fragment'lerden veri almak için metodlar
    public void setMedicineName(String name) {
        medicineDTO.setName(name);
    }

    public void setMedicineForm(String form) {
        medicineDTO.setForm(form);
    }

    public void setMedicineFrequency(String frequency) {
        medicineDTO.setFrequency(frequency);
    }

    public void setMedicineStartDate(Date startDate) {
        medicineDTO.setStartDate(startDate);
    }

    public void setMedicineTimeDoses(List<DoseTimeDTO> timeDoses) {
        medicineDTO.setDoseTimes(timeDoses);
    }

    public void setMedicineMealTime(String mealTime) {
        medicineDTO.setIntakeTime(mealTime);
    }
} 