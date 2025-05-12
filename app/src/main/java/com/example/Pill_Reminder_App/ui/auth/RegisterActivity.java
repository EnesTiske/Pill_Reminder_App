package com.example.Pill_Reminder_App.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Pill_Reminder_App.R;
import com.example.Pill_Reminder_App.utils.UserSessionManager;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private EditText nameInput;
    private EditText emailInput;
    private EditText passwordInput;
    private EditText confirmPasswordInput;
    private RadioGroup userTypeGroup;
    private RadioButton doctorRadio;
    private RadioButton patientRadio;
    private Button registerButton;
    private FirebaseFirestore db;
    private UserSessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        try {
            db = FirebaseFirestore.getInstance();
            sessionManager = new UserSessionManager(this);

            // Eğer kullanıcı zaten giriş yapmışsa ana sayfaya yönlendir
            if (sessionManager.isLoggedIn()) {
                String userType = sessionManager.getUserType();
                if ("doctor".equals(userType)) {
                    startActivity(new Intent(this, com.example.Pill_Reminder_App.ui.doctor.DoctorHomeActivity.class));
                } else {
                    startActivity(new Intent(this, com.example.Pill_Reminder_App.ui.patient.PatientHomeActivity.class));
                }
                finish();
                return;
            }

            // UI bileşenlerini başlat
            initializeViews();

            // Register butonuna tıklama olayını ekle
            registerButton.setOnClickListener(v -> register());
        } catch (Exception e) {
            Toast.makeText(this, "Bir hata oluştu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeViews() {
        try {
            nameInput = findViewById(R.id.nameInput);
            emailInput = findViewById(R.id.emailInput);
            passwordInput = findViewById(R.id.passwordInput);
            confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
            userTypeGroup = findViewById(R.id.userTypeGroup);
            doctorRadio = findViewById(R.id.doctorRadio);
            patientRadio = findViewById(R.id.patientRadio);
            registerButton = findViewById(R.id.registerButton);
        } catch (Exception e) {
            Toast.makeText(this, "Arayüz başlatılamadı: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void register() {
        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();

        // Form validasyonu
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Şifreler eşleşmiyor", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userTypeGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Lütfen kullanıcı tipini seçin", Toast.LENGTH_SHORT).show();
            return;
        }

        String userType = doctorRadio.isChecked() ? "doctor" : "patient";

        // Email ile kullanıcıyı kontrol et
        db.collection("users")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                if (!queryDocumentSnapshots.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Bu email adresi zaten kullanılıyor", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Şifreyi hashle
                String hashedPassword = hashPassword(password);
                if (hashedPassword == null) {
                    Toast.makeText(RegisterActivity.this, "Şifre hashlenemedi", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Yeni kullanıcı oluştur
                Map<String, Object> user = new HashMap<>();
                user.put("name", name);
                user.put("email", email);
                user.put("hashedPassword", hashedPassword);
                user.put("userType", userType);

                // Firestore'a kaydet
                db.collection("users")
                    .add(user)
                    .addOnSuccessListener(documentReference -> {
                        String userId = documentReference.getId();
                        
                        // Oturum bilgilerini kaydet
                        sessionManager.createLoginSession(userId, userType, email, name);

                        // Kullanıcı tipine göre yönlendir
                        if ("doctor".equals(userType)) {
                            startActivity(new Intent(RegisterActivity.this, com.example.Pill_Reminder_App.ui.doctor.DoctorHomeActivity.class));
                        } else {
                            // Hasta ayarlarını oluştur
                            Map<String, Object> userSettings = new HashMap<>();
                            userSettings.put("notificationsEnabled", true);
                            userSettings.put("theme", "light");
                            
                            db.collection("users").document(userId)
                                .collection("settings").document("preferences")
                                .set(userSettings)
                                .addOnSuccessListener(aVoid -> {
                                    startActivity(new Intent(RegisterActivity.this, com.example.Pill_Reminder_App.ui.patient.PatientHomeActivity.class));
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(RegisterActivity.this, "Ayarlar oluşturulamadı: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RegisterActivity.this, com.example.Pill_Reminder_App.ui.patient.PatientHomeActivity.class));
                                    finish();
                                });
                        }
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(RegisterActivity.this, "Kayıt oluşturulamadı: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
            })
            .addOnFailureListener(e -> {
                Toast.makeText(RegisterActivity.this, "Kayıt kontrolü yapılamadı: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
    }

    private String hashPassword(String password) {
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
} 