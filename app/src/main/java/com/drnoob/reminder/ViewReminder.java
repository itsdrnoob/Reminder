package com.drnoob.reminder;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.drnoob.reminder.Adapter.ReminderAdapter;
import com.drnoob.reminder.Utils.ReminderDBHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ViewReminder extends BottomSheetDialogFragment {
    public static final String TAG = "ViewReminder";

    public static ViewReminder viewReminder() {
        return new ViewReminder();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_reminder, container, false);
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
        TextView mViewReminderName, mViewReminderText;
        String name = ReminderAdapter.mReminderViewName;
        String date = ReminderAdapter.mReminderViewDate;
        String time = ReminderAdapter.mReminderViewTime;
        Resources resources = getResources();
        MainActivity activity = new MainActivity();
        ReminderDBHelper helper = new ReminderDBHelper(getContext());
        ReminderAdapter mAdapter = new ReminderAdapter(activity, helper);
        String text = resources.getString(R.string.view_reminder_text, name, time, date);

        mViewReminderName = view.findViewById(R.id.view_reminder_name);
        mViewReminderText = view.findViewById(R.id.view_reminder_text);

        mViewReminderName.setText(ReminderAdapter.mReminderViewName + "");
        mViewReminderText.setText(text + "");


    }
}