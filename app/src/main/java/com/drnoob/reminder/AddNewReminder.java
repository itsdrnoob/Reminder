package com.drnoob.reminder;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.drnoob.reminder.Broadcast.ReminderReceiver;
import com.drnoob.reminder.Models.ReminderModel;
import com.drnoob.reminder.Utils.ReminderDBHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class AddNewReminder extends BottomSheetDialogFragment {
    public static final String TAG = "AddNewReminder";
    public static String mReminderName;
    public static PendingIntent pendingIntent;
    public static AlarmManager manager;
    public static Intent reminderReciever;
    public static Intent intent;
    public static PendingIntent newPendingIntent;
    private static Boolean isDateTwo = false;
    private static Boolean isMonthTwo = false;
    private static Boolean isHourTwo = false;
    private static Boolean isMinTwo = false;
    private static String mReminderDate;
    private static String mReminderTime;
    private static int iHour;
    private static int iMinute;
    private static int iDay;
    private static int iMonth;
    private static int iYear;
    private static int id;
    private static Date dateObj;
    private static ReminderModel model;
    private long mReminderTimeMillis;
    private EditText mReminderNameInsert;
    private TextView mReminderDateInsert, mReminderTimeInsert, mReminderHeader,
            mEditReminderDateTextView, mEditReminderTimeTextView, mSave;

    public static AddNewReminder addNewReminder() {
        return new AddNewReminder();
    }

    public Context Context() {
        return new AddNewReminder().getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_new, container, false);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mReminderNameInsert = view.findViewById(R.id.reminderNameInsert);
        mReminderDateInsert = view.findViewById(R.id.reminderDateInsert);
        mReminderTimeInsert = view.findViewById(R.id.reminderTimeInsert);
        mEditReminderDateTextView = view.findViewById(R.id.add_reminder_date_textview);
        mEditReminderTimeTextView = view.findViewById(R.id.add_reminder_time_textview);
        mReminderHeader = view.findViewById(R.id.add_reminder_header);
        mSave = view.findViewById(R.id.save);
        ReminderDBHelper helper = new ReminderDBHelper(getActivity());
        model = new ReminderModel();
        manager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

//        mSave.setBackgroundColor(getResources().getColor(R.color.primary));

//        mSave.setBackgroundColor(getResources().getColor(R.color.background_dark));

        Bundle bundle = getArguments();
        Boolean isUpdate = false;

        if (bundle != null) {
            isUpdate = true;
            String name = bundle.getString("name");
            mReminderNameInsert.setText(name);
            mEditReminderDateTextView.setVisibility(View.GONE);
            mEditReminderTimeTextView.setVisibility(View.GONE);
            mReminderDateInsert.setVisibility(View.GONE);
            mReminderTimeInsert.setVisibility(View.GONE);
            mReminderHeader.setText("Update Reminder");
            mSave.setText("Update");

        }

        Boolean finalIsUpdate = isUpdate;

        mReminderDateInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int mCurrentYear = calendar.get(Calendar.YEAR);
                int mCurrentMonth = calendar.get(Calendar.MONTH);
                int mCurrentDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDateDialog;
                mDateDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        iDay = dayOfMonth;
                        iMonth = month;
                        iYear = year;

                        System.out.println(iMonth + "    " + iDay);

                        String day = dayOfMonth + "";
                        String newMonth = iMonth + "";

                        if (day.length() == 1) {
                            day = "0" + day;
                            isDateTwo = true;
                        }
                        if (newMonth.length() == 1) {
                            newMonth = "0" + newMonth;
                            isMonthTwo = true;
                        }
                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.MONTH, month);
                        String mMonthName = new SimpleDateFormat("MMMM").format(cal.getTime());
                        System.out.println("Month Name  " + mMonthName + "  ");
                        mReminderDateInsert.setText(mMonthName + " " + day + " " + year);
                    }
                }, mCurrentYear, mCurrentMonth, mCurrentDay);
                mDateDialog.setTitle("Select a Date");
                mDateDialog.show();
            }
        });

        mReminderTimeInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int mCurrentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int mCurrentMin = calendar.get(Calendar.MINUTE);

                TimePickerDialog mTimeDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        iHour = hourOfDay;
                        iMinute = minute;

                        System.out.println(hourOfDay + "   " + minute);

                        String m24HourTime = hourOfDay + ":" + minute;
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                            dateObj = sdf.parse(m24HourTime);
//                            System.out.println(dateObj);
                            System.out.println(new SimpleDateFormat("K:mm a").format(dateObj));
                        } catch (final ParseException e) {
                            e.printStackTrace();
                        }

                        if (hourOfDay == 0) {
                            hourOfDay = 12;
                        }
                        String hour = hourOfDay + "";
                        String min = minute + "";
                        if (hour.length() == 1) {
                            hour = "0" + hour;
                            isHourTwo = true;
                        }
                        if (min.length() == 1) {
                            min = "0" + min;
                            isMinTwo = true;
                        }
                        mReminderTimeInsert.setText(new SimpleDateFormat("K:mm a").format(dateObj));
                    }
                }, mCurrentHour, mCurrentMin, false);
                mTimeDialog.setTitle("Select Time");
                mTimeDialog.show();
            }
        });

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name;
                id = MainActivity.id;
                System.out.println(id + "");
