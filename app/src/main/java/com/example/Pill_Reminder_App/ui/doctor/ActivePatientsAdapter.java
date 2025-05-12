package com.example.Pill_Reminder_App.ui.doctor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.Pill_Reminder_App.R;
import com.example.Pill_Reminder_App.data.dto.UserDTO;
import java.util.List;

public class ActivePatientsAdapter extends RecyclerView.Adapter<ActivePatientsAdapter.PatientViewHolder> {
    private List<UserDTO> patients;

    public ActivePatientsAdapter(List<UserDTO> patients) {
        this.patients = patients;
    }

    @NonNull
    @Override
    public PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_active_patient, parent, false);
        return new PatientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientViewHolder holder, int position) {
        UserDTO patient = patients.get(position);
        holder.bind(patient);
    }

    @Override
    public int getItemCount() {
        return patients.size();
    }

    static class PatientViewHolder extends RecyclerView.ViewHolder {
        private TextView tvPatientName;
        private TextView tvMedicineCount;
        private TextView tvLastActivity;

        public PatientViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPatientName = itemView.findViewById(R.id.tvPatientName);
            tvMedicineCount = itemView.findViewById(R.id.tvMedicineCount);
            tvLastActivity = itemView.findViewById(R.id.tvLastActivity);
        }

        public void bind(UserDTO patient) {
            tvPatientName.setText(patient.getName());
            // TODO: İlaç sayısını ve son aktiviteyi hesapla ve göster
            tvMedicineCount.setText("Aktif İlaç: 0");
            tvLastActivity.setText("Son Aktivite: -");
        }
    }
} 