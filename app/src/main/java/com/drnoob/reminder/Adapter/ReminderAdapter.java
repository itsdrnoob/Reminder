package com.drnoob.reminder.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.drnoob.reminder.AddNewReminder;
import com.drnoob.reminder.MainActivity;
import com.drnoob.reminder.Models.ReminderModel;
import com.drnoob.reminder.R;
import com.drnoob.reminder.Utils.ReminderDBHelper;
import com.drnoob.reminder.ViewReminder;

import java.util.List;


public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> {

    public static String mReminderViewName;
    public static String mReminderViewDate;
    public static String mReminderViewTime;
    public static int mPosition;
    List<ReminderModel> mList;
    MainActivity mActivity;
    ReminderDBHelper mDbHelper;

    public ReminderAdapter(MainActivity mActivity, ReminderDBHelper mDbHelper) {
        this.mActivity = mActivity;
        this.mDbHelper = mDbHelper;
    }

    public ReminderAdapter(List<ReminderModel> mList, MainActivity mActivity) {
        this.mList = mList;
        this.mActivity = mActivity;
    }

    public ReminderAdapter() {

    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_list, parent, false);
        return new ReminderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {
        ReminderModel model = mList.get(position);
        holder.mReminderNameView.setText(model.getmReminderName());
        holder.mReminderDateView.setText(model.getmReminderDate());
        holder.mReminderTimeView.setText(model.getmReminderTime());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReminderViewName = model.getmReminderName();
                mReminderViewDate = model.getmReminderDate();
                mReminderViewTime = model.getmReminderTime();
                mPosition = position;
                FragmentManager manager = mActivity.getSupportFragmentManager();
                ViewReminder.viewReminder().show(manager, ViewReminder.TAG);
            }
        });
    }

    public Context getContext() {
        return mActivity;
    }

    public void newReminder(List<ReminderModel> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    public void deleteReminder(int position) {
        ReminderModel model = mList.get(position);
        mDbHelper.deleteReminder(model.getmReminderId());
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public void editReminder(int position) {
        ReminderModel model = mList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", model.getmReminderId());
        bundle.putString("name", model.getmReminderName());
        bundle.putString("date", model.getmReminderDate());
        bundle.putString("time", model.getmReminderTime());

        AddNewReminder addNewReminder = new AddNewReminder();
        addNewReminder.setArguments(bundle);
        addNewReminder.show(mActivity.getSupportFragmentManager(), AddNewReminder.TAG);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ReminderViewHolder extends RecyclerView.ViewHolder {
        TextView mReminderNameView, mReminderDateView, mReminderTimeView;

        public ReminderViewHolder(@NonNull View itemView) {
            super(itemView);
            mReminderNameView = itemView.findViewById(R.id.reminderNameView);
            mReminderDateView = itemView.findViewById(R.id.reminderDateView);
            mReminderTimeView = itemView.findViewById(R.id.reminderTimeView);
        }
    }
}