//                Toast.makeText(getContext(), id + "", Toast.LENGTH_SHORT).show();
                mReminderName = mReminderNameInsert.getText().toString();
                mReminderDate = mReminderDateInsert.getText().toString();
                mReminderTime = mReminderTimeInsert.getText().toString();

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, iHour);
                calendar.set(Calendar.MINUTE, iMinute);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.DAY_OF_MONTH, iDay);
                calendar.set(Calendar.MONTH, iMonth);
                calendar.set(Calendar.YEAR, iYear);
                Date tt = calendar.getTime();
                mReminderTimeMillis = calendar.getTimeInMillis();
                System.out.println(tt + "    " + System.currentTimeMillis() + "    "
                        + mReminderTimeMillis);

//                reminderReciever = new Intent(getContext(), ReminderReceiver.class);
//                reminderReciever.putExtra("reminderName", mReminderName)
//                        .putExtra("id", id);
//                pendingIntent = PendingIntent.getBroadcast(getActivity(), id, reminderReciever,
//                        0);
//
//                manager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, mReminderTimeMillis, pendingIntent);
//                }
//                else {
//                    manager.setExact(AlarmManager.RTC_WAKEUP, mReminderTimeMillis, pendingIntent);
//                }

//                List<ReminderModel> modelList = new ArrayList<>();
//                modelList = helper.getAllReminders();
//                ReminderModel model;
//                model = new ReminderModel();
//                model = modelList.get(ReminderAdapter.mPosition);
//                long m = model.getmReminderMilli();
//                System.out.println("       m       " + m);


                if (finalIsUpdate) {
                    System.out.println("isfinalupdateId :   " + id);
                    int newId = new Random().nextInt(100);
                    mReminderName = mReminderNameInsert.getText().toString();
                    helper.editReminder(bundle.getInt("id"), mReminderName);
                    System.out.println(mReminderName);
                    intent = new Intent(getContext(), ReminderReceiver.class);
                    intent.putExtra("reminderName", mReminderName);
                    intent.putExtra("id", id);
                    System.out.println(id + "");
                    System.out.println(newId + "n");
                    long mNewTime = helper.returnTime(mReminderName);
                    System.out.println("    time    " + mNewTime);
                    manager.cancel(pendingIntent);
                    newPendingIntent = PendingIntent.getBroadcast(getActivity(), newId, intent, 0);
//                    manager.setExact(AlarmManager.RTC_WAKEUP, mi, newPendingIntent);
//                    manager.cancel(pendingIntent);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, mNewTime, newPendingIntent);
                    } else {
                        manager.setExact(AlarmManager.RTC_WAKEUP, mNewTime, newPendingIntent);
                    }

                } else {
                    if (mReminderNameInsert.getText().toString().length() == 0 ||
                            mReminderDateInsert.getText().toString().length() == 0 ||
                            mReminderTimeInsert.getText().toString().length() == 0) {
                        if (mReminderNameInsert.getText().toString().length() == 0) {
                            mReminderNameInsert.requestFocus();
                            mReminderNameInsert.setBackground(getResources().getDrawable(R.drawable.insert_bg_error, null));
                            mReminderNameInsert.setError("Please enter a reminder name");
                            mReminderNameInsert.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    mReminderNameInsert.setBackground(getResources().getDrawable(R.drawable.insert_bg, null));
                                    mReminderNameInsert.setError(null);
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });
                        }
                        if (mReminderDateInsert.getText().toString().length() == 0) {
                            mReminderDateInsert.setBackground(getResources().getDrawable(R.drawable.insert_bg_error, null));
                            mReminderDateInsert.setError("Please add a date");
                            mReminderDateInsert.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    mReminderDateInsert.setBackground(getResources().getDrawable(R.drawable.insert_bg, null));
                                    mReminderDateInsert.setError(null);
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });
                        }
                        if (mReminderTimeInsert.getText().toString().length() == 0) {
                            mReminderTimeInsert.setBackground(getResources().getDrawable(R.drawable.insert_bg_error, null));
                            mReminderTimeInsert.setError("Please select a time");
                            mReminderTimeInsert.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    mReminderTimeInsert.setBackground(getResources().getDrawable(R.drawable.insert_bg, null));
                                    mReminderTimeInsert.setError(null);
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });
                        }
                    } else {
                        model.setmReminderName(mReminderName);
                        model.setmReminderDate(mReminderDate);
                        model.setmReminderTime(mReminderTime);
                        model.setmReminderMilli(mReminderTimeMillis);
                        helper.insertReminder(model);

                        name = model.getmReminderName();

                        System.out.println("name : " + mReminderName);
                        System.out.println("edited name : " + name);
                        System.out.println("milli :" + mReminderTimeMillis);

                        reminderReciever = new Intent(getContext(), ReminderReceiver.class);
                        reminderReciever.putExtra("reminderName", mReminderName)
                                .putExtra("id", id);
                        pendingIntent = PendingIntent.getBroadcast(getActivity(), id, reminderReciever,
                                0);


                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, mReminderTimeMillis, pendingIntent);
                        } else {
                            manager.setExact(AlarmManager.RTC_WAKEUP, mReminderTimeMillis, pendingIntent);
                        }
                        dismiss();
                    }
                }

//                int idd = mModel.getmReminderId();


//                long mCurrentTimeMili = System.currentTimeMillis();
//                Log.d("time", mReminderTimeMili + "");
//                Log.d("time", mCurrentTimeMili + "");


            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (activity instanceof OnDialogCloseListener) {
            ((OnDialogCloseListener) activity).onDialogClose(dialog);
        }
    }

}