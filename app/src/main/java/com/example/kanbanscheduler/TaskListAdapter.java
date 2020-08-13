package com.example.kanbanscheduler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Delete;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

    void setTasks(List<Task> newTaskList) {
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
    class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mTaskTitle;
        private TextView mTaskDescription;
        private TextView mDueDate;
        private TextView mDueTime;
        private ImageView mDeleteView;
        private ImageView mEditView;
        public TaskViewHolder(View itemView, TaskListAdapter adapter) {
            super(itemView);

            // Initialize the views
            mTaskTitle = itemView.findViewById(R.id.task);
            mTaskDescription = itemView.findViewById(R.id.description);
            mDueDate = itemView.findViewById(R.id.date);
            mDueTime = itemView.findViewById(R.id.time);
            mDeleteView = itemView.findViewById(R.id.delete_button);
            mEditView = itemView.findViewById(R.id.edit_button);
            itemView.setOnClickListener(this);
        }

        void bindTo(Task currentTask) {
            // Populate the TextViews with data.
            mTaskTitle.setText(currentTask.getName());
            mTaskDescription.setText(currentTask.getDescription());

            // Set date drawables to invisible
            mDueDate.setText(currentTask.getDateString());
//            mDueDate.setText(parseDateToString(currentTask.getDate()));
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

            // Set delete view listener
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
                            mDeleteListener.onDeleteClicked(getAdapterPosition());
                            mTaskList.remove(getAdapterPosition());
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

            // Set edit view listener
            mEditView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setCancelable(true);
                    builder.setTitle("Edit this task?");
                    builder.setMessage("Are you sure you want to edit this task?");
                    builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mEditListener.onEditClicked(getAdapterPosition());
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
        public void onClick(View view) {}
    }
}
