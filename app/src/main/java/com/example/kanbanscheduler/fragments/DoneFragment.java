package com.example.kanbanscheduler.fragments;

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

import com.example.kanbanscheduler.R;
import com.example.kanbanscheduler.room_db.Task;
import com.example.kanbanscheduler.activities.TaskFillActivity;
import com.example.kanbanscheduler.adapters.TaskListAdapter;
import com.example.kanbanscheduler.models.TaskViewModel;
import com.example.kanbanscheduler.activities.HomeActivity;

import java.util.Date;

import static com.example.kanbanscheduler.fragments.TodoFragment.EDIT_TASK_ACTIVITY_REQUEST_CODE;
import static android.app.Activity.RESULT_OK;

public class DoneFragment extends Fragment {
    private TaskListAdapter mAdapter;
    private static final String TAG = "DoneFragment";
    private TaskViewModel mViewModel;

    public DoneFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_done, container, false);

        // Initialize private variables
        mViewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);

        // Gets email from HomeActivity
        HomeActivity activity = (HomeActivity)getActivity();
        assert activity != null;
        Bundle results = activity.userEmailData();
        String email = results.getString("EMAIL");

        mViewModel.getTasks("done", email).observe(getViewLifecycleOwner(), tasks -> mAdapter.setTasks(tasks));
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

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        // Use for swiping CardView to next type
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Log.d(TAG, Integer.toString(position));
                Task task = mAdapter.getTaskAtPosition(position);
                if(direction == ItemTouchHelper.RIGHT) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setCancelable(true);
                    builder.setTitle("Delete this task?");
                    builder.setMessage("Are you sure you want to delete this task?");
                    builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Remove task from associated list
                            mViewModel.deleteTask(task);
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Cancel
                            mAdapter.notifyItemChanged(position);
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    mViewModel.updateTaskType("progress", task.getEmail(), task.getTid());
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
            Date taskDate = (Date) extras.getSerializable("EXTRA_RETURN_DATE");
            String taskTime = extras.getString("EXTRA_RETURN_TIME");
            String taskEmail = extras.getString("EXTRA_RETURN_EMAIL");
            int taskId = extras.getInt("EXTRA_RETURN_ID");
            assert taskName != null;
            mViewModel.updateTask(taskName, taskDescription, taskDate, taskTime, taskEmail, taskId);
        }
    }
}