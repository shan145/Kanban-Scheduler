package com.example.kanbanscheduler;

import android.content.ClipData;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class TodoFragment extends Fragment {

    private final ArrayList<String> mTaskList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private TaskListAdapter mAdapter;

    public TodoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_todo, container, false);
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int taskListSize = mTaskList.size();
                mTaskList.add("Fab button pressed!");
                mRecyclerView.getAdapter().notifyItemInserted(taskListSize);
                mRecyclerView.smoothScrollToPosition(taskListSize);
            }
        });

        // Initial test data in task list
        for(int i = 0; i < 5; i++) {
            mTaskList.add("Task " + i);
        }

        mRecyclerView = view.findViewById(R.id.recyclerview);
        mAdapter = new TaskListAdapter(view.getContext(), mTaskList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        // Use for swiping Cardview to next type
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                mTaskList.remove(viewHolder.getAdapterPosition());
                mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        });
        helper.attachToRecyclerView(mRecyclerView);
        return view;
    }
}