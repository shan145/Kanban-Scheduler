package com.example.kanbanscheduler.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.kanbanscheduler.R;

public class DashboardActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Spinner spinner = findViewById(R.id.date_spinner);
        if(spinner != null) spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.dates_array, R.layout.date_spinner);
        // Specify the layout to use when the list of choices appear.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if(spinner != null) spinner.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String spinnerLabel = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(this, spinnerLabel, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}