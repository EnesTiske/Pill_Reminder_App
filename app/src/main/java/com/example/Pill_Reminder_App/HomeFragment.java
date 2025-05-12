package com.example.Pill_Reminder_App;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.example.Pill_Reminder_App.data.repository.MedAlarmRepository;
import com.example.Pill_Reminder_App.data.dto.MedAlarmDTO;








public class HomeFragment extends Fragment {
    private static final String CHANNEL_ID = "pill_reminder_channel";
    private static final int NOTIFICATION_ID = 1;
    
    private Calendar selectedDate = Calendar.getInstance();
    private LinearLayout layoutDays;
    private TextView tvDayName, tvDate;
    private FloatingActionButton fab, fabAddMedicine, fabAddAlarm;

    private boolean isFabOpen = false;

    private MedAlarmRepository medAlarmRepository;












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
            Intent intent = new Intent(getActivity(), AddMedicineActivity.class);
            startActivity(intent);
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
        populateMedicineList(view);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNotificationChannel();
        medAlarmRepository = new MedAlarmRepository(requireContext());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "İlaç Hatırlatıcı";
            String description = "İlaç hatırlatma bildirimleri için kanal";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            
            NotificationManager notificationManager = requireContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void sendNotification(String medicineName, String amount) {
        Context context = requireContext();
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("İlaç Zamanı!")
                .setContentText(medicineName + " - " + amount + " almanız gerekiyor")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setVibrate(new long[]{0, 500, 200, 500})
                .setLights(Color.RED, 3000, 3000);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
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
            sendNotification("Aferin", "2 tablet");
        }

        isFabOpen = !isFabOpen;
    }

    private void updateCalendar() {
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", new Locale("tr", "TR"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy", new Locale("tr", "TR"));
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
                // Tarih değiştiğinde ilaç listesini güncelle
                populateMedicineList(getView());
            });

            layoutDays.addView(dayView);
        }
    }

    // TODO ----
    
    private boolean isSameDay(Calendar a, Calendar b) {
        return a.get(Calendar.YEAR)==b.get(Calendar.YEAR)
                && a.get(Calendar.DAY_OF_YEAR)==b.get(Calendar.DAY_OF_YEAR);
    }

    private void populateMedicineList(View rootView) {
        LinearLayout layoutMeds = rootView.findViewById(R.id.layoutMeds);
        if (layoutMeds == null) return;
        layoutMeds.removeAllViews();

        // Bugünün tarihini al
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = dateFormat.format(selectedDate.getTime());

        List<MedAlarmDTO> allMedAlarms = Arrays.asList(
            new MedAlarmDTO(1, "12:00", "Parol", "1 tablet", "2025-05-12", "PENDING", "user1"),
            new MedAlarmDTO(2, "12:00", "Aferin", "2 tablet", "2025-05-12", "PENDING", "user1"),
            new MedAlarmDTO(3, "12:00", "Vitamin C", "1 kapsül", "2025-05-12", "PENDING", "user1"),
            new MedAlarmDTO(4, "15:00", "Parol", "1 tablet", "2025-05-12", "PENDING", "user1"),
            new MedAlarmDTO(5, "15:00", "Aferin", "2 tablet", "2025-05-12", "PENDING", "user1"),
            new MedAlarmDTO(6, "15:00", "Vitamin D", "1 kapsül", "2025-05-12", "PENDING", "user1"),
            new MedAlarmDTO(7, "18:00", "Parol", "1 tablet", "2025-05-12", "PENDING", "user1"),
            new MedAlarmDTO(8, "18:00", "Aferin", "2 tablet", "2025-05-14", "PENDING", "user1"),
            new MedAlarmDTO(9, "18:00", "B12", "1 ampul", "2025-05-12", "PENDING", "user1"),
            new MedAlarmDTO(10, "21:00", "Parol", "1 tablet", "2025-05-13", "PENDING", "user1"),
            new MedAlarmDTO(11, "21:00", "Aferin", "2 tablet", "2025-05-14", "PENDING", "user1"),
            new MedAlarmDTO(12, "21:00", "Vitamin C", "1 kapsül", "2025-05-15", "PENDING", "user1")
        );

        // Seçili tarihe göre filtrele
        List<MedAlarmDTO> filteredMedAlarms = allMedAlarms.stream()
            .filter(alarm -> alarm.getDate().equals(currentDate))
            .collect(Collectors.toList());

        processMedicineList(filteredMedAlarms, layoutMeds);
    }

    private void processMedicineList(List<MedAlarmDTO> medAlarms, LinearLayout layoutMeds) {
        String lastTime = "";
        LayoutInflater inflater = LayoutInflater.from(getContext());
        
        // Bugünün tarihini al
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        
        for (MedAlarmDTO medAlarm : medAlarms) {
            if (!medAlarm.getTime().equals(lastTime)) {
                // Saat başlığı ekle
                View header = inflater.inflate(R.layout.med_time_header, layoutMeds, false);
                ((TextView)header.findViewById(R.id.tvTimeHeader)).setText(medAlarm.getTime());
                layoutMeds.addView(header);
                lastTime = medAlarm.getTime();
            }
            // İlaç kutusu ekle
            View medItem = inflater.inflate(R.layout.med_item, layoutMeds, false);
            TextView tvMedName = medItem.findViewById(R.id.tvMedName);
            TextView tvMedAmount = medItem.findViewById(R.id.tvMedAmount);
            tvMedName.setText(medAlarm.getMedicineName());
            tvMedAmount.setText(medAlarm.getAmount());
            CheckBox cbTaken = medItem.findViewById(R.id.cbTaken);
            
            // İlaç durumuna göre checkbox'ı ayarla
            cbTaken.setChecked(medAlarm.getState().equals("TAKEN"));
            
            cbTaken.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    tvMedName.setPaintFlags(tvMedName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    tvMedAmount.setPaintFlags(tvMedAmount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    medAlarm.setState("TAKEN");
                } else {
                    tvMedName.setPaintFlags(tvMedName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    tvMedAmount.setPaintFlags(tvMedAmount.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    
                    // Alarm tarihini Calendar nesnesine çevir
                    Calendar alarmDate = Calendar.getInstance();
                    String[] dateParts = medAlarm.getDate().split("-");
                    alarmDate.set(Integer.parseInt(dateParts[0]), 
                                Integer.parseInt(dateParts[1]) - 1, 
                                Integer.parseInt(dateParts[2]));
                    alarmDate.set(Calendar.HOUR_OF_DAY, 0);
                    alarmDate.set(Calendar.MINUTE, 0);
                    alarmDate.set(Calendar.SECOND, 0);
                    alarmDate.set(Calendar.MILLISECOND, 0);
                    
                    // Tarihi karşılaştır ve durumu güncelle
                    if (alarmDate.before(today)) {
                        medAlarm.setState("MISSED");
                    } else {
                        medAlarm.setState("PENDING");
                    }
                }
                // Veritabanı güncellemesi geçici olarak kaldırıldı
                // medAlarmRepository.updateState(medAlarm.getId(), medAlarm.getState());
            });
            layoutMeds.addView(medItem);
        }
    }
}