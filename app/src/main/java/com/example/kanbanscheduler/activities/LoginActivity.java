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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        mAuth = FirebaseAuth.getInstance();
    }

    // Signs in user by authentication through Firebase
    public void signIn(View view) {
        // Make sure fields are filled out
        String emailString = email.getText().toString();
        String passwordString = password.getText().toString();
        if(TextUtils.isEmpty(emailString) || TextUtils.isEmpty(passwordString)) {
            Toast.makeText(this, "Please fill out all the fields!", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful() && mAuth.getCurrentUser().isEmailVerified()) {
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        // Used to clear all previous activities in stack
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid Email or Password.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    // Redirects to different activity page that resets password
    public void resetPassword(View view) {
        Intent intent = new Intent(this, ResetActivity.class);
        startActivity(intent);
    }
}