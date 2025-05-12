package com.example.Pill_Reminder_App;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.Pill_Reminder_App.data.dto.MedicineDTO;
import com.example.Pill_Reminder_App.utils.UserSessionManager;

public class AddMedicineStep7Fragment extends Fragment {
    private View view;
    private TextView tvMedicineCode;
    private TextView tvMedicineDetails;
    private Button btnGoHome;
    private UserSessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_medicine_step7, container, false);
        
        sessionManager = new UserSessionManager(requireContext());
        
        tvMedicineCode = view.findViewById(R.id.tv_medicine_code);
        tvMedicineDetails = view.findViewById(R.id.tv_medicine_details);
        btnGoHome = view.findViewById(R.id.btnGoHome);

        // Kullanıcı tipi kontrolü
        if (!sessionManager.isDoctor()) {
            Toast.makeText(getContext(), "İlaç başarıyla eklendi", Toast.LENGTH_LONG).show();
            getActivity().finish();
            return view;
        }

        // İlaç kodunu göster
        if (getActivity() instanceof AddMedicineActivity) {
            String medicineCode = ((AddMedicineActivity) getActivity()).getMedicineCode();
            if (medicineCode != null) {
                tvMedicineCode.setText("İlaç Kodu: " + medicineCode);
            }

            // İlaç detaylarını göster
            String details = buildMedicineDetails();
            tvMedicineDetails.setText(details);
        }

        // Ana sayfaya dön butonu
        btnGoHome.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), com.example.Pill_Reminder_App.ui.doctor.DoctorHomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            getActivity().finish();
        });

        return view;
    }

    private String buildMedicineDetails() {
        if (getActivity() instanceof AddMedicineActivity) {
            AddMedicineActivity activity = (AddMedicineActivity) getActivity();
            StringBuilder details = new StringBuilder();
            
            details.append("İlaç Adı: ").append(activity.getMedicineDTO().getName()).append("\n");
            details.append("Form: ").append(activity.getMedicineDTO().getForm()).append("\n");
            details.append("Sıklık: ").append(activity.getMedicineDTO().getFrequency()).append("\n");
            details.append("Başlangıç Tarihi: ").append(activity.getMedicineDTO().getStartDate()).append("\n");
            details.append("Alım Zamanı: ").append(activity.getMedicineDTO().getIntakeTime());
            
            return details.toString();
        }
        return "";
    }

    public boolean isStepValid() {
        return true; // Son adım olduğu için her zaman true döndür
    }
}