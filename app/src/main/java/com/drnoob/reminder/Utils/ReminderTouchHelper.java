package com.drnoob.reminder.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.drnoob.reminder.Adapter.ReminderAdapter;
import com.drnoob.reminder.MainActivity;
import com.drnoob.reminder.R;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class ReminderTouchHelper extends ItemTouchHelper.SimpleCallback {

    private final ReminderAdapter mAdapter;

    //    /**
//     * Creates a Callback for the given drag and swipe allowance. These values serve as
//     * defaults
//     * and if you want to customize behavior per ViewHolder, you can override
//     * {@link #getSwipeDirs(RecyclerView, ViewHolder)}
//     * and / or {@link #getDragDirs(RecyclerView, ViewHolder)}.
//     *
//     * @param dragDirs  Binary OR of direction flags in which the Views can be dragged. Must be
//     *                  composed of {@link #LEFT}, {@link #RIGHT}, {@link #START}, {@link
//     *                  #END},
//     *                  {@link #UP} and {@link #DOWN}.
//     * @param swipeDirs Binary OR of direction flags in which the Views can be swiped. Must be
//     *                  composed of {@link #LEFT}, {@link #RIGHT}, {@link #START}, {@link
//     *                  #END},
//     *                  {@link #UP} and {@link #DOWN}.
//     */
    public ReminderTouchHelper(ReminderAdapter mAdapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.mAdapter = mAdapter;

    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        final int position = viewHolder.getAdapterPosition();
        if (direction == ItemTouchHelper.LEFT) {
            TextView deleteReminder, cancelDeleteReminder;
            LayoutInflater inflater = (LayoutInflater) mAdapter.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.delete_reminder_dalog, null);
            deleteReminder = (TextView) view.findViewById(R.id.delete_reminder);
            cancelDeleteReminder = (TextView) view.findViewById(R.id.cancel_delete_reminder);
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(mAdapter.getContext(), R.style.DialogTheme);
//            mBuilder.setTitle("Delete Reminder")
//                    .setMessage("Are you sure you want to delete this reminder?\n" +
//                            "You won’t be notified about the event.")
            mBuilder.setCancelable(true)
                    .setView(view)

//                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            mAdapter.deleteReminder(position);
////                            if (MainActivity.mList.size() <= 0) {
////                                MainActivity.mNoReminderLayout.setVisibility(View.VISIBLE);
////                            }
////                            else {
////                                MainActivity.mNoReminderLayout.setVisibility(View.GONE);
////                            }
//                            MainActivity.noReminders();
//                        }
//                    })
//                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            mAdapter.notifyItemChanged(position);
//                        }
//                    })
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            mAdapter.notifyItemChanged(position);
                        }
                    });
//                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
//                        @Override
//                        public void onDismiss(DialogInterface dialog) {
//                            mAdapter.notifyItemChanged(position);
//                        }
//                    });
            AlertDialog dialog = mBuilder.create();
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            dialog.show();
//            mAdapter.deleteReminder(position);

            deleteReminder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdapter.deleteReminder(position);
                    MainActivity.noReminders();
                    dialog.dismiss();
                }
            });

            cancelDeleteReminder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdapter.notifyItemChanged(position);
                    dialog.dismiss();
                }
            });

        } else if (direction == ItemTouchHelper.RIGHT) {
            mAdapter.editReminder(position);
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                .addSwipeRightBackgroundColor(ContextCompat.getColor(mAdapter.getContext(), R.color.success))
                .addSwipeRightActionIcon(R.drawable.ic_edit)
                .addSwipeLeftActionIcon(R.drawable.ic_delete)
                .addSwipeLeftBackgroundColor(ContextCompat.getColor(mAdapter.getContext(), R.color.error))
                .create()
                .decorate();

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}