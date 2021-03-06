package com.example.kanbanscheduler.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kanbanscheduler.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TaskFormActivity extends AppCompatActivity {
    private EditText mTaskName;
    private EditText mTaskDescription;
    private Button mSetReminderButton;
    private Button mDiscardButton;
    private TextView mDate;
    private View mDateLine;
    private TextView mTime;
    private View mTimeLine;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;
    private int editId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_form);
        configureUIComponents();
        configureFormValues();
        configureListeners();
    }

    public void cancelForm(View view) {
        Intent intent = new Intent();
        // Used to clear all previous activities in stack
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    public void submitForm(View view) {
        // If the task name isn't filled out
        if(mTaskName.getText().toString().equals("")) {
            Toast.makeText(this, "Please fill out the task name.", Toast.LENGTH_SHORT).show();
        } else {
            // if discard button is visible, then do check the following
            if(mDiscardButton.getVisibility() == View.VISIBLE) {
                // If date is filled out but not time
                if(!mDate.getText().toString().equals("") && mTime.getText().equals("")) {
                    Toast.makeText(this, "Please fill out the time as well.", Toast.LENGTH_SHORT).show();
                // If time is filled out but not date
                } else if(!mTime.getText().equals("") && mDate.getText().equals("")) {
                    Toast.makeText(this, "Please fill out date as well.", Toast.LENGTH_SHORT).show();
                // If neither time nor date is filled out
                } else if(mTime.getText().equals("") && mDate.getText().equals("")) {
                    Toast.makeText(this, "Please select a time and date.", Toast.LENGTH_SHORT).show();
                // If passes all conditions, successfully submit
                } else {
                    Intent intent = new Intent();
                    Bundle extras = new Bundle();
                    extras.putString("EXTRA_TASK_NAME", mTaskName.getText().toString());
                    extras.putString("EXTRA_TASK_DESCRIPTION", mTaskDescription.getText().toString());
                    extras.putSerializable("EXTRA_DATE", parseDateString(mDate.getText().toString()));
                    extras.putString("EXTRA_TIME", mTime.getText().toString());
                    extras.putInt("EXTRA_TASK_ID", editId);
                    intent.putExtras(extras);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            // If discard button is invisible, then simply submit
            } else {
                Intent intent = new Intent();
                Bundle extras = new Bundle();
                extras.putString("EXTRA_TASK_NAME", mTaskName.getText().toString());
                extras.putString("EXTRA_TASK_DESCRIPTION", mTaskDescription.getText().toString());
                extras.putSerializable("EXTRA_DATE", parseDateString(mDate.getText().toString()));
                extras.putString("EXTRA_TIME", mTime.getText().toString());
                extras.putInt("EXTRA_TASK_ID", editId);
                intent.putExtras(extras);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
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
        mDate.setText(null);
        mDateLine.setVisibility(View.INVISIBLE);
        mTime.setVisibility(View.INVISIBLE);
        mTime.setText(null);
        mTimeLine.setVisibility(View.INVISIBLE);
        mSetReminderButton.setVisibility(View.VISIBLE);
    }

    public void showDatePicker(View view) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String currentDate = mDate.getText().toString();
        if(!currentDate.equals("")) {
            String[] str = currentDate.split("/");
            month = Integer.parseInt(str[0])-1;
            day = Integer.parseInt(str[1]);
            year = Integer.parseInt(str[2]);
        }
        DatePickerDialog dialog = new DatePickerDialog(TaskFormActivity.this, R.style.Theme_AppCompat_DayNight_Dialog, mDateSetListener, year, month, day);
        dialog.show();
    }

    public void showTimePicker(View view) {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY)+1;
        int min = 0;
        String currentTime = mTime.getText().toString();
        if(!currentTime.equals("")) {
            String timeString = parseCurrentTime(currentTime);
            String[] str = timeString.split(":");
            hour = Integer.parseInt(str[0]);
            min = Integer.parseInt(str[1]);
        }
        TimePickerDialog dialog = new TimePickerDialog(TaskFormActivity.this, 3, mTimeSetListener, hour, min, false);
        dialog.show();
    }

    private void configureUIComponents() {
        mTaskName = findViewById(R.id.task);
        mTaskDescription = findViewById(R.id.task_description);
        mSetReminderButton = findViewById(R.id.set_reminder);
        mDiscardButton = findViewById(R.id.discard_reminder);
        mDate = findViewById(R.id.pick_date);
        mDateLine = findViewById(R.id.pick_date_line);
        mTime = findViewById(R.id.pick_time);
        mTimeLine = findViewById(R.id.pick_time_line);
    }

    private void configureFormValues() {
        Bundle b = getIntent().getExtras();
        if(b != null) {
            mTaskName.setText(b.getString("EXTRA_EDIT_NAME"));
            mTaskDescription.setText(b.getString("EXTRA_EDIT_DESCRIPTION"));
            mDate.setText(b.getString("EXTRA_EDIT_DATE"));
            mTime.setText(b.getString("EXTRA_EDIT_TIME"));
            editId = b.getInt("EXTRA_EDIT_ID");
            // If date isn't empty, then show it reminder tags.
            if(!mDate.getText().toString().equals("")) {
                showReminder(mSetReminderButton);
            }
        }
    }

    private void configureListeners() {
        mDateSetListener = (datePicker, year, month, day) -> {
            month++;
            String pickedDate = month+"/"+day+"/"+year;
            mDate.setText(pickedDate);
        };

        mTimeSetListener = (timePicker, hourOfDay, minute) -> {
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.US);
            String timeString = hourOfDay+":"+minute;
            try {
                // TimePicker always return in 0-23 hr format, so must use HH instead of hh
                Date pickedTime = new SimpleDateFormat("HH:mm", Locale.US).parse(timeString);
                assert pickedTime != null;
                String timeFormatString = timeFormat.format(pickedTime);
                mTime.setText(timeFormatString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        };
    }

    // Reverse time from AM/PM to 24 hour format for TimePickerDialog
    private String parseCurrentTime(String timeString) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.US);
        try {
            // TimePicker always return in 0-23 hr format, so must use HH instead of hh
            Date pickedTime = new SimpleDateFormat("hh:mm a", Locale.US).parse(timeString);
            assert pickedTime != null;
            return timeFormat.format(pickedTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    // Changes date string to Date for DB use
    private Date parseDateString(String dateString) {
        try {
            Date d = new SimpleDateFormat("MM/dd/yyyy", Locale.US).parse(dateString);
            assert d != null;
            return d;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}