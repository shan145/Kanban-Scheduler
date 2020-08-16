package com.example.kanbanscheduler.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kanbanscheduler.R;

public class DashboardActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private TextView mProgressText;
    private ProgressBar mProgressBar;
    private TextView mTodoCount;
    private TextView mProgressCount;
    private TextView mDoneCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mProgressBar = findViewById(R.id.circular_progress_bar);
        mProgressText = findViewById(R.id.progress_percent);
        mTodoCount = findViewById(R.id.total_todo);
        mProgressCount = findViewById(R.id.total_progress);
        mDoneCount = findViewById(R.id.total_done);
        Spinner spinner = findViewById(R.id.date_spinner);
        if(spinner != null) spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.dates_array, R.layout.date_spinner);
        // Specify the layout to use when the list of choices appear.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if(spinner != null) spinner.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {}

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}
}