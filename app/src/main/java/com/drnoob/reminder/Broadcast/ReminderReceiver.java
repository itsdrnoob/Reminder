package com.drnoob.reminder.Broadcast;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.drnoob.reminder.Adapter.ReminderAdapter;
import com.drnoob.reminder.Models.ReminderModel;
import com.drnoob.reminder.R;
import com.drnoob.reminder.Utils.ReminderDBHelper;

import java.util.ArrayList;
import java.util.List;

public class ReminderReceiver extends BroadcastReceiver {

    String mReminderName;
    String CHANNEL_ID = "com.drnoob.reminder";
    int id;
    List<ReminderModel> mList;
    ReminderDBHelper mDBHelper;
    ReminderAdapter mAdapter;
//    MainActivity activity;
//    SharedPreferences tonePreferences;
//    private NotificationChannel mChannel;

//    public ReminderReceiver(ReminderAdapter adapter) {
//        this.mAdapter = adapter;
//    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mReminderName = intent.getStringExtra("reminderName");
        id = intent.getIntExtra("id", 0);
        mList = new ArrayList<>();
        mDBHelper = new ReminderDBHelper(context);

        Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/" + R.raw.default_tone);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            AudioAttributes attributes = new AudioAttributes.Builder()
//                    .setUsage(AudioAttributes.USAGE_ALARM)
//                    .build();
//
//            mChannel = MainActivity.mChannel;
//            mChannel.setSound(sound, attributes);
//            mChannel.setBypassDnd(true);
//            mChannel.enableLights(true);
//            mChannel.enableVibration(true);
//            mChannel.setImportance(NotificationManager.IMPORTANCE_HIGH);
//            mChannel.canBypassDnd();
//
//            NotificationManager manager = context.getSystemService(NotificationManager.class);
//            manager.createNotificationChannel(mChannel);

//            if (manager != null) {
//                List<NotificationChannel> channelList = manager.getNotificationChannels();
//                for (int i = 0; channelList != null && i < channelList.size(); i++) {
//                    manager.deleteNotificationChannel(channelList.get(i).getId());
//                }
//                System.out.println(mChannel.getId());
//                for (int i = 0; i <= channelList.size(); i ++) {
//                    System.out.println(i);
//                    System.out.println(mChannel.getId());
//                }
//            }

//        }


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID);
        mBuilder.setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                .setContentTitle("It's Time..!!")
                .setContentText("Hey, it's time to " + mReminderName)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSound(sound)
                .setDefaults(NotificationManagerCompat.IMPORTANCE_MAX)
                .setAutoCancel(true);

        NotificationManagerCompat compat = NotificationManagerCompat.from(context);
        compat.notify(id, mBuilder.build());
    }


}