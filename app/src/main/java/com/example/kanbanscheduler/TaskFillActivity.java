package com.example.kanbanscheduler;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TaskFillActivity extends AppCompatActivity {
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
    private String dateString;
    private String timeString;
    private String editEmail;
    private int editId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_fill);
        mTaskName = findViewById(R.id.task);
        mTaskDescription = findViewById(R.id.task_description);
        mSetReminderButton = findViewById(R.id.set_reminder);
        mDiscardButton = findViewById(R.id.discard_reminder);
        mDate = findViewById(R.id.pick_date);
        mDateLine = findViewById(R.id.pick_date_line);
        mTime = findViewById(R.id.pick_time);
        mTimeLine = findViewById(R.id.pick_time_line);

        // Use in case of filling out form
        Bundle b = getIntent().getExtras();
        if(b != null) {
            mTaskName.setText(b.getString("EXTRA_EDIT_NAME"));
            mTaskDescription.setText(b.getString("EXTRA_EDIT_DESCRIPTION"));
            mDate.setText(b.getString("EXTRA_EDIT_DATE"));
            mTime.setText(b.getString("EXTRA_EDIT_TIME"));
            editEmail = b.getString("EXTRA_EDIT_EMAIL");
            editId = b.getInt("EXTRA_EDIT_ID");
        }
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM", Locale.US);
                month = month + 1;
                dateString = month+"/"+day+"/"+year;
                try {
                    // MM is month while mm is minutes
                    Date pickedDate = new SimpleDateFormat("MM/dd/yyyy", Locale.US).parse(dateString);
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
                timeString = hourOfDay+":"+minute;
                try {
                    // TimePicker always return in 0-23 hr format, so must use HH instead of hh
                    Date pickedTime = new SimpleDateFormat("HH:mm", Locale.US).parse(timeString);
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
                // If time is filled out but note date
                } else if(!mTime.getText().equals("") && mDate.getText().equals("")) {
                    Toast.makeText(this, "Please fill out date as well.", Toast.LENGTH_SHORT).show();
                // If neither time nor date is filled out
                } else if(mTime.getText().equals("") && mDate.getText().equals("")) {
                    Toast.makeText(this, "Please select a time and date.", Toast.LENGTH_SHORT).show();
                // If passes all conditions, successfully submit
                } else {
                    // For editing
                    if (editEmail != null ) {
                        Intent intent = new Intent();
                        Bundle extras = new Bundle();
                        extras.putString("EXTRA_RETURN_NAME", mTaskName.getText().toString());
                        extras.putString("EXTRA_RETURN_DESCRIPTION", mTaskDescription.getText().toString());
                        extras.putString("EXTRA_RETURN_DATE", mDate.getText().toString());
                        extras.putString("EXTRA_RETURN_TIME", mTime.getText().toString());
                        extras.putString("EXTRA_RETURN_EMAIL", editEmail);
                        extras.putInt("EXTRA_RETURN_ID", editId);
                        intent.putExtras(extras);
                        setResult(RESULT_OK, intent);
                        finish();
                    // For a new filled out form
                    } else {
                        Intent intent = new Intent();
                        Bundle extras = new Bundle();
                        extras.putString("EXTRA_TASK_NAME", mTaskName.getText().toString());
                        extras.putString("EXTRA_TASK_DESCRIPTION", mTaskDescription.getText().toString());
                        extras.putString("EXTRA_DATE", mDate.getText().toString());
                        extras.putString("EXTRA_TIME", mTime.getText().toString());
                        intent.putExtras(extras);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            // If discard button is invisible, then simply submit
            } else {
                // For editing
                if (editEmail != null ) {
                    Intent intent = new Intent();
                    Bundle extras = new Bundle();
                    extras.putString("EXTRA_RETURN_NAME", mTaskName.getText().toString());
                    extras.putString("EXTRA_RETURN_DESCRIPTION", mTaskDescription.getText().toString());
                    extras.putString("EXTRA_RETURN_DATE", mDate.getText().toString());
                    extras.putString("EXTRA_RETURN_TIME", mTime.getText().toString());
                    extras.putString("EXTRA_RETURN_EMAIL", editEmail);
                    extras.putInt("EXTRA_RETURN_ID", editId);
                    intent.putExtras(extras);
                    setResult(RESULT_OK, intent);
                    finish();
                    // For a new filled out form
                } else {
                    Intent intent = new Intent();
                    Bundle extras = new Bundle();
                    extras.putString("EXTRA_TASK_NAME", mTaskName.getText().toString());
                    extras.putString("EXTRA_TASK_DESCRIPTION", mTaskDescription.getText().toString());
                    extras.putString("EXTRA_DATE", mDate.getText().toString());
                    extras.putString("EXTRA_TIME", mTime.getText().toString());
                    intent.putExtras(extras);
                    setResult(RESULT_OK, intent);
                    finish();
                }
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
//        if(!mDate.getText().toString().equals("")) {
//            String[] str = dateString.split("/");
//            month = Integer.parseInt(str[0])-1;
//            day = Integer.parseInt(str[1]);
//            year = Integer.parseInt(str[2]);
//        }
        DatePickerDialog dialog = new DatePickerDialog(TaskFillActivity.this, R.style.Theme_AppCompat_DayNight_Dialog, mDateSetListener, year, month, day);
        dialog.show();
    }


    public void showTimePicker(View view) {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY)+1;
        int min = 0;
//        if(!mTime.getText().toString().equals("")) {
//            String[] str = timeString.split(":");
//            hour = Integer.parseInt(str[0]);
//            min = Integer.parseInt(str[1]);
//        }
        TimePickerDialog dialog = new TimePickerDialog(TaskFillActivity.this, 3, mTimeSetListener, hour, min, false);
        dialog.show();
    }
}