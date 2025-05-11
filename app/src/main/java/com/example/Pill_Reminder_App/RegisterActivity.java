package com.example.Pill_Reminder_App;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import com.example.Pill_Reminder_App.data.dto.UserDTO;
import com.example.Pill_Reminder_App.data.repository.UserRepository;
import com.example.Pill_Reminder_App.domain.service.UserService;

public class RegisterActivity extends AppCompatActivity {
    private EditText nameInput;
    private EditText emailInput;
    private EditText passwordInput;
    private EditText confirmPasswordInput;
    private Button registerButton;
    private TextView loginLink;
    private FirebaseFirestore db;
    private RadioGroup userTypeRadioGroup;
    private RadioButton radioDoctor, radioPatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = FirebaseFirestore.getInstance();

        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        registerButton = findViewById(R.id.registerButton);
        loginLink = findViewById(R.id.loginLink);
        userTypeRadioGroup = findViewById(R.id.userTypeRadioGroup);
        radioDoctor = findViewById(R.id.radioDoctor);
        radioPatient = findViewById(R.id.radioPatient);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameInput.getText().toString().trim();
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString();
                String confirmPassword = confirmPasswordInput.getText().toString();

                int selectedId = userTypeRadioGroup.getCheckedRadioButtonId();
                String userType;
                if (selectedId == R.id.radioDoctor) {
                    userType = "doctor";
                } else if (selectedId == R.id.radioPatient) {
                    userType = "patient";
                } else {
                    Toast.makeText(RegisterActivity.this, "Kullanıcı tipini seçin", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "Şifreler eşleşmiyor", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Şifre en az 6 karakter olmalıdır", Toast.LENGTH_SHORT).show();
                    return;
                }

                UserDTO userDTO = new UserDTO(name, email, password, userType);
                UserService userService = new UserService(new UserRepository());
                userService.add(userDTO,
                    unused -> {
                        Toast.makeText(RegisterActivity.this, "Kayıt başarılı!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    },
                    e -> Toast.makeText(RegisterActivity.this, "Kayıt başarısız: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


} 