package com.example.kanbanscheduler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskViewHolder> {
    private final ArrayList<String> mTaskList;
    private LayoutInflater mInflator;

    public TaskListAdapter(Context context, ArrayList<String> taskList) {
        mInflator = LayoutInflater.from(context);
        this.mTaskList = taskList;
    }
    @NonNull
    @Override
    public TaskListAdapter.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflator.inflate(R.layout.list_item, parent, false);
        return new TaskViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskListAdapter.TaskViewHolder holder, int position) {
        String mCurrent = mTaskList.get(position);
        holder.taskItemView.setText(mCurrent);
    }

    @Override
    public int getItemCount() {
        return mTaskList.size();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView taskItemView;
        final TaskListAdapter mAdapter;
        public TaskViewHolder(View itemView, TaskListAdapter adapter) {
            super(itemView);
            taskItemView = itemView.findViewById(R.id.task);
            this.mAdapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
