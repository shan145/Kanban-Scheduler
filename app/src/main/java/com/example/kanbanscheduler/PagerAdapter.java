package com.example.kanbanscheduler;

import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class PagerAdapter extends FragmentStateAdapter {
    int mNumOfTabs;

    public PagerAdapter(@NonNull FragmentManager fragmentManager, Lifecycle lifecycle, int numOfTabs) {
        super(fragmentManager, lifecycle);
        this.mNumOfTabs = numOfTabs;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position) {
            case 0: return new TodoFragment();
            case 1: return new ProgFragment();
            case 2: return new DoneFragment();
            default: return null;
        }
    }

    @Override
    public int getItemCount() {
        return mNumOfTabs;
    }
}
