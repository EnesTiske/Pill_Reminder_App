package com.example.Pill_Reminder_App;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class HomeFragment extends Fragment {
    private Calendar selectedDate = Calendar.getInstance();
    private LinearLayout layoutDays;
    private TextView tvDayName, tvDate;
    private FloatingActionButton fab, fabAddMedicine, fabAddAlarm;

    private LinearLayout layoutAddMedicine, layoutAddAlarm;

    private boolean isFabOpen = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        layoutDays = view.findViewById(R.id.layoutDays);
        tvDayName = view.findViewById(R.id.tvDayName);
        tvDate = view.findViewById(R.id.tvDate);

        // FAB'ları başlat
        fab = view.findViewById(R.id.fab);
        fabAddMedicine = view.findViewById(R.id.fabAddMedicine);
        fabAddAlarm = view.findViewById(R.id.fabAddAlarm);

        // FAB tıklama olayı
        fab.setOnClickListener(v -> toggleFab());

        // Alt FAB'ların tıklama olayları
        fabAddMedicine.setOnClickListener(v -> {
            // İlaç ekleme işlemi
            toggleFab();
        });

        fabAddAlarm.setOnClickListener(v -> {
            // Alarm ekleme işlemi
            toggleFab();
        });

        Button btnToday = view.findViewById(R.id.btnToday);
        ImageButton btnPrevWeek = view.findViewById(R.id.btnPrevWeek);
        ImageButton btnNextWeek = view.findViewById(R.id.btnNextWeek);

        btnToday.setOnClickListener(v -> {
            selectedDate = Calendar.getInstance();
            updateCalendar();
        });

        btnPrevWeek.setOnClickListener(v -> {
            selectedDate.add(Calendar.DATE, -7);
            updateCalendar();
        });

        btnNextWeek.setOnClickListener(v -> {
            selectedDate.add(Calendar.DATE, 7);
            updateCalendar();
        });

        updateCalendar();
        return view;
    }

    private void toggleFab() {
        if (isFabOpen) {
            // KAPANMA animasyonları
            AnimatorSet animatorSet = new AnimatorSet();

            ObjectAnimator scaleX = ObjectAnimator.ofFloat(fabAddMedicine, "scaleX", 1f, 0f);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(fabAddMedicine, "scaleY", 1f, 0f);
            ObjectAnimator alpha = ObjectAnimator.ofFloat(fabAddMedicine, "alpha", 1f, 0f);
            ObjectAnimator translateY = ObjectAnimator.ofFloat(fabAddMedicine, "translationY", -480f, 0f);

            ObjectAnimator scaleX2 = ObjectAnimator.ofFloat(fabAddAlarm, "scaleX", 1f, 0f);
            ObjectAnimator scaleY2 = ObjectAnimator.ofFloat(fabAddAlarm, "scaleY", 1f, 0f);
            ObjectAnimator alpha2 = ObjectAnimator.ofFloat(fabAddAlarm, "alpha", 1f, 0f);
            ObjectAnimator translateY2 = ObjectAnimator.ofFloat(fabAddAlarm, "translationY", -240f, 0f);

            animatorSet.playTogether(scaleX, scaleY, alpha, translateY, scaleX2, scaleY2, alpha2, translateY2);
            animatorSet.setDuration(300);
            animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());

            // Animasyon bitince görünmez yap
            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    fabAddMedicine.setVisibility(View.GONE);
                    fabAddAlarm.setVisibility(View.GONE);
                }
            });

            animatorSet.start();
            fab.animate().rotation(0f).setDuration(300).start();

        } else {
            // AÇILMA animasyonları
            fabAddMedicine.setVisibility(View.VISIBLE);
            fabAddAlarm.setVisibility(View.VISIBLE);

            fabAddMedicine.setAlpha(0f);
            fabAddAlarm.setAlpha(0f);
            fabAddMedicine.setScaleX(0f);
            fabAddMedicine.setScaleY(0f);
            fabAddAlarm.setScaleX(0f);
            fabAddAlarm.setScaleY(0f);

            AnimatorSet animatorSet = new AnimatorSet();

            ObjectAnimator scaleX = ObjectAnimator.ofFloat(fabAddMedicine, "scaleX", 0f, 1f);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(fabAddMedicine, "scaleY", 0f, 1f);
            ObjectAnimator alpha = ObjectAnimator.ofFloat(fabAddMedicine, "alpha", 0f, 1f);
            ObjectAnimator translateY = ObjectAnimator.ofFloat(fabAddMedicine, "translationY", 0f, -480f);

            ObjectAnimator scaleX2 = ObjectAnimator.ofFloat(fabAddAlarm, "scaleX", 0f, 1f);
            ObjectAnimator scaleY2 = ObjectAnimator.ofFloat(fabAddAlarm, "scaleY", 0f, 1f);
            ObjectAnimator alpha2 = ObjectAnimator.ofFloat(fabAddAlarm, "alpha", 0f, 1f);
            ObjectAnimator translateY2 = ObjectAnimator.ofFloat(fabAddAlarm, "translationY", 0f, -240f);

            animatorSet.playTogether(scaleX, scaleY, alpha, translateY, scaleX2, scaleY2, alpha2, translateY2);
            animatorSet.setDuration(300);
            animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
            animatorSet.start();

            fab.animate().rotation(45f).setDuration(300).start();
        }

        isFabOpen = !isFabOpen;
    }



    private void updateCalendar() {

        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault());
        tvDate.setText(dateFormat.format(selectedDate.getTime()));
        tvDayName.setText(dayFormat.format(selectedDate.getTime()));

        layoutDays.removeAllViews();
        Calendar weekStart = (Calendar) selectedDate.clone();
        weekStart.set(Calendar.DAY_OF_WEEK, weekStart.getFirstDayOfWeek());

        // DP -> PX
        final float scale = getResources().getDisplayMetrics().density;
        int sizePx = (int)(36 * scale + 0.5f);
        int marginPx = (int)(4 * scale + 0.5f);

        for (int i = 0; i < 7; i++) {
            Calendar day = (Calendar) weekStart.clone();
            day.add(Calendar.DATE, i);

            TextView dayView = new TextView(getContext());
            dayView.setText(String.valueOf(day.get(Calendar.DAY_OF_MONTH)));
            dayView.setGravity(Gravity.CENTER);

            // Eşit sütun genişliği için weight yerine sabit drawable boyutu kullanıyoruz:
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(sizePx, sizePx);
            p.setMargins(marginPx, 0, marginPx, 0);
            dayView.setLayoutParams(p);

            if (isSameDay(day, selectedDate)) {
                dayView.setBackgroundResource(R.drawable.circle_bg);
                dayView.setTextColor(Color.WHITE);
            } else {
                dayView.setBackgroundColor(Color.TRANSPARENT);
                dayView.setTextColor(Color.BLACK);
            }

            final int off = i;
            dayView.setOnClickListener(v -> {
                selectedDate = (Calendar) weekStart.clone();
                selectedDate.add(Calendar.DATE, off);
                updateCalendar();
            });

            layoutDays.addView(dayView);
        }
    }

    private boolean isSameDay(Calendar a, Calendar b) {
        return a.get(Calendar.YEAR)==b.get(Calendar.YEAR)
                && a.get(Calendar.DAY_OF_YEAR)==b.get(Calendar.DAY_OF_YEAR);
    }

}