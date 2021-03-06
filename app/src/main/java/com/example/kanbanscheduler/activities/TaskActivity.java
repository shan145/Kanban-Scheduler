package com.example.kanbanscheduler.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.example.kanbanscheduler.R;
import com.example.kanbanscheduler.adapters.TaskListAdapter;
import com.example.kanbanscheduler.models.TaskViewModel;
import com.example.kanbanscheduler.room.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.Date;
import java.util.Objects;

public class TaskActivity extends AppCompatActivity {
    public static final int NEW_TASK_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_TASK_ACTIVITY_REQUEST_CODE = 2;
    private TaskListAdapter mAdapter;
    private TaskViewModel mViewModel;
    private String topicName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        configureTopicName();
        configureViewModel();
        configureRecyclerViewAndAdapter();
        configureFabButton();
        configureToolBar();
    }

    private void configureTopicName() {
        topicName = getIntent().getStringExtra("EXTRA_TOPIC_NAME");
    }

    private void configureViewModel() {
        mViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        // Updates cached copy of tasks in adapter if change to live data is observed
        mViewModel.getTasks(topicName).observe(this, tasks-> mAdapter.setTasks(tasks));
    }

    private void configureRecyclerViewAndAdapter() {
        RecyclerView mRecyclerView = findViewById(R.id.recyclerview);
        mAdapter = new TaskListAdapter(this);

        mAdapter.setDeleteListener(pos -> {
            Task task = mAdapter.getTaskAtPosition(pos);
            mViewModel.deleteTask(task);
        });

        mAdapter.setEditListener(pos -> {
            Task task = mAdapter.getTaskAtPosition(pos);
            Intent intent = new Intent(TaskActivity.this, TaskFormActivity.class);
            Bundle b = new Bundle();
            b.putString("EXTRA_EDIT_NAME", task.getName());
            b.putString("EXTRA_EDIT_DESCRIPTION", task.getDescription());
            b.putString("EXTRA_EDIT_DATE", task.getDateString());
            b.putString("EXTRA_EDIT_TIME", task.getTime());
            b.putInt("EXTRA_EDIT_ID", task.getTid());
            intent.putExtras(b);
            startActivityForResult(intent, EDIT_TASK_ACTIVITY_REQUEST_CODE);
        });

        mAdapter.setCheckListener((isChecked, pos) -> {
            Task task = mAdapter.getTaskAtPosition(pos);
            if(isChecked) {
                mViewModel.updateTaskType(1, task.getTid());
            } else {
                mViewModel.updateTaskType(0, task.getTid());
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void configureFabButton() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(TaskActivity.this, TaskFormActivity.class);
            startActivityForResult(intent, NEW_TASK_ACTIVITY_REQUEST_CODE);
        });
    }

    private void configureToolBar() {
        Toolbar toolbar = findViewById(R.id.topic_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(topicName);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            assert extras != null;
            String taskName = extras.getString("EXTRA_TASK_NAME");
            String taskDescription = extras.getString("EXTRA_TASK_DESCRIPTION");
            Date taskDate = (Date) extras.getSerializable("EXTRA_DATE");
            String taskTime = extras.getString("EXTRA_TIME");
            if (requestCode == NEW_TASK_ACTIVITY_REQUEST_CODE) {
                assert taskName != null;
                Task task = new Task(topicName, taskName, taskDescription, taskDate, taskTime, 0);
                mViewModel.insertTask(task);
            } else if (requestCode == EDIT_TASK_ACTIVITY_REQUEST_CODE) {
                int taskId = extras.getInt("EXTRA_TASK_ID");
                assert taskName != null;
                mViewModel.updateTask(taskName, taskDescription, taskDate, taskTime, taskId);
            }
        }
    }

    // Ensures that we move up one level instead of back to have dashboard register changes
    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
        super.onBackPressed();
    }
}