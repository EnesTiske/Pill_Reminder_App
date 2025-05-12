package com.example.Pill_Reminder_App.ui.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Pill_Reminder_App.LoginActivity;
import com.example.Pill_Reminder_App.MainActivity;
import com.example.Pill_Reminder_App.R;
import com.example.Pill_Reminder_App.data.dto.MedicineDTO;
import com.example.Pill_Reminder_App.data.dto.UserDTO;
import com.example.Pill_Reminder_App.data.repository.MedicineRepository;
import com.example.Pill_Reminder_App.data.repository.UserRepository;
import com.example.Pill_Reminder_App.domain.service.MedicineService;
import com.example.Pill_Reminder_App.domain.service.UserService;
import com.example.Pill_Reminder_App.utils.UserSessionManager;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class DoctorHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private UserSessionManager sessionManager;
    private MedicineService medicineService;
    private UserService userService;
    private RecyclerView rvCreatedMedicines;
    private RecyclerView rvActivePatients;
    private TextView tvDoctorName;
    private Button btnAddMedicine;
    private Button btnViewStatistics;
    private CreatedMedicinesAdapter medicinesAdapter;
    private ActivePatientsAdapter patientsAdapter;
    private List<MedicineDTO> createdMedicines;
    private List<UserDTO> activePatients;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_home);

        try {
            // Session manager'ı başlat
            sessionManager = new UserSessionManager(this);

            // Session kontrolü
            if (!sessionManager.isLoggedIn()) {
                Toast.makeText(this, "Lütfen giriş yapın", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return;
            }

            if (!sessionManager.isDoctor()) {
                Toast.makeText(this, "Bu sayfaya sadece doktorlar erişebilir", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return;
            }

            String doctorId = sessionManager.getUserId();
            if (doctorId == null || doctorId.isEmpty()) {
                Toast.makeText(this, "Doktor bilgisi bulunamadı, lütfen tekrar giriş yapın", Toast.LENGTH_SHORT).show();
                sessionManager.logout();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return;
            }

            // Service'leri başlat
            medicineService = new MedicineService(new MedicineRepository());
            userService = new UserService(new UserRepository());

            // UI bileşenlerini başlat
            initializeViews();

            // Verileri yükle
            loadDoctorData();
            loadCreatedMedicines();
            loadActivePatients();

            // Toolbar ve Navigation Drawer'ı ayarla
            setupNavigationDrawer();
        } catch (Exception e) {
            Toast.makeText(this, "Bir hata oluştu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    private void setupNavigationDrawer() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        drawerToggle = new ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        );

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Ana sayfa zaten açık
        } else if (id == R.id.nav_patients) {
            // Hasta listesi sayfasına git
            startActivity(new Intent(this, PatientListActivity.class));
        } else if (id == R.id.nav_medicines) {
            // İlaç listesi sayfasına git
            startActivity(new Intent(this, MedicineListActivity.class));
        } else if (id == R.id.nav_statistics) {
            // İstatistik sayfasına git
            startActivity(new Intent(this, DoctorStatisticsActivity.class));
        } else if (id == R.id.nav_logout) {
            logout();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initializeViews() {
        try {
            tvDoctorName = findViewById(R.id.tvDoctorName);
            btnAddMedicine = findViewById(R.id.btnAddMedicine);
            btnViewStatistics = findViewById(R.id.btnViewStatistics);
            rvCreatedMedicines = findViewById(R.id.rvCreatedMedicines);
            rvActivePatients = findViewById(R.id.rvActivePatients);

            // RecyclerView'ları ayarla
            rvCreatedMedicines.setLayoutManager(new LinearLayoutManager(this));
            rvActivePatients.setLayoutManager(new LinearLayoutManager(this));

            createdMedicines = new ArrayList<>();
            activePatients = new ArrayList<>();

            medicinesAdapter = new CreatedMedicinesAdapter(createdMedicines);
            patientsAdapter = new ActivePatientsAdapter(activePatients);

            rvCreatedMedicines.setAdapter(medicinesAdapter);
            rvActivePatients.setAdapter(patientsAdapter);

            // Buton click listener'ları
            btnAddMedicine.setOnClickListener(v -> {
                try {
                    startActivity(new Intent(this, com.example.Pill_Reminder_App.AddMedicineActivity.class));
                } catch (Exception e) {
                    Toast.makeText(this, "İlaç ekleme sayfası açılamadı: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            btnViewStatistics.setOnClickListener(v -> {
                try {
                    startActivity(new Intent(this, DoctorStatisticsActivity.class));
                } catch (Exception e) {
                    Toast.makeText(this, "İstatistik sayfası açılamadı: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Arayüz başlatılamadı: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadDoctorData() {
        try {
            String doctorId = sessionManager.getUserId();
            if (doctorId == null || doctorId.isEmpty()) {
                tvDoctorName.setText("Doktor Bilgisi");
                Toast.makeText(this, "Doktor bilgisi bulunamadı", Toast.LENGTH_SHORT).show();
                return;
            }

            userService.getUser(doctorId, 
                user -> {
                    try {
                        if (user != null) {
                            tvDoctorName.setText("Dr. " + user.getName());
                        } else {
                            tvDoctorName.setText("Doktor Bilgisi");
                            Toast.makeText(this, "Henüz doktor bilgisi girilmemiş", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        tvDoctorName.setText("Doktor Bilgisi");
                        Toast.makeText(this, "Doktor bilgileri işlenemedi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                e -> {
                    tvDoctorName.setText("Doktor Bilgisi");
                    Toast.makeText(this, "Doktor bilgileri yüklenemedi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            );
        } catch (Exception e) {
            tvDoctorName.setText("Doktor Bilgisi");
            Toast.makeText(this, "Doktor bilgileri yüklenemedi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void loadCreatedMedicines() {
        try {
            String doctorId = sessionManager.getUserId();
            if (doctorId == null || doctorId.isEmpty()) {
                Toast.makeText(this, "Doktor bilgisi bulunamadı", Toast.LENGTH_SHORT).show();
                return;
            }

            medicineService.getMedicines(doctorId,
                medicines -> {
                    try {
                        if (medicines != null && !medicines.isEmpty()) {
                            createdMedicines.clear();
                            createdMedicines.addAll(medicines);
                            medicinesAdapter.notifyDataSetChanged();
                        } else {
                            createdMedicines.clear();
                            medicinesAdapter.notifyDataSetChanged();
                            Toast.makeText(this, "Henüz ilaç eklenmemiş", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        createdMedicines.clear();
                        medicinesAdapter.notifyDataSetChanged();
                        Toast.makeText(this, "İlaç listesi işlenemedi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                e -> {
                    createdMedicines.clear();
                    medicinesAdapter.notifyDataSetChanged();
                    Toast.makeText(this, "İlaçlar yüklenemedi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            );
        } catch (Exception e) {
            createdMedicines.clear();
            medicinesAdapter.notifyDataSetChanged();
            Toast.makeText(this, "İlaçlar yüklenemedi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void loadActivePatients() {
        try {
            String doctorId = sessionManager.getUserId();
            if (doctorId == null || doctorId.isEmpty()) {
                Toast.makeText(this, "Doktor bilgisi bulunamadı", Toast.LENGTH_SHORT).show();
                return;
            }

            userService.getPatients(doctorId,
                patients -> {
                    try {
                        if (patients != null && !patients.isEmpty()) {
                            activePatients.clear();
                            activePatients.addAll(patients);
                            patientsAdapter.notifyDataSetChanged();
                        } else {
                            activePatients.clear();
                            patientsAdapter.notifyDataSetChanged();
                            Toast.makeText(this, "Henüz hasta kaydı bulunmuyor", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        activePatients.clear();
                        patientsAdapter.notifyDataSetChanged();
                        Toast.makeText(this, "Hasta listesi işlenemedi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                e -> {
                    activePatients.clear();
                    patientsAdapter.notifyDataSetChanged();
                    Toast.makeText(this, "Hasta listesi yüklenemedi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            );
        } catch (Exception e) {
            activePatients.clear();
            patientsAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Hasta listesi yüklenemedi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            // Sayfa her açıldığında verileri yenile
            loadCreatedMedicines();
            loadActivePatients();
        } catch (Exception e) {
            Toast.makeText(this, "Veriler yenilenemedi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void logout() {
        sessionManager.logout();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
} 