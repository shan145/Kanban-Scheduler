package com.example.kanbanscheduler;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskViewHolder> {
    private List<Task> mTaskList;
    private LayoutInflater mInflator;

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

    void setTasks(List<Task> newTaskList) {
        mTaskList = newTaskList;
        notifyDataSetChanged();
    }


    class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTaskTitle;
        private TextView mTaskDescription;
        private TextView mDueDate;
        private TextView mDueTime;
        private ImageView mDeleteView;
        public TaskViewHolder(View itemView, TaskListAdapter adapter) {
            super(itemView);

            // Initialize the views
            mTaskTitle = itemView.findViewById(R.id.task);
            mTaskDescription = itemView.findViewById(R.id.description);
            mDueDate = itemView.findViewById(R.id.date);
            mDueTime = itemView.findViewById(R.id.time);
            mDeleteView = itemView.findViewById(R.id.delete_button);
            itemView.setOnClickListener(this);
        }

        void bindTo(Task currentTask) {
            // Populate the TextViews with data.
            mTaskTitle.setText(currentTask.getName());
            mTaskDescription.setText(currentTask.getDescription());
            mDueDate.setText(currentTask.getDate());
            if(currentTask.getDate().equals("")) {
                mDueDate.setCompoundDrawables(null, null, null, null);
            }
            mDueTime.setText(currentTask.getTime());
            if(currentTask.getTime().equals("")) {
                mDueTime.setCompoundDrawables(null, null, null, null);
            }
            mDeleteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setCancelable(true);
                    builder.setTitle("Delete this task?");
                    builder.setMessage("Are you sure you want to delete this task?");
                    builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Remove task from associated list
                            mTaskList.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Cancel
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        }

        @Override
        public void onClick(View view) {

        }
    }
}
