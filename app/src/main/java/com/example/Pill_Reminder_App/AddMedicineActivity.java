package com.example.Pill_Reminder_App;

import android.content.Intent;
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
import androidx.fragment.app.FragmentManager;

import com.example.Pill_Reminder_App.data.dto.MedicineDTO;
import com.example.Pill_Reminder_App.domain.service.MedicineService;
import com.example.Pill_Reminder_App.data.repository.MedicineRepository;
import com.example.Pill_Reminder_App.data.dto.DoseTimeDTO;
import com.example.Pill_Reminder_App.utils.UserSessionManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class AddMedicineActivity extends AppCompatActivity {
    private int currentStep = 1;
    private int stepAmount = 7;
    private Button btnNext;
    private ImageButton btnBack;
    private MedicineService medicineService;
    private MedicineDTO medicineDTO;
    private String medicineCode;
    private UserSessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);

        // Session manager'ı başlat
        sessionManager = new UserSessionManager(this);

        // Eğer kullanıcı giriş yapmamışsa login ekranına yönlendir
        if (!sessionManager.isLoggedIn()) {
            Toast.makeText(this, "Lütfen önce giriş yapın", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // Service ve DTO'yu başlat
        MedicineRepository repository = new MedicineRepository();
        medicineService = new MedicineService(repository);
        medicineDTO = new MedicineDTO();
        medicineDTO.setDoseTimes(new ArrayList<>());

        // Doktor ID'sini DTO'ya ekle
        medicineDTO.setDoctorId(sessionManager.getUserId());

        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);

        btnNext.setOnClickListener(v -> nextStep());
        btnBack.setOnClickListener(v -> previousStep());

        // İlk adımı göster
        showStep(currentStep);
    }

    private void showStep(int step) {
        AtomicReference<Fragment> fragment = new AtomicReference<>();
        final String[] titleText = {""};
        switch (step) {
            case 1:
                fragment.set(new AddMedicineStep1Fragment());
                titleText[0] = "Hangi ilacı eklemek istiyorsunuz?";
                break;
            case 2:
                fragment.set(new AddMedicineStep2Fragment());
                titleText[0] = "İlacın şekli nedir?";
                break;
            case 3:
                fragment.set(new AddMedicineStep3Fragment());
                titleText[0] = "Hangi sıklıkta kullanılır?";
                break;
            case 4:
                fragment.set(new AddMedicineStep4Fragment());
                titleText[0] = "Başlangıç tarihi seçin";
                break;
            case 5:
                fragment.set(new AddMedicineStep5Fragment());
                titleText[0] = "Doz ve saat bilgilerini kontrol edin";
                break;
            case 6:
                fragment.set(new AddMedicineStep6Fragment());
                titleText[0] = "İlacınızı ne zaman alacaksınız?";
                break;
            case 7:
                // İlaç kodunu oluştur
                medicineCode = generateMedicineCode();
                medicineDTO.setCode(medicineCode);
                
                // İlaç detaylarını güncelle
                medicineDTO.setName(medicineDTO.getName());
                medicineDTO.setForm(medicineDTO.getForm());
                medicineDTO.setFrequency(medicineDTO.getFrequency());
                medicineDTO.setStartDate(medicineDTO.getStartDate());
                medicineDTO.setDoseTimes(medicineDTO.getDoseTimes());
                medicineDTO.setIntakeTime(medicineDTO.getIntakeTime());
                
                // Veritabanına kaydet
                medicineService.add(
                    medicineDTO,
                    unused -> {
                        // Başarılı kayıt sonrası fragment'i göster
                        fragment.set(new AddMedicineStep7Fragment());
                        titleText[0] = "İlaç başarıyla eklendi!";
                        
                        if (fragment.get() != null) {
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            transaction.replace(R.id.fragmentContainer, fragment.get());
                            transaction.commit();
                        }
                    },
                    e -> Toast.makeText(this, "Hata oluştu: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
                return;
        }

        if (fragment.get() != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragmentContainer, fragment.get());
            transaction.commit();
        }

        // Başlık metnini güncelle
        TextView stepTitle = findViewById(R.id.stepTitle);
        if (stepTitle != null) {
            stepTitle.setText(titleText[0]);
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

    private void nextStep() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if (currentFragment instanceof AddMedicineStep1Fragment) {
            if (((AddMedicineStep1Fragment) currentFragment).isStepValid()) {
                currentStep++;
                showStep(currentStep);
            }
        } else if (currentFragment instanceof AddMedicineStep2Fragment) {
            if (((AddMedicineStep2Fragment) currentFragment).isStepValid()) {
                currentStep++;
                showStep(currentStep);
            }
        } else if (currentFragment instanceof AddMedicineStep3Fragment) {
            if (((AddMedicineStep3Fragment) currentFragment).isStepValid()) {
                currentStep++;
                showStep(currentStep);
            }
        } else if (currentFragment instanceof AddMedicineStep4Fragment) {
            if (((AddMedicineStep4Fragment) currentFragment).isStepValid()) {
                currentStep++;
                showStep(currentStep);
            }
        } else if (currentFragment instanceof AddMedicineStep5Fragment) {
            if (((AddMedicineStep5Fragment) currentFragment).isStepValid()) {
                currentStep++;
                showStep(currentStep);
            }
        } else if (currentFragment instanceof AddMedicineStep6Fragment) {
            if (((AddMedicineStep6Fragment) currentFragment).isStepValid()) {
                currentStep++;
                showStep(currentStep);
            }
        } else if (currentFragment instanceof AddMedicineStep7Fragment) {
            if (((AddMedicineStep7Fragment) currentFragment).isStepValid()) {
                saveMedicine();
            }
        }
    }

    private void previousStep() {
        if (currentStep > 1) {
            currentStep--;
            showStep(currentStep);
        }
    }

    private String generateMedicineCode() {
        // Benzersiz bir kod oluştur (örnek: MED-XXXX-YYYY)
        String timestamp = String.valueOf(System.currentTimeMillis());
        String random = String.format("%04d", (int) (Math.random() * 10000));
        return "MED-" + timestamp.substring(timestamp.length() - 4) + "-" + random;
    }

    public String getMedicineCode() {
        return medicineCode;
    }

    private void saveMedicine() {
        // Bu metot artık kullanılmıyor, tüm işlemler showStep(7) içinde yapılıyor
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

    public MedicineDTO getMedicineDTO() {
        return medicineDTO;
    }
} 