package com.example.kanbanscheduler.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.kanbanscheduler.R;
import com.example.kanbanscheduler.adapters.TaskListAdapter;
import com.example.kanbanscheduler.models.TaskViewModel;
import com.example.kanbanscheduler.room_db.Task;
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
        // Get topic name from intent
        topicName = getIntent().getStringExtra("EXTRA_TOPIC_NAME");
        mViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        RecyclerView mRecyclerView = findViewById(R.id.recyclerview);
        mAdapter = new TaskListAdapter(this);

        // Set delete listener for adapter
        mAdapter.setDeleteListener(pos -> {
            Task task = mAdapter.getTaskAtPosition(pos);
            mViewModel.deleteTask(task);
        });
        // Set edit listener for adapter
        mAdapter.setEditListener(pos -> {
            Task task = mAdapter.getTaskAtPosition(pos);
            Intent intent = new Intent(TaskActivity.this, TaskFillActivity.class);
            Bundle b = new Bundle();
            b.putString("EXTRA_EDIT_NAME", task.getName());
            b.putString("EXTRA_EDIT_DESCRIPTION", task.getDescription());
            b.putString("EXTRA_EDIT_DATE", task.getDateString());
            b.putString("EXTRA_EDIT_TIME", task.getTime());
            b.putInt("EXTRA_EDIT_ID", task.getTid());
            intent.putExtras(b);
            startActivityForResult(intent, EDIT_TASK_ACTIVITY_REQUEST_CODE);
        });
        // Set check listener for adapter
        mAdapter.setCheckListener((isChecked, pos) -> {
            Task task = mAdapter.getTaskAtPosition(pos);
            if(isChecked) {
                mViewModel.updateTaskType(1, task.getTid());
            } else {
                mViewModel.updateTaskType(0, task.getTid());
            }
        });
        // Set adapter for recycler view
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(TaskActivity.this, TaskFillActivity.class);
            startActivityForResult(intent, NEW_TASK_ACTIVITY_REQUEST_CODE);
        });
        // Updates cached copy of tasks in adapter if change to live data is observed
        mViewModel.getTasks(topicName).observe(this, tasks-> mAdapter.setTasks(tasks));
        configureToolBar();
        // configureTabLayout();
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

    private void configureToolBar() {
        Toolbar toolbar = findViewById(R.id.topic_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(topicName);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void configureTabLayout() {
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Today"));
        tabLayout.addTab(tabLayout.newTab().setText("Tomorrow"));
        tabLayout.addTab(tabLayout.newTab().setText("Upcoming"));
        // Set tabs to fill entire layout
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Toast.makeText(TaskActivity.this, Objects.requireNonNull(tab.getText()).toString(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

}