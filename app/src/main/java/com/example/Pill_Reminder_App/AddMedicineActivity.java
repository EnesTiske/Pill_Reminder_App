package com.example.Pill_Reminder_App;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class AddMedicineActivity extends AppCompatActivity {
    private int currentStep = 1;
    private int stepAmount = 7;
    private Button btnNext;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);

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
} 