package com.example.Pill_Reminder_App.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSessionManager {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_TYPE = "userType";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_NAME = "name";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    public UserSessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    // Kullanıcı giriş yaptığında çağrılır
    public void createLoginSession(String userId, String userType, String email, String name) {
        try {
            editor.putBoolean(KEY_IS_LOGGED_IN, true);
            editor.putString(KEY_USER_ID, userId);
            editor.putString(KEY_USER_TYPE, userType);
            editor.putString(KEY_EMAIL, email);
            editor.putString(KEY_NAME, name);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Kullanıcı çıkış yaptığında çağrılır
    public void logout() {
        try {
            editor.clear();
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Kullanıcının giriş yapmış olup olmadığını kontrol eder
    public boolean isLoggedIn() {
        try {
            return pref.getBoolean(KEY_IS_LOGGED_IN, false);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Kullanıcı ID'sini döndürür
    public String getUserId() {
        try {
            return pref.getString(KEY_USER_ID, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Kullanıcı tipini döndürür (doctor/patient)
    public String getUserType() {
        try {
            return pref.getString(KEY_USER_TYPE, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Kullanıcının doktor olup olmadığını kontrol eder
    public boolean isDoctor() {
        try {
            String userType = getUserType();
            return userType != null && "doctor".equals(userType);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Kullanıcının hasta olup olmadığını kontrol eder
    public boolean isPatient() {
        try {
            String userType = getUserType();
            return userType != null && "patient".equals(userType);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Kullanıcı email'ini döndürür
    public String getUserEmail() {
        try {
            return pref.getString(KEY_EMAIL, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Kullanıcı adını döndürür
    public String getUserName() {
        try {
            return pref.getString(KEY_NAME, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
} 