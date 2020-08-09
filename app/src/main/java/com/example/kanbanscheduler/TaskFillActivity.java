package com.example.kanbanscheduler;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TaskFillActivity extends AppCompatActivity {
    private Button mSetReminderButton;
    private Button mDiscardButton;
    private TextView mDate;
    private View mDateLine;
    private TextView mTime;
    private View mTimeLine;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_fill);
        mSetReminderButton = findViewById(R.id.set_reminder);
        mDiscardButton = findViewById(R.id.discard_reminder);
        mDate = findViewById(R.id.pick_date);
        mDateLine = findViewById(R.id.pick_date_line);
        mTime = findViewById(R.id.pick_time);
        mTimeLine = findViewById(R.id.pick_time_line);
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM", Locale.US);
                String dateString = month+"/"+day+"/"+year;
                try {
                    Date pickedDate = new SimpleDateFormat("mm/dd/yyyy", Locale.US).parse(dateString);
                    assert pickedDate != null;
                    String dateFormatString = dateFormat.format(pickedDate);
                    mDate.setText(dateFormatString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };
        mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.US);
                String timeString = hourOfDay+":"+minute;
                try {
                    Date pickedTime = new SimpleDateFormat("h:m", Locale.US).parse(timeString);
                    assert pickedTime != null;
                    String timeFormatString = timeFormat.format(pickedTime);
                    mTime.setText(timeFormatString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public void cancelForm(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        // Used to clear all previous activities in stack
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void submitForm(View view) {
        Toast.makeText(this, "Successfully submitted for now!", Toast.LENGTH_SHORT).show();
    }

    public void showReminder(View view) {
        mSetReminderButton.setVisibility(View.INVISIBLE);
        mDiscardButton.setVisibility(View.VISIBLE);
        mDate.setVisibility(View.VISIBLE);
        mDateLine.setVisibility(View.VISIBLE);
        mTime.setVisibility(View.VISIBLE);
        mTimeLine.setVisibility(View.VISIBLE);
    }

    public void discardReminder(View view) {
        mDiscardButton.setVisibility(View.INVISIBLE);
        mDate.setVisibility(View.INVISIBLE);
        mDateLine.setVisibility(View.INVISIBLE);
        mTime.setVisibility(View.INVISIBLE);
        mTimeLine.setVisibility(View.INVISIBLE);
        mSetReminderButton.setVisibility(View.VISIBLE);
    }

    public void showDatePicker(View view) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(TaskFillActivity.this, R.style.Theme_AppCompat_DayNight_Dialog, mDateSetListener, year, month, day);
        dialog.show();
    }

    public void showTimePicker(View view) {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY)+1;
        int min = 0;
        TimePickerDialog dialog = new TimePickerDialog(TaskFillActivity.this, 3, mTimeSetListener, hour, min, false);
        dialog.show();
    }
}