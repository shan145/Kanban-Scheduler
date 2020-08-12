package com.example.kanbanscheduler;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import static android.app.Activity.RESULT_OK;
import static com.example.kanbanscheduler.TodoFragment.EDIT_TASK_ACTIVITY_REQUEST_CODE;

public class ProgFragment extends Fragment {
    private static final String TAG = "ProgFragment";
    private TaskListAdapter mAdapter;
    private TaskViewModel mViewModel;

    public ProgFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_prog, container, false);

        // Initialize private variables
        mViewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);

        // Gets email from HomeActivity
        HomeActivity activity = (HomeActivity)getActivity();
        assert activity != null;
        Bundle results = activity.userEmailData();
        String email = results.getString("EMAIL");

        mViewModel.getTasks("progress", email).observe(getViewLifecycleOwner(), tasks -> mAdapter.setTasks(tasks));
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
                b.putString("EXTRA_EDIT_DATE", task.getDate());
                b.putString("EXTRA_EDIT_TIME", task.getTime());
                b.putString("EXTRA_EDIT_EMAIL", task.getEmail());
                b.putInt("EXTRA_EDIT_ID", task.getTid());
                intent.putExtras(b);
                startActivityForResult(intent, EDIT_TASK_ACTIVITY_REQUEST_CODE);
            }
        });

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_TASK_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            assert extras != null;
            String taskName = extras.getString("EXTRA_RETURN_NAME");
            String taskDescription = extras.getString("EXTRA_RETURN_DESCRIPTION");
            String taskDate = extras.getString("EXTRA_RETURN_DATE");
            String taskTime = extras.getString("EXTRA_RETURN_TIME");
            String taskEmail = extras.getString("EXTRA_RETURN_EMAIL");
            int taskId = extras.getInt("EXTRA_RETURN_ID");
            assert taskName != null;
            mViewModel.updateTask(taskName, taskDescription, taskDate, taskTime, taskEmail, taskId);
        }
    }
}