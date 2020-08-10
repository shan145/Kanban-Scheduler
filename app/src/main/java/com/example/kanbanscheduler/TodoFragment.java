package com.example.kanbanscheduler;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static android.app.Activity.RESULT_OK;


public class TodoFragment extends Fragment {
    private static final String TAG = "TodoFragment";
    public static final int NEW_TASK_ACTIVITY_REQUEST_CODE = 1;
    private RecyclerView mRecyclerView;
    private TaskListAdapter mAdapter;
    private TaskViewModel mViewModel;
    private String email;

    public TodoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_todo, container, false);
        mViewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);

        // Gets email from HomeActivity
        HomeActivity activity = (HomeActivity)getActivity();
        Bundle results = activity.userEmailData();
        email = results.getString("EMAIL");
        assert email != null;
        Log.d(TAG, email);

        mViewModel.getTasks("todo", email).observe(getViewLifecycleOwner(), tasks -> mAdapter.setTasks(tasks));
        // Initialize private variables
        mRecyclerView = view.findViewById(R.id.recyclerview);
        mRecyclerView.getItemAnimator().setChangeDuration(0);
        mAdapter = new TaskListAdapter(view.getContext());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), TaskFillActivity.class);
                startActivityForResult(intent, NEW_TASK_ACTIVITY_REQUEST_CODE);
            }
        });

        // Use for swiping CardView to next type
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Task task = mAdapter.getTaskAtPosition(position);
                mViewModel.updateTaskType("progress", task.getEmail(), task.getTid());
            }
        });
        helper.attachToRecyclerView(mRecyclerView);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_TASK_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            String taskName = extras.getString("EXTRA_TASK_NAME");
            String taskDescription = extras.getString("EXTRA_TASK_DESCRIPTION");
            String taskDate = extras.getString("EXTRA_DATE");
            String taskTime = extras.getString("EXTRA_TIME");
            Task task = new Task(email, taskName, taskDescription, taskDate, taskTime, "todo");
            mViewModel.insertTask(task);
        }
    }
}