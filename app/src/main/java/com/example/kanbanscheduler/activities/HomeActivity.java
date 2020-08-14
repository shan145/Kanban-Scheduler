package com.example.kanbanscheduler.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.kanbanscheduler.adapters.PagerAdapter;
import com.example.kanbanscheduler.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        user = FirebaseAuth.getInstance().getCurrentUser();

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("To Do"));
        tabLayout.addTab(tabLayout.newTab().setText("In Progress"));
        tabLayout.addTab(tabLayout.newTab().setText("Done"));
        // Set tabs to fill entire layout
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Use PagerAdapter to manage page views in fragments.
        // Each page is represented by its own fragment.
        final ViewPager2 viewPager = findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(),getLifecycle(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        //Setting a listener for clicks.
        viewPager.setUserInputEnabled(false);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public Bundle userEmailData() {
        Bundle emailBundle = new Bundle();
        emailBundle.putString("EMAIL", user.getEmail());
        return emailBundle;
    }
}