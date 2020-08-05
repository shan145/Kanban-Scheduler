package com.example.kanbanscheduler;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ProgFragment extends Fragment {
    private ArrayList<Task> mTaskList;
    private RecyclerView mRecyclerView;
    private TaskListAdapter mAdapter;

    public ProgFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_prog, container, false);

        // Initialize private variables
        mTaskList = new ArrayList<>();
        mRecyclerView = view.findViewById(R.id.recyclerview);
        mAdapter = new TaskListAdapter(view.getContext(), mTaskList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        // Initializes the "In Progress" Data
        initializeProgressData();

        // Use for swiping Cardview to next type
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
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

    // Use to initialize TO-DO tasks
    private void initializeProgressData() {
        // Clears existing data (to avoid duplication).
        mTaskList.clear();

        for(int i = 0; i <5; i++) {
            String taskName = "Progress " + i;
            String taskDescription ="Description includes the following: \n-Tap\n-Tap\n-Tap " + i;
            String taskDate = "Sun 02 Aug";
            String taskTime = "7:30 pm";
            mTaskList.add(new Task(taskName, taskDescription, taskDate, taskTime));
            mAdapter.notifyDataSetChanged();
        }
    }
}