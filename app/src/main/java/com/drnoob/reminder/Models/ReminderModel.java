package com.drnoob.reminder.Models;

public class ReminderModel {
    private int mReminderId;
    private String mReminderName, mReminderDate, mReminderTime;
    private long mReminderMilli;

    public ReminderModel(int mReminderId, String mReminderName, String mReminderDate, String mReminderTime, long mReminderMilli) {
        this.mReminderId = mReminderId;
        this.mReminderName = mReminderName;
        this.mReminderDate = mReminderDate;
        this.mReminderTime = mReminderTime;
        this.mReminderMilli = mReminderMilli;
    }

    public ReminderModel() {

    }

    public int getmReminderId() {
        return mReminderId;
    }

    public void setmReminderId(int mReminderId) {
        this.mReminderId = mReminderId;
    }

    public String getmReminderName() {
        return mReminderName;
    }

    public void setmReminderName(String mReminderName) {
        this.mReminderName = mReminderName;
    }

    public String getmReminderDate() {
        return mReminderDate;
    }

    public void setmReminderDate(String mReminderDate) {
        this.mReminderDate = mReminderDate;
    }

    public String getmReminderTime() {
        return mReminderTime;
    }

    public void setmReminderTime(String mReminderTime) {
        this.mReminderTime = mReminderTime;
    }

    public long getmReminderMilli() {
        return mReminderMilli;
    }

    public void setmReminderMilli(long mReminderMilli) {
        this.mReminderMilli = mReminderMilli;
    }
}