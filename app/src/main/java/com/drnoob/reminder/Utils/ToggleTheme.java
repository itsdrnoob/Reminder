package com.drnoob.reminder.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

public class ToggleTheme {
    public static void toggleTheme(Context context, SharedPreferences preferences) {
        preferences = context.getSharedPreferences("app_theme", Context.MODE_PRIVATE);
        Boolean switchState = preferences.getBoolean("dark_theme", false);

        if (switchState) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
