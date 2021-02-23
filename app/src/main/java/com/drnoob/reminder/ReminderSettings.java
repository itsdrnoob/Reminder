package com.drnoob.reminder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class ReminderSettings extends AppCompatActivity {

    public static Boolean mSwitchStateChanged;
    private static LinearLayout mToneView;
    SharedPreferences preferences;
    String currentTheme;
    private ImageButton mBack;
    private Switch mAppthemeSwitch;
    private TextView mCurrentTheme, mToneSelect, mToneTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences("app_theme", MODE_PRIVATE);
        Boolean switchState = preferences.getBoolean("dark_theme", false);
        setContentView(R.layout.activity_reminder_settings);

        mBack = (ImageButton) findViewById(R.id.back);
        mToneSelect = (TextView) findViewById(R.id.tone_select);
        mAppthemeSwitch = (Switch) findViewById(R.id.app_theme_switch);
        preferences = getSharedPreferences("app_theme", MODE_PRIVATE);
        mCurrentTheme = (TextView) findViewById(R.id.current_theme);
        SharedPreferences.Editor editor = preferences.edit();
        mSwitchStateChanged = false;
        mToneTV = (TextView) findViewById(R.id.tone_selector_layout_tv);
        mToneView = (LinearLayout) findViewById(R.id.tone_selector_layout);

        currentTheme = preferences.getString("current_theme", "Currently set to light Theme");
        mCurrentTheme.setText(currentTheme);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(ReminderSettings.this, MainActivity.class));
                finish();
            }
        });

//        Boolean switchState = preferences.getBoolean("dark_theme", false);
        mAppthemeSwitch.setChecked(switchState);
        mAppthemeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    currentTheme = String.format(getResources().getString(R.string.current_theme), "dark");
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    currentTheme = String.format(getResources().getString(R.string.current_theme), "light");
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
//                TaskStackBuilder.create(ReminderSettings.this)
//                        .addNextIntent(new Intent(ReminderSettings.this, MainActivity.class))
//                        .addNextIntent(ReminderSettings.this.getIntent())
//                        .startActivities();
                mSwitchStateChanged = true;
                editor.putBoolean("dark_theme", isChecked);
                editor.putString("current_theme", currentTheme);
                editor.commit();

            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mToneSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent toneSettingsIntent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                    toneSettingsIntent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                    toneSettingsIntent.putExtra(Settings.EXTRA_CHANNEL_ID, "com.drnoob.reminder");
                    startActivity(toneSettingsIntent);

                }
            });
        } else {
            mToneView.setVisibility(View.GONE);
            mToneTV.setVisibility(View.GONE);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        overridePendingTransition(R.anim.slide_out_right, R.anim.slide_out_right);
    }
}