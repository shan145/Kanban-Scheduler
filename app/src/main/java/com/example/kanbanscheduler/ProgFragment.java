package com.example.kanbanscheduler;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ProgFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private TaskListAdapter mAdapter;
    private String email;

    public ProgFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_prog, container, false);

        // Initialize private variables
        TaskViewModel mViewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);

        // Gets email from HomeActivity
        HomeActivity activity = (HomeActivity)getActivity();
        Bundle results = activity.userEmailData();
        email = results.getString("EMAIL");

        mViewModel.getTasks("progress", email).observe(getViewLifecycleOwner(), tasks -> mAdapter.setTasks(tasks));
        mRecyclerView = view.findViewById(R.id.recyclerview);
        mAdapter = new TaskListAdapter(view.getContext());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        // Use for swiping Cardview to next type
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Task task = mAdapter.getTaskAtPosition(position);
                if(direction == ItemTouchHelper.RIGHT) {
                    mViewModel.updateTaskType("done", task.getEmail(), task.getTid());
                } else {
                    mViewModel.updateTaskType("todo", task.getEmail(), task.getTid());
                }
            }
        });
        helper.attachToRecyclerView(mRecyclerView);
        return view;
    }
}