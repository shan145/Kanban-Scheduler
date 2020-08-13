package com.example.kanbanscheduler.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kanbanscheduler.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.reset_email);
    }

    // Sends password reset email to allow users who forgot passwords to reset password
    public void reset(View view) {
        // Ensure email textfield is filled out
        String emailString = email.getText().toString();
        if(TextUtils.isEmpty(emailString)) {
            Toast.makeText(this, "Please fill out all fields!", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.sendPasswordResetEmail(emailString).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        Toast.makeText(ResetActivity.this, "Please check your email for reset instructions.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ResetActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(ResetActivity.this, "Reset failed. Please try again later.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}