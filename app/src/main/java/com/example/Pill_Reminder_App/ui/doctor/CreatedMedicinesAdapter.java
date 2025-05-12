package com.example.Pill_Reminder_App.ui.doctor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.Pill_Reminder_App.R;
import com.example.Pill_Reminder_App.data.dto.MedicineDTO;
import java.util.List;

public class CreatedMedicinesAdapter extends RecyclerView.Adapter<CreatedMedicinesAdapter.MedicineViewHolder> {
    private List<MedicineDTO> medicines;

    public CreatedMedicinesAdapter(List<MedicineDTO> medicines) {
        this.medicines = medicines;
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_created_medicine, parent, false);
        return new MedicineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
        MedicineDTO medicine = medicines.get(position);
        holder.bind(medicine);
    }

    @Override
    public int getItemCount() {
        return medicines.size();
    }

    static class MedicineViewHolder extends RecyclerView.ViewHolder {
        private TextView tvMedicineName;
        private TextView tvMedicineCode;
        private TextView tvPatientCount;

        public MedicineViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMedicineName = itemView.findViewById(R.id.tvMedicineName);
            tvMedicineCode = itemView.findViewById(R.id.tvMedicineCode);
            tvPatientCount = itemView.findViewById(R.id.tvPatientCount);
        }

        public void bind(MedicineDTO medicine) {
            tvMedicineName.setText(medicine.getName());
            tvMedicineCode.setText("Kod: " + medicine.getCode());
            // TODO: Hasta sayısını hesapla ve göster
            tvPatientCount.setText("Kullanan Hasta: 0");
        }
    }
} 