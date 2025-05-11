package com.example.Pill_Reminder_App;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class StatisticsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        populateStatistics(view);
        return view;
    }

    private void populateStatistics(View rootView) {
        LinearLayout layoutStatistics = rootView.findViewById(R.id.layoutStatistics);
        layoutStatistics.removeAllViews();

        // Örnek veri: gün, ilaçlar (isim, alınan, alınması gereken)
        class MedRow {
            String name;
            int taken;
            int shouldTake;
            MedRow(String n, int taken, int shouldTake) { this.name = n; this.taken = taken; this.shouldTake = shouldTake; }
        }
        class DayStat {
            String day;
            List<MedRow> meds;
            DayStat(String d, List<MedRow> m) { day = d; meds = m; }
        }
        List<DayStat> days = Arrays.asList(
            new DayStat("Pazartesi", Arrays.asList(
                new MedRow("Parol 1 tablet", 1, 1),
                new MedRow("Aferin 2 tablet", 1, 2),
                new MedRow("Vitamin C 1 kapsül", 0, 1)
            )),
            new DayStat("Salı", Arrays.asList(
                new MedRow("Parol 1 tablet", 0, 1),
                new MedRow("Aferin 2 tablet", 0, 2),
                new MedRow("Vitamin C 1 kapsül", 0, 1)
            )),
            new DayStat("Çarşamba", Arrays.asList(
                new MedRow("Parol 1 tablet", 1, 1),
                new MedRow("Aferin 2 tablet", 2, 2),
                new MedRow("Vitamin C 1 kapsül", 1, 1)
            ))
        );

        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (DayStat day : days) {
            View dayItem = inflater.inflate(R.layout.statistics_day_item, layoutStatistics, false);
            ((TextView)dayItem.findViewById(R.id.tvDayTitle)).setText(day.day);

            // Bar ayarı
            View barGreen = dayItem.findViewById(R.id.barGreen);
            View barRed = dayItem.findViewById(R.id.barRed);
            int total = 0;
            int taken = 0;
            for (MedRow m : day.meds) {
                total += m.shouldTake;
                taken += m.taken;
            }
            float percent = total == 0 ? 0 : (float)taken / total;
            barGreen.post(() -> {
                int width = barRed.getWidth();
                barGreen.getLayoutParams().width = (int)(width * percent);
                barGreen.requestLayout();
            });

            // İlaç satırları
            LinearLayout layoutDayMeds = dayItem.findViewById(R.id.layoutDayMedicines);
            for (MedRow med : day.meds) {
                View medRow = inflater.inflate(R.layout.statistics_medicine_row, layoutDayMeds, false);
                ((TextView)medRow.findViewById(R.id.tvMedicineName)).setText(med.name + " (" + med.taken + "/" + med.shouldTake + ")");
                View statusDot = medRow.findViewById(R.id.statusDot);
                statusDot.setBackgroundResource(med.taken == med.shouldTake ? android.R.color.holo_green_dark : android.R.color.holo_red_dark);
                // Mini bar ayarı
                View miniBarGreen = medRow.findViewById(R.id.miniBarGreen);
                View miniBarRed = medRow.findViewById(R.id.miniBarRed);
                float miniPercent = med.shouldTake == 0 ? 0 : (float)med.taken / med.shouldTake;
                miniBarGreen.post(() -> {
                    int width = miniBarRed.getWidth();
                    miniBarGreen.getLayoutParams().width = (int)(width * Math.min(miniPercent, 1f));
                    miniBarGreen.requestLayout();
                });
                layoutDayMeds.addView(medRow);
            }
            layoutStatistics.addView(dayItem);
        }
    }
} 