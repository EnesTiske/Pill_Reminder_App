package com.example.Pill_Reminder_App;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.example.Pill_Reminder_App.utils.UserSessionManager;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private EditText emailInput;
    private EditText passwordInput;
    private Button loginButton;
    private TextView registerLink;
    private FirebaseFirestore db;
    private UserSessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = FirebaseFirestore.getInstance();
        sessionManager = new UserSessionManager(this);

        // Eğer kullanıcı zaten giriş yapmışsa ana sayfaya yönlendir
        if (sessionManager.isLoggedIn()) {
            String userType = sessionManager.getUserType();
            if ("doctor".equals(userType)) {
                startActivity(new Intent(this, com.example.Pill_Reminder_App.ui.doctor.DoctorHomeActivity.class));
            } else {
                startActivity(new Intent(this, MainActivity.class));
            }
            finish();
            return;
        }

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        registerLink = findViewById(R.id.registerLink);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Firestore'da email kontrolü yap
                db.collection("users")
                    .whereEqualTo("email", email)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (queryDocumentSnapshots.isEmpty()) {
                            // Email bulunamadı
                            Toast.makeText(LoginActivity.this, "Email veya şifre hatalı", Toast.LENGTH_SHORT).show();
                        } else {
                            // Email bulundu, şimdi şifre kontrolü yap
                            String hashedPassword = queryDocumentSnapshots.getDocuments().get(0).getString("hashedPassword");
                            String userId = queryDocumentSnapshots.getDocuments().get(0).getId();
                            String userName = queryDocumentSnapshots.getDocuments().get(0).getString("name");
                            
                            // Şifreyi hashle ve kontrol et
                            String inputHashedPassword = hashPassword(password);
                            if (inputHashedPassword != null && inputHashedPassword.equals(hashedPassword)) {
                                // Giriş başarılı
                                String userType = queryDocumentSnapshots.getDocuments().get(0).getString("userType");
                                
                                // Oturum bilgilerini kaydet
                                sessionManager.createLoginSession(userId, userType, email, userName);

                                if ("doctor".equals(userType)) {
                                    Intent intent = new Intent(LoginActivity.this, com.example.Pill_Reminder_App.ui.doctor.DoctorHomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // Kullanıcı ayarlarını kontrol et
                                    db.collection("users").document(userId)
                                        .collection("settings").document("preferences").get()
                                        .addOnSuccessListener(settingsSnapshot -> {
                                            if (!settingsSnapshot.exists()) {
                                                // Ayarlar yoksa varsayılan ayarları oluştur
                                                Map<String, Object> userSettings = new HashMap<>();
                                                userSettings.put("notificationsEnabled", true);
                                                userSettings.put("theme", "light");
                                                
                                                db.collection("users").document(userId)
                                                    .collection("settings").document("preferences")
                                                    .set(userSettings);
                                            }
                                            
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(LoginActivity.this, "Ayarlar yüklenemedi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                                }
                            } else {
                                // Şifre yanlış
                                Toast.makeText(LoginActivity.this, "Email veya şifre hatalı", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(LoginActivity.this, "Giriş başarısız: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
            }
        });

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
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