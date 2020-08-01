package com.example.kanbanscheduler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // Takes one to register view from home page
    public void registerView(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    // Takes one to login view from home page
    public void loginView(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}