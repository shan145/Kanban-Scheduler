package com.example.kanbanscheduler.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kanbanscheduler.R;
import com.example.kanbanscheduler.adapters.TopicGridAdapter;
import com.example.kanbanscheduler.models.TaskViewModel;
import com.example.kanbanscheduler.models.TopicViewModel;
import com.example.kanbanscheduler.room_db.Topic;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private TextView mProgressText;
    private ProgressBar mProgressBar;
    private TextView mTodoCount;
    private TextView mProgressCount;
    private TextView mDoneCount;
    private Spinner spinner;
    private RecyclerView mRecyclerView;
    private TopicGridAdapter mAdapter;
    private TopicViewModel mTopicViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTopicViewModel = new ViewModelProvider(this).get(TopicViewModel.class);
        setContentView(R.layout.activity_dashboard);
        mProgressBar = findViewById(R.id.circular_progress_bar);
        mProgressText = findViewById(R.id.progress_percent);
        mTodoCount = findViewById(R.id.total_todo);
        mProgressCount = findViewById(R.id.total_progress);
        mDoneCount = findViewById(R.id.total_done);
        spinner = findViewById(R.id.date_spinner);
        if(spinner != null) spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.dates_array, R.layout.date_spinner);
        // Specify the layout to use when the list of choices appear.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if(spinner != null) spinner.setAdapter(adapter);
        mRecyclerView = findViewById(R.id.dashboard_recycler);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mAdapter = new TopicGridAdapter(this);
        mTopicViewModel.getTopics().observe(this, topics -> mAdapter.setTopics(topics));
        mRecyclerView.setAdapter(mAdapter);

        // Sets up adapter to listen to clicks
        mAdapter.setClickListener(new TopicGridAdapter.ClickListener() {
            @Override
            public void onClicked(int pos, Context context) {
                Topic topic = mAdapter.getTopicAtPosition(pos);
                Toast.makeText(context, topic.getTopicName(), Toast.LENGTH_SHORT).show();
            }
        });

        // Sets up bottom navigation
        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.add_topic:
                        LinearLayout container = new LinearLayout(DashboardActivity.this);
                        container.setOrientation(LinearLayout.VERTICAL);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        lp.setMargins(40, 15, 40, 15);
                        EditText topicText = new EditText(DashboardActivity.this);
                        topicText.setLayoutParams(lp);
                        topicText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(20)});
                        container.addView(topicText);
                        // Create dialog builder
                        AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
                        builder.setTitle("Enter a Topic Name:");
                        builder.setView(container)
                                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        String topicName = topicText.getText().toString();
                                        mTopicViewModel.insertTopic(new Topic(topicName));
                                    }
                                }).setNegativeButton("Cancel", null);
                        AlertDialog build = builder.create();
                        build.show();
                        break;
                    case R.id.topic_home:
                        Toast.makeText(DashboardActivity.this, "You have reached the Home Button", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.task_today:
                        Toast.makeText(DashboardActivity.this, "You have reached today's tasks", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {}

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}
}