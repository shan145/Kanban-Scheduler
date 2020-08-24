package com.example.kanbanscheduler.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
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
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DashboardActivity extends AppCompatActivity { // implements AdapterView.OnItemSelectedListener{
    private TextView mProgressText;
    private ProgressBar mProgressBar;
    private TextView mTodoCount;
    private TextView mTotalCount;
    private TextView mDoneCount;
//    private Spinner spinner;
    private RecyclerView mRecyclerView;
    private TopicGridAdapter mAdapter;
    private TopicViewModel mTopicViewModel;
    private TaskViewModel mTaskViewModel;
    private DrawerLayout mDrayerLayout;
    private int totalTodos;
    private int totalDones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Setting up user, toolbar, drawer, and drawer header
        mDrayerLayout = findViewById(R.id.drawer_layout);
        configureToolBar();
        configureNavigationDrawer();

        // Setting up view models
        mTopicViewModel = new ViewModelProvider(this).get(TopicViewModel.class);
        mTaskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        // Setting up top card views and functions
        mProgressBar = findViewById(R.id.circular_progress_bar);
        mProgressText = findViewById(R.id.progress_percent);
        mTodoCount = findViewById(R.id.total_todo);
        mTotalCount = findViewById(R.id.total_tasks);
        mDoneCount = findViewById(R.id.total_done);

        mTaskViewModel.getTotalTodos(getStartDate(), getEndDate()).observe(this, integer -> {
            if(integer != null) totalTodos=integer;
            else totalTodos=0;
            String todoString="Todos\n"+ totalTodos;
            mTodoCount.setText(todoString);
        });
        mTaskViewModel.getTotalDones(getStartDate(), getEndDate()).observe(this, integer -> {
            if(integer!= null) totalDones=integer;
            else totalDones=0;
            String doneString="Dones\n"+totalDones;
            mDoneCount.setText(doneString);
        });
        configureTotalTasks();

//        spinner = findViewById(R.id.date_spinner);
//        if(spinner != null) spinner.setOnItemSelectedListener(this);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.dates_array, R.layout.date_spinner);
//        // Specify the layout to use when the list of choices appear.
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        if(spinner != null) spinner.setAdapter(adapter);

        // Setting up recycler view for dashboard and functions
        mRecyclerView = findViewById(R.id.dashboard_recycler);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mAdapter = new TopicGridAdapter(this);
        mTopicViewModel.getTopics().observe(this, topics -> mAdapter.setTopics(topics));
        mRecyclerView.setAdapter(mAdapter);
        // Sets up adapter to listen to clicks
        mAdapter.setClickListener((pos, context) -> {
            Topic topic = mAdapter.getTopicAtPosition(pos);
            Intent intent = new Intent(DashboardActivity.this, TaskActivity.class);
            intent.putExtra("EXTRA_TOPIC_NAME", topic.getTopicName());
            startActivity(intent);
        });
        mAdapter.setLongClickListener(pos -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
            builder.setCancelable(true);
            builder.setTitle("Delete the topic?");
            builder.setMessage("Would you like to delete this topic?");
            builder.setPositiveButton("Delete", (dialogInterface, i) -> {
                Topic topic = mAdapter.getTopicAtPosition(pos);
                mTopicViewModel.deleteTopic(topic);
            }).setNegativeButton("Cancel", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        // Setting up bottom navigation
        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(item -> {
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
                                    String topicName = topicText.getText().toString().trim();
                                    if(mAdapter.inTopicList(topicName)) {
                                        Toast.makeText(DashboardActivity.this, "Unable to get add topic. The topic name may already exist.", Toast.LENGTH_LONG).show();
                                    } else {
                                        mTopicViewModel.insertTopic(new Topic(topicName));
                                    }
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
        });
    }

//    @Override
//    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//        String spinnerLabel = adapterView.getItemAtPosition(i).toString();
//        int totalTodos=0;
//        int totalDones=0;
//        int totalTasks=0;
//        Date today = new Date();
//        if(spinnerLabel.equals("Weekly")) {
//            totalTasks = totalTodos+totalDones;
//        } else {
//            Toast.makeText(this, "Will do rest later", Toast.LENGTH_SHORT).show();
//        }
//        String todoString="Todos\n"+ totalTodos;
//        mTodoCount.setText(todoString);
//        String doneString = "Done\n"+ totalDones;
//        mDoneCount.setText(doneString);
//        String totalString = "Total\n"+totalTasks;
//        mTotalCount.setText(totalString);
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> adapterView) {}

    private void configureTotalTasks() {
        int totalTasks = totalDones+totalTodos;
        String totalString="Total\n"+totalTasks;
        mTotalCount.setText(totalString);
        int totalProgress = 100;
        if(totalTasks != 0) {
            double base = (double)totalDones/totalTasks;
            double rounded=Math.round(base*100.0)/100.0;
            totalProgress = (int) rounded*100;

        }
        mProgressBar.setProgress(totalProgress);
        String progressText=totalProgress+"%\nDone";
        mProgressText.setText(progressText);

    }
    private void configureToolBar() {
        Toolbar navToolbar = findViewById(R.id.nav_toolbar);
        navToolbar.setTitle("Dashboard");
        setSupportActionBar(navToolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
    }

    private void configureNavigationDrawer() {
        NavigationView navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                switch(itemId) {
                    case R.id.dashboard:
                        Toast.makeText(DashboardActivity.this, "You pressed Dashboard!", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                }
                mDrayerLayout.closeDrawers();
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch(itemId) {
            //Android Home
            case android.R.id.home:
                mDrayerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
        return true;
    }

    private Date getStartDate() {
        Calendar calDate = new GregorianCalendar();
        calDate.set(Calendar.HOUR_OF_DAY, 0);
        calDate.set(Calendar.MINUTE, 0);
        calDate.set(Calendar.SECOND, 0);
        calDate.set(Calendar.MILLISECOND, 0);
        return calDate.getTime();
    }

    private Date getEndDate() {
        Calendar calDate = new GregorianCalendar();
        calDate.set(Calendar.HOUR_OF_DAY, 23);
        calDate.set(Calendar.MINUTE, 59);
        calDate.set(Calendar.SECOND, 59);
        calDate.set(Calendar.MILLISECOND, 999);
        return calDate.getTime();
    }
}