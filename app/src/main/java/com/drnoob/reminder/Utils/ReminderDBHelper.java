package com.drnoob.reminder.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.drnoob.reminder.AddNewReminder;
import com.drnoob.reminder.Models.ReminderModel;

import java.util.ArrayList;
import java.util.List;

public class ReminderDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "com.drnoob.reminders";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_NAME = "REMINDERS";
    private static final String COL_1 = "REMINDER_ID";
    private static final String COL_2 = "REMINDER_NAME";
    private static final String COL_3 = "REMINDER_DATE";
    private static final String COL_4 = "REMINDER_TIME";
    private static final String COL_5 = "REMINDER_MILLI";
    private static SQLiteDatabase mDatabase;
    private static int returnedId;

    public ReminderDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists " + TABLE_NAME + "(" +
                "REMINDER_ID integer primary key autoincrement," +
                "REMINDER_NAME text," +
                "REMINDER_DATE text," +
                "REMINDER_TIME text," +
                "REMINDER_MILLI integer)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("drop table if exists " + TABLE_NAME);
            onCreate(db);
        }

    }

    public void insertReminder(ReminderModel model) {
        mDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2, model.getmReminderName());
        values.put(COL_3, model.getmReminderDate());
        values.put(COL_4, model.getmReminderTime());
        values.put(COL_5, model.getmReminderMilli());
//        mDatabase.insert(TABLE_NAME, null, values);
        try {
            mDatabase.insert(TABLE_NAME, null, values);
        } catch (Exception e) {
            Log.e("DatabaseErr", e + "");
        }
    }

    public void deleteReminder(int id) {
        mDatabase = this.getWritableDatabase();
        if (AddNewReminder.manager != null) {
            AddNewReminder.manager.cancel(AddNewReminder.pendingIntent);
        }
        mDatabase.delete(TABLE_NAME, "REMINDER_ID=?", new String[]{String.valueOf(id)});
    }

    public void editReminder(int id, String name) {
        mDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2, name);
        mDatabase.update(TABLE_NAME, values, "REMINDER_ID=?", new String[]{String.valueOf(id)});
        AddNewReminder.mReminderName = name;
//        AddNewReminder.manager.cancel(AddNewReminder.pendingIntent);
//        AddNewReminder.manager.setExact(AlarmManager.RTC_WAKEUP, AddNewReminder.mReminderTimeMillis,
//                AddNewReminder.newPendingIntent);
    }

    public List<ReminderModel> getAllReminders() {
        mDatabase = this.getReadableDatabase();
        List<ReminderModel> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = mDatabase.rawQuery("select * from " + TABLE_NAME, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        ReminderModel model = new ReminderModel();
                        model.setmReminderId(cursor.getInt(cursor.getColumnIndex(COL_1)));
                        model.setmReminderName(cursor.getString(cursor.getColumnIndex(COL_2)));
                        model.setmReminderDate(cursor.getString(cursor.getColumnIndex(COL_3)));
                        model.setmReminderTime(cursor.getString(cursor.getColumnIndex(COL_4)));
                        model.setmReminderMilli(cursor.getLong(cursor.getColumnIndex(COL_5)));
                        list.add(model);
                    }
                    while (cursor.moveToNext());
                }
            }
        } finally {
            mDatabase.close();
            cursor.close();
            return list;
        }
    }

    public void deleteEditedReminder() {
        AddNewReminder.manager.cancel(AddNewReminder.pendingIntent);
    }

    public long returnTime(String name) {
        mDatabase = this.getWritableDatabase();
        Cursor cursor = null;
        Long millis = null;
        ReminderModel model = new ReminderModel();
        try {
            cursor = mDatabase.rawQuery("select " + COL_5 + " from " + TABLE_NAME + " where " + COL_2 + " = ?", new String[]{name});
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        model.setmReminderMilli(cursor.getLong(cursor.getColumnIndex(COL_5)));
                        millis = cursor.getLong(cursor.getColumnIndex(COL_5));
                    }
                    while (cursor.moveToNext());
                }

            }
        } finally {
            mDatabase.close();
            cursor.close();
            return millis;
        }
    }
}