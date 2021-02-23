package com.drnoob.reminder;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.drnoob.reminder.Adapter.ReminderAdapter;
import com.drnoob.reminder.Models.ReminderModel;
import com.drnoob.reminder.Utils.ReminderDBHelper;
import com.drnoob.reminder.Utils.ReminderTouchHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements OnDialogCloseListener {

    private static final List<String> names = new ArrayList<>();
    public static List<ReminderModel> mList;
    public static RelativeLayout mNoReminderLayout;
    public static TextView mText;
    public static int id;
    public static NotificationChannel mChannel;
    RecyclerView mRecycler;
    //    FloatingActionButton mFab;
    ImageButton mAdd, mSetting;
    ReminderAdapter mAdapter;
    ReminderDBHelper mDbHelper;
    androidx.appcompat.widget.Toolbar mToolbar;
    SharedPreferences preferences;
    SharedPreferences dndPreferences;
    SharedPreferences.Editor dndEditor;
    String CHANNEL_ID = "com.drnoob.reminder";

    public static void noReminders() {
        if (mList.size() > 0) {
            mNoReminderLayout.setVisibility(View.GONE);
            mText.setVisibility(View.VISIBLE);
        } else {
            mNoReminderLayout.setVisibility(View.VISIBLE);
            mText.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        preferences= getSharedPreferences("app_theme", MODE_PRIVATE);
//        Boolean switchState = preferences.getBoolean("dark_theme", false);
//        ToggleTheme.toggleTheme(getApplicationContext(), preferences);
//        toggleTheme();
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.toolbar);
        mRecycler = findViewById(R.id.mainRecycler);
        mAdd = findViewById(R.id.add);
        mSetting = findViewById(R.id.settings);
        mDbHelper = new ReminderDBHelper(MainActivity.this);
        mAdapter = new ReminderAdapter(MainActivity.this, mDbHelper);
        mList = new ArrayList<>();
        mList = mDbHelper.getAllReminders();
        Collections.reverse(mList);
        mAdapter.newReminder(mList);
        mNoReminderLayout = findViewById(R.id.no_reminder);
        mText = findViewById(R.id.text);
        dndPreferences = getSharedPreferences("dnd", MODE_PRIVATE);
        dndEditor = dndPreferences.edit();
        int d = dndPreferences.getInt("dnd_access", 0);

        noReminders();
        createNotificationChannel();

        if (mList.size() >= 1) {
            removeViewedReminders();
        }

        mRecycler.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        mRecycler.setAdapter(mAdapter);

        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = new Random().nextInt(100);
                System.out.println("mId :  " + id);
                AddNewReminder.addNewReminder().show(getSupportFragmentManager(), AddNewReminder.TAG);

            }
        });


        mSetting.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ReminderSettings.class));
        });

        NotificationManager notificationManager =
                null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            // The id of the channel.
            String Channel_Id = "id";
            notificationManager.deleteNotificationChannel(Channel_Id);
        }


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ReminderTouchHelper(mAdapter));
        itemTouchHelper.attachToRecyclerView(mRecycler);


    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        Boolean mSwitchStateChanged = ReminderSettings.mSwitchStateChanged;
//        if (mSwitchStateChanged) {
//            recreate();
//        }
////        recreate();
//    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mList.size() >= 1) {
            removeViewedReminders();
        }
        noReminders();
    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        mList = mDbHelper.getAllReminders();
        Collections.reverse(mList);
        mAdapter.newReminder(mList);
        mAdapter.notifyDataSetChanged();

        if (mList.size() >= 1) {
            removeViewedReminders();
        }

        noReminders();
        overrideDND();

    }

    public void removeViewedReminders() {
        for (int i = 0; i < mList.size(); i++) {
            ReminderModel model = mList.get(i);
            long mReminderTime = model.getmReminderMilli();
            long mCurrentTime = System.currentTimeMillis();
            int val = Long.valueOf(mReminderTime).compareTo(Long.valueOf(mCurrentTime));
            if (val < 0) {
                mAdapter.deleteReminder(i);
            }
        }
    }

    public void overrideDND() {
        int dndAccess = dndPreferences.getInt("dnd_access", 0);
        System.out.println("dnd   " + dndAccess);
        if (dndAccess == 0) {
            if (mList.size() > 0) {
                LayoutInflater inflater = getLayoutInflater();
                View dndView = inflater.inflate(R.layout.override_dnd_dialog, null);
                TextView openDNDSettings, cancelOpenDND;
                openDNDSettings = dndView.findViewById(R.id.open_dnd_settings);
                cancelOpenDND = dndView.findViewById(R.id.open_dnd_cancel);
                AlertDialog.Builder dndSettingsDialog = new AlertDialog.Builder(MainActivity.this, R.style.DialogTheme)
//                    .setTitle("Allow override Do Not Disturb")
//                    .setMessage("This will ensure that you get your reminder notification even on dnd")
                        .setView(dndView)
                        .setCancelable(true);

                AlertDialog dndDialog = dndSettingsDialog.create();
                dndDialog.setCancelable(true);
                dndDialog.setCanceledOnTouchOutside(true);
                dndDialog.show();

                openDNDSettings.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent toneSettingsIntent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                        toneSettingsIntent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                        toneSettingsIntent.putExtra(Settings.EXTRA_CHANNEL_ID, "com.drnoob.reminder");
                        startActivity(toneSettingsIntent);
                        SharedPreferences dndPreferences = getSharedPreferences("dnd", MODE_PRIVATE);
                        SharedPreferences.Editor dndEditor = dndPreferences.edit();
                        dndEditor.putInt("dnd_access", 1);
                        dndEditor.commit();
                        dndDialog.dismiss();
                    }
                });

                cancelOpenDND.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dndDialog.dismiss();
                    }
                });

            } else {

            }
        } else {
            System.out.println(dndAccess + "   dndAccess  ");
        }
    }

    public void createNotificationChannel() {
        Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/" + R.raw.default_tone);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(CHANNEL_ID, "Reminders", NotificationManager.IMPORTANCE_HIGH);
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();
            mChannel.setSound(sound, attributes);
            mChannel.setBypassDnd(true);
            mChannel.enableLights(true);
            mChannel.enableVibration(true);
            mChannel.setImportance(NotificationManager.IMPORTANCE_HIGH);
            mChannel.canBypassDnd();

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(mChannel);
        }
    }

}