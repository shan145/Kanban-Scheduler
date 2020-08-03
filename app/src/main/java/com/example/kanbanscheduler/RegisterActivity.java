package com.example.kanbanscheduler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private EditText name;
    private EditText email;
    private EditText password;
    private EditText confirm_password;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name = findViewById(R.id.register_name);
        email = findViewById(R.id.register_email);
        password = findViewById(R.id.register_password);
        confirm_password = findViewById(R.id.register_password2);
        mAuth = FirebaseAuth.getInstance();

    }

    // Creates user through Firebase
    public void createUser(View view) {
        // Make sure all fields are filled
        if(TextUtils.isEmpty(name.getText().toString()) || TextUtils.isEmpty(email.getText().toString()) || TextUtils.isEmpty(password.getText().toString()) || TextUtils.isEmpty(confirm_password.getText().toString())) {
            Toast.makeText(this, "Please fill out all the fields!", Toast.LENGTH_SHORT).show();
        // Make sure passwords match
        } else if(!(password.getText().toString()).equals(confirm_password.getText().toString())) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
        // Check authentication works
        } else {
            mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    // User created successfully with email and password
                    if(task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(name.getText().toString()).build();
                        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User profile updated");
                                }
                            }
                        });
                        // Sends email to verify account and redirects to main page
                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "Please check your email to verify your registration.", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(RegisterActivity.this, "Registration error. Please try later.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();

                    // Unsuccessful creation of user
                    } else {
                        Toast.makeText(RegisterActivity.this, "Email is already registered.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}