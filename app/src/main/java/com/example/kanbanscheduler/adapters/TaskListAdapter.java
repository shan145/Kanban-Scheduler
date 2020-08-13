package com.example.kanbanscheduler.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kanbanscheduler.R;
import com.example.kanbanscheduler.room_db.Task;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskViewHolder> {
    private List<Task> mTaskList;
    private LayoutInflater mInflator;
    private EditListener mEditListener;
    private DeleteListener mDeleteListener;

    public TaskListAdapter(Context context) {
        mInflator = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public TaskListAdapter.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflator.inflate(R.layout.list_item, parent, false);
        return new TaskViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskListAdapter.TaskViewHolder holder, int position) {
        Task mCurrent = mTaskList.get(position);
        holder.bindTo(mCurrent);
        // holder.itemView.setOnLongClickListener();
    }

    @Override
    public int getItemCount() {
        if(mTaskList != null)
            return mTaskList.size();
        return 0;
    }

    public Task getTaskAtPosition(int position) {
        return mTaskList.get(position);
    }

    public void setTasks(List<Task> newTaskList) {
        mTaskList = newTaskList;
        notifyDataSetChanged();
    }

    /*
     * Interface to allow calling of edit button in Fragments
     */
    public interface EditListener {
        void onEditClicked(int pos);
    }
    public void setEditListener (EditListener listener){
        mEditListener = listener;
    }

    /*
     * Interface to allow calling of delete button in Fragments
     */
    public interface DeleteListener {
        void onDeleteClicked(int pos);
    }
    public void setDeleteListener (DeleteListener listener) {
        mDeleteListener = listener;
    }

    /*
     * TaskViewHolder class that works to hold associated values from onBindViewHolder
     */
    class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        private TextView mTaskTitle;
        private TextView mTaskDescription;
        private TextView mDueDate;
        private TextView mDueTime;
        public TaskViewHolder(View itemView, TaskListAdapter adapter) {
            super(itemView);

            // Initialize the views
            mTaskTitle = itemView.findViewById(R.id.task);
            mTaskDescription = itemView.findViewById(R.id.description);
            mDueDate = itemView.findViewById(R.id.date);
            mDueTime = itemView.findViewById(R.id.time);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        void bindTo(Task currentTask) {
            // Populate the TextViews with data.
            mTaskTitle.setText(currentTask.getName());
            if(currentTask.getTaskType().equals("done")) {
                mTaskTitle.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }
            mTaskDescription.setText(currentTask.getDescription());
            // Set date drawables to invisible
            mDueDate.setText(currentTask.getDateString());
            if(currentTask.getDateString().equals("")) {
                mDueDate.setVisibility(View.INVISIBLE);
            } else {
                mDueDate.setVisibility(View.VISIBLE);
            }

            // Set time drawables to invisible
            mDueTime.setText(currentTask.getTime());
            if(currentTask.getTime().equals("")) {
                mDueTime.setVisibility(View.INVISIBLE);
            } else {
                mDueTime.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onClick(View view) {}

        @Override
        public boolean onLongClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setCancelable(true);
            builder.setTitle("Edit or Delete?");
            builder.setMessage("Would you like to edit or delete this task?");
            builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mEditListener.onEditClicked(getAdapterPosition());
                }
            }).setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Remove task from associated list
                    mDeleteListener.onDeleteClicked(getAdapterPosition());
                    mTaskList.remove(getAdapterPosition());
                }
            }).setNeutralButton("Cancel", null);
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        }
    }
}
