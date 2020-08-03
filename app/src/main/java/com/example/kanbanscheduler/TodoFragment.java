package com.example.kanbanscheduler;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
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
        for(int i = 0; i < 25; i++) {
            mTaskList.add("Task " + i);
        }

        mRecyclerView = view.findViewById(R.id.recyclerview);
        mAdapter = new TaskListAdapter(view.getContext(), mTaskList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        return view;
    }
}