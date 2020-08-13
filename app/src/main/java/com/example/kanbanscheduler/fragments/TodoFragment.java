package com.example.kanbanscheduler.fragments;

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

import com.example.kanbanscheduler.R;
import com.example.kanbanscheduler.activities.HomeActivity;
import com.example.kanbanscheduler.activities.TaskFillActivity;
import com.example.kanbanscheduler.adapters.TaskListAdapter;
import com.example.kanbanscheduler.room_db.Task;
import com.example.kanbanscheduler.models.TaskViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;

import static android.app.Activity.RESULT_OK;


public class TodoFragment extends Fragment{
    private static final String TAG = "TodoFragment";
    public static final int NEW_TASK_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_TASK_ACTIVITY_REQUEST_CODE = 2;
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

        // Creating ViewModel for Fragment (shared with other fragments through requireActivity())
        mViewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);

        // Gets email from HomeActivity
        HomeActivity activity = (HomeActivity)getActivity();
        assert activity != null;
        Bundle results = activity.userEmailData();
        email = results.getString("EMAIL");
        assert email != null;
        Log.d(TAG, email);

        // Updates cached copy of words in adapter if change to live data is observed
        mViewModel.getTasks("todo", email).observe(getViewLifecycleOwner(), tasks -> mAdapter.setTasks(tasks));

        // Initialize private variables
        RecyclerView mRecyclerView = view.findViewById(R.id.recyclerview);
        mAdapter = new TaskListAdapter(view.getContext());

        // Set delete listener for adapter
        mAdapter.setDeleteListener(new TaskListAdapter.DeleteListener() {
            @Override
            public void onDeleteClicked(int pos) {
                Task task = mAdapter.getTaskAtPosition(pos);
                mViewModel.deleteTask(task);
            }
        });

        // Set edit listener for adapter
        mAdapter.setEditListener(new TaskListAdapter.EditListener() {
            @Override
            public void onEditClicked(int pos) {
                Task task = mAdapter.getTaskAtPosition(pos);
                Intent intent = new Intent(view.getContext(), TaskFillActivity.class);
                Bundle b = new Bundle();
                b.putString("EXTRA_EDIT_NAME", task.getName());
                b.putString("EXTRA_EDIT_DESCRIPTION", task.getDescription());
                b.putString("EXTRA_EDIT_DATE", task.getDateString());
                b.putString("EXTRA_EDIT_TIME", task.getTime());
                b.putString("EXTRA_EDIT_EMAIL", task.getEmail());
                b.putInt("EXTRA_EDIT_ID", task.getTid());
                intent.putExtras(b);
                startActivityForResult(intent, EDIT_TASK_ACTIVITY_REQUEST_CODE);
            }
        });

        // Set adapter for recycler view
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
            assert extras != null;
            String taskName = extras.getString("EXTRA_TASK_NAME");
            String taskDescription = extras.getString("EXTRA_TASK_DESCRIPTION");
            Date taskDate = (Date) extras.getSerializable("EXTRA_DATE");
            String taskTime = extras.getString("EXTRA_TIME");
            assert taskName != null;
            Task task = new Task(email, taskName, taskDescription, taskDate, taskTime, "todo");
            mViewModel.insertTask(task);
        } else if (requestCode == EDIT_TASK_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            assert extras != null;
            String taskName = extras.getString("EXTRA_RETURN_NAME");
            String taskDescription = extras.getString("EXTRA_RETURN_DESCRIPTION");
            Date taskDate = (Date) extras.getSerializable("EXTRA_RETURN_DATE");
            String taskTime = extras.getString("EXTRA_RETURN_TIME");
            String taskEmail = extras.getString("EXTRA_RETURN_EMAIL");
            int taskId = extras.getInt("EXTRA_RETURN_ID");
            assert taskName != null;
            mViewModel.updateTask(taskName, taskDescription, taskDate, taskTime, taskEmail, taskId);
        }
    }
}