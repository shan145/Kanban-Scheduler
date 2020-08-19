package com.example.kanbanscheduler.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
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
import com.example.kanbanscheduler.room_db.Topic;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private TextView mProgressText;
    private ProgressBar mProgressBar;
    private TextView mTodoCount;
    private TextView mProgressCount;
    private TextView mDoneCount;
    private Spinner spinner;
    private RecyclerView mRecyclerView;
    private ArrayList<Topic> mTopicData;
    private TopicGridAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        mTopicData = new ArrayList<>();
        mAdapter = new TopicGridAdapter(this, mTopicData);
        // Used to initialize data at first
        initializeData();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setClickListener(new TopicGridAdapter.ClickListener() {
            @Override
            public void onClicked(int pos, Context context) {
                int topicSize = mTopicData.size();
                Topic topic = mTopicData.get(pos);
                LinearLayout container = new LinearLayout(context);
                container.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(40, 15, 40, 15);
                EditText topicText = new EditText(context);
                topicText.setLayoutParams(lp);
                topicText.setMaxEms(20);
                container.addView(topicText);
                if(topic.getTopicName().equals("+Add")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Enter a Topic Name:");
                    builder.setView(container)
                            .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String topicName = topicText.getText().toString();
                                    mTopicData.add(topicSize-1, new Topic(topicName));
                                    mAdapter.notifyItemInserted(topicSize-1);
                                }
                            }).setNegativeButton("Cancel", null);
                    AlertDialog build = builder.create();
                    build.show();
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {}

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    private void initializeData() {
        mTopicData.clear();
        mTopicData.add(new Topic("+Add"));
        mAdapter.notifyDataSetChanged();
    }
}