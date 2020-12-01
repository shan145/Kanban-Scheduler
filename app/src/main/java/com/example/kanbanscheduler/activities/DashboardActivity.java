package com.example.kanbanscheduler.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kanbanscheduler.R;
import com.example.kanbanscheduler.adapters.TopicGridAdapter;
import com.example.kanbanscheduler.models.TaskViewModel;
import com.example.kanbanscheduler.models.TopicViewModel;
import com.example.kanbanscheduler.room.Topic;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.atomic.AtomicInteger;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        configureUIComponents();
        configureToolBar();
        configureNavigationDrawer();
        tryToConfigureProgressCard();
        configureSpinner();
        configureClickListenersOnAdapters();
        configureBottomNavigation();
    }

    private void configureUIComponents() {
        mDrayerLayout = findViewById(R.id.drawer_layout);
        mProgressBar = findViewById(R.id.circular_progress_bar);
        mProgressText = findViewById(R.id.progress_percent);
        mTodoCount = findViewById(R.id.total_todo);
        mTotalCount = findViewById(R.id.total_tasks);
        mDoneCount = findViewById(R.id.total_done);
        mTopicViewModel = new ViewModelProvider(this).get(TopicViewModel.class);
        mTaskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        mRecyclerView = findViewById(R.id.dashboard_recycler);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mAdapter = new TopicGridAdapter(this);
        mTopicViewModel.getTopics().observe(this, topics -> mAdapter.setTopics(topics));
        mRecyclerView.setAdapter(mAdapter);
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
        navView.setNavigationItemSelectedListener(item -> {
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
        });
    }

    private void tryToConfigureProgressCard() {
        try {
            configureProgressCard();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void configureProgressCard() throws InterruptedException {
        int todoCount = getTodoCount();
        String todos = "Todos\n"+todoCount;
        mTodoCount.setText(todos);

        int doneCount = getDoneCount();
        String dones = "Done\n"+doneCount;
        mDoneCount.setText(dones);

        int totalTasks = todoCount+doneCount;
        String total = "Total\n"+totalTasks;
        mTotalCount.setText(total);

        int totalProgress = 100;
        if(totalTasks != 0) {
            double base = (double)doneCount/totalTasks;
            double rounded=Math.round(base*100.0)/100.0;
            totalProgress = (int) Math.round(rounded*100);
        }
        mProgressBar.setProgress(totalProgress);
        String progressText=totalProgress+"%\nDone";
        mProgressText.setText(progressText);
    }

    private int getTodoCount() throws InterruptedException {
        AtomicInteger totalTodosCount = new AtomicInteger();
        Thread todoThread = new Thread(() -> {
            int totalTodos = mTaskViewModel.getTotalTodos(getStartOfToday(), getEndOfToday());
            totalTodosCount.set(totalTodos);
        });
        todoThread.start();
        todoThread.join();
        return totalTodosCount.get();
    }

    private int getDoneCount() throws InterruptedException {
        AtomicInteger totalDonesCount = new AtomicInteger();
        Thread doneThread = new Thread(() -> {
            int totalDones = mTaskViewModel.getTotalDones(getStartOfToday(), getEndOfToday());
            totalDonesCount.set(totalDones);
        });
        doneThread.start();
        doneThread.join();
        return totalDonesCount.get();
    }

    private Date getStartOfToday() {
        Calendar calDate = new GregorianCalendar();
        calDate.set(Calendar.HOUR_OF_DAY, 0);
        calDate.set(Calendar.MINUTE, 0);
        calDate.set(Calendar.SECOND, 0);
        calDate.set(Calendar.MILLISECOND, 0);
        return calDate.getTime();
    }

    private Date getEndOfToday() {
        Calendar calDate = new GregorianCalendar();
        calDate.set(Calendar.HOUR_OF_DAY, 23);
        calDate.set(Calendar.MINUTE, 59);
        calDate.set(Calendar.SECOND, 59);
        calDate.set(Calendar.MILLISECOND, 999);
        return calDate.getTime();
    }

    private void configureSpinner() {
//        spinner = findViewById(R.id.date_spinner);
//        if(spinner != null) spinner.setOnItemSelectedListener(this);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.dates_array, R.layout.date_spinner);
//        // Specify the layout to use when the list of choices appear.
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        if(spinner != null) spinner.setAdapter(adapter);
    }

    private void configureClickListenersOnAdapters() {
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
                // Reload activity to pick up any changes to UI top dashboard
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }).setNegativeButton("Cancel", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    private void configureBottomNavigation() {
        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(item -> {
            switch(item.getItemId()) {
                case R.id.add_topic:
                    createTopicDialog();
                    break;
                case R.id.topic_home:
                    Toast.makeText(DashboardActivity.this, "You have reached the Home Button", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.task_today:
                    Toast.makeText(DashboardActivity.this, "You have reached today's tasks", Toast.LENGTH_SHORT).show();
                    Intent tasksTodayIntent = new Intent(DashboardActivity.this, TasksTodayActivity.class);
                    startActivity(tasksTodayIntent);
                    break;
            }
            return true;
        });
    }

    private void createTopicDialog() {
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
        builder.setTitle("Enter a Topic");
        builder.setView(container)
                .setPositiveButton("Add", (dialogInterface, i) -> {
                    String topicName = topicText.getText().toString().trim();
                    if(mAdapter.inTopicList(topicName)) {
                        Toast.makeText(DashboardActivity.this, "Unable to add topic. The topic name may already exist.", Toast.LENGTH_LONG).show();
                    } else {
                        mTopicViewModel.insertTopic(new Topic(topicName));
                    }
                }).setNegativeButton("Cancel", null);
        AlertDialog build = builder.create();
        build.show();
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
}